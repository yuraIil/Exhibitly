package com.yuralil.domain.entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class Report {
    private int id;
    private String type; // exhibit_description, category_statistics
    private LocalDateTime generatedAt;
    private String content;

    public Report() {
    }

    public Report(int id, String type, LocalDateTime generatedAt, String content) {
        this.id = id;
        this.type = type;
        this.generatedAt = generatedAt;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Report)) return false;
        Report report = (Report) o;
        return id == report.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
