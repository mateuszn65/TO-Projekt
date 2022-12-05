package agh.oop.gallery.controllers;
import agh.oop.backend.IGalleryService;
import agh.oop.gallery.model.GalleryImage;
import io.reactivex.rxjava3.core.Observable;
import javafx.beans.property.ObjectProperty;

import java.awt.*;
import java.util.List;
import java.util.zip.ZipFile;

public class GalleryController {
    private ObjectProperty<GalleryImage> selectedImage;
    private IGalleryService backendService;


    public void sendImages(ZipFile file, int dest_height, int dest_width) {
        List<Image> galleryImages = ZipController.unzipImages(file);
        for(Image i : galleryImages) {
            backendService.upload(i, dest_height, dest_width);
        }
    }

    public Observable<GalleryImage> getImagesFromService(int noImages) {
        return backendService.getImages(noImages);
    }
}
