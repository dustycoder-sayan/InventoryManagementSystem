package com.inventoryui;

import com.inventory.DAO.CustomerDAO;
import com.inventory.DAO.IssueProductDAO;
import com.inventory.DAO.ProductsDAO;
import com.inventory.DAO.UsersDAO;
import com.inventory.DTO.CustomerDTO;
import com.inventory.DTO.IssueProductNamesDTO;
import com.inventory.DTO.ProductsSupNameDTO;
import com.inventory.DataSource.Admin;
import com.inventory.DataSource.Seller;
import com.inventory.database.ConnectionFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;

public class SellProduct {
    // TODO: This program has to use main connection to get username of user
    private String username;
    private Connection conn;
    private BorderPane layout;
    private Label userUsername, userCategory, userTotalEarnings, userTotalEarningsLabel;

    public SellProduct(String username, Connection conn, BorderPane layout) {
        this.username = username;
        this.conn = conn;
        this.layout = layout;
    }

    public void start() {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Sell Product");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label custNameLabel = new Label("Customer Name:");
        custNameLabel.setFont(new Font("Arial", 15));
        grid.add(custNameLabel, 0, 1);

        TextField custName = new TextField();
        grid.add(custName, 1, 1);

        Label custContactLabel = new Label("Customer Contact No:");
        custContactLabel.setFont(new Font("Arial", 15));
        grid.add(custContactLabel, 0, 2);

        TextField custContact = new TextField();
        grid.add(custContact, 1, 2);

        Label custLocationLabel = new Label("Customer Location:");
        custLocationLabel.setFont(new Font("Arial", 15));
        grid.add(custLocationLabel, 0, 3);

        TextField custLocation = new TextField();
        grid.add(custLocation, 1, 3);

        Label prodIdLabel = new Label("Product ID:");
        prodIdLabel.setFont(new Font("Arial", 15));
        grid.add(prodIdLabel, 0, 4);

        TextField prodId = new TextField();
        grid.add(prodId, 1, 4);

        Label quantityLabel = new Label("Quantity:");
        quantityLabel.setFont(new Font("Arial", 15));
        grid.add(quantityLabel, 0, 5);

        TextField quantity = new TextField();
        grid.add(quantity, 1, 5);

        Button submit = new Button("Submit");
        submit.setFont(new Font("Arial", 15));
        submit.setTextFill(Color.DARKCYAN);
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER);
        hbBtn.getChildren().add(submit);
        grid.add(hbBtn, 1, 7);

        submit.setOnAction(e -> {
            try {
                boolean stockAvailable = true;
                try {
                    stockAvailable = new ProductsDAO(conn).checkStock(Integer.parseInt(prodId.getText()), Integer.parseInt(quantity.getText()));
                } catch (SQLException e1) {
                    System.out.println(e1.getMessage());
                }
                if (!stockAvailable) {
                    AlertBox2.alert("Unsuccessful", "Stock Insufficient");
                    primaryStage.close();
                    return;
                }

                CustomerDTO customerDTO = new CustomerDTO();
                customerDTO.setCustomerName(custName.getText());
                customerDTO.setCustomerPhone(custContact.getText());
                customerDTO.setCustomerLocation(custLocation.getText());
                int custId = -1;
                try {
                    custId = new CustomerDAO(conn).addCustomer(customerDTO);
                } catch (SQLException e1) {
                    System.out.println(e1.getMessage());
                }
                if (custId == -1) {
                    AlertBox2.alert("Unsuccessful", "Customer Could not be Added");
                    primaryStage.close();
                    return;
                }

                int uId = new UsersDAO(conn).getUserId(username);
                boolean sold = new Seller(conn, username).sellProduct(Integer.parseInt(prodId.getText()), custId, uId, Integer.parseInt(quantity.getText()));
                if (sold) {
                    Label about = new Label("Personal Details");    // TODO: If possible, Image of User
                    about.setPadding(new Insets(10, 10, 10, 10));
                    Label userName1 = new Label(new Seller(conn, username).getSellerFullName());  // Todo: Get all from table
                    userName1.setPadding(new Insets(20, 10, 10, 10));
                    userName1.setAlignment(Pos.CENTER);
                    userName1.setFont(new Font("Calibri", 20));
                    userUsername = new Label(username);
                    userUsername.setPadding(new Insets(10, 10, 10, 10));
                    userUsername.setAlignment(Pos.CENTER);
                    userCategory = new Label("Seller");
                    userCategory.setPadding(new Insets(10, 10, 10, 10));
                    userCategory.setAlignment(Pos.CENTER);
                    Label userPhone1 = new Label(new Seller(conn,username).getSellerPhone());
                    userPhone1.setPadding(new Insets(10, 10, 10, 10));
                    userPhone1.setAlignment(Pos.CENTER);
                    Label userLocation1 = new Label(new Seller(conn,username).getSellerLocation());
                    userLocation1.setPadding(new Insets(10, 10, 10, 10));
                    userLocation1.setAlignment(Pos.CENTER);

                    userTotalEarningsLabel = new Label("Total Sold: ");
                    userTotalEarningsLabel.setPadding(new Insets(10, 10, 10, 10));
                    userTotalEarningsLabel.setAlignment(Pos.CENTER);
                    userTotalEarningsLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));

                    double totalEarnings = new Seller(conn, username).getTotalEarnings();
                    if(totalEarnings == -1)
                        totalEarnings=0;

                    userTotalEarnings = new Label(String.valueOf(totalEarnings));
                    userTotalEarnings.setPadding(new Insets(10, 10, 10, 10));
                    userTotalEarnings.setAlignment(Pos.CENTER);
                    userTotalEarnings.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));

                    HBox spacing = new HBox();
                    spacing.setMinHeight(50);
                    VBox right = new VBox(20);
                    right.setMinHeight(775);
                    right.setBackground(new Background(new BackgroundFill(Color.WHITE,
                            CornerRadii.EMPTY, Insets.EMPTY)));
                    right.setMinWidth(250);
                    right.getChildren().addAll(userName1, userCategory, userUsername, userPhone1, userLocation1, userTotalEarningsLabel,
                            userTotalEarnings);
                    right.setAlignment(Pos.TOP_CENTER);
                    right.setBorder(new Border(new BorderStroke(Color.LIGHTBLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3))));
                    layout.setRight(right);
                    AlertBox2.alert("Successful", "Product Sold Successfully");
                    primaryStage.close();
                } else {
                    AlertBox2.alert("Unsuccessful", "Product Could not be sold");
                    primaryStage.close();
                }
            } catch (Exception ex) {
                AlertBox2.alert("Unsuccessful", "Wrong Value Made");
                primaryStage.close();
            }
        });

        Scene scene = new Scene(grid, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
