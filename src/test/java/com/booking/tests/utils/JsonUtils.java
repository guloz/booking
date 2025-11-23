package com.booking.tests.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static ObjectNode readJsonAsNode(String fileName) {
        try {
            String content = new String(Files.readAllBytes(Paths.get("src/test/resources/json/" + fileName)));
            return (ObjectNode) mapper.readTree(content);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON file: " + fileName, e);
        }
    }

    public static ObjectNode makeInvalid(ObjectNode validNode) {
        // Example modifications for invalid payload:
        validNode.put("firstName", "");          // empty firstName
        validNode.put("totalPrice", -10);        // invalid price
        validNode.remove("lastName");            // remove mandatory field
        return validNode;
    }
}

