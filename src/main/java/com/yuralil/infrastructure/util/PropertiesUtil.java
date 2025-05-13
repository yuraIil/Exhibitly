package com.yuralil.infrastructure.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Утилітний клас для читання конфігураційних параметрів із файлу {@code application.properties}.
 * Використовується для завантаження налаштувань підключення до бази даних та інших параметрів.
 */
public class PropertiesUtil {
    private static final Properties PROPERTIES = new Properties();

    // Статичний блок завантаження налаштувань при ініціалізації класу
    static {
        loadProperties();
    }

    /**
     * Завантажує властивості з файлу {@code application.properties}, що знаходиться в ресурсах.
     * Викликається автоматично при першому доступі до класу.
     *
     * @throws RuntimeException якщо файл не знайдено або виникла помилка при читанні
     */
    private static void loadProperties() {
        try (InputStream input = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                PROPERTIES.load(input);
            } else {
                throw new RuntimeException("application.properties not found");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties", e);
        }
    }

    /**
     * Повертає значення властивості за ключем.
     *
     * @param key ключ властивості
     * @return значення або {@code null}, якщо не знайдено
     */
    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    /**
     * Повертає значення властивості за ключем або значення за замовчуванням, якщо не знайдено.
     *
     * @param key          ключ властивості
     * @param defaultValue значення за замовчуванням
     * @return значення або {@code defaultValue}, якщо не знайдено
     */
    public static String get(String key, String defaultValue) {
        return PROPERTIES.getProperty(key, defaultValue);
    }
}
