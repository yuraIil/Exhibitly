package com.yuralil.infrastructure.util;

import java.io.*;
import java.nio.file.*;

public class SessionStorage {
    private static final String SESSION_FILE = "storage/session.txt";

    public static void saveUsername(String username) {
        try {
            Files.createDirectories(Paths.get("storage"));
            Files.writeString(Paths.get(SESSION_FILE), username);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    public static void clear() {
        try {
            Files.deleteIfExists(Paths.get(SESSION_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
