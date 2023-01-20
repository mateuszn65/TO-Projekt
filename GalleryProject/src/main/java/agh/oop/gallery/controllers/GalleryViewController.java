package agh.oop.gallery.controllers;
import agh.oop.RetrofitController;
import agh.oop.gallery.model.GalleryDirectory;
import agh.oop.gallery.model.GalleryImage;
import agh.oop.gallery.model.ImageContainer;
import agh.oop.gallery.model.MiniatureSize;
import agh.oop.gallery.view.DialogScene;
import agh.oop.gallery.view.DirectoryCellFactory;
import agh.oop.gallery.view.GalleryCellFactory;
import agh.oop.gallery.view.GalleryGridCell;
import agh.oop.utils.FileUtils;
import agh.oop.utils.LabelMapper;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.GridView;

import java.io.*;
import java.util.EventListener;
import java.util.List;

public class GalleryViewController {
    @FXML
    public GridView<GalleryImage> imagesGridView;
    @FXML
    public ListView<GalleryDirectory> directoriesListView;
    @FXML
    private ScrollPane scrollPane;
    private ImageContainer imageContainer;
    private Stage primaryStage;

    private RetrofitController retrofitController;
    private Image placeholder;
    private int page = 0;
    private int rowSize = 4;
    private int initPageSize = 20;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setPrimaryStageListeners();
    }
    private void setPrimaryStageListeners() {
        ChangeListener<Number> resizeListener = (observable, oldValue, newValue) -> {
            updatePageSize();
            if (newValue.intValue() < oldValue.intValue())
                return;

            int newInitPageSize = getNumberOfImagesInPage();
            loadMoreOnResize(newInitPageSize);
        };

        this.primaryStage.widthProperty().addListener(resizeListener);
        this.primaryStage.heightProperty().addListener(resizeListener);

        this.primaryStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
    }
    private int getNumberOfImagesInRow() {
        return (int) Math.floor((this.primaryStage.getWidth() - directoriesListView.getWidth()) / (LabelMapper.getWidth(imageContainer.getCurrentSize()) + GalleryGridCell.cellPadding * 2));
    }
    private int getNumberOfImagesInColumn() {
        return (int) Math.floor((primaryStage.getHeight() - 150) / (LabelMapper.getHeight(imageContainer.getCurrentSize()) + GalleryGridCell.cellPadding * 2));
    }
    private int getNumberOfImagesInPage() {
        return getNumberOfImagesInRow() * getNumberOfImagesInColumn() + getNumberOfImagesInRow();
    }
    private void loadMoreOnResize(int newInitPageSize) {
        while (newInitPageSize > initPageSize + (page) * rowSize) {
            retrofitController.loadMore(imageContainer, imageContainer.getCurrentSize(), page++, rowSize);
        }
    }

    private void prepareGridView(int cellHeight, int cellWidth, ObservableList<GalleryImage> images) {
        imagesGridView.setCellHeight(cellHeight);
        imagesGridView.setCellWidth(cellWidth);
        imagesGridView.setItems(images);
    }

    private void prepareDirListView() {
        directoriesListView.setItems(imageContainer.getDirs());
        directoriesListView.setCellFactory(new DirectoryCellFactory(imageContainer));
    }


    @FXML
    public void initialize() throws IOException {

        initialize(MiniatureSize.SMALL);
    }

    public void initialize(MiniatureSize size) throws IOException {
        addScrollListeners();
        imageContainer = new ImageContainer(size);
        retrofitController = new RetrofitController();

        placeholder = retrofitController.getPlaceholder();
        int miniatureHeight = LabelMapper.getHeight(size);
        int miniatureWidth = LabelMapper.getWidth(size);
        prepareGridView(miniatureHeight, miniatureWidth, imageContainer.getGallery());
        imagesGridView.setCellFactory(new GalleryCellFactory(miniatureHeight, miniatureWidth, placeholder, primaryStage, retrofitController));
        prepareDirListView();
        retrofitController.loadMore(imageContainer, imageContainer.getCurrentSize(), page++, initPageSize);
    }


    private void uploadFiles(List<File> files) throws IOException {
        List<GalleryImage> galleryImages = FileUtils.getImagesFromFiles(files);
        for (GalleryImage image : galleryImages) {
            imageContainer.addToGallery(image);
            retrofitController.upload(image, imageContainer.getCurrentSize());
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
        imageContainer.setMiniatureSize(MiniatureSize.SMALL);
        resetImages();
    }

    public void setMediumMiniatures(ActionEvent actionEvent) {
        imageContainer.setMiniatureSize(MiniatureSize.MEDIUM);
        resetImages();
    }

    public void setBigMiniatures(ActionEvent actionEvent) {
        imageContainer.setMiniatureSize(MiniatureSize.BIG);
        resetImages();
    }

    private void resetImages() {
        updatePageSize();
        loadMoreOnResize(getNumberOfImagesInPage());
        int currentMiniatureHeight = LabelMapper.getHeight(imageContainer.getCurrentSize());
        int currentMiniatureWidth = LabelMapper.getWidth(imageContainer.getCurrentSize());
        imageContainer.getGallery().forEach(img -> retrofitController.getMiniature(img, imageContainer.getCurrentSize()));
        prepareGridView(currentMiniatureHeight, currentMiniatureWidth, imageContainer.getGallery());
        imagesGridView.setCellFactory(new GalleryCellFactory(currentMiniatureHeight, currentMiniatureWidth, placeholder, primaryStage, retrofitController));
    }

    private void updatePageSize() {
        rowSize = getNumberOfImagesInRow();
    }
    public void handleAddDir(String dirName) {
        imageContainer.createDirectory(dirName);
    }

    public void openAddDirDialog(ActionEvent actionEvent) throws IOException {
        Stage dialogStage = new Stage();
        dialogStage.setScene(new DialogScene(this, dialogStage).getScene());
        dialogStage.showAndWait();
    }

    public void removeDir(GalleryDirectory dir) {
        imageContainer.deleteDir(dir);
    }
    public void addScrollListeners() {
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        scrollPane.setOnScroll(event -> {
            if (event.getDeltaY() >= 0)
                return;

            double vvalue = scrollPane.getVvalue();
            vvalue = Math.max(vvalue - 0.01, 0);
            scrollPane.setVvalue(vvalue);
        });

        scrollPane.vvalueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() == scrollPane.getVmax()) {
                retrofitController.loadMore(imageContainer, imageContainer.getCurrentSize(), page++, rowSize);
            }
        });

        imagesGridView.setOnScroll(event -> {
            ScrollBar scrollBar = (ScrollBar) imagesGridView.lookup(".scroll-bar");
            scrollBar.disableProperty().set(true);
            scrollBar.setVisible(false);
        });
    }
}
