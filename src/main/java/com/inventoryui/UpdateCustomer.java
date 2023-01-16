package com.inventoryui;

import com.inventory.DAO.SuppliersDAO;
import com.inventory.DataSource.Admin;
import com.inventory.DataSource.Seller;
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

public class UpdateCustomer {
    // todo: if time permits, add label within the scene for "Add Product"
    // todo: Cement oldPhone after retrieving from db

    public static void start() {
        Stage primaryStage = new Stage();
//        primaryStage.initModality(Modality.APPLICATION_MODAL);
        Connection conn = ConnectionFactory.getInstance().open();
        primaryStage.setTitle("Update Customer");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        TextField customerName = new TextField();
        customerName.setPromptText("Customer Name");
        grid.add(customerName, 0, 1);

        TextField customerLocation = new TextField();
        customerLocation.setPromptText("Customer Location");
        grid.add(customerLocation, 1, 1);

        TextField customerOldPhone = new TextField();
        customerOldPhone.setPromptText("Customer Prev Number");
        grid.add(customerOldPhone, 0, 2);

        TextField customerNewPhone = new TextField();
        customerNewPhone.setPromptText("Customer Current Number");
        grid.add(customerNewPhone, 1, 2);

        Button submit = new Button("Submit");
        submit.setFont(new Font("Arial", 15));
        submit.setTextFill(Color.DARKCYAN);
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER_LEFT);
        hbBtn.getChildren().add(submit);
        grid.add(hbBtn, 0, 4);

        submit.setOnAction(e -> {
            boolean updated = new Seller(conn, "fgh").updateCustomer(customerName.getText(), customerOldPhone.getText(),
                    customerNewPhone.getText(), customerLocation.getText());
            if(!updated) {
                AlertBox2.alert("Unsuccessful", "Customer Could not be Updated");
                try {
                    conn.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                primaryStage.close();
            } else {
                AlertBox2.alert("Successful", "Customer Added Successfully");
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
