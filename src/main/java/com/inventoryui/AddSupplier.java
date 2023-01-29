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

public class AddSupplier {
    private final Connection conn;
    public AddSupplier(Connection conn) {
        this.conn = conn;
    }

    public void start() {
        Stage primaryStage = new Stage();

        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.setTitle("Add Supplier");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label nameLabel = new Label("Supplier Name: ");
        nameLabel.setFont(new Font("Arial", 15));
        grid.add(nameLabel, 0, 1);

        TextField supplierName = new TextField();
        grid.add(supplierName, 1, 1);

        Label contactLabel = new Label("Supplier Contact: ");
        contactLabel.setFont(new Font("Arial", 15));
        grid.add(contactLabel, 0, 2);

        TextField supplierPhone = new TextField();
        grid.add(supplierPhone, 1, 2);

        Label locationLabel = new Label("Supplier Location: ");
        locationLabel.setFont(new Font("Arial", 15));
        grid.add(locationLabel, 0, 3);

        TextField supplierLocation = new TextField();
        grid.add(supplierLocation, 1, 3);

        Button submit = new Button("Submit");
        submit.setFont(new Font("Arial", 15));
        submit.setTextFill(Color.DARKCYAN);
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER);
        hbBtn.getChildren().add(submit);
        grid.add(hbBtn, 1, 5);

        submit.setOnAction(e -> {
            try {
                int sId = new Admin(conn, "dummy.xyz").insertSupplier(supplierName.getText(), supplierPhone.getText(), supplierLocation.getText());
                if (sId == -1) {
                    AlertBox2.alert("Unsuccessful", "Supplier Could not be Added");
                    primaryStage.close();
                } else {
                    AlertBox2.alert("Successful", "Supplier Added Successfully");
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
