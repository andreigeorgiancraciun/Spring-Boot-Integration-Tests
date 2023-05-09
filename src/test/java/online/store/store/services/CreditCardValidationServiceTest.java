package online.store.store.services;

import online.store.exceptions.CreditCardValidationException;
import online.store.services.CreditCardValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class CreditCardValidationServiceTest {

    private final CreditCardValidationService creditCardValidationService =
            new CreditCardValidationService();

    @Test
    public void validate_validCard_noExceptionThrown() {
        creditCardValidationService.validate("1234567890123456");
    }

    @Test
    public void validate_invalidNumberOfDigits_throwsException() {
        assertThrows(CreditCardValidationException.class,
                () -> creditCardValidationService.validate("1234"));
    }

    @Test
    public void validate_withInvalidCardFormat_throwsException() {
        assertThrows(CreditCardValidationException.class,
                () -> creditCardValidationService.validate("abcdabcdabcdabcs"));
    }

    @Test
    public void validate_stolenCreditCard_throwsException() {
        assertThrows(CreditCardValidationException.class,
                () -> creditCardValidationService.validate("1111111111111111"));
    }
}
