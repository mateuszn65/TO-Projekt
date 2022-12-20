package agh.oop.backend.services.converter;


import net.coobird.thumbnailator.Thumbnails;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageConverter implements Runnable{
    private final int id;
    private final byte[] img;
    private final int destWidth;
    private final int destHeight;
    private ImageConverterService imageConverterService;

    private static final String OUTPUT_FORMAT = "JPEG";


    public ImageConverter(int id, byte[] img, int destWidth, int destHeight) {
        this.id = id;
        this.img = img;
        this.destWidth = destWidth;
        this.destHeight = destHeight;
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
                    .forceSize(destWidth, destHeight)
                    .outputFormat(OUTPUT_FORMAT)
                    .outputQuality(1)
                    .toOutputStream(outputStream);
            byte[] data = outputStream.toByteArray();

            imageConverterService.notifyConverted(id, data);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
