package com.sorcery.flashcards.Model;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Possible Modes of operation of application.
 * <p>
 * Created by Ritesh Shakya on 8/25/2016.
 */

public enum CurrentMode {
    ENGLISH("english_mode", "English"),
    GREEK("greek_mode", "Greek");

    public String getMode() {
        return mode;
    }

    /**
     * List of all available modes.
     */
    private static final Map<String, CurrentMode> ENUM_MAP;

    public String getDisplayText() {
        return displayText;
    }

    /**
     * Name of the mode of Enum
     */
    private String mode;
    /**
     * Text to display if mode is active.
     */
    private String displayText;

    CurrentMode(String mode, String displayText) {
        this.mode = mode;
        this.displayText = displayText;
    }

    static {
        // Gets the list of all the modes of operation.
        Map<String, CurrentMode> map = new ConcurrentHashMap<>();
        for (CurrentMode instance : CurrentMode.values()) {
            map.put(instance.getMode(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    /**
     * Get {@link CurrentMode} from {@param name}
     *
     * @param name Name of mode to get {@link CurrentMode}
     * @return Enum represented by {@param name}
     */
    public static CurrentMode getMode(String name) {
        return ENUM_MAP.get(name);
    }
}
