package online.store.model;

import javax.persistence.*;

@Entity
@Table(name = "ProductCategories")
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;

    public ProductCategory() {
    }

    public ProductCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
