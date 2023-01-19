package agh.oop.gallery.view;

import agh.oop.gallery.model.GalleryImage;
import agh.oop.gallery.model.ImageStatus;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.controlsfx.control.GridCell;

public class GalleryGridCell extends GridCell<GalleryImage> {
    public static final int cellPadding = 10;
    private final int cellHeight;
    private final int cellWidth;
    private final Image placeholderImage;

    public GalleryGridCell(int cellHeight, int cellWidth, Image placeholderImage) {
        this.cellHeight = cellHeight;
        this.cellWidth = cellWidth;
        this.placeholderImage = placeholderImage;
    }

    @Override
    protected void updateItem(GalleryImage item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item.getImageStatus() == ImageStatus.UPLOADING) {
            setGraphic(null);
            return;
        }

        ImageView photoIcon = new ImageView();
        photoIcon.setPreserveRatio(true);
        photoIcon.setFitHeight(cellHeight);
        photoIcon.setFitWidth(cellWidth);

        if (item.getImageStatus() == ImageStatus.RECEIVED){
            photoIcon.imageProperty().set(item.getMiniImage());
            setGraphic(photoIcon);
        } else if (item.getImageStatus() == ImageStatus.LOADING) {
            photoIcon.imageProperty().set(placeholderImage);
            setGraphic(photoIcon);
        }
    }
}
