package com.example.KeyCraftApp.Model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: [Lucas Bernabé Fernández]
 */
class ProductDataTest {

    @Test
    void getId() {
        ProductData product = new ProductData(1, "P001", "Product Name", "Type A", 100, 29.99, "Available", "image.jpg", "2025-05-18");
        assertEquals(1, product.getId());
    }

    @Test
    void getProductId() {
        ProductData product = new ProductData(1, "P001", "Product Name", "Type A", 100, 29.99, "Available", "image.jpg", "2025-05-18");
        assertEquals("P001", product.getProductId());
    }

    @Test
    void getProductName() {
        ProductData product = new ProductData(1, "P001", "Product Name", "Type A", 100, 29.99, "Available", "image.jpg", "2025-05-18");
        assertEquals("Product Name", product.getProductName());
    }

    @Test
    void getType() {
        ProductData product = new ProductData(1, "P001", "Product Name", "Type A", 100, 29.99, "Available", "image.jpg", "2025-05-18");
        assertEquals("Type A", product.getType());
    }

    @Test
    void getStock() {
        ProductData product = new ProductData(1, "P001", "Product Name", "Type A", 100, 29.99, "Available", "image.jpg", "2025-05-18");
        assertEquals(100, product.getStock());
    }

    @Test
    void getPrice() {
        ProductData product = new ProductData(1, "P001", "Product Name", "Type A", 100, 29.99, "Available", "image.jpg", "2025-05-18");
        assertEquals(29.99, product.getPrice());
    }

    @Test
    void getStatus() {
        ProductData product = new ProductData(1, "P001", "Product Name", "Type A", 100, 29.99, "Available", "image.jpg", "2025-05-18");
        assertEquals("Available", product.getStatus());
    }

    @Test
    void getImage() {
        ProductData product = new ProductData(1, "P001", "Product Name", "Type A", 100, 29.99, "Available", "image.jpg", "2025-05-18");
        assertEquals("image.jpg", product.getImage());
    }

    @Test
    void getDate() {
        ProductData product = new ProductData(1, "P001", "Product Name", "Type A", 100, 29.99, "Available", "image.jpg", "2025-05-18");
        assertEquals("2025-05-18", product.getDate());
    }
}
