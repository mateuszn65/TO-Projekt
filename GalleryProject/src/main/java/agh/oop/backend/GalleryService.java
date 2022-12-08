package agh.oop.backend;

import agh.oop.backend.model.ImageDAO;
import agh.oop.gallery.model.GalleryImage;
import io.reactivex.rxjava3.core.Observable;
import javafx.scene.image.Image;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
@Service
public class GalleryService{

    private GalleryRepository repository;
    private ImageConverterQueue queue;
    private OriginalImagesFileRepository originalRepository;

    public GalleryService(GalleryRepository repository, ImageConverterQueue queue, OriginalImagesFileRepository originalRepository) {
        this.repository = repository;
        this.queue = queue;
        this.originalRepository = originalRepository;
    }


    public Observable<Image> getImages(int noImages) {
        List<ImageDAO> imageDescriptors = repository.findAll();
        List<Image> images = new ArrayList<>();
        for(ImageDAO imgDesc : imageDescriptors) {
            images.add(originalRepository.getImageByDescriptor(imgDesc.getMiniFilename()));
        }
        return Observable.fromIterable(images);
    }

    public CompletableFuture<Image> upload(Image image, int dest_height, int dest_width) {
        CompletableFuture<Image> result = new CompletableFuture<>();
        queue.addImage(image, result, dest_height, dest_width);
        return result;
    }


    public Image getOriginalImage(String filename) {
        return originalRepository.getImageByDescriptor(filename);
    }
}
