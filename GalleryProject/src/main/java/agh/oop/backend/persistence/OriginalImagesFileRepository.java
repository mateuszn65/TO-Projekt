package agh.oop.backend.persistence;

import com.google.common.primitives.Bytes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class OriginalImagesFileRepository {
    @Value("${gallery.app.storage.path}")
    private String resourcesPath;

    public static final String IMG_FILETYPE = "png";

    public List<Byte> getImageData(String filename) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new File(resourcesPath + filename));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, IMG_FILETYPE, byteArrayOutputStream);

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
            ImageIO.write(bufferedImage, IMG_FILETYPE, new File( resourcesPath + filename));
            return true;
        }catch (IOException e){
            return false;
        }
    }
}
