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

        Label prodIdLabel = new Label("Product ID: ");
        prodIdLabel.setFont(new Font("Arial", 15));
        grid.add(prodIdLabel, 0, 1);

        TextField prodId = new TextField();
        grid.add(prodId, 1, 1);

        Label stockLabel = new Label("New Stock: ");
        stockLabel.setFont(new Font("Arial", 15));
        grid.add(stockLabel, 0, 2);

        TextField stock = new TextField();
        grid.add(stock, 1, 2);

        Button submit = new Button("Submit");
        submit.setFont(new Font("Arial", 15));
        submit.setTextFill(Color.DARKCYAN);
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER);
        hbBtn.getChildren().add(submit);
        grid.add(hbBtn, 1, 4);

        submit.setOnAction(e -> {
            if(!new ProductsDAO(conn).productExists(Integer.parseInt(prodId.getText()))) {
                AlertBox2.alert("Unsuccessful", "Product Not found");
                primaryStage.close();
                return;
            }

            Admin admin = new Admin(conn, "dummy");
            boolean updated = new ProductsDAO(conn).addStock(Integer.parseInt(prodId.getText()),
                    Integer.parseInt(stock.getText()), new java.sql.Date(System.currentTimeMillis()).toString());

            if(updated) {
                AlertBox2.alert("Successful", "Product Stock Updated Successfully");
                primaryStage.close();
            }
            else {
                AlertBox2.alert("Unsuccessful", "Stock Could not be Updated");
                primaryStage.close();
            }
        });

        Scene scene = new Scene(grid, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
