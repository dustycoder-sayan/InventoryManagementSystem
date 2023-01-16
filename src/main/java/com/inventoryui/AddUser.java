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

public class AddUser {

    public static void start() {
        Stage primaryStage = new Stage();
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        Connection conn = ConnectionFactory.getInstance().open();
        primaryStage.setTitle("Add Employee");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        TextField userName = new TextField();
        userName.setPromptText("Employee Name");
        grid.add(userName, 0, 1);

        TextField userPhone = new TextField();
        userPhone.setPromptText("Employee Contact");
        grid.add(userPhone, 1, 1);

        TextField userLocation = new TextField();
        userLocation.setPromptText("Employee Location");
        grid.add(userLocation, 0, 2);

        TextField userCategory = new TextField();
        userCategory.setPromptText("Employee Category");
        grid.add(userCategory, 1, 2);

        Button submit = new Button("Submit");
        submit.setFont(new Font("Arial", 15));
        submit.setTextFill(Color.DARKCYAN);
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER_LEFT);
        hbBtn.getChildren().add(submit);
        grid.add(hbBtn, 0, 4);

        submit.setOnAction(e -> {

            String category = userCategory.getText();
            if(category.equalsIgnoreCase("Admin") || category.equalsIgnoreCase("Seller") || category.equalsIgnoreCase("Analyst")) {
                int uId = new Admin(conn, "dummy.xyz").addNewUser(userName.getText(), userPhone.getText(), userLocation.getText(),
                        userCategory.getText());
                if(uId == -1) {
                    AlertBox2.alert("Unsuccessful", "User Could not be Added");
                    try {
                        conn.close();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    primaryStage.close();
                } else {
                    AlertBox2.alert("Successful", "User Added Successfully");
                    try {
                        conn.close();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    primaryStage.close();
                }
            } else {
                AlertBox2.alert("Unsuccessful", "Category Non Existent");
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
