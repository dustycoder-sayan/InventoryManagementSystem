package com.inventoryui;

import com.inventory.DAO.UsersDAO;
import com.inventory.DataSource.Admin;
import com.inventory.database.ConnectionFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;

public class DeleteUser {
    private final Connection conn;
    public DeleteUser(Connection conn) {
        this.conn = conn;
    }

    public void start() {
        Stage primaryStage = new Stage();
//        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.setTitle("Delete Employee");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        TextField userId = new TextField();
        userId.setPromptText("User ID");
        grid.add(userId, 0, 1);

        Button submit = new Button("Delete");
        submit.setFont(new Font("Arial", 15));
        submit.setTextFill(Color.RED);
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER_LEFT);
        hbBtn.getChildren().add(submit);
        grid.add(hbBtn, 0, 3);

        submit.setOnAction(e -> {   // todo: check if supplier exists first
            if(!new UsersDAO(conn).userExists(Integer.parseInt(userId.getText()))) {
                AlertBox2.alert("Unsuccessful", "Employee Not found");
                primaryStage.close();
                return;
            }

            boolean deleted = new Admin(conn, "xyz").deleteUser(Integer.parseInt(userId.getText()));
            if(!deleted) {
                AlertBox2.alert("Unsuccessful", "Employee Could not be Deleted");
                primaryStage.close();
            } else {
                AlertBox2.alert("Successful", "Employee Deleted Successfully");
                primaryStage.close();
            }
        });

        Scene scene = new Scene(grid, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
