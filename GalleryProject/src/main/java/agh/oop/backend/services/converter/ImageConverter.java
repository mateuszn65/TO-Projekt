package agh.oop.backend.services.converter;


import agh.oop.backend.model.MiniatureSize;
import agh.oop.backend.utils.LabelMapper;
import net.coobird.thumbnailator.Thumbnails;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageConverter implements Runnable{
    private final int id;
    private final byte[] img;
    private MiniatureSize size;
    private ImageConverterService imageConverterService;

    public static final String OUTPUT_FORMAT = "JPEG";


    public ImageConverter(int id, byte[] img, MiniatureSize size) {
        this.id = id;
        this.img = img;
        this.size = size;
    }

    public void setImageConverterService(ImageConverterService imageConverterService) {
        this.imageConverterService = imageConverterService;
    }

    @Override
    public void run() {
        //converting
        try {
            int destWidth = LabelMapper.getWidth(size);
            int destHeight = LabelMapper.getHeight(size);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ByteArrayInputStream is = new ByteArrayInputStream(img);
            BufferedImage bufferedImage = ImageIO.read(is);
            Thumbnails.of(bufferedImage)
                    .forceSize(destWidth, destHeight)
                    .outputFormat(OUTPUT_FORMAT)
                    .outputQuality(1)
                    .toOutputStream(outputStream);
            byte[] data = outputStream.toByteArray();

            imageConverterService.notifyConverted(id, data, size);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
