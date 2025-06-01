package com.yuralil.domain.entities;

import com.yuralil.domain.enums.ReportType;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Представляє звіт, що генерується системою.
 * <p>
 * Звіт зберігає:
 * <ul>
 *     <li>ідентифікатор {@code id}</li>
 *     <li>тип {@link ReportType}</li>
 *     <li>дату генерації {@code generatedAt}</li>
 *     <li>вміст звіту у вигляді {@code byte[]}</li>
 * </ul>
 */
public class Report {

    private int id;
    private ReportType type;
    private LocalDateTime generatedAt;
    private byte[] content;

    /**
     * Порожній конструктор.
     */
    public Report() {}

    /**
     * Повний конструктор звіту.
     *
     * @param id          ідентифікатор
     * @param type        тип звіту
     * @param generatedAt дата та час створення
     * @param content     вміст звіту у байтах
     */
    public Report(int id, ReportType type, LocalDateTime generatedAt, byte[] content) {
        this.id = id;
        this.type = type;
        this.generatedAt = generatedAt;
        this.content = content;
    }

    /**
     * @return ідентифікатор звіту
     */
    public int getId() {
        return id;
    }

    /**
     * Встановлює ідентифікатор звіту.
     *
     * @param id новий ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return тип звіту
     */
    public ReportType getType() {
        return type;
    }

    /**
     * Встановлює тип звіту.
     *
     * @param type тип {@link ReportType}
     */
    public void setType(ReportType type) {
        this.type = type;
    }

    /**
     * @return дата та час генерації звіту
     */
    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    /**
     * Встановлює дату генерації звіту.
     *
     * @param generatedAt дата {@link LocalDateTime}
     */
    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    /**
     * @return вміст звіту у вигляді байтового масиву
     */
    public byte[] getContent() {
        return content;
    }

    /**
     * Встановлює вміст звіту.
     *
     * @param content масив байтів
     */
    public void setContent(byte[] content) {
        this.content = content;
    }

    /**
     * Перевіряє рівність за ID.
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

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
