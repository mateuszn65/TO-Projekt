package agh.oop.gallery.controllers;
import agh.oop.backend.IGalleryService;
import agh.oop.gallery.model.GalleryImage;
import agh.oop.gallery.model.ImageContainer;
import io.reactivex.rxjava3.core.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.zip.ZipFile;

public class GalleryController {

//    @FXML
//    public Pane galleryView;
    @FXML
    private ImageView imageView;
    private ImageContainer imageContainer;
    private IGalleryService backendService;

//    public Observable<Image> getImagesFromService(int noImages) {
//        return backendService.getImages(noImages);
//    }

    public void setGalleryView(GalleryImage galleryImage) {
        try {
            this.imageView.imageProperty().bind(galleryImage.getOriginalImageProperty());
        }catch (Exception e){
            e.getCause();
            e.printStackTrace();
        }

    }

    @FXML
    public void initialize(){

    }
}
