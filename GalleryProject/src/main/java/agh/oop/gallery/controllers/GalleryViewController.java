package agh.oop.gallery.controllers;
import agh.oop.RetrofitController;
import agh.oop.gallery.model.GalleryImage;
import agh.oop.gallery.model.ImageContainer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipFile;

public class GalleryViewController {

    @FXML
    private ListView<GalleryImage> imagesListView;
    private ImageContainer imageContainer;
    private Stage primaryStage;
    private RetrofitController retrofitController = new RetrofitController();

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    @FXML
    public void initialize(){
        imageContainer = new ImageContainer();
//        retrofitController.initModel(imageContainer);

        imagesListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(GalleryImage item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else if (item.getMiniImage() != null) {
                    ImageView photoIcon = new ImageView(item.getMiniImage());
                    photoIcon.setPreserveRatio(true);
                    photoIcon.setFitHeight(50);
                    setGraphic(photoIcon);
                } else {
                    //loading cell
                    setText(null);
                    setGraphic(null);
                }
            }
        });

        imagesListView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null)
                showPreview(newValue);
        }));


    }
    public void showPreview(GalleryImage newValue){

    }
    public void handleUploadOnAction(ActionEvent actionEvent) {
    }

    public void handleDragDropped(DragEvent dragEvent) {
        List<File> files = dragEvent.getDragboard().getFiles();
        if (files.size() != 1)
            return;
        if (!ZipUtils.isZip(files.get(0)))
            return;

        try {
            List<GalleryImage> galleryImages = ZipUtils.unzipImages(new ZipFile(files.get(0)));

            galleryImages.forEach(galleryImage -> {
                imageContainer.addToGallery(galleryImage);
                retrofitController.upload(galleryImage);
            });


        } catch (IOException e) {
            e.getCause();
            e.printStackTrace();
        }
    }

    public void handleDragOver(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasFiles()){
            dragEvent.acceptTransferModes(TransferMode.COPY);
        }
    }
}
