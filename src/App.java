/*
 * Name: Staff Database
 * Author: Leah Boalich
 * Date: October 5, 2024
 * Assignment: Chapter 34 Exercise 1
 * Description: This program connects to a Staff database.  The user can view, add, and update records.
 */

/* Imports */
import java.sql.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/* View, add, or update Staff Database */
public class App extends Application {
    // Attributes
    private PreparedStatement preparedView;
    private PreparedStatement preparedInsert;
    private PreparedStatement preparedUpdate;
    private Label lblStatus = new Label("Status");
    private TextField tfID = new TextField();
    private TextField tfLast = new TextField();
    private TextField tfFirst = new TextField();
    private TextField tfMI = new TextField();
    private TextField tfAddress = new TextField();
    private TextField tfCity = new TextField();
    private TextField tfState = new TextField();
    private TextField tfPhone = new TextField();

    // Override the start method in the Application class
    public void start(Stage primaryStage) {
        // Initialize database connection and create a Statement object
        initializeDB();

        // Create status line
        HBox hBoxStatus = new HBox();
        hBoxStatus.getChildren().addAll(lblStatus);

        // Create id line
        Label lbID = new Label("ID", tfID);
        lbID.setContentDisplay(ContentDisplay.RIGHT);
        HBox hBoxID = new HBox();
        hBoxID.getChildren().addAll(lbID);

        // Create name line
        Label lbLast = new Label("Last Name", tfLast);
        Label lbFirst = new Label("First Name", tfFirst);
        Label lbMI = new Label("MI", tfMI);
        lbLast.setContentDisplay(ContentDisplay.RIGHT);
        lbFirst.setContentDisplay(ContentDisplay.RIGHT);
        lbMI.setContentDisplay(ContentDisplay.RIGHT);
        HBox hBoxName = new HBox();
        hBoxName.getChildren().addAll(lbLast, lbFirst, lbMI);

        // Create address line
        Label lbAddress = new Label("Address", tfAddress);
        lbAddress.setContentDisplay(ContentDisplay.RIGHT);
        HBox hBoxAddress = new HBox();
        hBoxAddress.getChildren().addAll(lbAddress);

        // Create address2 line
        Label lbCity = new Label("City", tfCity);
        Label lbState = new Label("State", tfState);
        lbCity.setContentDisplay(ContentDisplay.RIGHT);
        lbState.setContentDisplay(ContentDisplay.RIGHT);
        HBox hBoxAddress2 = new HBox();
        hBoxAddress2.getChildren().addAll(lbCity, lbState);

        // Create phone line
        Label lbPhone = new Label("Telephone", tfPhone);
        lbPhone.setContentDisplay(ContentDisplay.RIGHT);
        HBox hBoxPhone = new HBox();
        hBoxPhone.getChildren().addAll(lbPhone);

        // Create button line
        Button btView = new Button("View");
        Button btInsert = new Button("Insert");
        Button btUpdate = new Button("Update");
        Button btClear = new Button("Clear");
        HBox hBoxButtons = new HBox();
        hBoxButtons.getChildren().addAll(btView, btInsert, btUpdate, btClear);
        hBoxButtons.setAlignment(Pos.CENTER);

        // Set button event handlers
        btView.setOnAction(e -> view());
        btInsert.setOnAction(e -> insert());
        btUpdate.setOnAction(e -> update());
        btClear.setOnAction(e -> clear());

        // Create vBox to hold all hBoxes
        VBox vBox = new VBox();
        vBox.getChildren().addAll(hBoxStatus, hBoxID, hBoxName, hBoxAddress, hBoxAddress2, hBoxPhone, hBoxButtons);
        vBox.setPadding(new Insets(5));

        /* Create scene and place it in the stage */
        Scene scene = new Scene(vBox);
        // set the stage title
        primaryStage.setTitle("ExtraExercise 34_01");
        // Place the scene in the stage
        primaryStage.setScene(scene);
        // Display the stage
        primaryStage.show();
    }

