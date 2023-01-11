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

public class DeleteProduct {

    public static void start() {
        Stage primaryStage = new Stage();
//        primaryStage.initModality(Modality.APPLICATION_MODAL);
        Connection conn = ConnectionFactory.getInstance().open();
        primaryStage.setTitle("Delete Product");
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

        Button submit = new Button("Delete");
        submit.setFont(new Font("Arial", 15));
        submit.setTextFill(Color.RED);
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER_LEFT);
        hbBtn.getChildren().add(submit);
        grid.add(hbBtn, 0, 4);

        // TODO: Complete function to delete Product. Check if Product exists first
        submit.setOnAction(e -> {
            String supName = supplierName.getText();
            String supPhone = supplierPhone.getText();
            String prodName = name.getText();
            String prodBrand = brand.getText();

            int sId = new SuppliersDAO(conn).getSupplierId(supName, supPhone);
            if(sId == -1) {
                AlertBox2.alert("Unsuccessful", "Supplier Not found");
                primaryStage.close();
                return;
            }
            int pId = new ProductsDAO(conn).getProductId(prodName, prodBrand, sId);
            if(pId == -1) {
                AlertBox2.alert("Unsuccessful", "Product Not found");
                primaryStage.close();
                return;
            }

            boolean deleted = new Admin(conn, "dummy.xyz").deleteProduct(prodName, prodBrand, supName, supPhone);
            if(deleted) {
                AlertBox2.alert("Successful", "Product Deleted Successfully");
                primaryStage.close();
            }
            else {
                AlertBox2.alert("Unsuccessful", "Product Could Not be Deleted");
                primaryStage.close();
            }
        });

        Scene scene = new Scene(grid, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
