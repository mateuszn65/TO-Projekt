package agh.oop.gallery.controllers;
import agh.oop.RetrofitController;
import agh.oop.gallery.model.GalleryImage;
import agh.oop.gallery.model.ImageContainer;
import agh.oop.gallery.model.ImageStatus;
import javafx.application.Platform;
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
        this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
    }


    @FXML
    public void initialize() throws IOException {
        imageContainer = new ImageContainer();
        retrofitController = new RetrofitController();
        retrofitController.initModel(imageContainer);
        placeholder = retrofitController.getPlaceholder();

        imagesGridView.setCellHeight(GalleryImage.miniHeight);
        imagesGridView.setCellWidth(GalleryImage.miniWidth);
        imagesGridView.setItems(imageContainer.getGallery());

        imagesGridView.setCellFactory(new Callback<>() {
            @Override
            public GridCell<GalleryImage> call(GridView<GalleryImage> param) {
                GridCell<GalleryImage> gridCell =  new GridCell<>() {
                    @Override
                    protected void updateItem(GalleryImage item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item.getImageStatus() == ImageStatus.UPLOADING) {
                            setGraphic(null);
                            return;
                        }

                        ImageView photoIcon = new ImageView();
                        photoIcon.setPreserveRatio(true);
                        photoIcon.setFitHeight(imagesGridView.getCellHeight());
                        photoIcon.setFitWidth(imagesGridView.getCellWidth());

                        if (item.getImageStatus() == ImageStatus.RECEIVED){
                            photoIcon.imageProperty().set(item.getMiniImage());
                            setGraphic(photoIcon);
                        } else if (item.getImageStatus() == ImageStatus.LOADING) {
                            photoIcon.imageProperty().set(placeholder);
                            setGraphic(photoIcon);
                        }

                    }
                };
                gridCell.setOnMouseClicked(event -> {
                    if (gridCell.getItem() != null) {
                        try {
                            showPreview(gridCell.getItem());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                return gridCell;
            }
        });
    }
    public void showPreview(GalleryImage galleryImage) throws IOException {
        Stage previewStage = new Stage();
        previewStage.setTitle(galleryImage.getName());
        previewStage.initModality(Modality.WINDOW_MODAL);
        previewStage.initOwner(primaryStage);

        Pane pane = new Pane();
        ImageView imageView = new ImageView(retrofitController.getOriginalImage(galleryImage.getId()));
        pane.getChildren().add(imageView);

        Scene scene = new Scene(pane);
        previewStage.setScene(scene);
        previewStage.showAndWait();
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
