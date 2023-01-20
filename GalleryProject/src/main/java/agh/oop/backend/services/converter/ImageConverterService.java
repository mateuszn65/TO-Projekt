package agh.oop.backend.services.converter;

import agh.oop.backend.model.MiniatureSize;
import agh.oop.backend.persistence.GalleryRepository;
import agh.oop.backend.persistence.OriginalImagesFileRepository;
import agh.oop.backend.model.ImageDescriptor;
import agh.oop.backend.model.ImageDescriptorStatus;
import agh.oop.backend.utils.FilenameMapper;
import agh.oop.backend.utils.LabelMapper;
import com.google.common.primitives.Bytes;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static agh.oop.backend.model.MiniatureSize.SMALL;
import static agh.oop.backend.model.MiniatureSize.MEDIUM;
import static agh.oop.backend.model.MiniatureSize.BIG;

@Service
public class ImageConverterService implements ApplicationListener<ContextRefreshedEvent> {
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


    public void convertRecovered() throws IOException {
        List<ImageDescriptor> descriptorList = (List<ImageDescriptor>) repository.findAll();
        for(ImageDescriptor descriptor : descriptorList) {
            int id = descriptor.getId();
            String filename = id + ".png";
            byte[] imgData = Bytes.toArray(originalRepository.getImageData(filename));
            if(descriptor.getImageStatusSmall() == ImageDescriptorStatus.CONVERTING) {
                convert(id, imgData, SMALL);
            }
            if(descriptor.getImageStatusMedium() == ImageDescriptorStatus.CONVERTING) {
                convert(id, imgData, MEDIUM);
            }
            if(descriptor.getImageStatusBig() == ImageDescriptorStatus.CONVERTING) {
                convert(id, imgData, BIG);
            }
        }

    }
    public void notifyConverted(int id, byte[] data, MiniatureSize size) throws IOException {
        int width = LabelMapper.getWidth(size);
        int height = LabelMapper.getHeight(size);
        originalRepository.saveImage(Bytes.asList(data), FilenameMapper.getFilename(id, width, height));

        Optional<ImageDescriptor> imageDescriptor = repository.findById(id);
        if (imageDescriptor.isEmpty()){
            throw new IllegalStateException("Information was not found in the database");
        }
        imageDescriptor.get().setStatusOfMiniature(size, ImageDescriptorStatus.FINISHED);
        repository.save(imageDescriptor.get());
    }
    public void convert(int id, byte[] imageData, MiniatureSize size){
        queue.addImage(id, imageData, size);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            convertRecovered();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
