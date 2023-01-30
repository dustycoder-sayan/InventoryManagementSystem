package com.inventoryui;

import com.inventory.DataSource.Admin;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;

public class AddUser {
    private final Connection conn;
    public AddUser(Connection conn) {
        this.conn = conn;
    }

    public void start() {
        Stage primaryStage = new Stage();
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.setTitle("Add Employee");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label nameLabel = new Label("Employee Name: ");
        nameLabel.setFont(new Font("Arial", 15));
        grid.add(nameLabel, 0, 1);

        TextField userName = new TextField();
        grid.add(userName, 1, 1);

        Label contactLabel = new Label("Employee Contact: ");
        contactLabel.setFont(new Font("Arial", 15));
        grid.add(contactLabel, 0, 2);

        TextField userPhone = new TextField();
        grid.add(userPhone, 1, 2);

        Label locationLabel = new Label("Employee Location: ");
        locationLabel.setFont(new Font("Arial", 15));
        grid.add(locationLabel, 0, 3);

        TextField userLocation = new TextField();
        grid.add(userLocation, 1, 3);

        Label categoryLabel = new Label("Employee Category: ");
        categoryLabel.setFont(new Font("Arial", 15));
        grid.add(categoryLabel, 0, 4);

        TextField userCategory = new TextField();
        grid.add(userCategory, 1, 4);

        Button submit = new Button("Submit");
        submit.setFont(new Font("Arial", 15));
        submit.setTextFill(Color.DARKCYAN);
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER);
        hbBtn.getChildren().add(submit);
        grid.add(hbBtn, 1, 6);

        submit.setOnAction(e -> {
            try {
                String category = userCategory.getText();
                if (category.equalsIgnoreCase("Admin") || category.equalsIgnoreCase("Seller") || category.equalsIgnoreCase("Analyst")) {
                    int uId = new Admin(conn, "dummy.xyz").addNewUser(userName.getText(), userPhone.getText(), userLocation.getText(),
                            userCategory.getText());
                    if (uId == -1) {
                        AlertBox2.alert("Unsuccessful", "User Could not be Added");
                        primaryStage.close();
                    } else {
                        AlertBox2.alert("Successful", "User Added Successfully");
                        primaryStage.close();
                    }
                } else {
                    AlertBox2.alert("Unsuccessful", "Category Non Existent");
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
