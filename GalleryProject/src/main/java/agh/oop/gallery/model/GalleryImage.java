package agh.oop.gallery.model;

import java.awt.*;

public class GalleryImage {

    private String name;
    private Image originalImage;
    private Image miniImage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getOriginalImage() {
        return originalImage;
    }

    public void setOriginalImage(Image originalImage) {
        this.originalImage = originalImage;
    }

    public Image getMiniImage() {
        return miniImage;
    }

    public void setMiniImage(Image miniImage) {
        this.miniImage = miniImage;
    }
}
