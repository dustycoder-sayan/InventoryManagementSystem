package com.inventoryui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {

    public static void alert(String title, String message) {
        Stage stage = new Stage();

        stage.initModality(Modality.APPLICATION_MODAL);     // does not allow user to perform any action until stage closes

        Label label = new Label(message);

        VBox layout = new VBox(20);
        layout.getChildren().add(label);
        layout.setMinHeight(250);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);

        stage.setTitle(title);
        stage.setScene(scene);
    }
}