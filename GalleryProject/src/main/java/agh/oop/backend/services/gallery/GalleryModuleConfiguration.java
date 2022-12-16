package agh.oop.backend.services.gallery;

import agh.oop.backend.persistence.GalleryRepository;
import agh.oop.backend.persistence.OriginalImagesFileRepository;
import agh.oop.backend.services.gallery.GalleryService;
import agh.oop.backend.services.converter.ImageConverterQueue;
import agh.oop.backend.services.converter.ImageConverterRunner;
import agh.oop.backend.services.converter.ImageConverterService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GalleryModuleConfiguration {

    @Bean
    public GalleryService galleryService(GalleryRepository galleryRepository,
                                         OriginalImagesFileRepository originalImagesFileRepository,
                                         ImageConverterService imageConverterService){
        return new GalleryService(galleryRepository, imageConverterService, originalImagesFileRepository);
    }
}
