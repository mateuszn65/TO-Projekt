package agh.oop.backend;

import javafx.scene.image.Image;
import java.util.concurrent.CompletableFuture;

public class ImageConverter implements Runnable{
    private Image img;
    private int dest_width;
    private int dest_height;
    private CompletableFuture<Image> associatedFuture;

    public ImageConverter(Image img, int dest_width, int dest_height, CompletableFuture<Image> associatedFuture) {
        this.img = img;
        this.dest_width = dest_width;
        this.dest_height = dest_height;
        this.associatedFuture = associatedFuture;
    }

    @Override
    public void run() {
        //converting
        Image converted = null;
        associatedFuture.complete(converted);
    }
}
