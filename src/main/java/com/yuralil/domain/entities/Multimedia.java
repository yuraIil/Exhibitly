package com.yuralil.domain.entities;

import java.util.Objects;

/**
 * Представляє мультимедійний обʼєкт, пов'язаний з експонатом.
 * Це може бути зображення, відео або аудіо.
 */
public class Multimedia {
    private int id;
    private Exhibit exhibit;
    private String type; // image, video, audio
    private String filePath;

    /**
     * Порожній конструктор.
     */
    public Multimedia() {
    }

    /**
     * Повний конструктор для створення мультимедіа.
     *
     * @param id        унікальний ідентифікатор
     * @param exhibit   пов'язаний експонат (може бути null)
     * @param type      тип мультимедіа (image, video, audio)
     * @param filePath  шлях до файлу
     */
    public Multimedia(int id, Exhibit exhibit, String type, String filePath) {
        this.id = id;
        this.exhibit = exhibit;
        this.type = type;
        this.filePath = filePath;
    }

    /**
     * Повертає ID мультимедійного обʼєкта.
     *
     * @return ідентифікатор
     */
    public int getId() {
        return id;
    }

    /**
     * Встановлює ID мультимедійного обʼєкта.
     *
     * @param id новий ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Повертає експонат, до якого привʼязане це мультимедіа.
     *
     * @return обʼєкт {@link Exhibit}
     */
    public Exhibit getExhibit() {
        return exhibit;
    }

    /**
     * Встановлює повʼязаний експонат.
     *
     * @param exhibit обʼєкт {@link Exhibit}
     */
    public void setExhibit(Exhibit exhibit) {
        this.exhibit = exhibit;
    }

    /**
     * Повертає тип мультимедіа (наприклад: image, video, audio).
     *
     * @return тип
     */
    public String getType() {
        return type;
    }

    /**
     * Встановлює тип мультимедіа.
     *
     * @param type тип мультимедіа
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Повертає шлях до файлу мультимедіа.
     *
     * @return шлях до файлу
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Встановлює шлях до файлу мультимедіа.
     *
     * @param filePath новий шлях
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Перевіряє рівність мультимедіа за їх ID.
     *
     * @param o інший обʼєкт
     * @return {@code true}, якщо ID збігаються
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Multimedia)) return false;
        Multimedia that = (Multimedia) o;
        return id == that.id;
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
