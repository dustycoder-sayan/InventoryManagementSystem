package com.inventoryui;

import com.inventory.DAO.CustomerDAO;
import com.inventory.DAO.IssueProductDAO;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;

public class SellProduct {
    // TODO: This program has to use main connection to get username of user
    static String username;
    public SellProduct(String username) {
        SellProduct.username = username;
    }

    public static void start() {
        Stage primaryStage = new Stage();
        Connection conn = ConnectionFactory.getInstance().open();
        primaryStage.setTitle("Sell Product");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        TextField custName = new TextField();
        custName.setPromptText("Customer Name");
        grid.add(custName, 0, 1);

        TextField custContact = new TextField();
        custContact.setPromptText("Customer Contact");
        grid.add(custContact, 1, 1);

        TextField custLocation = new TextField();
        custLocation.setPromptText("Customer Location");
        grid.add(custLocation, 0, 2);

        TextField prodId = new TextField();
        prodId.setPromptText("Product ID");
        grid.add(prodId, 0, 3);

        TextField quantity = new TextField();
        quantity.setPromptText("Quantity");
        grid.add(quantity, 1, 3);

        Button submit = new Button("Submit");
        submit.setFont(new Font("Arial", 15));
        submit.setTextFill(Color.DARKCYAN);
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER_LEFT);
        hbBtn.getChildren().add(submit);
        grid.add(hbBtn, 0, 4);

        submit.setOnAction(e -> {
            CustomerDTO customerDTO = new CustomerDTO();
            customerDTO.setCustomerName(custName.getText());
            customerDTO.setCustomerPhone(custContact.getText());
            customerDTO.setCustomerLocation(custLocation.getText());
            int custId=-1;
            try {
                custId = new CustomerDAO(conn).addCustomer(customerDTO);
            } catch (SQLException e1) {
                System.out.println(e1.getMessage());
            }
            if(custId == -1) {
                AlertBox2.alert("Unsuccessful", "Customer Could not be Added");
                try {
                    conn.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                primaryStage.close();
                return;
            }
            int uId = new UsersDAO(conn).getUserId(username);
            boolean sold = new Seller(conn, username).sellProduct(Integer.parseInt(prodId.getText()), custId, uId, Integer.parseInt(quantity.getText()));
            if(sold) {
                AlertBox2.alert("Successful", "Product Sold Successfully");
                try {
                    conn.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                primaryStage.close();
            } else {
                AlertBox2.alert("Unsuccessful", "Product Could not be sold");
                try {
                    conn.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                primaryStage.close();
            }
        });

        Scene scene = new Scene(grid, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
