package agh.oop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class GalleryApp extends Application {
    private Parent rootLayout;
    @Override
    public void init() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/mainView.fxml"));
        rootLayout = fxmlLoader.load();
    }
    @Override
    public void start(Stage primaryStage) throws Exception {

        //load layout from FXML file
//        var loader = new FXMLLoader();
//
//        loader.setLocation(GalleryApp.class.getResource("/view/mainView.fxml"));
//        VBox rootLayout = loader.load();


         //add layout to a scene and show them all
         configureStage(primaryStage);
         primaryStage.show();

    }
    private void configureStage(Stage primaryStage) {
        var scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Gallery app");
//        primaryStage.minWidthProperty().bind(rootLayout.minWidthProperty());
//        primaryStage.minHeightProperty().bind(rootLayout.minHeightProperty());
    }
    public static void main(String[] args){
        launch(args);
    }
}
