package agh.oop.gallery.view;

import agh.oop.gallery.controllers.GalleryViewController;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;

public class DialogScene {

    private GalleryViewController controller;
    private Stage dialogStage;
    public DialogScene(GalleryViewController controller, Stage dialogStage) {
        this.controller = controller;
        this.dialogStage = dialogStage;
    }

    public Scene getScene() {
        VBox sceneRoot = new VBox();
        TextField dirNameField = new TextField();
        sceneRoot.getChildren().add(dirNameField);
        Button submitBtn = new Button();
        submitBtn.setText("add");
        submitBtn.setOnAction(ev -> {
            controller.handleAddDir(dirNameField.getText());
            dialogStage.close();
        });
        sceneRoot.getChildren().add(submitBtn);
        Scene sc = new Scene(sceneRoot, 500, 300);
        return sc;
    }
}
