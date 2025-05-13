package com.yuralil.domain.entities;

import java.util.Objects;

/**
 * Представляє категорію експонатів у системі.
 * Кожна категорія має унікальний ідентифікатор, назву та опис.
 */
public class Category {
    private int id;
    private String name;
    private String description;

    /**
     * Порожній конструктор для створення пустої категорії.
     */
    public Category() {
    }

    /**
     * Конструктор для створення категорії з повними параметрами.
     *
     * @param id          унікальний ідентифікатор
     * @param name        назва категорії
     * @param description опис категорії
     */
    public Category(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * Повертає ідентифікатор категорії.
     *
     * @return ID категорії
     */
    public int getId() {
        return id;
    }

    /**
     * Встановлює ідентифікатор категорії.
     *
     * @param id новий ID категорії
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Повертає назву категорії.
     *
     * @return назва
     */
    public String getName() {
        return name;
    }

    /**
     * Встановлює назву категорії.
     *
     * @param name нова назва
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Повертає опис категорії.
     *
     * @return опис
     */
    public String getDescription() {
        return description;
    }

    /**
     * Встановлює опис категорії.
     *
     * @param description новий опис
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Перевіряє рівність категорій за їх ID.
     *
     * @param o об'єкт для порівняння
     * @return {@code true}, якщо ID збігаються
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return id == category.id;
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

    /**
     * Повертає рядкове представлення категорії (назву).
     *
     * @return назва категорії
     */
    @Override
    public String toString() {
        return name;
    }
}
