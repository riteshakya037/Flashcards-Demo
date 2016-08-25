package com.sorcery.flashcards.Model;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ritesh Shakya on 8/25/2016.
 */

public enum CurrentMode {
    ENGLISH("english_mode", "English"),
    GREEK("greek_mode", "Greek");

    public String getMode() {
        return mode;
    }

    private static final Map<String, CurrentMode> ENUM_MAP;

    public String getDisplayText() {
        return displayText;
    }

    private String mode;
    private String displayText;

    CurrentMode(String mode, String displayText) {
        this.mode = mode;
        this.displayText = displayText;
    }

    static {
        Map<String, CurrentMode> map = new ConcurrentHashMap<>();
        for (CurrentMode instance : CurrentMode.values()) {
            map.put(instance.getMode(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    public static CurrentMode getMode(String name) {
        return ENUM_MAP.get(name);
    }
}
