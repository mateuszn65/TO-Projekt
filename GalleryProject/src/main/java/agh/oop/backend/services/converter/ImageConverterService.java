package agh.oop.backend.services.converter;

import agh.oop.backend.persistence.GalleryRepository;
import agh.oop.backend.persistence.OriginalImagesFileRepository;
import agh.oop.backend.model.ImageDescriptor;
import agh.oop.backend.model.ImageDescriptorStatus;
import com.google.common.primitives.Bytes;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class ImageConverterService {
    private final GalleryRepository repository;
    private final ImageConverterQueue queue;
    private final OriginalImagesFileRepository originalRepository;

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
