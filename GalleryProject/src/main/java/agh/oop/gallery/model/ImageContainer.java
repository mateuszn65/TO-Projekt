package agh.oop.gallery.model;

import java.util.ArrayList;
import java.util.List;

public class ImageContainer {
    private List<GalleryImage> gallery = new ArrayList<>();

    public void addToGallery(GalleryImage img) {
        gallery.add(img);
    }
}
