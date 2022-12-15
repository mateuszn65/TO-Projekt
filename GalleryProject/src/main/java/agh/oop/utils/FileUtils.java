package agh.oop.utils;

import org.apache.tika.Tika;

import java.io.IOException;

public class FileUtils {
    public static boolean isImage(byte[] imageData) throws IOException {
        Tika tika = new Tika();
        String fileType = tika.detect(imageData);
        return fileType.startsWith("image");
    }
}
