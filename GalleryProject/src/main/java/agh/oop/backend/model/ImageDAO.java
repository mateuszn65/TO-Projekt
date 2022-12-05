package agh.oop.backend.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ImageDAO {
    @Id
    @GeneratedValue
    private int id;
    private String filename;
    private String miniFilename;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getMiniFilename() {
        return miniFilename;
    }

    public void setMiniFilename(String miniFilename) {
        this.miniFilename = miniFilename;
    }
}
