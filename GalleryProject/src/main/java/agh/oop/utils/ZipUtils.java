package agh.oop.utils;

import agh.oop.gallery.model.GalleryImage;
import javafx.event.ActionEvent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import org.apache.tika.Tika;


import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipUtils {

    public static List<GalleryImage> unzipImages(ZipFile file) throws IOException {
        Enumeration<? extends  ZipEntry> entries = file.entries();
        ZipEntry zipEntry;
        List<GalleryImage> imageList = new ArrayList<>();
        while (entries.hasMoreElements()){
            zipEntry = entries.nextElement();
            String name = zipEntry.getName();
            InputStream stream = file.getInputStream(zipEntry);

//            byte[] buffer = getImageData(stream);
            byte[] buffer = stream.readAllBytes();


            if (!isImage(buffer))
                continue;

            GalleryImage galleryImage = new GalleryImage(name, buffer);
            imageList.add(galleryImage);
        }
        return imageList;
    }
    public static byte[] getImageData(InputStream inputStream){
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[16384];

            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            return buffer.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static boolean isImage(byte[] imageData) throws IOException {
        Tika tika = new Tika();
        String fileType = tika.detect(imageData);
        return fileType.startsWith("image");
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
