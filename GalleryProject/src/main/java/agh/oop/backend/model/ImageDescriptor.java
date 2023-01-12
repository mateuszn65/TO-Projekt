package agh.oop.backend.model;

import agh.oop.backend.utils.LabelMapper;

import javax.persistence.*;

@Entity
public class ImageDescriptor {
    @Id
    @GeneratedValue
    private int id;
    private String filename;
    @Enumerated(EnumType.STRING)
    private ImageDescriptorStatus imageStatusSmall;
    @Enumerated(EnumType.STRING)
    private ImageDescriptorStatus imageStatusMedium;
    @Enumerated(EnumType.STRING)
    private ImageDescriptorStatus imageStatusBig;


    public ImageDescriptor(){}

    public ImageDescriptor(String filename, ImageDescriptorStatus imageStatusSmall, ImageDescriptorStatus imageStatusMedium, ImageDescriptorStatus imageStatusBig) {
        this.filename = filename;
        this.imageStatusSmall = imageStatusSmall;
        this.imageStatusMedium = imageStatusMedium;
        this.imageStatusBig = imageStatusBig;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public ImageDescriptorStatus getImageStatusSmall() {
        return imageStatusSmall;
    }

    public void setImageStatusSmall(ImageDescriptorStatus imageStatusSmall) {
        this.imageStatusSmall = imageStatusSmall;
    }

    public ImageDescriptorStatus getImageStatusMedium() {
        return imageStatusMedium;
    }

    public void setImageStatusMedium(ImageDescriptorStatus imageStatusMedium) {
        this.imageStatusMedium = imageStatusMedium;
    }

    public ImageDescriptorStatus getImageStatusBig() {
        return imageStatusBig;
    }

    public void setImageStatusBig(ImageDescriptorStatus imageStatusBig) {
        this.imageStatusBig = imageStatusBig;
    }

    public ImageDescriptorStatus getStatusOfMiniature(int width, int height) {
        return switch (LabelMapper.getLabel(width, height)) {
            case SMALL -> getImageStatusSmall();
            case MEDIUM -> getImageStatusMedium();
            case BIG -> getImageStatusBig();
        };
    }

    public void setStatusOfMiniature(int width, int height, ImageDescriptorStatus status) {
        switch (LabelMapper.getLabel(width, height)) {
            case SMALL -> setImageStatusSmall(status);
            case MEDIUM -> setImageStatusMedium(status);
            case BIG -> setImageStatusBig(status);
        };
    }
}
