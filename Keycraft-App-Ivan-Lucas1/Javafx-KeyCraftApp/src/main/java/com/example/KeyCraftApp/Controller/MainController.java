package com.example.KeyCraftApp.Controller;

import com.example.KeyCraftApp.App;
import com.example.KeyCraftApp.Database.Database;
import com.example.KeyCraftApp.Model.CustomerModel;
import com.example.KeyCraftApp.Model.ProductData;
import com.example.KeyCraftApp.Model.UserDetail;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    public Label username;
    public Button dashboard_button;
    public Button inventory_button;
    public Button menu_button;
    public Button log_out_button;
    public AnchorPane inventory_form;
    public TableView<ProductData> inventory_tableview;
    public TableColumn<ProductData, String> inventory_product_id;
    public TableColumn<ProductData, String> inventory_product_name;
    public TableColumn<ProductData, String> inventory_product_type;
    public TableColumn<ProductData, String> inventory_product_stock;
    public TableColumn<ProductData, String> inventory_product_price;
    public TableColumn<ProductData, String> inventory_product_status;
    public TableColumn<ProductData, String> inventory_product_date;
    public TextField product_id_textfield;
    public TextField product_name_textfield;
    public ComboBox<String> type_combobox;
    public TextField stock_textfield;
    public TextField price_textfield;
    public ImageView display_selected_image;
    public Button choose_image_button;
    public Button add_button;
    public Button update_button;
    public Button delete_button;
    public Button clear_button;
    public ComboBox<String> status_combobox;
    public AnchorPane main_form;
    public ScrollPane menu_scroll_pane;
    public GridPane menu_grid_pane;
    public AnchorPane menu_section;
    public TableView<CustomerModel> menu_table_view;
    public TableColumn<CustomerModel, String> menu_product_name;
    public TableColumn<CustomerModel, String> menu_price;
    public TableColumn<CustomerModel, String> menu_quantity;
    public Label menu_total;
    public TextField menu_amount_textfield;
    public Label menu_change;
    public Button menu_pay_btn;
    public Button menu_remove_btn;
    public AnchorPane dashboard_section;

    private Alert alert;

    private Image image;
    private String[] list = {
            "Casing",
            "Switch",
            "Plate",
            "PCB",
            "Keycaps",
    };

    ObservableList<String> typeList = FXCollections.observableArrayList(list);

    private String[] status = {
            "Disponible",
            "No disponible"
    };

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private final ObservableList<ProductData> cardListData = FXCollections.observableArrayList();
    LoginController loginController = new LoginController();

    /**
     * Retrieves product data from the database and returns it as an ObservableList.
     * Executes an SQL query to fetch all records from the "Product" table,
     * maps each record to a ProductData object, and adds it to the list.
     *
     * @return ObservableList<ProductData> containing all product records from the database
     */
    public ObservableList<ProductData> menuGetData() {
        String sql = "SELECT * FROM Product";
        connection = Database.connectionDB();
        ObservableList<ProductData> listData = FXCollections.observableArrayList();
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            ProductData productData;

            // Iterate through the result set and map each record to a ProductData object
            while (resultSet.next()) {
                productData = new ProductData(
                        resultSet.getInt("id"),
                        resultSet.getString("product_id"),
                        resultSet.getString("product_name"),
                        resultSet.getString("type"),
                        resultSet.getInt("stock"),
                        resultSet.getDouble("price"),
                        resultSet.getString("status"),
                        resultSet.getString("image"),
                        resultSet.getString("date")
                );
                listData.add(productData); // Add the product data to the list
            }
        } catch (SQLException e) {
            throw new RuntimeException(e); // Handle SQL exceptions
        }
        return listData; // Return the list of product data
    }

    /**
     * Displays product cards in a grid layout on the menu screen.
     * Clears existing data and layout, retrieves product data,
     * and populates the grid with product cards using FXML templates.
     */
    public void menuDisplayCard() {

        cardListData.clear();
        cardListData.addAll(menuGetData());

        int row = 0;
        int column = 0;

        menu_grid_pane.getRowConstraints().clear();
        menu_grid_pane.getColumnConstraints().clear();
        menu_grid_pane.getChildren().clear();

        // Iterate over each product and create a card
        for (ProductData cardListDatum : cardListData) {

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("FXML/CardProduct.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                CardProductController cardProductController = fxmlLoader.getController();
                cardProductController.setData(cardListDatum);

                if (column == 3) {
                    column = 0;
                    row += 1;
                }
                column++;
                menu_grid_pane.add(anchorPane, column, row);
                GridPane.setMargin(anchorPane, new Insets(10));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private int customerID;

    private ObservableList<CustomerModel> menuListData;

    public void menuShowData() {
        menuListData = menuDisplayOrder();
        menu_product_name.setCellValueFactory(new PropertyValueFactory<>("product_name"));
        menu_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        menu_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        menu_table_view.setItems(menuListData);
    }

    private String totalPrice = "";

    /**
     * Calculates the total price of products for the current user.
     * Executes an SQL query to sum the "price" column from the "Customer" table
     * where the username matches the logged-in user's username.
     */
    public void menuGetTotal() {

        String total = "SELECT SUM(price) FROM Customer WHERE em_username = ?"; // SQL query to calculate total price
        connection = Database.connectionDB();
        String user = UserDetail.getUsername();

        try {
            preparedStatement = connection.prepareStatement(total);
            preparedStatement.setString(1, user);
            resultSet = preparedStatement.executeQuery();

            // Retrieve the total price from the result set
            while (resultSet.next()) {
                totalPrice = resultSet.getString("SUM(price)");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Displays the total price on the menu screen.
     * Calls the method to calculate the total price and sets the total amount
     * in the designated UI component with a currency symbol.
     */
    public void menuDisplayTotal() {
        menuGetTotal(); // Calculate the total price
        menu_total.setText("€" + totalPrice); // Display the total price with a currency symbol
    }


    private double change;
    private double amount;
    private double tPrice;

    /**
     * Handles the amount entered by the user for payment and calculates the change.
     * Validates the input and compares it with the total price to ensure sufficient payment.
     * Displays an error alert if the input is invalid or insufficient.
     * Otherwise, calculates and displays the change.
     */
    public void menuAmount() {
        menuGetTotal();

        // Validate input and total price
        if (menu_amount_textfield.getText().isEmpty() || totalPrice.equals("0")) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalido");
            alert.showAndWait();
        } else {
            amount = Double.parseDouble(menu_amount_textfield.getText()); // Parse user input as a double
            tPrice = Double.parseDouble(totalPrice); // Parse total price as a double

            // Check if the entered amount is less than the total price
            if (amount < tPrice) {
                menu_amount_textfield.setText(""); // Clear the input field
            } else {
                change = (amount - tPrice); // Calculate the change
                menu_change.setText("€" + change); // Display the change with a currency symbol
            }
        }
    }

    /**
     * Handles the payment process when the pay button is clicked.
     * Validates the total price and payment amount, confirms the transaction,
     * inserts payment details into the database, and provides user feedback.
     */
    public void menuPayBtn() {
        if (tPrice == 0) {
            // Show error alert if no order is selected
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, seleccione su pedido primero");
            alert.showAndWait();
        } else {
            String insertPay = "INSERT INTO Receipt (customer_id, total, date, em_username) VALUES(?,?,?,?)";
            connection = Database.connectionDB(); // Establish database connection
            try {
                if (amount == 0) {
                    // Show error alert if payment amount is invalid
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error ");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid :2");
                    alert.showAndWait();
                }
                // Get the current date
                Date date = new Date();
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());

                // Show confirmation alert
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmar Mensaje");
                alert.setHeaderText(null);
                alert.setContentText("¿Estás seguro?");
                Optional<ButtonType> optional = alert.showAndWait();

                if (optional.get().equals(ButtonType.OK)) {
                    // Insert payment details into the database
                    preparedStatement = connection.prepareStatement(insertPay);
                    preparedStatement.setString(1, String.valueOf(customerID));
                    preparedStatement.setString(2, String.valueOf(tPrice));
                    preparedStatement.setString(3, String.valueOf(sqlDate));
                    preparedStatement.setString(4, UserDetail.getUsername());
                    preparedStatement.executeUpdate();

                    menuShowData(); // Update the menu data

                    // Show success alert
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Mensaje");
                    alert.setHeaderText(null);
                    alert.setContentText("¡Compra realizada!");
                    alert.showAndWait();

                    menuShowData(); // Refresh menu data
                    menuRestart(); // Restart the menu
                } else {
                    // Show warning alert if the user cancels
                    alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Atencion");
                    alert.setHeaderText(null);
                    alert.setContentText("Error.");
                    alert.showAndWait();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Resets the menu fields and variables to their default values.
     * Clears the total price, change, and amount, and updates the UI components accordingly.
     */
    public void menuRestart() {
        tPrice = 0;
        change = 0;
        amount = 0;
        menu_amount_textfield.setText("$0.0");
        menu_total.setText("");
        menu_change.setText("$0.0");
    }

    /**
     * Retrieves and displays the order details for a specific customer.
     * Fetches data from the database based on the customer ID and populates it into an observable list.
     *
     * @return ObservableList<CustomerModel> containing the customer's order details.
     */
    public ObservableList<CustomerModel> menuDisplayOrder() {
        getCustomerID();
        ObservableList<CustomerModel> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Customer WHERE customer_id = ?"; // SQL query to fetch customer data
        connection = Database.connectionDB();

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, String.valueOf(customerID));
            resultSet = preparedStatement.executeQuery();

            CustomerModel customerModel;

            // Iterate through the result set and populate the list
            while (resultSet.next()) {
                customerModel = new CustomerModel(
                        resultSet.getInt("id"),
                        resultSet.getString("customer_id"),
                        resultSet.getString("product_id"),
                        resultSet.getString("product_name"),
                        resultSet.getString("quantity"),
                        resultSet.getString("price"),
                        resultSet.getString("date"),
                        resultSet.getString("em_username")
                );
                listData.add(customerModel); // Add the customer model to the list
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listData;
    }

    /**
     * Retrieves and sets the next customer ID based on the maximum customer IDs in the Customer and Receipt tables.
     * Ensures the customer ID is incremented appropriately if it matches the latest ID in the Receipt table.
     */
    public void getCustomerID() {
        String sql = "SELECT MAX(customer_id) FROM Customer"; // Query to get the highest customer ID from the Customer table
        connection = Database.connectionDB();

        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                customerID = resultSet.getInt("MAX(customer_id)");
            }

            String checkCustomersID = "SELECT MAX(customer_id) FROM Receipt"; // Query to get the highest customer ID from the Receipt table
            preparedStatement = connection.prepareStatement(checkCustomersID);
            resultSet = preparedStatement.executeQuery();

            int checkID = 0; // Variable to store the maximum customer ID from Receipt
            if (resultSet.next()) {
                checkID = resultSet.getInt("MAX(customer_id)");
            }

            // Increment customer ID if necessary
            if (customerID == 0) {
                customerID += 1;
            } else if (customerID == checkID) {
                customerID += 1;
            }

            UserDetail.setCustomerID(customerID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles the addition of a new product to the inventory.
     * Validates input fields, checks for duplicate product IDs, and inserts the new product data into the database.
     * Updates the inventory display and clears the input fields upon successful addition.
     */
    public void inventoryAddBtn() {
        // Validate input fields
        if (product_id_textfield.getText().isEmpty()
                || product_name_textfield.getText().isEmpty()
                || stock_textfield.getText().isEmpty()
                || price_textfield.getText().isEmpty()
                || type_combobox.getSelectionModel().getSelectedItem() == null
                || status_combobox.getSelectionModel().getSelectedItem() == null
                || UserDetail.getPath() == null
        ) {
            loginController.fillAllFieldError(); // Show error for missing fields
        } else {
            String checkProductID = "SELECT product_id FROM Product WHERE product_id = ?"; // Query to check for duplicate product IDs
            connection = Database.connectionDB();

            try {
                preparedStatement = connection.prepareStatement(checkProductID);
                preparedStatement.setString(1, product_id_textfield.getText());
                resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    // Show error if the product ID already exists
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText(product_id_textfield.getText() + " ya existe.");
                } else {
                    // Insert new product data into the database
                    String insertData = "INSERT INTO Product (product_id, product_name, type, stock, price, status, image, date) VALUES(?,?,?,?,?,?,?,?)";
                    Date date = new Date(); // Get the current date
                    java.sql.Date _date = new java.sql.Date(date.getTime()); // Convert to SQL date
                    String path = UserDetail.getPath(); // Get the image path
                    path = path.replace("\\", "\\\\"); // Replace backslashes for compatibility

                    preparedStatement = connection.prepareStatement(insertData); // Prepare the SQL statement
                    preparedStatement.setString(1, product_id_textfield.getText());
                    preparedStatement.setString(2, product_name_textfield.getText());
                    preparedStatement.setString(3, type_combobox.getSelectionModel().getSelectedItem());
                    preparedStatement.setString(4, stock_textfield.getText());
                    preparedStatement.setString(5, price_textfield.getText());
                    preparedStatement.setString(6, status_combobox.getSelectionModel().getSelectedItem());
                    preparedStatement.setString(7, path);
                    preparedStatement.setString(8, String.valueOf(_date));
                    preparedStatement.executeUpdate();

                    inventoryShowData();
                    getSuccessAlert("Añadido");
                    inventoryClearBtn();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Handles the deletion of a product from the inventory.
     * Validates input fields and confirms the deletion before removing the product from the database.
     * Updates the inventory display and clears the input fields upon successful deletion.
     */
    public void inventoryDeleteBtn() {
        // Validate input fields
        if (product_id_textfield.getText().isEmpty()
                || product_name_textfield.getText().isEmpty()
                || stock_textfield.getText().isEmpty()
                || price_textfield.getText().isEmpty()
                || type_combobox.getSelectionModel().getSelectedItem() == null
                || status_combobox.getSelectionModel().getSelectedItem() == null
                || UserDetail.getPath() == null
                || UserDetail.getId() == 0
        ) {
            loginController.fillAllFieldError(); // Show error for missing fields
        } else {
            String deleteData = "DELETE FROM Product WHERE id = ?"; // Query to delete a product by its ID
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert");
            alert.setHeaderText(null);
            alert.setContentText("¿Seguro que quieres borrar el producto con ID : " + product_id_textfield.getText() + "?");
            connection = Database.connectionDB();
            Optional<ButtonType> optional = alert.showAndWait();

            if (optional.get().equals(ButtonType.OK)) {
                try {
                    preparedStatement = connection.prepareStatement(deleteData);
                    preparedStatement.setString(1, String.valueOf(UserDetail.getId()));
                    preparedStatement.executeUpdate();

                    getSuccessAlert("Eliminado"); // Show success alert

                    inventoryShowData(); // Refresh the inventory display
                    inventoryClearBtn(); // Clear input fields
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                // Show error alert if deletion is canceled
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error ");
                alert.setHeaderText(null);
                alert.setContentText("Cancelado");
                alert.showAndWait();
            }
        }
    }

    /**
     * Handles the update of an existing product in the inventory.
     * Validates input fields and confirms the update before modifying the product data in the database.
     * Updates the inventory display and clears the input fields upon successful update.
     */
    public void inventoryUpdateBtn() {
        // Validate input fields
        if (product_id_textfield.getText().isEmpty()
                || product_name_textfield.getText().isEmpty()
                || stock_textfield.getText().isEmpty()
                || price_textfield.getText().isEmpty()
                || type_combobox.getSelectionModel().getSelectedItem() == null
                || status_combobox.getSelectionModel().getSelectedItem() == null
                || UserDetail.getPath() == null
                || UserDetail.getId() == 0
        ) {
            loginController.fillAllFieldError(); // Show error for missing fields
        } else {
            String path = UserDetail.getPath(); // Get the image path
            String updateData = "UPDATE Product SET product_id = ?, product_name=?, type=?, stock=?, price =?, status=?, image=?, date=? WHERE id= ?"; // Query to update product data
            connection = Database.connectionDB();
            Date date = new Date();
            java.sql.Date _date = new java.sql.Date(date.getTime()); // Convert to SQL date

            try {
                preparedStatement = connection.prepareStatement(updateData); // Prepare the SQL statement
                preparedStatement.setString(1, product_id_textfield.getText()); // Set product ID
                preparedStatement.setString(2, product_name_textfield.getText()); // Set product name
                preparedStatement.setString(3, type_combobox.getSelectionModel().getSelectedItem()); // Set product type
                preparedStatement.setString(4, stock_textfield.getText()); // Set stock quantity
                preparedStatement.setString(5, price_textfield.getText()); // Set price
                preparedStatement.setString(6, status_combobox.getSelectionModel().getSelectedItem()); // Set status
                preparedStatement.setString(7, path); // Set image path
                preparedStatement.setString(8, String.valueOf(_date)); // Set date
                preparedStatement.setString(9, String.valueOf(UserDetail.getId())); // Set product ID for the WHERE clause

                alert = new Alert(Alert.AlertType.CONFIRMATION); // Confirmation alert
                alert.setTitle("Alert");
                alert.setHeaderText(null);
                alert.setContentText("¿Seguro que quieres actualizar el producto con ID : " + product_id_textfield.getText());

                Optional<ButtonType> optional = alert.showAndWait();
                if (optional.get().equals(ButtonType.OK)) {
                    preparedStatement.executeUpdate();
                    inventoryShowData();
                    getSuccessAlert("Actualizado");
                    inventoryClearBtn();
                } else {
                    // Show error alert if update is canceled
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error ");
                    alert.setHeaderText(null);
                    alert.setContentText("Cancelado");
                    alert.showAndWait();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Handles the importing of an image file for a product.
     * Opens a file chooser dialog to select an image file and displays the selected image in the UI.
     * Updates the user's detail with the file path of the selected image.
     */
    public void inventoryImportBtn() {
        FileChooser fileChooser = new FileChooser(); // Create a file chooser
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Open Image File", "*.png", "*.jpg") // Filter for image files
        );
        File file = fileChooser.showOpenDialog(main_form.getScene().getWindow());

        if (file != null) { // Check if a file was selected
            UserDetail.setPath(file.getAbsolutePath()); // Set the file path in user details
            image = new Image(file.toURI().toString()); // Load the image
            display_selected_image.setImage(image); // Display the selected image in the UI
        }
    }

    ObservableList<String> observableStatus = FXCollections.observableArrayList(status);

    /**
     * Retrieves the list of product data from the database.
     * Executes a query to fetch all products and maps the result set to an observable list of ProductData objects.
     * This list can be used to display inventory data in the UI.
     *
     * @return ObservableList<ProductData> containing all products from the database.
     */
    public ObservableList<ProductData> inventoryDataList() {
        ObservableList<ProductData> listData = FXCollections.observableArrayList(); // Create an observable list
        String sql = "SELECT * FROM Product"; // SQL query to fetch all products
        connection = Database.connectionDB();

        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            ProductData productData;

            while (resultSet.next()) { // Iterate through the result set
                productData = new ProductData(
                        resultSet.getInt("id"),
                        resultSet.getString("product_id"),
                        resultSet.getString("product_name"),
                        resultSet.getString("type"),
                        resultSet.getInt("stock"),
                        resultSet.getDouble("price"),
                        resultSet.getString("status"),
                        resultSet.getString("image"),
                        resultSet.getString("date")
                );
                listData.add(productData); // Add the product data to the list
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return listData;
    }

    private ObservableList<ProductData> inventoryListData;

    /**
     * Displays the inventory data in the table view.
     * Sets up the table columns with their corresponding property values and populates the table with product data.
     */
    public void inventoryShowData() {
        inventoryListData = inventoryDataList();
        inventory_product_id.setCellValueFactory(new PropertyValueFactory<>("productId"));
        inventory_product_name.setCellValueFactory(new PropertyValueFactory<>("productName"));
        inventory_product_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        inventory_product_stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        inventory_product_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        inventory_product_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        inventory_product_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        inventory_tableview.setItems(inventoryListData);
    }

    /**
     * Handles the logout process for the application.
     * Displays a confirmation dialog to the user and, if confirmed, closes the current window and opens the login screen.
     */
    public void logout() {

        // Create a confirmation alert
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cerrar sesión");
        alert.setHeaderText(null);
        alert.setContentText("¿Seguro que quieres cerrar sesión?");

        // Wait for the user's response
        Optional<ButtonType> optional = alert.showAndWait();
        if (optional.get().equals(ButtonType.OK)) {

            // Hide the current window
            log_out_button.getScene().getWindow().hide();

            // Load the login screen
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("Login.fxml"));
            Stage stage = new Stage();
            try {
                Scene scene = new Scene(fxmlLoader.load());
                stage.setTitle("KeyCraft");
                stage.setMinHeight(430);
                stage.setMinWidth(610);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Retrieves and formats the username for display.
     * Capitalizes the first letter of the username and sets it in the UI.
     */
    public void getUsername() {
        String user = UserDetail.getUsername();
        user = user.substring(0, 1).toUpperCase() + user.substring(1);
        username.setText(user);
    }

    /**
     * Displays a success alert with a custom message.
     * The alert is of type INFORMATION and waits for user acknowledgment.
     *
     * @param message The message to display in the alert.
     */
    public void getSuccessAlert(String message) {
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ok");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Clears all input fields and resets selections in the inventory form.
     * Resets the image display and user details to their default states.
     */
    public void inventoryClearBtn() {
        product_id_textfield.setText("");
        product_name_textfield.setText("");
        type_combobox.getSelectionModel().clearSelection();
        stock_textfield.setText("");
        price_textfield.setText("");
        status_combobox.getSelectionModel().clearSelection();
        UserDetail.setPath("");
        display_selected_image.setImage(null);
        UserDetail.setId(0);
    }

    /**
     * Handles the selection of a product from the inventory table view.
     * Populates the form fields with the selected product's data and updates user details.
     */
    public void inventorySelectedData() {
        ProductData productData = inventory_tableview.getSelectionModel().getSelectedItem();
        int getIndex = inventory_tableview.getSelectionModel().getSelectedIndex();

        if ((getIndex - 1) < -1) { // Check for invalid selection
            return;
        }

        // Populate the form fields with the selected product's data
        product_id_textfield.setText(productData.getProductId());
        product_name_textfield.setText(productData.getProductName());
        type_combobox.setValue(productData.getType());
        stock_textfield.setText(String.valueOf(productData.getStock()));
        price_textfield.setText(String.valueOf(productData.getPrice()));
        status_combobox.setValue(productData.getStatus());

        // Update user details and image display
        UserDetail.setPath("File:" + productData.getImage());
        UserDetail.setDate(productData.getDate());
        UserDetail.setId(productData.getId());
        display_selected_image.setImage(new Image(UserDetail.getPath()));
    }

    /**
     * Switches between different sections of the application based on the button clicked.
     * Displays the corresponding section and hides the others.
     *
     * @param event The action event triggered by button clicks.
     */
    public void switchForm(ActionEvent event) {

        if (event.getSource() == dashboard_button) {
            // Show the dashboard section
            dashboard_section.setVisible(true);
            inventory_form.setVisible(false);
            menu_section.setVisible(false);
        } else if (event.getSource() == inventory_button) {
            // Show the inventory form
            dashboard_section.setVisible(false);
            inventory_form.setVisible(true);
            menu_section.setVisible(false);

            inventoryShowData(); // Update inventory data display
        } else if (event.getSource() == menu_button) {
            // Show the menu section
            dashboard_section.setVisible(false);
            inventory_form.setVisible(false);
            menu_section.setVisible(true);

            menuDisplayCard();
            menuDisplayOrder();
            menuDisplayTotal();
            menuShowData();
        }
    }

    /**
     * Removes the selected product from the shopping cart.
     * Updates the database, adjusts product stock, and refreshes the cart view.
     */
    public void menuRemoveBtn() {
        // Get the selected product from the cart table
        CustomerModel selectedProduct = menu_table_view.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            // Show alert if no product is selected
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, seleccione un producto para eliminar.");
            alert.showAndWait();
        } else {
            // Confirm removal
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación");
            alert.setHeaderText(null);
            alert.setContentText("¿Está seguro de que desea eliminar este producto del carrito?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.isPresent() && option.get() == ButtonType.OK) {
                // Remove the product from the cart
                String deleteQuery = "DELETE FROM Customer WHERE id = ?";
                connection = Database.connectionDB();

                try {
                    preparedStatement = connection.prepareStatement(deleteQuery);
                    preparedStatement.setInt(1, selectedProduct.getId());
                    preparedStatement.executeUpdate();

                    // Increase the stock of the removed product
                    String updateStock = "UPDATE Product SET stock = stock + ? WHERE product_id = ?";
                    preparedStatement = connection.prepareStatement(updateStock);
                    preparedStatement.setInt(1, Integer.parseInt(selectedProduct.getQuantity()));
                    preparedStatement.setString(2, selectedProduct.getProduct_id());
                    preparedStatement.executeUpdate();

                    // Update product status if it was marked as "Unavailable"
                    String checkStockQuery = "SELECT stock FROM Product WHERE product_id = ?";
                    preparedStatement = connection.prepareStatement(checkStockQuery);
                    preparedStatement.setString(1, selectedProduct.getProduct_id());
                    resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        int updatedStock = resultSet.getInt("stock");
                        if (updatedStock > 0) {
                            String updateStatusQuery = "UPDATE Product SET status = ? WHERE product_id = ?";
                            preparedStatement = connection.prepareStatement(updateStatusQuery);
                            preparedStatement.setString(1, "Disponible");
                            preparedStatement.setString(2, selectedProduct.getProduct_id());
                            preparedStatement.executeUpdate();
                        }
                    }

                    // Refresh the cart table and total
                    menuShowData();
                    menuGetTotal();
                    menuDisplayTotal();

                    // Show success message
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Información");
                    alert.setHeaderText(null);
                    alert.setContentText("Producto eliminado del carrito con éxito.");
                    alert.showAndWait();
                } catch (SQLException e) {
                    e.printStackTrace();
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Ocurrió un error al eliminar el producto.");
                    alert.showAndWait();
                }
            }
        }
    }

    /**
     * Initializes the controller class and sets up the event handlers, data bindings, and UI components.
     * This method is automatically called after the FXML file has been loaded.
     *
     * @param url The location used to resolve relative paths for the root object, or null if not known.
     * @param resourceBundle The resources used to localize the root object, or null if not applicable.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Retrieve and display the username
        getUsername();

        // Set up the logout button action
        log_out_button.setOnAction(event -> logout());

        // Set up combo box items
        type_combobox.setItems(typeList);
        status_combobox.setItems(observableStatus);

        // Display inventory data
        inventoryShowData();

        // Set up button actions for inventory management
        choose_image_button.setOnAction(event -> inventoryImportBtn());
        add_button.setOnAction(event -> inventoryAddBtn());
        update_button.setOnAction(event -> inventoryUpdateBtn());
        delete_button.setOnAction(event -> inventoryDeleteBtn());
        clear_button.setOnAction(event -> inventoryClearBtn());

        // Display menu-related data and UI components
        menuDisplayCard();
        menuDisplayOrder();
        menuDisplayTotal();
        menuShowData();
    }
}
