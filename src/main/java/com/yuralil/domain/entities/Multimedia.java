package com.yuralil.domain.entities;

import java.util.Objects;

public class Multimedia {
    private int id;
    private Exhibit exhibit;
    private String type; // image, video, audio
    private String filePath;

    public Multimedia() {
    }

    public Multimedia(int id, Exhibit exhibit, String type, String filePath) {
        this.id = id;
        this.exhibit = exhibit;
        this.type = type;
        this.filePath = filePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Exhibit getExhibit() {
        return exhibit;
    }

    public void setExhibit(Exhibit exhibit) {
        this.exhibit = exhibit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Multimedia)) return false;
        Multimedia that = (Multimedia) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
