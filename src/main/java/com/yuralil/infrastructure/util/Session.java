package com.yuralil.infrastructure.util;

import com.yuralil.domain.entities.Users;

public class Session {
    private static Users currentUser;

    public static void setCurrentUser(Users user) {
        currentUser = user;
    }

    public static Users getCurrentUser() {
        return currentUser;
    }

    public static void clear() {
        currentUser = null;
    }
}
