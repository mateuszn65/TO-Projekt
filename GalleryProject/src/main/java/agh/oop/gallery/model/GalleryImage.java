package agh.oop.gallery.model;


import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;

public class GalleryImage {
    public static int miniWidth = 160;
    public static int miniHeight = 100;
    private String name;
    private Integer id;
    private final ObjectProperty<ImageStatus> imageStatusObjectProperty;

    private Image miniImage;
    private byte[] imageData;

    public byte[] getImageData() {
        return imageData;
    }

    public GalleryImage(String name, byte[] imageData){
        this(null, name, imageData);
    }

    public GalleryImage(Integer id, String name){
        this(id, name, null);
    }
    private GalleryImage(Integer id, String name, byte[] imageData) {
        this.id = id;
        this.name = name;
        this.imageData = imageData;
        this.imageStatusObjectProperty = new SimpleObjectProperty<>(ImageStatus.UPLOADING);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getMiniImage() {
        return miniImage;
    }

    public void setMiniImage(byte[] imageData) {
        this.miniImage = new Image(new ByteArrayInputStream(imageData));
        this.imageData = null;
    }

    public ImageStatus getImageStatus() {
        return imageStatusObjectProperty.get();
    }

    public ObjectProperty<ImageStatus> getImageStatusProperty() {
        return imageStatusObjectProperty;
    }

    public void setImageStatusProperty(ImageStatus imageStatusObjectProperty) {
        this.imageStatusObjectProperty.set(imageStatusObjectProperty);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
