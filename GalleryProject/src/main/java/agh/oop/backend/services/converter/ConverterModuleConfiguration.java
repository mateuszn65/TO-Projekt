package agh.oop.backend.services.converter;

import agh.oop.backend.persistence.GalleryRepository;
import agh.oop.backend.persistence.OriginalImagesFileRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConverterModuleConfiguration {

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
}
