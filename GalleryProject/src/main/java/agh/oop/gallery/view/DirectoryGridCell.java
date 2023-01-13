package agh.oop.gallery.view;

import agh.oop.gallery.model.GalleryDirectory;
import agh.oop.gallery.model.GalleryImage;
import agh.oop.gallery.model.ImageContainer;
import agh.oop.gallery.model.ImageStatus;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class DirectoryGridCell extends ListCell<GalleryDirectory> {

    private ImageContainer container;
    public DirectoryGridCell(ImageContainer container) {
        this.container = container;
    }

    @Override
    protected void updateItem(GalleryDirectory item, boolean empty) {
        super.updateItem(item, empty);
        if(item != null) {
            GridPane pane = new GridPane();
            Button btn = new Button();
            btn.setText("X");
            Label label = new Label();
            setGraphic(pane);
            if(item.canBeDelted()) {
                pane.addRow(0, label, btn);
            } else {
                pane.addRow(0, label);
            }

            label.setText(item.getDirName());
            btn.setOnAction(ev -> {
                container.deleteDir(item);
                setGraphic(null);

            });


            //pane.add(btn, 1, 0);*/
        }
    }

}
