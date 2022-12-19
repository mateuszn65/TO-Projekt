package agh.oop.gallery.controllers;
import agh.oop.RetrofitController;
import agh.oop.gallery.model.GalleryImage;
import agh.oop.gallery.model.ImageContainer;
import agh.oop.gallery.model.ImageStatus;
import agh.oop.gallery.view.GalleryCellFactory;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;
import java.io.*;
import java.util.List;
import java.util.zip.ZipFile;

import static agh.oop.utils.GalleryImageUtils.getFilesContent;
import static agh.oop.utils.ZipUtils.isZip;
import static agh.oop.utils.ZipUtils.unzipImages;

public class GalleryViewController {
    @FXML
    public GridView<GalleryImage> imagesGridView;
    private ImageContainer imageContainer;
    private Stage primaryStage;

    private RetrofitController retrofitController;
    private Image placeholder;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
    }

    private void prepareGridView(int cellHeight, int cellWidth, ObservableList<GalleryImage> images) {
        imagesGridView.setCellHeight(cellHeight);
        imagesGridView.setCellWidth(cellWidth);
        imagesGridView.setItems(images);
    }


    @FXML
    public void initialize() throws IOException {
        imageContainer = new ImageContainer();
        retrofitController = new RetrofitController();
        retrofitController.initModel(imageContainer);
        placeholder = retrofitController.getPlaceholder();
        prepareGridView(GalleryImage.miniatureHeight, GalleryImage.miniatureWidth, imageContainer.getGallery());
        imagesGridView.setCellFactory(new GalleryCellFactory(GalleryImage.miniatureHeight, GalleryImage.miniatureWidth, placeholder, primaryStage, retrofitController));
    }


    private void uploadFiles(List<File> files){
        try {
            List<GalleryImage> galleryImages;
            if (isZip(files.get(0))) {
                galleryImages = unzipImages(new ZipFile(files.get(0)));
            } else {
                galleryImages = getFilesContent(files);
            }
            for (int i = 0; i < galleryImages.size(); i++) {
                imageContainer.addToGallery(galleryImages.get(i));
                retrofitController.upload(galleryImages.get(i));
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void handleUploadOnAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file == null) return;
        List<File> files = List.of(file);
        uploadFiles(files);
    }

    public void handleDragDropped(DragEvent dragEvent) {
        uploadFiles(dragEvent.getDragboard().getFiles());
    }

    public void handleDragOver(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasFiles()){
            dragEvent.acceptTransferModes(TransferMode.COPY);
        }
    }

}
