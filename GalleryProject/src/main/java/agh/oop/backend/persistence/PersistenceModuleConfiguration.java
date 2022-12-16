package agh.oop.backend.persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersistenceModuleConfiguration {
    @Bean
    public OriginalImagesFileRepository fileRepository() {
        return new OriginalImagesFileRepository();
    }
}
