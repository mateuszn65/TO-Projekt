package agh.oop.backend.utils;

import agh.oop.backend.services.converter.ImageConverter;

public class FilenameMapper {
    public static String getFilename(int id, int width, int height) {
        String extension = ImageConverter.OUTPUT_FORMAT.toLowerCase();
        return String.format("%d-%dx%d.%s", id, width, height, extension);
    }
}
