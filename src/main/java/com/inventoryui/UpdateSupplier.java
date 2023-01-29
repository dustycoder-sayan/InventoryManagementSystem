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

public class UpdateSupplier {
    // todo: if time permits, add label within the scene for "Add Product"
    // todo: Cement oldPhone after retrieving from db

    private final Connection conn;
    public UpdateSupplier(Connection conn) {
        this.conn = conn;
    }

    public void start() {
        Stage primaryStage = new Stage();
//        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.setTitle("Update Supplier");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label supIdLabel = new Label("Supplier ID: ");
        supIdLabel.setFont(new Font("Arial", 15));
        grid.add(supIdLabel, 0, 1);

        TextField supplierId = new TextField();
        grid.add(supplierId, 1, 1);

        Label contactLabel = new Label("Supplier Contact: ");
        contactLabel.setFont(new Font("Arial", 15));
        grid.add(contactLabel, 0, 2);

        TextField supplierNewPhone = new TextField();
        supplierNewPhone.setPromptText("Supplier Contact");
        grid.add(supplierNewPhone, 1, 2);

        Label supplierLocationLabel = new Label("Supplier Location: ");
        supplierLocationLabel.setFont(new Font("Arial", 15));
        grid.add(supplierLocationLabel, 0, 3);

        TextField supplierLocation = new TextField();
        supplierLocation.setPromptText("Supplier Location");
        grid.add(supplierLocation, 1, 3);

        Button submit = new Button("Submit");
        submit.setFont(new Font("Arial", 15));
        submit.setTextFill(Color.DARKCYAN);
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER);
        hbBtn.getChildren().add(submit);
        grid.add(hbBtn, 1, 5);

        submit.setOnAction(e -> {   // todo: check if supplier exists first
            if(!new SuppliersDAO(conn).supplierExists(Integer.parseInt(supplierId.getText()))) {
                AlertBox2.alert("Unsuccessful", "Supplier Not found");
                primaryStage.close();
                return;
            }

            boolean updated = new Admin(conn, "dummy.xyz").updateSupplier(Integer.parseInt(supplierId.getText()),
                    supplierNewPhone.getText(), supplierLocation.getText());

            if(!updated) {
                AlertBox2.alert("Unsuccessful", "Supplier Could not be Updated");
                primaryStage.close();
            } else {
                AlertBox2.alert("Successful", "Supplier Updated Successfully");
                primaryStage.close();
            }
        });

        Scene scene = new Scene(grid, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
