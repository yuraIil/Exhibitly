package com.yuralil.domain.entities;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Представляє звіт, що генерується системою.
 * Може містити інформацію про експонати, категорії тощо.
 */
public class Report {
    private int id;
    private String type; // exhibit_description, category_statistics
    private LocalDateTime generatedAt;
    private String content;

    /**
     * Порожній конструктор.
     */
    public Report() {
    }

    /**
     * Повний конструктор для створення звіту.
     *
     * @param id          унікальний ідентифікатор звіту
     * @param type        тип звіту (наприклад: exhibit_description, category_statistics)
     * @param generatedAt дата й час генерації звіту
     * @param content     вміст звіту (наприклад, згенерований текст чи JSON)
     */
    public Report(int id, String type, LocalDateTime generatedAt, String content) {
        this.id = id;
        this.type = type;
        this.generatedAt = generatedAt;
        this.content = content;
    }

    /**
     * Повертає ID звіту.
     *
     * @return ідентифікатор звіту
     */
    public int getId() {
        return id;
    }

    /**
     * Встановлює ID звіту.
     *
     * @param id новий ID звіту
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Повертає тип звіту.
     *
     * @return тип (наприклад: exhibit_description, category_statistics)
     */
    public String getType() {
        return type;
    }

    /**
     * Встановлює тип звіту.
     *
     * @param type новий тип
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Повертає дату і час, коли звіт був згенерований.
     *
     * @return {@link LocalDateTime} дата й час генерації
     */
    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    /**
     * Встановлює дату й час генерації звіту.
     *
     * @param generatedAt нова дата й час
     */
    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    /**
     * Повертає вміст звіту.
     *
     * @return текст або дані звіту
     */
    public String getContent() {
        return content;
    }

    /**
     * Встановлює вміст звіту.
     *
     * @param content новий вміст
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Перевіряє рівність звітів за їх ID.
     *
     * @param o інший обʼєкт
     * @return {@code true}, якщо ID збігаються
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Report)) return false;
        Report report = (Report) o;
        return id == report.id;
    }

    /**
     * Генерує хешкод на основі ID.
     *
     * @return хешкод
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
