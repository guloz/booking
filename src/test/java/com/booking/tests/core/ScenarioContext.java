package com.booking.tests.core;

import java.util.HashMap;
import java.util.Map;

public class ScenarioContext {
    private Map<String, Object> context = new HashMap<>();

    public void set(String key, Object value) {
        context.put(key, value);
    }

    public Object get(String key) {
        return context.get(key);
    }

    public boolean contains(String key) {
        return context.containsKey(key);
    }

    public void clear() {
        context.clear();
    }
}
