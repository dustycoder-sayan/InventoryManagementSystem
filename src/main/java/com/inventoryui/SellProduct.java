package com.inventoryui;

import com.inventory.DTO.IssueProductNamesDTO;
import com.inventory.DTO.ProductsSupNameDTO;
import com.inventory.DataSource.Admin;
import com.inventory.database.ConnectionFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Connection;

public class SellProduct {
    // TODO: This program has to use main connection to get username of user

    public static void start() {
        Stage primaryStage = new Stage();
        Connection conn = ConnectionFactory.getInstance().open();
        primaryStage.setTitle("Sell Product");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        TextField name = new TextField();
        name.setPromptText("Customer Name");
        grid.add(name, 0, 1);

        TextField brand = new TextField();
        brand.setPromptText("Customer Contact");
        grid.add(brand, 1, 1);

        TextField prodId = new TextField();
        prodId.setPromptText("Product ID");
        grid.add(prodId, 0, 2);

        TextField quantity = new TextField();
        quantity.setPromptText("Quantity");
        grid.add(quantity, 1, 2);

        Button submit = new Button("Submit");
        submit.setFont(new Font("Arial", 15));
        submit.setTextFill(Color.DARKCYAN);
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER_LEFT);
        hbBtn.getChildren().add(submit);
        grid.add(hbBtn, 0, 4);

        submit.setOnAction(e -> {

        });
    }
}
