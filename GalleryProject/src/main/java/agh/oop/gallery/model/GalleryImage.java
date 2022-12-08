package agh.oop.gallery.model;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;

public class GalleryImage {
    public static int miniWidth = 200;
    public static int miniHeight = 200;

    private String name;
    private ObjectProperty<Image> originalImageProperty;
    private Image miniImage;
    private byte[] imageData;

    public byte[] getImageData() {
        return imageData;
    }

    public GalleryImage(byte[] imageData){
        this.imageData = imageData;
        Image image = new Image(new ByteArrayInputStream(imageData));
        this.originalImageProperty = new SimpleObjectProperty<>(image);
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getOriginalImage() {
        return originalImageProperty.get();
    }

    public void setOriginalImage(Image originalImage) {
        this.originalImageProperty.set(originalImage);
    }

    public Image getMiniImage() {
        return miniImage;
    }

    public void setMiniImage(Image miniImage) {
        this.miniImage = miniImage;
    }

    public ObjectProperty<Image> getOriginalImageProperty() {
        return originalImageProperty;
    }
}
