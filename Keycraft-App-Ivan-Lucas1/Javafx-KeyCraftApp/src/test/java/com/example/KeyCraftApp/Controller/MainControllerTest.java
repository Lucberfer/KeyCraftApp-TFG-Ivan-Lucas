package com.example.KeyCraftApp.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: [Lucas Bernabé Fernández]
 */
class MainControllerTest {

    private MainController mainController;

    @BeforeEach
    void setUp() {
        mainController = new MainController();
    }

    @Test
    void menuGetData() {
        // Simula el comportamiento esperado.
        assertNotNull(mainController.menuGetData(), "El método menuGetData debería devolver datos no nulos.");
    }

    @Test
    void menuGetTotal() {
        // Simula el comportamiento esperado.
        assertDoesNotThrow(() -> mainController.menuGetTotal(), "El método menuGetTotal no debería lanzar excepciones.");
    }

    @Test
    void menuDisplayOrder() {
        // Simula el comportamiento esperado.
        assertNotNull(mainController.menuDisplayOrder(), "El método menuDisplayOrder debería devolver datos no nulos.");
    }

    @Test
    void getCustomerID() {
        // Simula el comportamiento esperado.
        assertDoesNotThrow(() -> mainController.getCustomerID(), "El método getCustomerID no debería lanzar excepciones.");
    }

    @Test
    void inventoryDataList() {
        // Simula el comportamiento esperado.
        assertNotNull(mainController.inventoryDataList(), "El método inventoryDataList debería devolver datos no nulos.");
    }
}
