package agh.oop.gallery.model;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import java.io.ByteArrayInputStream;
import java.util.Optional;


public class GalleryImage {

    private String name;
    private Integer id;
    private final ObjectProperty<ImageStatus> imageStatusObjectProperty;
    private Image miniImage;
    private byte[] imageData;
    private GalleryDirectory directory;

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

    public GalleryDirectory getDirectory() {
        return directory;
    }
    public void setDirectory(GalleryDirectory directory) {
        this.directory = directory;
    }

    public void deleteFromCurrentDir() {
        if(directory != null) {
            directory.removeImage(this);
        }
        directory = null;
    }


    public Optional<byte[]> getImageData() {
        return Optional.of(imageData);
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
