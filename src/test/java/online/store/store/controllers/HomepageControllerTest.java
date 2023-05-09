package online.store.store.controllers;

import online.store.controllers.HomepageController;
import online.store.model.Product;
import online.store.model.wrappers.ProductsWrapper;
import online.store.services.ProductsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class HomepageControllerTest {
    private static final Product PRODUCT1 = new Product("Color Markers",
            "Four high quality markers for any art project",
            null,
            14.99f,
            "art");
    private static final Product PRODUCT2 = new Product("Desktop Monitor",
            null,
            null,
            10.0f,
            "electronics");

    @Mock
    private ProductsService productsService;

    @InjectMocks
    private HomepageController homepageController;

    @Test
    public void getProductCategories_returnsString() {
        when(productsService.getAllSupportedCategories()).thenReturn(Arrays.asList("category1",
                "category2"));

        String response = homepageController.getProductCategories();

        assertThat(response).isEqualTo("category1,category2");
    }

    @Test
    public void getDealsOfTheDay_success_returnsListOfOneProduct() {
        when(productsService.getDealsOfTheDay(any(Integer.class))).thenReturn(List.of(PRODUCT1));

        ProductsWrapper productsWrapper = homepageController.getDealsOfTheDay(1);

        assertThat(productsWrapper.getProducts()).containsExactly(PRODUCT1);
    }

    @Test
    public void getProductsForCategory_nullCategory_returnsList() {
        when(productsService.getAllProducts()).thenReturn(Arrays.asList(PRODUCT1, PRODUCT2));

        ProductsWrapper productsWrapper = homepageController.getProductsForCategory(null);

        assertThat(productsWrapper.getProducts()).containsExactlyInAnyOrder(PRODUCT2, PRODUCT1);
    }

    @Test
    public void getProductsForCategory_withCategory_returnsList() {
        String category = "electronics";
        when(productsService.getProductsByCategory(eq(category))).thenReturn(List.of(PRODUCT2));

        ProductsWrapper productsWrapper = homepageController.getProductsForCategory(category);

        assertThat(productsWrapper.getProducts()).containsExactly(PRODUCT2);
    }
}
