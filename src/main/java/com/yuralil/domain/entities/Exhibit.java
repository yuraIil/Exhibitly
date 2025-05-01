package com.yuralil.domain.entities;

import java.time.LocalDate;
import java.util.Objects;

public class Exhibit {
    private int id;
    private String name;
    private Category category;
    private String description;
    private LocalDate acquisitionDate;
    private Multimedia multimedia;

    public Exhibit() {
    }

    public Exhibit(int id, String name, Category category, String description, LocalDate acquisitionDate, Multimedia multimedia) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.acquisitionDate = acquisitionDate;
        this.multimedia = multimedia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getAcquisitionDate() {
        return acquisitionDate;
    }

    public void setAcquisitionDate(LocalDate acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public Multimedia getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(Multimedia multimedia) {
        this.multimedia = multimedia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exhibit)) return false;
        Exhibit exhibit = (Exhibit) o;
        return id == exhibit.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
