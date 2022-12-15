package agh.oop.backend.model;


import javax.persistence.*;

@Entity
public class ImageDescriptor {
    @Id
    @GeneratedValue
    private int id;
    private String filename;
    @Enumerated(EnumType.STRING)
    private ImageDescriptorStatus imageStatus;
//    private String miniFilename;
    public ImageDescriptor(){}

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
    public ImageDescriptorStatus getImageStatus() {
        return imageStatus;
    }

    public void setImageStatus(ImageDescriptorStatus imageStatus) {
        this.imageStatus = imageStatus;
    }
//    public String getMiniFilename() {
//        return miniFilename;
//    }

//    public void setMiniFilename(String miniFilename) {
//        this.miniFilename = miniFilename;
//    }
}
