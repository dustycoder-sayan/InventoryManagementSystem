package com.inventoryui;

import com.inventory.DAO.UsersDAO;
import com.inventory.DataSource.Admin;
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

public class DeleteUser {
    private final Connection conn;
    public DeleteUser(Connection conn) {
        this.conn = conn;
    }

    public void start() {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("Delete Employee");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label userLabel = new Label("Employee ID: ");
        userLabel.setFont(new Font("Arial", 15));
        grid.add(userLabel, 0, 1);

        TextField userId = new TextField();
        grid.add(userId, 1, 1);

        Button submit = new Button("Delete");
        submit.setFont(new Font("Arial", 15));
        submit.setTextFill(Color.RED);
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER);
        hbBtn.getChildren().add(submit);
        grid.add(hbBtn, 1, 3);

        submit.setOnAction(e -> {
            try {
                if (!new UsersDAO(conn).userExists(Integer.parseInt(userId.getText()))) {
                    AlertBox2.alert("Unsuccessful", "Employee Not found");
                    primaryStage.close();
                    return;
                }

                boolean deleted = new Admin(conn, "xyz").deleteUser(Integer.parseInt(userId.getText()));
                if (!deleted) {
                    AlertBox2.alert("Unsuccessful", "Employee Could not be Deleted");
                    primaryStage.close();
                } else {
                    AlertBox2.alert("Successful", "Employee Deleted Successfully");
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
