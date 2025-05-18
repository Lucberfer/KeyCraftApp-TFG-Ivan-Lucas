package com.example.KeyCraftApp.Model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: [Lucas Bernabé Fernández]
 */
class CustomerModelTest {

    @Test
    void getId() {
        CustomerModel customer = new CustomerModel(1, "C001", "P001", "Product Name", "2", "19.99", "2025-05-18", "username123");
        assertEquals(1, customer.getId());
    }

    @Test
    void getCustomer_id() {
        CustomerModel customer = new CustomerModel(1, "C001", "P001", "Product Name", "2", "19.99", "2025-05-18", "username123");
        assertEquals("C001", customer.getCustomer_id());
    }

    @Test
    void getProduct_id() {
        CustomerModel customer = new CustomerModel(1, "C001", "P001", "Product Name", "2", "19.99", "2025-05-18", "username123");
        assertEquals("P001", customer.getProduct_id());
    }

    @Test
    void getProduct_name() {
        CustomerModel customer = new CustomerModel(1, "C001", "P001", "Product Name", "2", "19.99", "2025-05-18", "username123");
        assertEquals("Product Name", customer.getProduct_name());
    }

    @Test
    void getQuantity() {
        CustomerModel customer = new CustomerModel(1, "C001", "P001", "Product Name", "2", "19.99", "2025-05-18", "username123");
        assertEquals("2", customer.getQuantity());
    }

    @Test
    void getPrice() {
        CustomerModel customer = new CustomerModel(1, "C001", "P001", "Product Name", "2", "19.99", "2025-05-18", "username123");
        assertEquals("19.99", customer.getPrice());
    }

    @Test
    void getDate() {
        CustomerModel customer = new CustomerModel(1, "C001", "P001", "Product Name", "2", "19.99", "2025-05-18", "username123");
        assertEquals("2025-05-18", customer.getDate());
    }

    @Test
    void getEm_username() {
        CustomerModel customer = new CustomerModel(1, "C001", "P001", "Product Name", "2", "19.99", "2025-05-18", "username123");
        assertEquals("username123", customer.getEm_username());
    }
}
