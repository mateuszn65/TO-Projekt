package agh.oop.backend.services.adminfolder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static agh.oop.utils.FileUtils.getBytesFromFile;

@Slf4j
@Component
public class FolderMonitorHandler {

    public List<Byte> processFile(Path file) {
        log.info("Processing file: " + file);
        List<Byte> imageData = null;
        try {
            imageData = getBytesFromFile(file.toFile());
        } catch (Exception e) {
            log.error("Error while processing file: " + file, e);
        }
        return imageData;
    }
}