    // Initalize database
    private void initializeDB() {
        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded");

            // Establish a connection
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/Ch34Ex1", "root", "");
            System.out.println("Database connected");

            /* 
            // Create a statement
            stmt = connection.createStatement();

            // Create table
            stmt.executeUpdate("create table Staff (id char(9) not null, lastName varchar(15), firstName varchar(15), mi char(1), address varchar(20), city varchar(20), state char(2), telephone char(10), email varchar(40), primary key (id))");
            */

            // Create view string and statement
            String viewString = "select * from Staff where id = ?";
            preparedView = connection.prepareStatement(viewString);

            // Create insert string and statement
            String insertString = "insert into Staff (id, lastName, firstName, mi, address, city, state, telephone) values(?, ?, ?, ?, ?, ?, ?, ?)";
            preparedInsert = connection.prepareStatement(insertString);

            // Create update string and statement
            String updateString = "update Staff" + 
            " set lastName = ?, firstName = ?, mi = ?, address = ?, city = ?, state = ?, telephone = ?" + 
            " where id = ?";
            preparedUpdate = connection.prepareStatement(updateString);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }  
    
    /* View the record for the entered id */
    private void view() {
        // Get the entered id
        String id = tfID.getText();
        // Get the corresponding info if it exists
        try {
            // Set statement parameters
            preparedView.setString(1, id);
            // Get results
            ResultSet rset = preparedView.executeQuery();
            // Set returned data to appropriate variables
            if (rset.next()) {
                String rsetLast = rset.getString(2);
                String rsetFirst = rset.getString(3);
                String rsetMI = rset.getString(4);
                String rsetAddress = rset.getString(5);
                String rsetCity = rset.getString(6);
                String rsetState = rset.getString(7);
                String rsetPhone = rset.getString(8);

                // Display results
                tfLast.setText(rsetLast);
                tfFirst.setText(rsetFirst);
                tfMI.setText(rsetMI);
                tfAddress.setText(rsetAddress);
                tfCity.setText(rsetCity);
                tfState.setText(rsetState);
                tfPhone.setText(rsetPhone);
                lblStatus.setText("Status: Found");
            }
            else {
                lblStatus.setText("Status: Not found");
            }
            
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /* Insert the record to the table */
    private void insert() {
        String id = tfID.getText();
        String last = tfLast.getText();
        String first = tfFirst.getText();
        String mi = tfMI.getText();
        String address = tfAddress.getText();
        String city = tfCity.getText();
        String state = tfState.getText();
        String phone = tfPhone.getText();

        try {
            // Set statement parameters
            preparedInsert.setString(1, id);
            preparedInsert.setString(2, last);
            preparedInsert.setString(3, first);
            preparedInsert.setString(4, mi);
            preparedInsert.setString(5, address);
            preparedInsert.setString(6, city);
            preparedInsert.setString(7, state);
            preparedInsert.setString(8, phone);

            // Insert to table
            preparedInsert.executeUpdate();

            // Update status label
            lblStatus.setText("Status: Record Inserted");
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /* Clear the displayed data */
    private void clear() {
        // Clear fields
        tfID.setText(null);
        tfLast.setText(null);
        tfFirst.setText(null);
        tfMI.setText(null);
        tfAddress.setText(null);
        tfCity.setText(null);
        tfState.setText(null);
        tfPhone.setText(null);
        lblStatus.setText("Status: Ready");
    }

    /* Update the record based on the displayed id */
    private void update() {
        String id = tfID.getText();
        String last = tfLast.getText();
        String first = tfFirst.getText();
        String mi = tfMI.getText();
        String address = tfAddress.getText();
        String city = tfCity.getText();
        String state = tfState.getText();
        String phone = tfPhone.getText();

        try {
            // Set statement parameters
            preparedUpdate.setString(1, last);
            preparedUpdate.setString(2, first);
            preparedUpdate.setString(3, mi);
            preparedUpdate.setString(4, address);
            preparedUpdate.setString(5, city);
            preparedUpdate.setString(6, state);
            preparedUpdate.setString(7, phone);
            preparedUpdate.setString(8, id);

            // Update the table
            preparedUpdate.executeUpdate();

            // Update status label
            lblStatus.setText("Status: Record Updated");
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
