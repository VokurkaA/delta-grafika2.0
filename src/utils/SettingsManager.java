package utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class SettingsManager {
    private static final String SETTINGS_FILE = "settings.json";
    private static final Map<String, Object> cache = new HashMap<>();
    private static JsonObject settings;

    static {
        loadSettings();
    }

    private static void loadSettings() {
        try {
            Path settingsPath = Paths.get(SETTINGS_FILE);
            if (Files.exists(settingsPath)) {
                try (FileReader reader = new FileReader(settingsPath.toFile())) {
                    settings = JsonParser.parseReader(reader).getAsJsonObject();
                }
            } else {
                InputStream inputStream = SettingsManager.class.getClassLoader().getResourceAsStream(SETTINGS_FILE);
                if (inputStream != null) {
                    try (InputStreamReader reader = new InputStreamReader(inputStream)) {
                        settings = JsonParser.parseReader(reader).getAsJsonObject();
                    }
                } else {
                    System.err.println("Settings file not found: " + SETTINGS_FILE);
                    settings = new JsonObject();
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading settings: " + e.getMessage());
            settings = new JsonObject();
        }
    }

    public static int getInt(String path, int defaultValue) {
        String cacheKey = "int:" + path;
        if (cache.containsKey(cacheKey)) {
            return (int) cache.get(cacheKey);
        }

        try {
            JsonElement element = getNestedElement(path);
            if (element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
                int value = element.getAsInt();
                cache.put(cacheKey, value);
                return value;
            }
        } catch (Exception e) {
            System.err.println("Error getting setting " + path + ": " + e.getMessage());
        }
        return defaultValue;
    }

    public static String getString(String path, String defaultValue) {
        String cacheKey = "string:" + path;
        if (cache.containsKey(cacheKey)) {
            return (String) cache.get(cacheKey);
        }

        try {
            JsonElement element = getNestedElement(path);
            if (element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
                String value = element.getAsString();
                cache.put(cacheKey, value);
                return value;
            }
        } catch (Exception e) {
            System.err.println("Error getting setting " + path + ": " + e.getMessage());
        }
        return defaultValue;
    }

    public static boolean getBoolean(String path, boolean defaultValue) {
        String cacheKey = "boolean:" + path;
        if (cache.containsKey(cacheKey)) {
            return (boolean) cache.get(cacheKey);
        }

        try {
            JsonElement element = getNestedElement(path);
            if (element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isBoolean()) {
                boolean value = element.getAsBoolean();
                cache.put(cacheKey, value);
                return value;
            }
        } catch (Exception e) {
            System.err.println("Error getting setting " + path + ": " + e.getMessage());
        }
        return defaultValue;
    }

    public static Color getColor(String path, Color defaultValue) {
        String cacheKey = "color:" + path;
        if (cache.containsKey(cacheKey)) {
            return (Color) cache.get(cacheKey);
        }

        try {
            JsonElement element = getNestedElement(path);
            if (element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
                String colorStr = element.getAsString();
                if (colorStr.startsWith("#")) {
                    Color color = Color.decode(colorStr);
                    cache.put(cacheKey, color);
                    return color;
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting color setting " + path + ": " + e.getMessage());
        }
        return defaultValue;
    }

    private static JsonElement getNestedElement(String path) {
        String[] parts = path.split("\\.");
        JsonObject current = settings;

        for (int i = 0; i < parts.length - 1; i++) {
            if (current.has(parts[i]) && current.get(parts[i]).isJsonObject()) {
                current = current.getAsJsonObject(parts[i]);
            } else {
                return null;
            }
        }

        return current.get(parts[parts.length - 1]);
    }

    public static Font getFont(String pathPrefix, Font defaultValue) {
        String family = getString(pathPrefix + ".fontFamily", defaultValue.getFamily());
        int style = getInt(pathPrefix + ".fontStyle", defaultValue.getStyle());
        int size = getInt(pathPrefix + ".fontSize", defaultValue.getSize());
        return new Font(family, style, size);
    }

    public static void resetCache() {
        cache.clear();
        loadSettings();
    }
}