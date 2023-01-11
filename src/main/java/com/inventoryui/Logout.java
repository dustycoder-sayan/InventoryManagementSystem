package com.inventoryui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Logout {

    static boolean answer;

    public static boolean alert() {

        Stage stage = new Stage();

        stage.initModality(Modality.APPLICATION_MODAL);     // does not allow user to perform any action until stage closes
        Label label = new Label("Confirm Logout?");

        Button yesBtn = new Button("Yes");
        Button noBtn = new Button("No");

        yesBtn.setOnAction(e -> {
            answer = true;
            stage.close();
        });

        noBtn.setOnAction(e -> {
            answer = false;
            stage.close();
        });

        VBox layout = new VBox(20);
        layout.getChildren().addAll(label, yesBtn, noBtn);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 300);

        stage.setTitle("Logout Confirmation");
        stage.setScene(scene);

        stage.showAndWait();

        return answer;
    }
} 