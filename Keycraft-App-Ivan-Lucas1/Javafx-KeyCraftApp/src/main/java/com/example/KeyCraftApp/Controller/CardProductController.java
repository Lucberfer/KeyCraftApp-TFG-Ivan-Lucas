package com.example.KeyCraftApp.Controller;

import com.example.KeyCraftApp.Database.Database;
import com.example.KeyCraftApp.Model.ProductData;
import com.example.KeyCraftApp.Model.UserDetail;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

public class CardProductController implements Initializable {
    public Label card_product_name;
    public Label card_price;
    public ImageView card_imageview;
    public Spinner<Integer> card_spinner;
    public Button card_add_btn;
    private ProductData productData;
    private Image image;
    private int quantity;
    private String productID;

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private Alert alert;


    private String type;
    private String prod_date;
    private String prod_image;

    public void setData(ProductData productData) {
        this.productData = productData;
        type = productData.getType();
        prod_image = productData.getImage();
        prod_date = productData.getDate();
        productID = productData.getProductId();
        card_product_name.setText(productData.getProductName());
        card_price.setText("$" + String.valueOf(productData.getPrice()));
        String path = "File:" + productData.getImage();
        image = new Image(path, 200, 150, false, true);
        card_imageview.setImage(image);
        pr = productData.getPrice();
    }

    private double total;
    private double pr;

    /**
     * Handles the action of adding a product to the customer's order.
     * Checks product availability and updates the database accordingly.
     */
    public void addBtn() {
        // Initialize the main controller to get customer details
        MainController mainController = new MainController();
        mainController.getCustomerID();

        // Retrieve the quantity selected by the user
        quantity = card_spinner.getValue();
        String check = "";

        // Query to check product availability
        String checkAvailable = "SELECT status FROM Product WHERE product_id = ?";
        connection = Database.connectionDB();

        try {
            // Get the current date for database operations
            Date date = new Date();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());

            // Variable to store the current stock of the product
            int checkStck = 0;

            // Query to check the current stock of the product
            String checkStock = "SELECT stock FROM Product WHERE product_name = ?";
            preparedStatement = connection.prepareStatement(checkStock);
            preparedStatement.setString(1, card_product_name.getText());
            resultSet = preparedStatement.executeQuery();

            // Retrieve the stock value if the product exists
            if (resultSet.next()) {
                checkStck = resultSet.getInt("stock");
            }

            // If stock is zero, update the product status to "No disponible"
            if (checkStck == 0) {
                String updateStock = "UPDATE Product SET status = ? WHERE product_id = ?";
                preparedStatement = connection.prepareStatement(updateStock);
                preparedStatement.setString(1, "No disponible");
                preparedStatement.setString(2, productID);
                preparedStatement.executeUpdate();
            }

            // Check the availability status of the product
            preparedStatement = connection.prepareStatement(checkAvailable);
            preparedStatement.setString(1, productID);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                check = resultSet.getString("status");
            }

            // Display error alert if the product is not available or quantity is zero
            if (!check.equals("Disponible") || quantity == 0) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Error inesperado");
                alert.showAndWait();
            } else {
                // Check if the selected quantity exceeds available stock
                if (checkStck < card_spinner.getValue()) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("No queda stock.");
                    alert.showAndWait();
                } else {
                    // Insert the order details into the Customer table
                    String insertData = "INSERT INTO Customer (customer_id, product_id, product_name, quantity, price, date, em_username) VALUES(?,?,?,?,?,?,?)";
                    preparedStatement = connection.prepareStatement(insertData);
                    preparedStatement.setString(1, String.valueOf(UserDetail.getCustomerID()));
                    preparedStatement.setString(2, productID);
                    preparedStatement.setString(3, card_product_name.getText());
                    preparedStatement.setString(4, card_spinner.getValue().toString());
                    total = (quantity * pr);
                    preparedStatement.setString(5, String.valueOf(total));
                    preparedStatement.setString(6, String.valueOf(sqlDate));
                    preparedStatement.setString(7, UserDetail.getUsername());
                    preparedStatement.executeUpdate();

                    // Update the stock of the product after the order
                    int upStock = checkStck - quantity;

                    // Format the product image path for database storage
                    prod_image = prod_image.replace("\\", "\\\\");

                    // Update the product details in the Product table
                    String updateStock = "UPDATE Product SET product_name =?, type = ?, stock=?, price = ?, status = ?, image = ?, date =? WHERE product_id = ?";
                    preparedStatement = connection.prepareStatement(updateStock);
                    preparedStatement.setString(1, card_product_name.getText());
                    preparedStatement.setString(2, type);
                    preparedStatement.setString(3, String.valueOf(upStock));
                    preparedStatement.setString(4, String.valueOf(pr));
                    preparedStatement.setString(5, check);
                    preparedStatement.setString(6, prod_image);
                    preparedStatement.setString(7, String.valueOf(sqlDate));
                    preparedStatement.setString(8, productID);
                    preparedStatement.executeUpdate();

                    // Display success alert to the user
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Mensaje de informacion");
                    alert.setHeaderText(null);
                    alert.setContentText("Añadido con exito");
                    alert.showAndWait();

                    mainController.menuGetTotal();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initializes the controller class.
     * This method is called automatically after the FXML file is loaded.
     * It sets up the spinner component with a range of values for user selection.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        card_spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0));
    }
}
