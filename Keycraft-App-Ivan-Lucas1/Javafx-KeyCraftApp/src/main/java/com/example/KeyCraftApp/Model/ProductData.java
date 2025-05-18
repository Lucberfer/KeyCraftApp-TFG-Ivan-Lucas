package com.example.KeyCraftApp.Model;

/**
 * The ProductData class is a model class that stores information about a product.
 * It includes fields for product identification, details, and status.
 */
public class ProductData {

    // Fields to store product details
    private Integer id; // Unique identifier for the product
    private String productId; // String identifier for the product
    private String productName; // Name of the product
    private String type; // Type or category of the product
    private Integer stock; // Stock quantity available
    private Double price; // Price of the product
    private String status; // Status of the product (e.g., available, discontinued)
    private String image; // URL or path to the product's image
    private String dates; // Date related to the product (e.g., release date)

    /**
     * Constructor for the ProductData class.
     * Initializes all fields with provided values.
     *
     * @param id Unique identifier for the product
     * @param productId String identifier for the product
     * @param productName Name of the product
     * @param type Type or category of the product
     * @param stock Stock quantity available
     * @param price Price of the product
     * @param status Status of the product
     * @param image URL or path to the product's image
     * @param date Date related to the product
     */
    public ProductData(
            Integer id,
            String productId,
            String productName,
            String type,
            Integer stock,
            Double price,
            String status,
            String image,
            String date
    ) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.type = type;
        this.stock = stock;
        this.price = price;
        this.status = status;
        this.image = image;
        this.dates = date;
    }

    // Getter methods for each field to access the values

    public Integer getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getType() {
        return type;
    }

    public Integer getStock() {
        return stock;
    }

    public Double getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public String getImage() {
        return image;
    }

    public String getDate() {
        return dates;
    }
}
