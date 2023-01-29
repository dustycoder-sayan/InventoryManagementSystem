package com.inventoryui;

import com.inventory.database.ConnectionFactory;
import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;

public class LoginPage extends Application {

    private Scene scene;
    private Connection connection;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
        try {
            connection = ConnectionFactory.getInstance().open(); }
        catch (Exception e) {
            AlertBox.alert("App Error", "FATAL ERROR");
            Platform.exit();
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        try {
            if(connection!=null)
                connection.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login to Inventory Management");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 40));
        scenetitle.setFill(Color.DARKSLATEGRAY);
        grid.add(scenetitle, 0, 0, 8, 1);

        Label userName = new Label("Username:");
        userName.setFont(new Font("Arial", 15));
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label password = new Label("Password:");
        password.setFont(new Font("Arial", 15));
        grid.add(password, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        Button loginButton = new Button("Sign in");
        loginButton.setFont(new Font("Arial", 15));
        loginButton.setTextFill(Color.DARKCYAN);
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(loginButton);
        grid.add(hbBtn, 1, 4);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);

        loginButton.setOnAction(e -> {
            String username = userTextField.getText();
            String password1 = pwBox.getText();
            String category = ConnectionFactory.getInstance().getUserCategory(username, password1);
            if(category == null) {
                actiontarget.setFill(Color.FIREBRICK);
                actiontarget.setText("Wrong Username or Password");
            }
            else if(category.equalsIgnoreCase("Admin"))
                primaryStage.setScene(new AdminPage(userTextField.getText(),connection).adminScene(primaryStage));
            else if(category.equalsIgnoreCase("Seller"))
                primaryStage.setScene(new SellerPage(userTextField.getText(),connection).sellerScene(primaryStage));
        });
        scene = new Scene(grid, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}