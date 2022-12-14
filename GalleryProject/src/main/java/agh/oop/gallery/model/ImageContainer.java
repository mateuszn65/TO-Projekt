package agh.oop.gallery.model;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ImageContainer {
    private ObservableList<GalleryImage> gallery = FXCollections.observableArrayList(galleryImage -> new Observable[]{galleryImage.getImageStatusProperty()});

    public void addToGallery(GalleryImage img) {
        gallery.add(img);
    }

    public ObservableList<GalleryImage> getGallery() {
        return gallery;
    }
}
