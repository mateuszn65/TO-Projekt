package agh.oop.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GalleryServiceConfiguration {
//    @Bean
//    CommandLineRunner commandLineRunner(GalleryRepository galleryRepository){
//        return args -> {
//
//        };
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
    public ImageConverterRunner converterRunner(ImageConverterQueue queue, GalleryService galleryService) {
        ImageConverterRunner runner = new ImageConverterRunner(Runtime.getRuntime().availableProcessors(), galleryService);
        queue.subscribe(runner);
        return runner;
    }
}
