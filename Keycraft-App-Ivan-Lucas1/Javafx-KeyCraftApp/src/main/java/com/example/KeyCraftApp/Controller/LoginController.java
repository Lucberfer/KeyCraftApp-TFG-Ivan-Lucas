package com.example.KeyCraftApp.Controller;

import com.example.KeyCraftApp.App;
import com.example.KeyCraftApp.Database.Database;
import com.example.KeyCraftApp.Model.UserDetail;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    public AnchorPane login_section;
    public TextField login_username;
    public PasswordField login_password;
    public Button login_button;
    public Hyperlink login_forget_password;
    public AnchorPane register_account_section;
    public TextField register_account_username;
    public PasswordField register_account_password;
    public Button create_account_button;
    public ComboBox<String> register_account_question;
    public TextField register_account_answer;
    public AnchorPane side_form;
    public Button side_create_account_button;
    public Button side_already_have_an_account;
    public TextField user_username;
    public Button user_proceed;
    public ComboBox<String> user_question;
    public Button back_to_login;
    public TextField user_answer;
    public Button back_to;
    public Button change_password;
    public PasswordField confirm_password;
    public PasswordField new_password;
    public AnchorPane forget_password_section;
    public AnchorPane forget_password_proceed_section;

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet;
    private final String[] questionList = {
            "¿Cual es tu color favorito?",
            "¿Cual es tu comida favorita?",
            "¿En que año naciste?",
    };

    ObservableList<String> observableList = FXCollections.observableArrayList(questionList);

    private Alert alert;

    /**
     * Handles the action for proceeding with password recovery.
     * Validates the input fields and checks the username, security question, and answer against the database.
     * Updates the UI based on the validation result.
     */
    public void proceedAction() {
        // Check if all required fields are filled
        if (user_username.getText().isEmpty() ||
                user_question.getSelectionModel().getSelectedItem() == null ||
                user_answer.getText().isEmpty()) {

            fillAllFieldError(); // Show an error if any field is empty
        } else {
            // Query to check if the username, question, and answer match the database
            String checkUsernameAndQuestion = "SELECT username, question, answer FROM Employee WHERE username = ? AND question = ? AND answer = ?";
            connection = Database.connectionDB();

            try {
                preparedStatement = connection.prepareStatement(checkUsernameAndQuestion);
                preparedStatement.setString(1, user_username.getText());
                preparedStatement.setString(2, user_question.getSelectionModel().getSelectedItem());
                preparedStatement.setString(3, user_answer.getText());
                resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    // If the credentials match, update the UI for password reset
                    side_already_have_an_account.setVisible(false);
                    side_create_account_button.setVisible(false);
                    forget_password_section.setVisible(true);
                    forget_password_proceed_section.setVisible(false);
                    side_create_account_button.setVisible(true);
                } else {
                    // Show an error if the credentials are incorrect
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrecto");
                    alert.showAndWait();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Handles the login action for the application.
     * Validates the input fields, checks the username and password against the database,
     * and navigates to the main application screen upon successful login.
     */
    public void loginAction() {

        // Check if the username or password fields are empty
        if (login_username.getText().isEmpty() || login_password.getText().isEmpty()) {
            fillAllFieldError(); // Show an error if any field is empty
        }
        // Check if the password length is less than 8 characters
        else if (login_password.getText().length() < 8) {
            invalidPassword(); // Show an error for an invalid password
        }
        else {
            // Query to validate the username and password in the database
            String confirmIfTrue = "SELECT username, password FROM Employee WHERE username = ? AND password = ?";
            connection = Database.connectionDB();

            try {
                preparedStatement = connection.prepareStatement(confirmIfTrue);
                preparedStatement.setString(1, login_username.getText());
                preparedStatement.setString(2, login_password.getText());
                resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    // Set the logged-in user's username
                    UserDetail.setUsername(login_username.getText());

                    // Show a success alert
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Información");
                    alert.setHeaderText(null);
                    alert.setContentText("Sesión iniciada");
                    alert.showAndWait();

                    // Load the main application screen
                    FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("FXML/Main.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(fxmlLoader.load());
                    stage.setScene(scene);
                    stage.setTitle("KeyCraft Management");
                    stage.setMinHeight(800);
                    stage.setMinWidth(1280);
                    stage.show();

                    // Close the login window
                    login_button.getScene().getWindow().hide();
                } else {
                    // Show an error if the username or password is incorrect
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Contraseña/Usuario incorrecto");
                    alert.showAndWait();
                }
            } catch (SQLException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Handles the registration process for a new account.
     * Validates the input fields, checks for existing usernames, and registers the user in the database if valid.
     */
    public void registrationButton() throws SQLException {

        // Check if any required fields are empty
        if (register_account_username.getText().isEmpty() ||
                register_account_password.getText().isEmpty() ||
                register_account_question.getSelectionModel().getSelectedItem() == null ||
                register_account_answer.getText().isEmpty()) {

            fillAllFieldError();
        } else {
            // Get the current date
            Date date = new Date();
            java.sql.Date _date = new java.sql.Date(date.getTime());

            // SQL query to insert new account data
            String regData = "INSERT INTO Employee (username, password, question, answer, date) VALUES (?, ?, ?, ?, ?)";
            connection = Database.connectionDB();
            System.out.println(isDBConnected());

            try {
                // Query to check if the username already exists
                String checkUsername = "SELECT username FROM Employee WHERE username == '" + register_account_username.getText() + "'";
                preparedStatement = connection.prepareStatement(checkUsername);
                resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    // Show an error if the username is already registered
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText(register_account_username.getText() + " ya está registrado\nPor favor, inicie sesión o pulse en olvidé la contraseña");
                    alert.showAndWait();

                    transitionLeft();
                }
                // Check if the password length is less than 8 characters
                else if (register_account_password.getText().length() < 8) {
                    invalidPassword();
                }
                else {
                    // Insert the new user data into the database
                    preparedStatement = connection.prepareStatement(regData);
                    preparedStatement.setString(1, register_account_username.getText());
                    preparedStatement.setString(2, register_account_password.getText());
                    preparedStatement.setString(3, register_account_question.getSelectionModel().getSelectedItem());
                    preparedStatement.setString(4, register_account_answer.getText());
                    preparedStatement.setString(5, String.valueOf(_date));
                    preparedStatement.executeUpdate();

                    // Show a success alert
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Terminado con éxito\nPor favor inicie sesión");

                    ButtonType closeButton = new ButtonType("Cerrar", ButtonType.OK.getButtonData());
                    alert.getButtonTypes().setAll(closeButton);

                    alert.showAndWait();

                    // Clear the input fields
                    register_account_username.setText("");
                    register_account_answer.setText("");
                    register_account_password.setText("");
                    register_account_question.getSelectionModel().clearSelection();

                    transitionLeft();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                preparedStatement.close();
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {
                    }
                }
            }
        }
    }


    /**
     * Checks if the database connection is active.
     *
     * @return true if the connection is open, false otherwise.
     */
    public boolean isDBConnected() {
        try {
            return !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    TranslateTransition translateTransition = new TranslateTransition();

    /**
     * Handles the form switching logic based on the button clicked.
     * Transitions to the appropriate form (create account or login).
     *
     * @param event The ActionEvent triggered by the button click.
     */
    public void switchForm(ActionEvent event) {
        if (event.getSource() == side_create_account_button) {
            transitionRight();
        }
        // Check if the "Already Have an Account" button is clicked
        else if (event.getSource() == side_already_have_an_account) {
            transitionLeft();
        }
    }

    /**
     * Displays an error alert for invalid passwords.
     * Notifies the user that the password must be at least 8 characters long.
     */
    public void invalidPassword() {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("La contraseña debe tener al menos 8 caracteres.");
        alert.showAndWait();
    }

    /**
     * Handles the left transition animation for the side form.
     * Adjusts the visibility of buttons and sections after the transition.
     */
    public void transitionLeft() {
        translateTransition.setNode(side_form);
        translateTransition.setToX(0);
        translateTransition.setDuration(Duration.millis(1000));
        translateTransition.play();

        // After the transition is finished, update the visibility of elements
        translateTransition.setOnFinished(e -> {
            side_already_have_an_account.setVisible(false);
            side_create_account_button.setVisible(true);
            forget_password_section.setVisible(false);
            forget_password_proceed_section.setVisible(false);
        });
    }

    /**
     * Handles the right transition animation for the side form.
     * Adjusts the visibility of buttons and sections after the transition.
     */
    public void transitionRight() {
        translateTransition.setNode(side_form);
        translateTransition.setToX(300);
        translateTransition.setDuration(Duration.millis(1000));
        translateTransition.play();

        // After the transition is finished, update the visibility of elements
        translateTransition.setOnFinished(e -> {
            side_already_have_an_account.setVisible(true);
            side_create_account_button.setVisible(false);
            forget_password_section.setVisible(false);
            forget_password_proceed_section.setVisible(false);
        });
    }

    /**
     * Displays an error alert when not all fields are filled.
     * Notifies the user to fill in all blank spaces.
     */
    public void fillAllFieldError() {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Por favor rellene los espacios en blanco.");
        alert.showAndWait();
    }

    /**
     * Handles the action for forgetting the password.
     * Updates the visibility of buttons and sections accordingly.
     */
    public void forgetPasswordAction() {
        side_already_have_an_account.setVisible(false);
        side_create_account_button.setVisible(false);
        forget_password_section.setVisible(false);
        forget_password_proceed_section.setVisible(true);
        side_create_account_button.setVisible(true);
    }

    /**
     * Handles the action to return to the login view.
     * Updates the visibility of buttons and sections accordingly.
     */
    public void backToLogin() {
        side_already_have_an_account.setVisible(false);
        side_create_account_button.setVisible(true);
        forget_password_section.setVisible(false);
        forget_password_proceed_section.setVisible(false);
    }

    /**
     * Handles the action of changing the password.
     * Validates input and updates the password in the database.
     */
    public void changePasswordAction() {
        // Check if the new password and confirmation do not match
        if (!new_password.getText().equals(confirm_password.getText())) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("La nueva contraseña y la contraseña de confirmación no son la misma.");
            alert.showAndWait();
        }
        // Check if any password fields are empty
        else if (new_password.getText().isEmpty() || confirm_password.getText().isEmpty()) {
            fillAllFieldError();
        }
        // Check if the new password is less than 8 characters
        else if (new_password.getText().length() < 8) {
            invalidPassword();
        }
        // Proceed to change the password in the database
        else {
            String changePassword = "UPDATE Employee SET password = ? WHERE username = ?";

            try {
                preparedStatement = connection.prepareStatement(changePassword);
                preparedStatement.setString(1, new_password.getText());
                preparedStatement.setString(2, user_username.getText());
                boolean rowsAffected = preparedStatement.execute();
                System.out.println(rowsAffected);

                // Check if the update was successful
                if (rowsAffected) {
                    System.out.println("Internal Error");
                } else {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Contraseña cambiada con éxito");
                    alert.showAndWait();

                    // Clear the fields after successful password change
                    new_password.setText("");
                    confirm_password.setText("");
                    user_username.setText("");
                    user_answer.setText("");
                    user_question.getSelectionModel().clearSelection();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Initializes the controller class.
     * Sets up the dropdown items and event handlers for various buttons.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set dropdown items for registration and user questions
        register_account_question.setItems(observableList);
        user_question.setItems(observableList);

        // Set action for the "Create Account" button
        create_account_button.setOnAction(event -> {
            try {
                registrationButton();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        login_button.setOnAction(event -> loginAction());

        login_forget_password.setOnAction(event -> forgetPasswordAction());

        back_to_login.setOnAction(event -> backToLogin());
        back_to.setOnAction(event -> backToLogin());

        user_proceed.setOnAction(event -> proceedAction());

        change_password.setOnAction(event -> changePasswordAction());
    }
}
