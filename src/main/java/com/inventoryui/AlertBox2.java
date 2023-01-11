package com.inventoryui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox2 {

    public static void alert(String title, String message) {
        Stage stage = new Stage();

        stage.initModality(Modality.APPLICATION_MODAL);     // does not allow user to perform any action until stage closes
        Label label = new Label(message);
        Button btn = new Button("OK");
        btn.setOnAction(e -> stage.close());

        VBox layout = new VBox(20);
        layout.getChildren().addAll(label, btn);
        layout.setMinHeight(250);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 300);

        stage.setTitle(title);
        stage.setScene(scene);

        stage.showAndWait();
    }
}