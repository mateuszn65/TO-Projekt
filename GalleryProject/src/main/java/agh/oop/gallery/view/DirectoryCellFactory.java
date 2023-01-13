package agh.oop.gallery.view;

import agh.oop.gallery.model.GalleryDirectory;
import agh.oop.gallery.model.GalleryImage;
import agh.oop.gallery.model.ImageContainer;
import javafx.event.EventHandler;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.util.Callback;

import java.io.IOException;

public class DirectoryCellFactory  implements Callback<ListView<GalleryDirectory>, ListCell<GalleryDirectory>> {

    public static final double directoryCellHeight = 30;
    private final ImageContainer container;

    public DirectoryCellFactory(ImageContainer container) {
        this.container = container;
    }

    @Override
    public ListCell<GalleryDirectory> call(ListView<GalleryDirectory> param) {
        ListCell<GalleryDirectory> directoryListCell = new DirectoryGridCell(container);
        directoryListCell.setOnMouseClicked(event -> {
            container.setCurrentDir(directoryListCell.getItem());
            System.out.println("CLICKED" + directoryListCell.getItem().getDirName());
        });
        directoryListCell.setOnDragDropped(ev -> {
            GalleryGridCell source = (GalleryGridCell) ev.getGestureSource();
            DirectoryGridCell target = (DirectoryGridCell) ev.getGestureTarget();
            System.out.println("adding img to dir");
            GalleryImage img = source.getItem();
            img.deleteFromCurrentDir();
            GalleryDirectory dir = target.getItem();
            dir.addImage(img);
            container.refreshView();
        });
        directoryListCell.setOnDragOver(new EventHandler<DragEvent>() {
            public void handle(DragEvent event) {
                event.acceptTransferModes(TransferMode.ANY);
                event.consume();
            }
        });
        return directoryListCell;
    }
}
