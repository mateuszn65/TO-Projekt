package agh.oop.gallery.model;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;

public class GalleryImage {
    public static int miniWidth = 200;
    public static int miniHeight = 200;

    private String name;

    private ObjectProperty<Image> miniImageProperty;
    private byte[] imageData;

    public byte[] getImageData() {
        return imageData;
    }

    public GalleryImage(String name, byte[] imageData){
        this.imageData = imageData;
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public Image getMiniImage() {
        return miniImageProperty.get();
    }

    public void setMiniImage(byte[] imageData) {
        this.miniImageProperty = new SimpleObjectProperty<>(new Image(new ByteArrayInputStream(imageData)));
    }

    public ObjectProperty<Image> getMiniImageProperty() {
        return this.miniImageProperty;
    }
}
