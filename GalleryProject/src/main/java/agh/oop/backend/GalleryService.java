package agh.oop.backend;

import agh.oop.backend.model.ImageDescriptor;
import agh.oop.backend.model.ImageDescriptorStatus;
import com.google.common.primitives.Bytes;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.image.Image;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
public class GalleryService{

    private GalleryRepository repository;
    private ImageConverterQueue queue;
    private OriginalImagesFileRepository originalRepository;
    private String resourcesPath = "src/main/resources/Storage/";

    public GalleryService(GalleryRepository repository, ImageConverterQueue queue, OriginalImagesFileRepository originalRepository) {
        this.repository = repository;
        this.queue = queue;
        this.originalRepository = originalRepository;
    }


    public List<Byte> getOriginalImage(int id) throws IOException {
        Optional<ImageDescriptor> imageDescriptor = repository.findById(id);

        return getImageData(imageDescriptor.get().getId() + ".png");
    }

    public int upload(List<Byte> listImageData, String name) throws IOException {
        byte[] imageData = Bytes.toArray(listImageData);
        ImageDescriptor imageDescriptor = new ImageDescriptor();
        imageDescriptor.setFilename(name);
        imageDescriptor.setImageStatus(ImageDescriptorStatus.UPLOADED);
        int id = repository.save(imageDescriptor).getId();


        ByteArrayInputStream bis = new ByteArrayInputStream(imageData);
        BufferedImage bufferedImage = ImageIO.read(bis);
        ImageIO.write(bufferedImage, "png", new File( resourcesPath + id + ".png"));
        return id;
    }

    public String getResourcesPath() {
        return resourcesPath;
    }

    public List<Byte> getMiniature(int id, int width, int height) throws IOException {
        Optional<ImageDescriptor> imageDescriptor = repository.findById(id);
        if (imageDescriptor.isEmpty())
            return null;

        //if exist
        ImageDescriptor image = imageDescriptor.get();

        if (image.getImageStatus() == ImageDescriptorStatus.FINISHED){
            return getImageData(image.getId() + "m.png");
        }
        if (image.getImageStatus() == ImageDescriptorStatus.CONVERTING)
            return null;

        if (image.getImageStatus() == ImageDescriptorStatus.UPLOADED){
            image.setImageStatus(ImageDescriptorStatus.CONVERTING);
            repository.save(image);
            queue.addImage(image.getId(), Bytes.toArray(getImageData(image.getId() + ".png")), width, height);
        }

        return null;
    }

    private List<Byte> getImageData(String filename) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new File(resourcesPath + filename));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);

        byte[] buffer = byteArrayOutputStream.toByteArray();
        List<Byte> res = Bytes.asList(buffer);
        byteArrayOutputStream.close();
        return res;
    }

    public void notifyConverted(int id, byte[] data) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        BufferedImage bufferedImage = ImageIO.read(bis);
        ImageIO.write(bufferedImage, "png", new File( resourcesPath + id + "m.png"));
        Optional<ImageDescriptor> imageDescriptor = repository.findById(id);
        imageDescriptor.get().setImageStatus(ImageDescriptorStatus.FINISHED);
        repository.save(imageDescriptor.get());
    }

    public Map<Integer, String> getInitialImages() {
        Map<Integer, String> map= new HashMap<>();
        for (ImageDescriptor imageDescriptor:repository.findAll()) {
            map.put(imageDescriptor.getId(), imageDescriptor.getFilename());
        }
        return map;
    }

    public List<Byte> getPlaceholder() throws IOException {
        return getImageData( "loading.jpg");
    }
}
