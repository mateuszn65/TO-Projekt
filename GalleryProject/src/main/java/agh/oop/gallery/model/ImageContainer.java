package agh.oop.gallery.model;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ImageContainer {
    private ObservableList<GalleryImage> gallery = FXCollections.observableArrayList(galleryImage -> new Observable[]{galleryImage.getImageStatusProperty()});
    private int miniatureWidth;
    private int miniatureHeight;

    public int getMiniatureWidth() {
        return miniatureWidth;
    }

    public void setMiniatureWidth(int miniatureWidth) {
        this.miniatureWidth = miniatureWidth;
    }

    public int getMiniatureHeight() {
        return miniatureHeight;
    }

    public void setMiniatureHeight(int miniatureHeight) {
        this.miniatureHeight = miniatureHeight;
    }

    public ImageContainer(int width, int height) {
        this.miniatureHeight = height;
        this.miniatureWidth = width;
    }

    public void addToGallery(GalleryImage img) {
        gallery.add(img);
    }

    public ObservableList<GalleryImage> getGallery() {
        return gallery;
    }
}
