package agh.oop.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GalleryServiceConfiguration {
    @Bean
    public OriginalImagesFileRepository fileRepository() {
        return new OriginalImagesFileRepository();
    }

    @Bean
    public ImageConverterQueue converterQueue() {
        return new ImageConverterQueue();
    }

    @Bean
    public ImageConverterRunner converterRunner(ImageConverterQueue queue,
                                                ImageConverterService imageConverterService) {
        ImageConverterRunner runner = new ImageConverterRunner(Runtime.getRuntime().availableProcessors(), imageConverterService);
        queue.subscribe(runner);
        return runner;
    }
    @Bean
    public ImageConverterService imageConverterService(GalleryRepository repository,
                                                       ImageConverterQueue queue,
                                                       OriginalImagesFileRepository originalRepository){
        return new ImageConverterService(repository, queue, originalRepository);
    }

    @Bean
    public GalleryService galleryService(GalleryRepository galleryRepository,
                                         OriginalImagesFileRepository originalImagesFileRepository,
                                         ImageConverterService imageConverterService){
        return new GalleryService(galleryRepository, imageConverterService, originalImagesFileRepository);
    }
}
