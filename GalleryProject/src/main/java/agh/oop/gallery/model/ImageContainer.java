package agh.oop.gallery.model;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageContainer {

    private ObservableList<GalleryDirectory> directories = FXCollections.observableArrayList();
    private GalleryDirectory currentDir;
    public static GalleryDirectory rootDir;
    private ObservableList<GalleryImage> gallery = FXCollections.observableArrayList(galleryImage -> new Observable[]{galleryImage.getImageStatusProperty()});
    private int miniatureWidth;
    private int miniatureHeight;
    public ImageContainer(int width, int height) {
        this.miniatureHeight = height;
        this.miniatureWidth = width;
        rootDir = new GalleryDirectory("root", false);
        directories.add(rootDir);
        currentDir = rootDir;
    }

    public boolean deleteDir(GalleryDirectory directory) {
        if (rootDir == directory) {
            return false;
        }
        if (currentDir == directory) {
            setCurrentDir(rootDir);
            refreshView();
        }
        directories.remove(directory);
        return true;
    }

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

    public void createDirectory(String dirName) {
        directories.add(new GalleryDirectory(dirName, true));
    }

    public void setCurrentDir(GalleryDirectory dir) {
        currentDir = dir;
        refreshView();
    }


    public ObservableList<GalleryDirectory> getDirs() {
        return directories;
    }

    public void addToGallery(GalleryImage img) {
        rootDir.addImage(img);
        refreshView();
    }

    public void refreshView() {
        gallery.clear();
        gallery.addAll(currentDir.getImages());
    }

    public ObservableList<GalleryImage> getGallery() {
        return gallery;
    }
}
