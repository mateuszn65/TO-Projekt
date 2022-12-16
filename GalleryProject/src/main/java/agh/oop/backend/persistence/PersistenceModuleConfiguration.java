package agh.oop.backend.persistence;

import org.springframework.context.annotation.Bean;

public class PersistenceModuleConfiguration {
    @Bean
    public OriginalImagesFileRepository fileRepository() {
        return new OriginalImagesFileRepository();
    }
}
