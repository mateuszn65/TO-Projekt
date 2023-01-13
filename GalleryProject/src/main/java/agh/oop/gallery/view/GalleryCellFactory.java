package agh.oop.gallery.view;

import agh.oop.RetrofitController;
import agh.oop.gallery.model.GalleryImage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;

import java.io.IOException;

public class GalleryCellFactory implements Callback<GridView<GalleryImage>, GridCell<GalleryImage>> {

    private final int cellHeight;
    private final int cellWidth;
    private final Image placeholderImage;
    private final Stage primaryStage;
    private final RetrofitController retrofitController;

    public GalleryCellFactory(int cellHeight, int cellWidth, Image placeholderImage, Stage primaryStage, RetrofitController retrofitController) {
        this.cellHeight = cellHeight;
        this.cellWidth = cellWidth;
        this.placeholderImage = placeholderImage;
        this.primaryStage = primaryStage;
        this.retrofitController = retrofitController;
    }

    @Override
    public GridCell<GalleryImage> call(GridView<GalleryImage> param) {
        GridCell<GalleryImage> gridCell =  new GalleryGridCell(cellHeight, cellWidth, placeholderImage);
        gridCell.setOnMouseClicked(event -> {
            if (gridCell.getItem() != null) {
                try {
                    showPreview(gridCell.getItem());
                } catch (IOException e) {
                    event.consume();
                    throw new RuntimeException(e);
                }
            }
            event.consume();
        });
        gridCell.setOnDragDetected(ev -> {
            System.out.println("yellow");
            Dragboard db = gridCell.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString("str");
            db.setContent(content);
            ev.consume();
        });
        return gridCell;
    }

    private void showPreview(GalleryImage galleryImage) throws IOException {
        Stage previewStage = new Stage();
        previewStage.setTitle(galleryImage.getName());
        previewStage.initModality(Modality.WINDOW_MODAL);
        previewStage.initOwner(primaryStage);

        Pane pane = new Pane();
        ImageView imageView = new ImageView(retrofitController.getOriginalImage(galleryImage.getId()));
        pane.getChildren().add(imageView);

        Scene scene = new Scene(pane);
        previewStage.setScene(scene);
        previewStage.showAndWait();
    }

}
