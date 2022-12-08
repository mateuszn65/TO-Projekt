package agh.oop.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GalleryServiceConfiguration {
    @Bean
    CommandLineRunner commandLineRunner(GalleryRepository galleryRepository){
        return args -> {

        };
    }

//    @Bean
//    public GalleryService galleryService(GalleryRepository galleryRepository,
//                                         OriginalImagesFileRepository fileRepository,
//                                         ImageConverterQueue queue) {
//        return new GalleryService(galleryRepository, queue, fileRepository);
//    }

    @Bean
    public OriginalImagesFileRepository fileRepository() {
        return new OriginalImagesFileRepository();
    }

    @Bean
    public ImageConverterQueue converterQueue() {
        return new ImageConverterQueue();
    }

    @Bean
    public ImageConverterRunner converterRunner(ImageConverterQueue queue) {
        ImageConverterRunner runner = new ImageConverterRunner(Runtime.getRuntime().availableProcessors());
        queue.subscribe(runner);
        return runner;
    }
}
