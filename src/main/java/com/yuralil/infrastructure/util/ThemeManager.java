package com.yuralil.infrastructure.util;

import javafx.scene.Scene;

import java.util.prefs.Preferences;

public class ThemeManager {

    private static final String KEY = "app.theme";
    private static final Preferences prefs = Preferences.userRoot().node("app");

    public enum Theme {
        DEFAULT("style/theme-default.css"),
        LIGHT("style/theme-light.css"),
        DARK("style/theme-dark.css");

        private final String path;
        Theme(String path) {
            this.path = path;
        }
        public String getPath() {
            return path;
        }
    }

    public static void applyTheme(Scene scene) {
        Theme theme = getTheme();
        scene.getStylesheets().clear();
        scene.getStylesheets().add(ThemeManager.class.getResource("/" + theme.getPath()).toExternalForm());
    }

    public static void setTheme(Theme theme) {
        prefs.put(KEY, theme.name());
    }

    public static Theme getTheme() {
        try {
            return Theme.valueOf(prefs.get(KEY, Theme.DEFAULT.name()));
        } catch (Exception e) {
            return Theme.DEFAULT;
        }
    }
}
