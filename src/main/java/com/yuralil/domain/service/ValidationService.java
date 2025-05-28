package com.yuralil.domain.service;

import com.yuralil.domain.dao.UsersDao;
import com.yuralil.domain.entities.Users;
import com.yuralil.domain.security.HashUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ValidationService {

    private static final ValidationService INSTANCE = new ValidationService();
    private final UsersDao usersDao = UsersDao.getInstance();

    public static ValidationService getInstance() {
        return INSTANCE;
    }

    private ValidationService() {}

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

    public List<String> validateLogin(String login, String password) {
        List<String> errors = new ArrayList<>();

        if (login == null || login.isBlank()) {
            errors.add("Логін не може бути порожнім");
        }

        if (password == null || password.isBlank()) {
            errors.add("Пароль не може бути порожнім");
        }

        if (!errors.isEmpty()) return errors;

        // Перевірка на захардкодженого адміна
        if (login.equals("admin") && password.equals("admin")) {
            return errors; // все ок
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
