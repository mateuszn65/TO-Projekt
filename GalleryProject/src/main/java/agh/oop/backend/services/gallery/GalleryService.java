package agh.oop.backend.services.gallery;

import agh.oop.backend.persistence.GalleryRepository;
import agh.oop.backend.persistence.OriginalImagesFileRepository;
import agh.oop.backend.model.ImageDescriptor;
import agh.oop.backend.model.ImageDescriptorStatus;
import agh.oop.backend.services.converter.ImageConverterService;
import agh.oop.backend.utils.FilenameMapper;
import agh.oop.backend.utils.LabelMapper;
import com.google.common.primitives.Bytes;
import org.springframework.data.domain.Pageable;

import java.io.*;
import java.util.*;


public class GalleryService{
    private GalleryRepository repository;
    private ImageConverterService imageConverterService;
    private OriginalImagesFileRepository originalRepository;


    public GalleryService(GalleryRepository repository,
                          ImageConverterService imageConverterService,
                          OriginalImagesFileRepository originalRepository) {
        this.repository = repository;
        this.imageConverterService = imageConverterService;
        this.originalRepository = originalRepository;
    }

    public List<Byte> getOriginalImage(int id) throws IOException {
        Optional<ImageDescriptor> imageDescriptor = repository.findById(id);
        return originalRepository.getImageData(imageDescriptor.get().getId() + ".png");
    }

    public int upload(List<Byte> listImageData, String name) {
        ImageDescriptor imageDescriptor = new ImageDescriptor(name, ImageDescriptorStatus.READY, ImageDescriptorStatus.READY, ImageDescriptorStatus.READY);
        int id = repository.save(imageDescriptor).getId();

        if (originalRepository.saveImage(listImageData, id + ".png"))
            return id;
        return -1;
    }


    public List<Byte> getMiniature(int id, int width, int height) throws IOException {
        Optional<ImageDescriptor> imageDescriptor = repository.findById(id);
        if (imageDescriptor.isEmpty()) {
            return null;
        }
        ImageDescriptor image = imageDescriptor.get();

        if (image.getStatusOfMiniature(width, height) == ImageDescriptorStatus.FINISHED){
            return originalRepository.getImageData(FilenameMapper.getFilename(id, width, height));
        }

        if (image.getStatusOfMiniature(width, height) == ImageDescriptorStatus.READY){
            image.setStatusOfMiniature(width, height, ImageDescriptorStatus.CONVERTING);
            repository.save(image);
            imageConverterService.convert(image.getId(), Bytes.toArray(originalRepository.getImageData(image.getId() + ".png")), width, height);
        }

        return null;
    }




    public Map<Integer, String> getInitialImages() {
        Map<Integer, String> map= new HashMap<>();
        for (ImageDescriptor imageDescriptor:repository.findAll()) {
            map.put(imageDescriptor.getId(), imageDescriptor.getFilename());
        }
        return map;
    }

    public List<Byte> getPlaceholder() throws IOException {
        return originalRepository.getImageData( "loading.png");
    }

    public Map<Integer, String> getImages(Pageable pageable) {
        Map<Integer, String> map= new HashMap<>();
        for (ImageDescriptor imageDescriptor:repository.findAll(pageable)) {
            map.put(imageDescriptor.getId(), imageDescriptor.getFilename());
        }
        return map;
    }
}
