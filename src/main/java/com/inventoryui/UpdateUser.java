package com.inventoryui;

import com.inventory.DAO.UsersDAO;
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
import javafx.stage.Stage;

import java.sql.Connection;

public class UpdateUser {
    // todo: if time permits, add label within the scene for "Add Product"
    private final Connection conn;
    public UpdateUser(Connection conn) {
        this.conn = conn;
    }

    public void start() {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Update User");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label empId = new Label("Employee ID: ");
        empId.setFont(new Font("Arial", 15));
        grid.add(empId, 0, 1);

        TextField userId = new TextField();
        grid.add(userId, 1, 1);

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
        hbBtn.setAlignment(Pos.CENTER_LEFT);
        hbBtn.getChildren().add(submit);
        grid.add(hbBtn, 1, 6);

        submit.setOnAction(e -> {
            try {
                String category = userCategory.getText();
                if (!new UsersDAO(conn).userExists(Integer.parseInt(userId.getText()))) {
                    AlertBox2.alert("Unsuccessful", "User Not found");
                    primaryStage.close();
                    return;
                }

                if (category.equalsIgnoreCase("Admin") || category.equalsIgnoreCase("Seller") || category.equalsIgnoreCase("Analyst")) {
                    boolean updated = new Admin(conn, "xyz").updateAnotherUser(Integer.parseInt(userId.getText()),
                            userPhone.getText(), userLocation.getText(),
                            userCategory.getText());

                    if (!updated) {
                        AlertBox2.alert("Unsuccessful", "User Could not be Updated");
                        primaryStage.close();
                    } else {
                        AlertBox2.alert("Successful", "User Updated Successfully");
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
