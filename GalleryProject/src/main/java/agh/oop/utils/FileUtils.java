package agh.oop.utils;

import agh.oop.gallery.model.GalleryImage;
import com.google.common.primitives.Bytes;
import org.apache.tika.Tika;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

import static agh.oop.utils.GalleryImageUtils.getFilesContent;
import static agh.oop.utils.ZipUtils.isZip;
import static agh.oop.utils.ZipUtils.unzipImages;

public class FileUtils {
    public static boolean isImage(byte[] imageData) {
        Tika tika = new Tika();
        String fileType = tika.detect(imageData);
        return fileType.startsWith("image");
    }

    public static List<GalleryImage> getImagesFromFiles(List<File> files) throws IOException {
        List<GalleryImage> galleryImages;
        if (isZip(files.get(0))) {
            galleryImages = unzipImages(new ZipFile(files.get(0)));
        } else {
            galleryImages = getFilesContent(files);
        }
        return galleryImages;
    }
    public static List<Byte> getBytesFromFile(File file) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        byte[] buffer = inputStream.readAllBytes();
        inputStream.close();

        if (isImage(buffer))
            return Bytes.asList(buffer);
        return null;
    }
}
