package com.yuralil.infrastructure.util;

import java.io.IOException;
import java.nio.file.*;

/**
 * Утилітний клас для збереження та відновлення імені користувача між сесіями.
 * <p>
 * Зберігає ім’я користувача у текстовому файлі {@code storage/session.txt},
 * що дозволяє автоматично авторизовувати користувача при повторному запуску застосунку.
 */
public class SessionStorage {

    private static final String SESSION_FILE = "storage/session.txt";

    /**
     * Зберігає ім’я користувача у файл сесії.
     *
     * @param username ім’я користувача
     */
    public static void saveUsername(String username) {
        try {
            Files.createDirectories(Paths.get("storage"));
            Files.writeString(Paths.get(SESSION_FILE), username);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Повертає збережене ім’я користувача з файлу.
     *
     * @return ім’я користувача або {@code null}, якщо файл не існує чи сталася помилка
     */
    public static String loadUsername() {
        try {
            Path path = Paths.get(SESSION_FILE);
            if (Files.exists(path)) {
                return Files.readString(path).trim();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Видаляє файл сесії, очищаючи збережене ім’я користувача.
     */
    public static void clear() {
        try {
            Files.deleteIfExists(Paths.get(SESSION_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
