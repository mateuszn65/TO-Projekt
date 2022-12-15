package agh.oop.gallery.controllers;
import agh.oop.RetrofitController;
import agh.oop.gallery.model.GalleryImage;
import agh.oop.gallery.model.ImageContainer;
import agh.oop.utils.ImageStatus;
import agh.oop.utils.ZipUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

public class GalleryViewController {
    @FXML
    public GridView<GalleryImage> imagesGridView;
    private ImageContainer imageContainer;
    private Stage primaryStage;
    private RetrofitController retrofitController = new RetrofitController();
    private Image placeholder;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    @FXML
    public void initialize() throws IOException {
        imageContainer = new ImageContainer();
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
//                        setText(item.getImageStatus().name());

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
    public void handleUploadOnAction(ActionEvent actionEvent) {
        try {

            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                System.out.println(file);
                if (file == null) {
                    return;
                }
                List<GalleryImage> galleryImages= new ArrayList<>();
                if (ZipUtils.isZip(file)){
                    galleryImages = ZipUtils.unzipImages(new ZipFile(file));
                }else {
                    GalleryImage galleryImage;
                    if ((galleryImage = createGalleryImage(file)) != null){
                        galleryImages.add(galleryImage);
                    }
                }
                for (int i = 0; i < galleryImages.size(); i++) {
                    imageContainer.addToGallery(galleryImages.get(i));
                    retrofitController.upload(galleryImages.get(i));
                }

            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void handleDragDropped(DragEvent dragEvent) {
        try {
            List<File> files = dragEvent.getDragboard().getFiles();
            List<GalleryImage> galleryImages;
            if (ZipUtils.isZip(files.get(0))) {
                galleryImages = ZipUtils.unzipImages(new ZipFile(files.get(0)));
            }else {
                galleryImages = getFilesContent(files);
            }
            for (int i = 0; i < galleryImages.size(); i++) {
                imageContainer.addToGallery(galleryImages.get(i));
                retrofitController.upload(galleryImages.get(i));
            }
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
    public List<GalleryImage> getFilesContent(List<File> files) throws IOException {
        List<GalleryImage> galleryImages = new ArrayList<>();

        for (File file:files) {
            GalleryImage galleryImage;
            if ((galleryImage = createGalleryImage(file)) != null){
                galleryImages.add(galleryImage);
            }
        }

        return galleryImages;
    }
    public GalleryImage createGalleryImage(File file) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        byte[] buffer = inputStream.readAllBytes();
        inputStream.close();
        if (ZipUtils.isImage(buffer)){
            return new GalleryImage(file.getName(), buffer);
        }
        return null;
    }
}
