package online.store.store.controllers;

import online.store.controllers.CheckoutController;
import online.store.exceptions.CreditCardValidationException;
import online.store.model.Order;
import online.store.model.Product;
import online.store.services.CreditCardValidationService;
import online.store.services.OrdersService;
import online.store.services.ProductsService;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CheckoutController.class)
public class CheckoutControllerIT {
    private static final String VALID_CHECKOUT_REQUEST = "{\n" +
            "  \"firstName\": \"John\",\n" +
            "  \"lastName\": \"Smith\",\n" +
            "  \"email\": \"john.smith@company.com\",\n" +
            "  \"shippingAddress\": \"130 Green Olive Dr. San Francisco\",\n" +
            "  \"products\": [{\"productId\":123, \"quantity\" : 5}],\n" +
            "  \"creditCard\" : \"1234123412341234\"\n" +
            "}";
    private static final String CHECKOUT_REQUEST_WITH_STOLEN_CREDIT_CARD = "{\n" +
            "  \"firstName\": \"John\",\n" +
            "  \"lastName\": \"Smith\",\n" +
            "  \"email\": \"john.smith@company.com\",\n" +
            "  \"shippingAddress\": \"130 Green Olive Dr. San Francisco\",\n" +
            "  \"products\": [{\"productId\":123, \"quantity\" : 5}],\n" +
            "  \"creditCard\" : \"1111111111111111\"\n" +
            "}";
    private static final Product PRODUCT = new Product("Audio Speakers",
            "Stereo speakers for listening to music at home",
            "speakers.jpeg",
            89f,
            "electronics");

    @MockBean
    private OrdersService ordersService;

    @MockBean
    private ProductsService productsService;

    @MockBean
    private CreditCardValidationService creditCardValidationService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void checkout_withCorrectRequest_success() throws Exception {
        when(productsService.getProductById(123)).thenReturn(PRODUCT);
        Set<Order> expectedOrders =
                Sets.set(
                        new Order("John",
                                "Smith",
                                "john.smith@company.com",
                                "130 Green Olive Dr. San Francisco",
                                5,
                                PRODUCT,
                                "1234123412341234"));

        mockMvc.perform(post("/checkout")
                        .content(VALID_CHECKOUT_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(content().string("success"))
                .andExpect(status().isOk());

        Mockito.verify(ordersService).placeOrders(expectedOrders);
    }

    @Test
    public void checkout_invalidCreditCard_returnsBadRequestStatus() throws Exception {
        doThrow(new CreditCardValidationException("Invalid Credit Card"))
                .when(creditCardValidationService)
                .validate(eq("1111111111111111"));

        mockMvc.perform(post("/checkout")
                        .content(CHECKOUT_REQUEST_WITH_STOLEN_CREDIT_CARD)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(content().string(startsWith("Credit card is invalid, please use another form of " +
                        "payment")))
                .andExpect(status().isBadRequest());
    }
}
