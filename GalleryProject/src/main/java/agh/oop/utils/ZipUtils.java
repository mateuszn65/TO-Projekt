package agh.oop.utils;

import agh.oop.gallery.model.GalleryImage;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static agh.oop.utils.FileUtils.isImage;

public class ZipUtils {

    public static List<GalleryImage> unzipImages(ZipFile file) throws IOException {
        Enumeration<? extends  ZipEntry> entries = file.entries();
        ZipEntry zipEntry;
        List<GalleryImage> imageList = new ArrayList<>();
        while (entries.hasMoreElements()){
            zipEntry = entries.nextElement();
            String name = zipEntry.getName();
            InputStream stream = file.getInputStream(zipEntry);

            byte[] buffer = stream.readAllBytes();

            if (!isImage(buffer))
                continue;

            GalleryImage galleryImage = new GalleryImage(name, buffer);
            imageList.add(galleryImage);
        }
        return imageList;
    }

    public static boolean isZip(File file){
        int fileSig = 0;
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            fileSig = raf.readInt();
        } catch (Exception e) {
            return false;
        }
        return fileSig == 0x504B0304 || fileSig == 0x504B0506  || fileSig == 0x504B0708;
    }

}
