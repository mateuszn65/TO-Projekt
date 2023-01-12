package agh.oop.gallery.controllers;
import agh.oop.RetrofitController;
import agh.oop.gallery.model.GalleryImage;
import agh.oop.gallery.model.ImageContainer;
import agh.oop.gallery.view.GalleryCellFactory;
import agh.oop.utils.FileUtils;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.GridView;
import java.io.*;
import java.util.List;

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
        initialize(GalleryImage.smallMiniatureWidth, GalleryImage.smallMiniatureHeight);
    }

    public void initialize(int miniatureWidth, int miniatureHeight) throws IOException {
        imageContainer = new ImageContainer(miniatureWidth, miniatureHeight);
        retrofitController = new RetrofitController();
        retrofitController.initModel(imageContainer, miniatureWidth, miniatureHeight);
        placeholder = retrofitController.getPlaceholder();
        prepareGridView(miniatureHeight, miniatureWidth, imageContainer.getGallery());
        imagesGridView.setCellFactory(new GalleryCellFactory(miniatureHeight, miniatureWidth, placeholder, primaryStage, retrofitController));
    }


    private void uploadFiles(List<File> files) throws IOException {
        List<GalleryImage> galleryImages = FileUtils.getImagesFromFiles(files);
        for (GalleryImage image : galleryImages) {
            imageContainer.addToGallery(image);
            retrofitController.upload(image, imageContainer.getMiniatureWidth(), imageContainer.getMiniatureHeight());
        }
    }
    public void handleUploadOnAction(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file == null) return;
        List<File> files = List.of(file);
        uploadFiles(files);
    }

    public void handleDragDropped(DragEvent dragEvent) throws IOException {
        uploadFiles(dragEvent.getDragboard().getFiles());
    }

    public void handleDragOver(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasFiles()){
            dragEvent.acceptTransferModes(TransferMode.COPY);
        }
    }

    public void setSmallMiniatures(ActionEvent actionEvent) {
        imageContainer.setMiniatureWidth(GalleryImage.smallMiniatureWidth);
        imageContainer.setMiniatureHeight(GalleryImage.smallMiniatureHeight);
        resetImages();
    }

    public void setMediumMiniatures(ActionEvent actionEvent) {
        imageContainer.setMiniatureWidth(GalleryImage.mediumMiniatureWidth);
        imageContainer.setMiniatureHeight(GalleryImage.mediumMiniatureHeight);
        resetImages();
    }

    public void setBigMiniatures(ActionEvent actionEvent) {
        imageContainer.setMiniatureWidth(GalleryImage.bigMiniatureWidth);
        imageContainer.setMiniatureHeight(GalleryImage.bigMiniatureHeight);
        resetImages();
    }

    private void resetImages() {
        int miniatureHeight = imageContainer.getMiniatureHeight();
        int miniatureWidth = imageContainer.getMiniatureWidth();
        imageContainer.getGallery().forEach(img -> retrofitController.getMiniature(img, miniatureWidth, miniatureHeight));
        prepareGridView(miniatureHeight, miniatureWidth, imageContainer.getGallery());
        imagesGridView.setCellFactory(new GalleryCellFactory(miniatureHeight, miniatureWidth, placeholder, primaryStage, retrofitController));

    }
}
