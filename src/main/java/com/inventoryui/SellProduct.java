package com.inventoryui;

import com.inventory.database.ConnectionFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Connection;

public class SellProduct {
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


    }
}
