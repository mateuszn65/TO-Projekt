package agh.oop.gallery.controllers;

import agh.oop.GalleryApp;
import agh.oop.RetrofitController;
import agh.oop.backend.IGalleryService;
import agh.oop.gallery.model.GalleryImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import org.apache.tika.Tika;

import javax.imageio.ImageIO;
import java.awt.*;



import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ZipController {
    public Button uploadButton;
    private IGalleryService backendService;
    private MainController mainController;
    private RetrofitController retrofitController = new RetrofitController();
    public void injectMainController(MainController mainController){
        this.mainController = mainController;
    }
    public List<GalleryImage> unzipImages(ZipFile file) throws IOException {
        Enumeration<? extends  ZipEntry> entries = file.entries();
        ZipEntry zipEntry;
        List<GalleryImage> imageList = new ArrayList<>();
        while (entries.hasMoreElements()){
            zipEntry = entries.nextElement();
            InputStream stream = file.getInputStream(zipEntry);

            byte[] buffer = getImageData(stream);


            if (!isImage(buffer))
                continue;

            GalleryImage galleryImage = new GalleryImage(buffer);
            imageList.add(galleryImage);
            mainController.getGalleryController().setGalleryView(galleryImage);
        }
        return imageList;
    }
    private byte[] getImageData(InputStream inputStream){
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
    private boolean isImage(byte[] photoData) throws IOException {
        Tika tika = new Tika();
        String fileType = tika.detect(photoData);
        return fileType.startsWith("image");
    }

    public void handleUploadOnAction(ActionEvent actionEvent) {
    }

    private boolean isZip(File file){
        int fileSig = 0;
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            fileSig = raf.readInt();
        } catch (Exception e) {
            return false;
        }
        return fileSig == 0x504B0304 || fileSig == 0x504B0506  || fileSig == 0x504B0708;
    }
    public void handleDragDropped(DragEvent dragEvent) {
        List<File> files = dragEvent.getDragboard().getFiles();
        if (files.size() != 1)
            return;
        if (!isZip(files.get(0)))
            return;

        try {
            List<GalleryImage> galleryImages = unzipImages(new ZipFile(files.get(0)));
            galleryImages.forEach(galleryImage -> retrofitController.upload(galleryImage.getImageData()));
        } catch (IOException e) {
            e.getCause();
            e.printStackTrace();
        }
    }

    public void handleDragOver(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasFiles()){
            dragEvent.acceptTransferModes(TransferMode.COPY);
        }
    }
}
