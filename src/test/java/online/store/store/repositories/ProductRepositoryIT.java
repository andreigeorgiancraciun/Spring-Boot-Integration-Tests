package online.store.store.repositories;

import online.store.model.Product;
import online.store.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProductRepositoryIT {
    private static final String ELECTRONICS_CATEGORY = "electronics";

    private static final Product ELECTRONICS_PRODUCT1 = new Product("Apple Laptop",
            null,
            null,
            5.5f,
            ELECTRONICS_CATEGORY);
    private static final Product ELECTRONICS_PRODUCT2 = new Product("Desktop Monitor",
            null,
            null,
            10.0f,
            ELECTRONICS_CATEGORY);
    private static final Product ART_PRODUCT = new Product("Color Markers",
            "Four high quality markers for any art project",
            "markers_640x426.jpeg",
            14.99f,
            "art");

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;


    @Test
    public void findByCategory_withMultipleProducts_returnsMatchingProducts() {
        entityManager.persist(ELECTRONICS_PRODUCT1);
        entityManager.persist(ELECTRONICS_PRODUCT2);
        entityManager.persist(ART_PRODUCT);

        List<Product> products =
                productRepository.findByCategory(ELECTRONICS_CATEGORY);

        assertThat(products).containsExactlyInAnyOrder(ELECTRONICS_PRODUCT2, ELECTRONICS_PRODUCT1);
    }
}
