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

public class AddProducts {
    // todo: if time permits, add label within the scene for "Add Product"
    // todo: if time permits, add label within the scene for each textfield
    public static void start() {
        Stage primaryStage = new Stage();

//        primaryStage.initModality(Modality.APPLICATION_MODAL);

        Connection conn = ConnectionFactory.getInstance().open();
        primaryStage.setTitle("Add Product");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        TextField name = new TextField();
        name.setPromptText("Product Name");
        grid.add(name, 0, 1);

        TextField brand = new TextField();
        brand.setPromptText("Product Brand");
        grid.add(brand, 1, 1);

        TextField costPrice = new TextField();
        costPrice.setPromptText("Cost Price");
        grid.add(costPrice, 0, 2);

        TextField sellingPrice = new TextField();
        sellingPrice.setPromptText("Selling Price");
        grid.add(sellingPrice, 1, 2);

        TextField supplierName = new TextField();
        supplierName.setPromptText("Supplier Name");
        grid.add(supplierName, 0, 3);

        TextField supplierPhone = new TextField();
        supplierPhone.setPromptText("Supplier Number");
        grid.add(supplierPhone, 1, 3);

        TextField stock = new TextField();
        stock.setPromptText("Stock");
        HBox stck = new HBox(10);
        stck.setAlignment(Pos.CENTER);
        stck.getChildren().add(stock);
        grid.add(stck, 0, 4);

        Button submit = new Button("Submit");
        submit.setFont(new Font("Arial", 15));
        submit.setTextFill(Color.DARKCYAN);
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER_LEFT);
        hbBtn.getChildren().add(submit);
        grid.add(hbBtn, 0, 6);

        submit.setOnAction(e -> {
            if(!new SuppliersDAO(conn).supplierExists(supplierName.getText(), supplierPhone.getText())) {
                AlertBox2.alert("Unsuccessful", "Supplier Not found");
                try {
                    conn.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                primaryStage.close();
                return;
            }

            int stockUse = Integer.parseInt(stock.getText());
            Admin admin = new Admin(conn, "dummy");
            int pId = admin.addProduct(
                    name.getText(), brand.getText(), Double.parseDouble(costPrice.getText()),
                    Double.parseDouble(sellingPrice.getText()), stockUse,
                    new java.sql.Date(System.currentTimeMillis()).toString(), supplierName.getText(), supplierPhone.getText());

            if(pId!=-1) {
                AlertBox2.alert("Successful", "Product Added Successfully");
                try {
                    conn.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                primaryStage.close();
            }
            else {
                AlertBox2.alert("Unsuccessful", "Product Could not be Added");
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
