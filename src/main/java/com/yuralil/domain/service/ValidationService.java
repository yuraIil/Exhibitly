package com.yuralil.domain.service;

import com.yuralil.domain.dao.UsersDao;
import com.yuralil.domain.entities.Users;
import com.yuralil.domain.security.HashUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Сервіс для валідації введених даних під час реєстрації та входу.
 * <p>
 * Перевіряє:
 * <ul>
 *     <li>формат та унікальність логіна</li>
 *     <li>довжину та відповідність пароля</li>
 *     <li>наявність користувача в базі</li>
 *     <li>захардкоджений адмін-аккаунт</li>
 * </ul>
 */
public class ValidationService {

    private static final ValidationService INSTANCE = new ValidationService();
    private final UsersDao usersDao = UsersDao.getInstance();

    /**
     * @return єдиний інстанс {@code ValidationService}
     */
    public static ValidationService getInstance() {
        return INSTANCE;
    }

    private ValidationService() {}

    /**
     * Перевіряє дані при реєстрації нового користувача.
     *
     * @param login    логін
     * @param password пароль
     * @return список повідомлень про помилки (порожній, якщо валідація пройдена)
     */
    public List<String> validateRegistration(String login, String password) {
        List<String> errors = new ArrayList<>();

        if (login == null || login.isBlank()) {
            errors.add("Логін не може бути порожнім");
        } else {
            if (login.length() < 5 || login.length() > 30) {
                errors.add("Логін має бути від 5 до 30 символів");
            }
            if (!login.matches("^[a-zA-Z0-9]+$")) {
                errors.add("Логін може містити лише латинські букви та цифри");
            }
            if (usersDao.findByUsername(login).isPresent()) {
                errors.add("Такий логін вже існує");
            }
        }

        if (password == null || password.isBlank()) {
            errors.add("Пароль не може бути порожнім");
        } else if (password.length() < 6) {
            errors.add("Пароль має бути не коротший за 6 символів");
        }

        return errors;
    }

    /**
     * Перевіряє дані під час входу користувача.
     *
     * @param login    логін
     * @param password пароль
     * @return список помилок (порожній — якщо авторизація пройдена)
     */
    public List<String> validateLogin(String login, String password) {
        List<String> errors = new ArrayList<>();

        if (login == null || login.isBlank()) {
            errors.add("Логін не може бути порожнім");
        }

        if (password == null || password.isBlank()) {
            errors.add("Пароль не може бути порожнім");
        }

        if (!errors.isEmpty()) return errors;

        // Обробка захардкодженого адміна
        if (login.equals("admin") && password.equals("admin")) {
            return errors;
        }

        Optional<Users> userOpt = usersDao.findByUsername(login);
        if (userOpt.isEmpty()) {
            errors.add("Користувача не знайдено");
            return errors;
        }

        Users user = userOpt.get();
        if (!user.getPassword().equals(HashUtil.hash(password))) {
            errors.add("Невірний пароль");
        }

        return errors;
    }
}
