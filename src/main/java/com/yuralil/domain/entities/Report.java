package com.yuralil.domain.entities;

import com.yuralil.domain.enums.ReportType;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Представляє звіт, що генерується системою.
 * Зберігає тип, дату створення та вміст (у форматі байтів).
 */
public class Report {
    private int id;
    private ReportType type;
    private LocalDateTime generatedAt;
    private byte[] content;

    public Report() {}

    public Report(int id, ReportType type, LocalDateTime generatedAt, byte[] content) {
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

    public ReportType getType() {
        return type;
    }

    public void setType(ReportType type) {
        this.type = type;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
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
