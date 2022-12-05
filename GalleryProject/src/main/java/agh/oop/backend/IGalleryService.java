package agh.oop.backend;

import agh.oop.gallery.model.GalleryImage;
import io.reactivex.rxjava3.core.Observable;
import java.awt.Image;

import java.util.concurrent.CompletableFuture;

public interface IGalleryService {
    Observable<Image> getImages(int noImages);
    CompletableFuture<Image> upload(java.awt.Image image, int dest_height, int dest_width);
    Image getOriginalImage(String filename);
}
