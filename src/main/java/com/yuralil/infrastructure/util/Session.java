package com.yuralil.infrastructure.util;

import com.yuralil.domain.entities.Users;

/**
 * Клас-синглтон для збереження інформації про активного користувача під час сесії.
 * <p>
 * Зберігає поточного авторизованого користувача в статичному полі,
 * що дозволяє отримувати доступ до його даних з будь-якого місця програми.
 */
public class Session {

    private static Users currentUser;

    /**
     * Встановлює поточного користувача.
     *
     * @param user екземпляр {@link Users}
     */
    public static void setCurrentUser(Users user) {
        currentUser = user;
    }

    /**
     * Повертає поточного користувача.
     *
     * @return {@link Users} або {@code null}, якщо ніхто не авторизований
     */
    public static Users getCurrentUser() {
        return currentUser;
    }

    /**
     * Очищає поточного користувача (вихід із системи).
     */
    public static void clear() {
        currentUser = null;
    }

    /**
     * Перевіряє, чи користувач є адміністратором.
     *
     * @return {@code true}, якщо роль користувача — ADMIN
     */
    public static boolean isAdmin() {
        return currentUser != null && "ADMIN".equalsIgnoreCase(currentUser.getRole());
    }
}
