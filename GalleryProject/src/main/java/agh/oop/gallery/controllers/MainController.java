package agh.oop.gallery.controllers;

import javafx.fxml.FXML;

public class MainController {
    @FXML
    private GalleryController galleryViewController;
    @FXML
    private ZipController uploadButtonController;
    @FXML
    private void initialize(){
        uploadButtonController.injectMainController(this);
    }

    public GalleryController getGalleryController() {
        return galleryViewController;
    }
}
