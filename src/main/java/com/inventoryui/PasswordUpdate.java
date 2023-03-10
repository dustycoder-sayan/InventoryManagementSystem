package com.inventoryui;

import com.inventory.DataSource.Admin;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;

public class PasswordUpdate {
    private String username;
    private Connection conn;

    public PasswordUpdate(String username, Connection conn) {
        this.username = username;
        this.conn = conn;
    }

    public void start() {
        Stage primaryStage = new Stage();
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.setTitle("Update Password");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label oldPassLabel = new Label("Old Password: ");
        oldPassLabel.setFont(new Font("Arial", 15));
        grid.add(oldPassLabel, 0, 1);

        PasswordField old = new PasswordField();
        grid.add(old, 1, 1);

        Label newPassLabel = new Label("New Password: ");
        newPassLabel.setFont(new Font("Arial", 15));
        grid.add(newPassLabel, 0, 2);

        PasswordField newP = new PasswordField();
        grid.add(newP, 1, 2);

        Button submit = new Button("Submit");
        submit.setFont(new Font("Arial", 15));
        submit.setTextFill(Color.DARKCYAN);
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER);
        hbBtn.getChildren().add(submit);
        grid.add(hbBtn, 1, 4);

        submit.setOnAction(e -> {
            boolean updated = new Admin(conn, username).setNewPassword(old.getText(), newP.getText());
            if(!updated) {
                AlertBox2.alert("Unsuccessful", "Old Password Entered Wrong");
                primaryStage.close();
            } else {
                AlertBox2.alert("Successful", "Password Updated Successfully");
                primaryStage.close();
            }
        });

        Scene scene = new Scene(grid, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
