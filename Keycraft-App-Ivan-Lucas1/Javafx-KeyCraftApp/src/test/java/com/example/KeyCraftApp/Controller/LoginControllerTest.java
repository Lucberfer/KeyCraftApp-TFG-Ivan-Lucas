package com.example.KeyCraftApp.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: [Lucas Bernabé Fernández]
 */
class LoginControllerTest {

    private LoginController loginController;

    @BeforeEach
    void setUp() {
        loginController = new LoginController();
    }

    @Test
    void transitionLeft() {
        // Verifica que el método no lanza excepciones
        assertDoesNotThrow(() -> loginController.transitionLeft(), "El método transitionLeft no debería lanzar excepciones.");
    }

    @Test
    void transitionRight() {
        // Verifica que el método no lanza excepciones
        assertDoesNotThrow(() -> loginController.transitionRight(), "El método transitionRight no debería lanzar excepciones.");
    }
}
