package com.yuralil.domain.entities;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Представляє експонат у системі.
 * Містить основну інформацію: назву, категорію, опис, дату надбання та мультимедіа.
 */
public class Exhibit {
    private int id;
    private String name;
    private Category category;
    private String description;
    private LocalDate acquisitionDate;
    private Multimedia multimedia;

    /**
     * Порожній конструктор.
     */
    public Exhibit() {
    }

    /**
     * Повний конструктор для створення експоната.
     *
     * @param id              унікальний ідентифікатор
     * @param name            назва експоната
     * @param category        категорія експоната
     * @param description     опис експоната
     * @param acquisitionDate дата надбання
     * @param multimedia      пов'язане мультимедіа
     */
    public Exhibit(int id, String name, Category category, String description, LocalDate acquisitionDate, Multimedia multimedia) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.acquisitionDate = acquisitionDate;
        this.multimedia = multimedia;
    }

    /**
     * Повертає ідентифікатор експоната.
     *
     * @return ID експоната
     */
    public int getId() {
        return id;
    }

    /**
     * Встановлює ідентифікатор експоната.
     *
     * @param id новий ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Повертає назву експоната.
     *
     * @return назва
     */
    public String getName() {
        return name;
    }

    /**
     * Встановлює назву експоната.
     *
     * @param name нова назва
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Повертає категорію експоната.
     *
     * @return обʼєкт {@link Category}
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Встановлює категорію експоната.
     *
     * @param category нова категорія
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Повертає опис експоната.
     *
     * @return опис
     */
    public String getDescription() {
        return description;
    }

    /**
     * Встановлює опис експоната.
     *
     * @param description новий опис
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Повертає дату надбання експоната.
     *
     * @return дата надбання
     */
    public LocalDate getAcquisitionDate() {
        return acquisitionDate;
    }

    /**
     * Встановлює дату надбання експоната.
     *
     * @param acquisitionDate дата надбання
     */
    public void setAcquisitionDate(LocalDate acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    /**
     * Повертає пов'язане мультимедіа експоната.
     *
     * @return обʼєкт {@link Multimedia}
     */
    public Multimedia getMultimedia() {
        return multimedia;
    }

    /**
     * Встановлює мультимедіа для експоната.
     *
     * @param multimedia нове мультимедіа
     */
    public void setMultimedia(Multimedia multimedia) {
        this.multimedia = multimedia;
    }

    /**
     * Перевіряє рівність експонатів за їх ID.
     *
     * @param o інший обʼєкт
     * @return {@code true}, якщо ID збігаються
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exhibit)) return false;
        Exhibit exhibit = (Exhibit) o;
        return id == exhibit.id;
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
