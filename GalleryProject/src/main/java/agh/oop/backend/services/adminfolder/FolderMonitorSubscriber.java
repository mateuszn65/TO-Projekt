package agh.oop.backend.services.adminfolder;

import java.nio.file.Path;

public interface FolderMonitorSubscriber {
    void notifyToProcess(Path file);
}
