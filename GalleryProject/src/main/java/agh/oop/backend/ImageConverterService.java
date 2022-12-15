package agh.oop.backend;

import agh.oop.backend.model.ImageDescriptor;
import agh.oop.backend.model.ImageDescriptorStatus;
import com.google.common.primitives.Bytes;
import java.io.IOException;
import java.util.Optional;


public class ImageConverterService {
    private GalleryRepository repository;
    private ImageConverterQueue queue;
    private OriginalImagesFileRepository originalRepository;

    public ImageConverterService(GalleryRepository repository,
                                 ImageConverterQueue queue,
                                 OriginalImagesFileRepository originalRepository) {
        this.repository = repository;
        this.queue = queue;
        this.originalRepository = originalRepository;
    }

    public void notifyConverted(int id, byte[] data) throws IOException {
        originalRepository.saveImage(Bytes.asList(data), id + "m.png");

        Optional<ImageDescriptor> imageDescriptor = repository.findById(id);
        if (imageDescriptor.isEmpty()){
            throw new IllegalStateException("Information was not found in the database");
        }
        imageDescriptor.get().setImageStatus(ImageDescriptorStatus.FINISHED);
        repository.save(imageDescriptor.get());
    }
    public void convert(int id, byte[] imageData, int dest_width, int dest_height){
        queue.addImage(id, imageData, dest_width, dest_height);
    }

}
