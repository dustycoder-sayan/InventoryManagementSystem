package com.inventoryui;

import com.inventory.DAO.ProductsDAO;
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

public class UpdateStock {

    private final Connection conn;
    public UpdateStock(Connection conn) {
        this.conn = conn;
    }

    public void start() {
        Stage primaryStage = new Stage();
//        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.setTitle("Update Stock");
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

        TextField supplierName = new TextField();
        supplierName.setPromptText("Supplier Name");
        grid.add(supplierName, 0, 2);

        TextField supplierPhone = new TextField();
        supplierPhone.setPromptText("Supplier Number");
        grid.add(supplierPhone, 1, 2);

        TextField stock = new TextField();
        stock.setPromptText("New Stock");
        HBox stck = new HBox(10);
        stck.setAlignment(Pos.CENTER);
        stck.getChildren().add(stock);
        grid.add(stck, 0, 3);

        Button submit = new Button("Submit");
        submit.setFont(new Font("Arial", 15));
        submit.setTextFill(Color.DARKCYAN);
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER_LEFT);
        hbBtn.getChildren().add(submit);
        grid.add(hbBtn, 0, 5);

        submit.setOnAction(e -> {
            int sId;
            if(!new SuppliersDAO(conn).supplierExists(supplierName.getText(), supplierPhone.getText())) {
                AlertBox2.alert("Unsuccessful", "Product Not found");
                primaryStage.close();
                return;
            }
            sId = new SuppliersDAO(conn).getSupplierId(supplierName.getText(), supplierPhone.getText());
            if(!new ProductsDAO(conn).productExists(name.getText(), brand.getText(), sId)) {
                AlertBox2.alert("Unsuccessful", "Product Not found");
                primaryStage.close();
                return;
            }

            Admin admin = new Admin(conn, "dummy");
            boolean updated = admin.updateStock(
                    name.getText(), brand.getText(), supplierName.getText(), supplierPhone.getText(),
                    Integer.parseInt(stock.getText()), new java.sql.Date(System.currentTimeMillis()).toString());

            if(updated) {
                AlertBox2.alert("Successful", "Product Stock Updated Successfully");
                primaryStage.close();
            }
            else {
                AlertBox2.alert("Unsuccessful", "Product Stock Could not be Updated");
                primaryStage.close();
            }
        });

        Scene scene = new Scene(grid, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
