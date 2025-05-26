package com.yuralil.application.service;

import com.yuralil.domain.dao.UsersDao;

import java.util.ArrayList;
import java.util.List;

public class ValidationService {

    private static final ValidationService INSTANCE = new ValidationService();
    private final UsersDao usersDao = UsersDao.getInstance();

    public static ValidationService getInstance() {
        return INSTANCE;
    }

    private ValidationService() {}

    public List<String> validateRegistration(String login, String password) {
        List<String> errors = new ArrayList<>();

        // Перевірка логіну
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

        // Перевірка пароля
        if (password == null || password.isBlank()) {
            errors.add("Пароль не може бути порожнім");
        } else if (password.length() < 6) {
            errors.add("Пароль має бути не коротший за 6 символів");
        }

        return errors;
    }
}
