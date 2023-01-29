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
    private final Connection conn;
    public UpdateCustomer(Connection conn) {
        this.conn = conn;
    }

    public void start() {
        Stage primaryStage = new Stage();
//        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.setTitle("Update Customer");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label nameLabel = new Label("Customer Name: ");
        nameLabel.setFont(new Font("Arial", 15));
        grid.add(nameLabel, 0, 1);

        TextField customerName = new TextField();
        grid.add(customerName, 1, 1);

        Label oldP = new Label("Old Contact No: ");
        oldP.setFont(new Font("Arial", 15));
        grid.add(oldP, 0, 2);

        TextField customerOldPhone = new TextField();
        grid.add(customerOldPhone, 1, 2);

        Label newP = new Label("New Contact No: ");
        newP.setFont(new Font("Arial", 15));
        grid.add(newP, 0, 3);

        TextField customerNewPhone = new TextField();
        grid.add(customerNewPhone, 1, 3);

        Label locationLabel = new Label("Customer Location: ");
        locationLabel.setFont(new Font("Arial", 15));
        grid.add(locationLabel, 0, 4);

        TextField customerLocation = new TextField();
        grid.add(customerLocation, 1, 4);

        Button submit = new Button("Submit");
        submit.setFont(new Font("Arial", 15));
        submit.setTextFill(Color.DARKCYAN);
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER);
        hbBtn.getChildren().add(submit);
        grid.add(hbBtn, 1, 6);

        submit.setOnAction(e -> {
            boolean updated = new Seller(conn, "fgh").updateCustomer(customerName.getText(), customerOldPhone.getText(),
                    customerNewPhone.getText(), customerLocation.getText());
            if(!updated) {
                AlertBox2.alert("Unsuccessful", "Customer Could not be Found");
                primaryStage.close();
            } else {
                AlertBox2.alert("Successful", "Customer Added Successfully");
                primaryStage.close();
            }
        });

        Scene scene = new Scene(grid, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
