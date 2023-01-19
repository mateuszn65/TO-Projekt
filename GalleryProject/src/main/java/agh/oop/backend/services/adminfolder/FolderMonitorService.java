package agh.oop.backend.services.adminfolder;

import agh.oop.backend.services.gallery.GalleryService;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;

@Service
public class FolderMonitorService implements FolderMonitorSubscriber {
    private final FolderMonitorHandler folderMonitorHandler;
    private final GalleryService galleryService;

    public FolderMonitorService(FolderMonitor folderMonitor, GalleryService galleryService, FolderMonitorHandler folderMonitorHandler) {
        this.folderMonitorHandler = folderMonitorHandler;
        this.galleryService = galleryService;
        folderMonitor.addSubscriber(this);
    }




    @Override
    public void notifyToProcess(Path file) {
        List<Byte> imageData = folderMonitorHandler.processFile(file);
        if (imageData != null) {
            galleryService.upload(imageData, file.getFileName().toString());
        }
    }
}
