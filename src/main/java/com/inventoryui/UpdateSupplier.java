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

    public static void start() {
        Stage primaryStage = new Stage();
//        primaryStage.initModality(Modality.APPLICATION_MODAL);
        Connection conn = ConnectionFactory.getInstance().open();
        primaryStage.setTitle("Update Supplier");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        TextField supplierName = new TextField();
        supplierName.setPromptText("Supplier Name");
        grid.add(supplierName, 0, 1);

        TextField supplierLocation = new TextField();
        supplierLocation.setPromptText("Supplier Location");
        grid.add(supplierLocation, 1, 1);

        TextField supplierOldPhone = new TextField();
        supplierOldPhone.setPromptText("Supplier Prev Contact");
        grid.add(supplierOldPhone, 0, 2);

        TextField supplierNewPhone = new TextField();
        supplierNewPhone.setPromptText("Supplier Current Contact");
        grid.add(supplierNewPhone, 1, 2);

        Button submit = new Button("Submit");
        submit.setFont(new Font("Arial", 15));
        submit.setTextFill(Color.DARKCYAN);
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER_LEFT);
        hbBtn.getChildren().add(submit);
        grid.add(hbBtn, 0, 4);

        submit.setOnAction(e -> {   // todo: check if supplier exists first
            if(!new SuppliersDAO(conn).supplierExists(supplierName.getText(), supplierOldPhone.getText())) {
                AlertBox2.alert("Unsuccessful", "Supplier Not found");
                try {
                    conn.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                primaryStage.close();
                return;
            }

            boolean updated = new Admin(conn, "dummy.xyz").updateSupplier(supplierName.getText(), supplierOldPhone.getText(),
                    supplierNewPhone.getText(), supplierLocation.getText());

            if(!updated) {
                AlertBox2.alert("Unsuccessful", "Supplier Could not be Updated");
                try {
                    conn.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                primaryStage.close();
            } else {
                AlertBox2.alert("Successful", "Supplier Updated Successfully");
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
