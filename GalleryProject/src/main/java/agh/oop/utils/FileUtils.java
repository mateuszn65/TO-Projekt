package agh.oop.utils;

import agh.oop.gallery.model.GalleryImage;
import org.apache.tika.Tika;

import java.io.File;
import java.io.IOException;
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
}
