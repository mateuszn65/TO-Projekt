package agh.oop.gallery.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class ImageContainer {

    private ObservableList<GalleryImage> gallery = FXCollections.observableArrayList();
    public void addToGallery(GalleryImage img) {
        gallery.add(img);
    }

    public ObservableList<GalleryImage> getGallery() {
        return gallery;
    }
}
