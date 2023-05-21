package de.dafuqs.spectrum.config;

import java.util.*;

public class CompatibilitySettingAccessors {
    public static final CompatibilitySettingAccessors INSTANCE;

    private final Set<String> compatModIds = new HashSet<>();
    private final Map<Class<?>, Map<String, Map<String, Object>>> settings = new HashMap<>();

    private CompatibilitySettingAccessors() {}

    public <T> void register(String modId, Class<T> type, String key, T value) {
        if (compatModIds.add(modId)) {
            return;
        }
        if (!settings.containsKey(type)) {
            settings.put(type, new HashMap<>());
        }
        Map<String, Map<String, Object>> map = settings.get(type);
        if (!map.containsKey(modId)) {
            map.put(modId, new HashMap<>());
        }
        Map<String, Object> fMap = map.get(modId);
        fMap.put(key, value);
    }

    public <T> T get(String modId, String key, Class<T> type, T defaultValue) {
        if (settings.containsKey(type)) {
            Map<String, Map<String, Object>> map = settings.get(type);
            if (map.containsKey(modId)) {
                Map<String, Object> fMap = map.get(modId);
                if (fMap.containsKey(key)) {
                    return type.cast(fMap.get(key));
                }
            }
        }
        return defaultValue;
    }

    static {
        INSTANCE = new CompatibilitySettingAccessors();
    }
}
