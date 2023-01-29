package com.inventoryui;

import com.inventory.DataSource.Admin;
import com.inventory.DataSource.Seller;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Connection;

public class UpdateSellerDetails {
    private String username;
    private Connection conn;

    public UpdateSellerDetails(String username, Connection conn) {
        this.username = username;
        this.conn = conn;
    }

    public void start() {
        Stage primaryStage = new Stage();
        //        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.setTitle("Update Details");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label userName = new Label(new Admin(conn, username).getAdminFullName());
        userName.setFont(new Font("Arial", 15));
        grid.add(userName, 0, 1);

        Label category = new Label(new Admin(conn, username).getAdminCategory());
        category.setFont(new Font("Arial", 15));
        grid.add(category, 1, 1);

        Label contactLabel = new Label("Contact No: ");
        contactLabel.setFont(new Font("Arial", 15));
        grid.add(contactLabel, 0, 2);

        TextField phone = new TextField();
        grid.add(phone, 1, 2);

        Label locationLabel = new Label("Location: ");
        locationLabel.setFont(new Font("Arial", 15));
        grid.add(locationLabel, 0, 3);

        TextField location = new TextField();
        grid.add(location, 1, 3);

        Button submit = new Button("Submit");
        submit.setFont(new Font("Arial", 15));
        submit.setTextFill(Color.DARKCYAN);
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER);
        hbBtn.getChildren().add(submit);
        grid.add(hbBtn, 1, 5);

        submit.setOnAction(e -> {
            try {
                String userPhone = phone.getText();
                String userLocation = location.getText();

                boolean updated = new Seller(conn, username).updateUserDetails(userPhone, userLocation);
                if (!updated) {
                    AlertBox2.alert("Unsuccessful", "FATAL ERROR: Could not Update Details");
                    primaryStage.close();
                } else {
                    AlertBox2.alert("Successful", "Details Updated Successfully");
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
