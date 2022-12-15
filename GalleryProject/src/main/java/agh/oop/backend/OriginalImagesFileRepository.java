package agh.oop.backend;

import com.google.common.primitives.Bytes;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class OriginalImagesFileRepository {
    private final String resourcesPath = "src/main/resources/Storage/";

    public List<Byte> getImageData(String filename) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new File(resourcesPath + filename));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);

        byte[] buffer = byteArrayOutputStream.toByteArray();
        List<Byte> res = Bytes.asList(buffer);
        byteArrayOutputStream.close();
        return res;
    }

    public boolean saveImage(List<Byte> listImageData, String filename){
        try {
            byte[] imageData = Bytes.toArray(listImageData);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
            BufferedImage bufferedImage = ImageIO.read(bis);
            ImageIO.write(bufferedImage, "png", new File( resourcesPath + filename));
            return true;
        }catch (IOException e){
            return false;
        }
    }
}
