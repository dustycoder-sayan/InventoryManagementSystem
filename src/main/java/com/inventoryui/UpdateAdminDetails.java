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
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.Connection;

public class UpdateAdminDetails {
    private String username;
    private Connection conn;
    private BorderPane layout;
    private Label userUsername, userCategory, userTotalEarningsLabel, userTotalEarnings;

    public UpdateAdminDetails(String username, Connection conn, BorderPane layout) {
        this.username = username;
        this.conn = conn;
        this.layout = layout;
    }

    public void start() {
        Stage primaryStage = new Stage();
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

        Label phoneLabel = new Label("Contact Number: ");
        phoneLabel.setFont(new Font("Arial", 15));
        grid.add(phoneLabel, 0, 2);

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

                boolean updated = new Admin(conn, username).updateUserDetails(userPhone, userLocation);
                if (!updated) {
                    AlertBox2.alert("Unsuccessful", "FATAL ERROR: Could not Update Details");
                    primaryStage.close();
                } else {
                    Label about = new Label("Personal Details");
                    about.setPadding(new Insets(10, 10, 10, 10));
                    Label userName1 = new Label(new Admin(conn, username).getAdminFullName());
                    userName1.setPadding(new Insets(20, 10, 10, 10));
                    userName1.setAlignment(Pos.CENTER);
                    userName1.setFont(new Font("Calibri", 20));
                    userUsername = new Label(username);
                    userUsername.setPadding(new Insets(10, 10, 10, 10));
                    userUsername.setAlignment(Pos.CENTER);
                    userCategory = new Label("Admin");
                    userCategory.setPadding(new Insets(10, 10, 10, 10));
                    userCategory.setAlignment(Pos.CENTER);
                    Label userPhone1 = new Label(new Admin(conn, username).getAdminPhone());
                    userPhone1.setPadding(new Insets(10, 10, 10, 10));
                    userPhone1.setAlignment(Pos.CENTER);
                    Label userLocation1 = new Label(new Admin(conn, username).getAdminLocation());
                    userLocation1.setPadding(new Insets(10, 10, 10, 10));
                    userLocation1.setAlignment(Pos.CENTER);

                    userTotalEarningsLabel = new Label("Total Sales: ");
                    userTotalEarningsLabel.setPadding(new Insets(10, 10, 10, 10));
                    userTotalEarningsLabel.setAlignment(Pos.CENTER);
                    userTotalEarningsLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));

                    double totalEarnings = new Admin(conn, username).getTotalSales();
                    if(totalEarnings == -1)
                        totalEarnings=0;

                    userTotalEarnings = new Label(String.valueOf(totalEarnings));
                    userTotalEarnings.setPadding(new Insets(10, 10, 10, 10));
                    userTotalEarnings.setAlignment(Pos.CENTER);
                    userTotalEarnings.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));

                    HBox spacing = new HBox();
                    spacing.setMinHeight(50);
                    VBox right = new VBox(20);
                    right.setMinHeight(775);
                    right.setBackground(new Background(new BackgroundFill(Color.WHITE,
                            CornerRadii.EMPTY, Insets.EMPTY)));
                    right.setMinWidth(250);
                    right.getChildren().addAll(userName1, userCategory, userUsername, userPhone1, userLocation1,
                            userTotalEarningsLabel, userTotalEarnings);
                    right.setAlignment(Pos.TOP_CENTER);
                    right.setBorder(new Border(new BorderStroke(Color.LIGHTBLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
                            new BorderWidths(3))));
                    layout.setRight(right);
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
