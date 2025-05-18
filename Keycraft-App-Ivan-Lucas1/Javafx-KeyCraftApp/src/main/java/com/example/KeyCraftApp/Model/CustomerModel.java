package com.example.KeyCraftApp.Model;

/**
 * The CustomerModel class is a model class that stores information about a customer and their transactions.
 * It includes fields for customer details, product details, and transaction information.
 */
public class CustomerModel {

    // Fields to store customer and transaction details
    private Integer id; // Unique identifier for the customer record
    private String customer_id; // Unique identifier for the customer
    private String product_id; // Unique identifier for the product
    private String product_name; // Name of the product
    private String quantity; // Quantity of the product purchased
    private String price; // Price of the product
    private String date; // Date of the transaction
    private String em_username; // Username of the employee handling the transaction

    /**
     * Constructor for the CustomerModel class.
     * Initializes all fields with provided values.
     *
     * @param id Unique identifier for the customer record
     * @param customerId Unique identifier for the customer
     * @param productId Unique identifier for the product
     * @param productName Name of the product
     * @param quantity Quantity of the product purchased
     * @param price Price of the product
     * @param date Date of the transaction
     * @param emUsername Username of the employee handling the transaction
     */
    public CustomerModel(
            Integer id,
            String customerId,
            String productId,
            String productName,
            String quantity,
            String price,
            String date,
            String emUsername
    ) {
        this.id = id;
        customer_id = customerId;
        product_id = productId;
        product_name = productName;
        this.quantity = quantity;
        this.price = price;
        this.date = date;
        em_username = emUsername;
    }

    // Getter methods for each field to access the values

    public Integer getId() {
        return id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }

    public String getEm_username() {
        return em_username;
    }
}
