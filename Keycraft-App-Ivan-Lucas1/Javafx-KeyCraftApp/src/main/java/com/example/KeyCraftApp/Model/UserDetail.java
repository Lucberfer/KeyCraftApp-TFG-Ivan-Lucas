package com.example.KeyCraftApp.Model;

/**
 * The UserDetail class is a model class that stores user-related details.
 * It contains static fields for global user information (e.g., username, path, date, ID, customerID),
 * as well as an instance-specific field for quantity.
 */
public class UserDetail {

    // Static fields to store global user details
    private static String username; // Stores the username of the user
    private static String path; // Stores a file path associated with the user
    private static String date; // Stores a date (likely related to user activity)
    private static Integer id; // Stores the unique ID of the user
    private static Integer customerID; // Stores the customer ID of the user

    // Non-static field for instance-specific data
    private Integer quantity; // Stores the quantity associated with a specific instance of UserDetail

    // Getter and setter for the static 'username' field
    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        UserDetail.username = username;
    }

    // Getter and setter for the static 'path' field
    public static String getPath() {
        return path;
    }

    public static void setPath(String path) {
        UserDetail.path = path;
    }

    // Getter and setter for the static 'date' field
    public static String getDate() {
        return date;
    }

    public static void setDate(String date) {
        UserDetail.date = date;
    }

    // Getter and setter for the static 'id' field
    public static void setId(Integer id) {
        UserDetail.id = id;
    }

    public static Integer getId() {
        return id;
    }

    // Getter and setter for the static 'customerID' field
    public static Integer getCustomerID() {
        return customerID;
    }

    public static void setCustomerID(Integer customerID) {
        UserDetail.customerID = customerID;
    }

    // Getter and setter for the non-static 'quantity' field
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
