package agh.oop.backend.services.converter;
;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConverterModuleConfiguration {
    @Bean
    public ImageConverterRunner converterRunner(ImageConverterQueue queue,
                                                ImageConverterService imageConverterService) {
        ImageConverterRunner runner = new ImageConverterRunner(Runtime.getRuntime().availableProcessors(), imageConverterService);
        queue.subscribe(runner);
        return runner;
    }
}
