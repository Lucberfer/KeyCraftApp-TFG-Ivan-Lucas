package com.example.KeyCraftApp.Model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: [Lucas Bernabé Fernández]
 */
class UserDetailTest {

    @Test
    void getUsername() {
        UserDetail.setUsername("TestUser");
        assertEquals("TestUser", UserDetail.getUsername());
    }

    @Test
    void setUsername() {
        UserDetail.setUsername("NewUser");
        assertEquals("NewUser", UserDetail.getUsername());
    }

    @Test
    void getPath() {
        UserDetail.setPath("/test/path");
        assertEquals("/test/path", UserDetail.getPath());
    }

    @Test
    void setPath() {
        UserDetail.setPath("/new/path");
        assertEquals("/new/path", UserDetail.getPath());
    }

    @Test
    void getDate() {
        UserDetail.setDate("2025-05-18");
        assertEquals("2025-05-18", UserDetail.getDate());
    }

    @Test
    void setDate() {
        UserDetail.setDate("2025-05-19");
        assertEquals("2025-05-19", UserDetail.getDate());
    }

    @Test
    void setId() {
        UserDetail.setId(123);
        assertEquals(123, UserDetail.getId());
    }

    @Test
    void getId() {
        UserDetail.setId(456);
        assertEquals(456, UserDetail.getId());
    }

    @Test
    void getCustomerID() {
        UserDetail.setCustomerID(789);
        assertEquals(789, UserDetail.getCustomerID());
    }

    @Test
    void setCustomerID() {
        UserDetail.setCustomerID(101);
        assertEquals(101, UserDetail.getCustomerID());
    }

    @Test
    void getQuantity() {
        UserDetail userDetail = new UserDetail();
        userDetail.setQuantity(5);
        assertEquals(5, userDetail.getQuantity());
    }

    @Test
    void setQuantity() {
        UserDetail userDetail = new UserDetail();
        userDetail.setQuantity(10);
        assertEquals(10, userDetail.getQuantity());
    }
}
