package agh.oop.backend;


import net.coobird.thumbnailator.Thumbnails;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageConverter implements Runnable{
    private int id;
    private byte[] img;
    private int dest_width;
    private int dest_height;
    private ImageConverterService imageConverterService;


    public ImageConverter(int id, byte[] img, int dest_width, int dest_height) {
        this.id = id;
        this.img = img;
        this.dest_width = dest_width;
        this.dest_height = dest_height;
    }

    public void setImageConverterService(ImageConverterService imageConverterService) {
        this.imageConverterService = imageConverterService;
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

            imageConverterService.notifyConverted(id, data);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
