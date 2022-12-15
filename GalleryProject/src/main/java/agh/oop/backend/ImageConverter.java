package agh.oop.backend;

import javafx.scene.image.Image;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class ImageConverter implements Runnable{
    private int id;
    private byte[] img;
    private int dest_width;
    private int dest_height;
    private GalleryService galleryService;


    public ImageConverter(int id, byte[] img, int dest_width, int dest_height) {
        this.id = id;
        this.img = img;
        this.dest_width = dest_width;
        this.dest_height = dest_height;
    }

    public void setGalleryService(GalleryService galleryService) {
        this.galleryService = galleryService;
    }

    @Override
    public void run() {
        //converting
        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ByteArrayInputStream is = new ByteArrayInputStream(img);
            BufferedImage bufferedImage = ImageIO.read(is);
            Thumbnails.of(bufferedImage)
                    .forceSize(dest_width, dest_height)
                    .outputFormat("JPEG")
                    .outputQuality(1)
                    .toOutputStream(outputStream);
            byte[] data = outputStream.toByteArray();

            galleryService.notifyConverted(id, data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
