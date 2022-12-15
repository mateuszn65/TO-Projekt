package agh.oop.utils;

import agh.oop.gallery.model.GalleryImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static agh.oop.utils.FileUtils.isImage;

public class GalleryImageUtils {
    public static List<GalleryImage> getFilesContent(List<File> files) throws IOException {
        List<GalleryImage> galleryImages = new ArrayList<>();

        for (File file:files) {
            GalleryImage galleryImage;
            if ((galleryImage = createGalleryImage(file)) != null){
                galleryImages.add(galleryImage);
            }
        }

        return galleryImages;
    }
    public static GalleryImage createGalleryImage(File file) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        byte[] buffer = inputStream.readAllBytes();
        inputStream.close();
        if (isImage(buffer)){
            return new GalleryImage(file.getName(), buffer);
        }
        return null;
    }
}
