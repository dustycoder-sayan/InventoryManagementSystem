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

public class DeleteSupplier {
    // todo: if time permits, add label within the scene for "Add Product"
    private final Connection conn;
    public DeleteSupplier(Connection conn) {
        this.conn = conn;
    }

    public void start() {
        Stage primaryStage = new Stage();
//        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.setTitle("Delete Supplier");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label supIdLabel = new Label("Supplier ID: ");
        supIdLabel.setFont(new Font("Arial", 15));
        grid.add(supIdLabel, 0, 1);

        TextField supId = new TextField();
        grid.add(supId, 1, 1);

        Button submit = new Button("Delete");
        submit.setFont(new Font("Arial", 15));
        submit.setTextFill(Color.RED);
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER);
        hbBtn.getChildren().add(submit);
        grid.add(hbBtn, 1, 3);

        submit.setOnAction(e -> {
            try {
                if (!new SuppliersDAO(conn).supplierExists(Integer.parseInt(supId.getText()))) {
                    AlertBox2.alert("Unsuccessful", "Supplier Not found");
                }

                boolean deleted = new Admin(conn, "xyz").deleteSupplier(Integer.parseInt(supId.getText()));
                if (deleted) {
                    AlertBox2.alert("Successful", "Supplier Deleted Successfully");
                    primaryStage.close();
                } else {
                    AlertBox2.alert("Unsuccessful", "Supplier Could not be Deleted");
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
