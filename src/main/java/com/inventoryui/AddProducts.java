package com.inventoryui;

import com.inventory.DAO.SuppliersDAO;
import com.inventory.DataSource.Admin;
import com.inventory.database.ConnectionFactory;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

public class AddProducts {
    // todo: if time permits, add label within the scene for "Add Product"
    // todo: if time permits, add label within the scene for each textfield
    private final Connection conn;
    public AddProducts(Connection conn) {
        this.conn = conn;
    }

    public void start() {
        Stage primaryStage = new Stage();

//        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.setTitle("Add Product");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label prodName = new Label("Product Name:");
        prodName.setFont(new Font("Arial", 15));
        grid.add(prodName, 0, 1);

        TextField name = new TextField();
        grid.add(name, 1, 1);

        Label prodBrand = new Label("Product Brand:");
        prodBrand.setFont(new Font("Arial", 15));
        grid.add(prodBrand, 0, 2);

        TextField brand = new TextField();
        grid.add(brand, 1, 2);

        Label costPriceLabel = new Label("Cost Price:");
        costPriceLabel.setFont(new Font("Arial", 15));
        grid.add(costPriceLabel, 0, 3);

        TextField costPrice = new TextField();
        grid.add(costPrice, 1, 3);

        Label sellingPriceLabel = new Label("Selling Price:");
        sellingPriceLabel.setFont(new Font("Arial", 15));
        grid.add(sellingPriceLabel, 0, 4);

        TextField sellingPrice = new TextField();
        grid.add(sellingPrice, 1, 4);

        Label supLabel = new Label("Supplier ID: ");
        supLabel.setFont(new Font("Arial", 15));
        grid.add(supLabel, 0, 5);

        TextField supId = new TextField();
        grid.add(supId, 1, 5);

        Label stockLabel = new Label("Stock: ");
        stockLabel.setFont(new Font("Arial", 15));
        grid.add(stockLabel, 0, 6);

        TextField stock = new TextField();
        grid.add(stock, 1, 6);

        Button submit = new Button("Submit");
        submit.setFont(new Font("Arial", 15));
        submit.setTextFill(Color.DARKCYAN);
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER);
        hbBtn.getChildren().add(submit);
        grid.add(hbBtn, 1, 8);

        submit.setOnAction(e -> {
            try {
                if (!new SuppliersDAO(conn).supplierExists(Integer.parseInt(supId.getText()))) {
                    AlertBox2.alert("Unsuccessful", "Supplier Not found");
                    primaryStage.close();
                    return;
                }

                int stockUse = Integer.parseInt(stock.getText());
                Admin admin = new Admin(conn, "dummy");
                int pId = admin.addProduct(
                        name.getText(), brand.getText(), Double.parseDouble(costPrice.getText()),
                        Double.parseDouble(sellingPrice.getText()), stockUse,
                        new java.sql.Date(System.currentTimeMillis()).toString(), Integer.parseInt(supId.getText()));

                if (pId != -1) {
                    AlertBox2.alert("Successful", "Product Added Successfully");
                    primaryStage.close();
                } else {
                    AlertBox2.alert("Unsuccessful", "Product Could not be Added");
                    primaryStage.close();
                }
            } catch (Exception ex) {
                AlertBox2.alert("Unsuccessful", "Wrong Value Entered");
                primaryStage.close();
            }
        });

        Scene scene = new Scene(grid, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
