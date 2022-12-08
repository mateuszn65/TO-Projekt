package agh.oop;


import agh.oop.gallery.controllers.GalleryViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GalleryApp extends Application {
    private Parent rootLayout;

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/galleryView.fxml"));
        rootLayout = fxmlLoader.load();
         //add layout to a scene and show them all
        configureStage(primaryStage);
        GalleryViewController galleryController = fxmlLoader.getController();
        galleryController.setPrimaryStage(primaryStage);
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
