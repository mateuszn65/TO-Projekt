package agh.oop.gallery.model;

import java.util.ArrayList;
import java.util.List;

public class GalleryDirectory {
    private List<GalleryImage> imageList;
    private String dirName;
    private boolean canBeDeleted;
    public GalleryDirectory(String dirName, boolean canBeDeleted) {
        this.dirName = dirName;
        this.imageList = new ArrayList<>();
        this.canBeDeleted =canBeDeleted;
    }

    public void addImage(GalleryImage image) {
        this.imageList.add(image);
        image.setDirectory(this);
    }
    public void removeImage(GalleryImage image) {
        imageList.remove(image);;
    }

    public List<GalleryImage> getImages() {
        return imageList;
    }

    public String getDirName() {
        return dirName;
    }

    public boolean canBeDelted() {
        return canBeDeleted;
    }
}
