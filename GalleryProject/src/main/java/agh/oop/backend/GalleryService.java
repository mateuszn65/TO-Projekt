package agh.oop.backend;

import agh.oop.backend.model.ImageDescriptor;
import agh.oop.backend.model.ImageDescriptorStatus;
import com.google.common.primitives.Bytes;
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
        ImageDescriptor imageDescriptor = new ImageDescriptor(name, ImageDescriptorStatus.UPLOADED);
        int id = repository.save(imageDescriptor).getId();

        if (originalRepository.saveImage(listImageData, id + ".png"))
            return id;
        return -1;
    }


    public List<Byte> getMiniature(int id, int width, int height) throws IOException {
        Optional<ImageDescriptor> imageDescriptor = repository.findById(id);
        if (imageDescriptor.isEmpty())
            return null;

        ImageDescriptor image = imageDescriptor.get();

        if (image.getImageStatus() == ImageDescriptorStatus.FINISHED){
            return originalRepository.getImageData(image.getId() + "m.png");
        }

        if (image.getImageStatus() == ImageDescriptorStatus.UPLOADED){
            image.setImageStatus(ImageDescriptorStatus.CONVERTING);
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
}
