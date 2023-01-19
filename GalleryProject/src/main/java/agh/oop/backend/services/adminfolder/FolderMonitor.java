package agh.oop.backend.services.adminfolder;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class FolderMonitor implements Runnable {

    private final Path folderPath;
    private final List<FolderMonitorSubscriber> subscribers;

    public FolderMonitor(@Value("${gallery.app.admin.folder}") String folderPath) {
        this.folderPath = Paths.get(folderPath);
        this.subscribers = new ArrayList<>();
        prepareFolder();
        startMonitoring();
    }
    private void prepareFolder() {
        if (!Files.exists(folderPath)) {
            try {
                Files.createDirectory(folderPath);
            } catch (IOException e) {
                log.error("Error while creating admin folder", e);
            }
        }
    }
    private void startMonitoring(){
        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }
    public void addSubscriber(FolderMonitorSubscriber subscriber) {
        subscribers.add(subscriber);
    }
    public void notifySubscribers(Path file) {
        for(FolderMonitorSubscriber subscriber : subscribers) {
            subscriber.notifyToProcess(file);
        }
    }

    @Override
    public void run() {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            folderPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
            log.info("Monitoring folder: " + folderPath);
            while (true) {
                log.info("Waiting for new file...");
                WatchKey key = watchService.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                        Path file = (Path) event.context();
                        log.info("File created: " + file);
                        notifySubscribers(folderPath.resolve(file));
                    }
                }
                key.reset();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
