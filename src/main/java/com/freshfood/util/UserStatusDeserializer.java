package com.freshfood.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class UserStatusDeserializer extends JsonDeserializer<UserStatus> {
    @Override
    public UserStatus deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText().toLowerCase(); // Đảm bảo không phân biệt hoa thường
        switch (value) {
            case "active":
                return UserStatus.ACTIVE;
            case "inactive":
                return UserStatus.INACTIVE;
            case "none":
                return UserStatus.NONE;
            default:
                throw new IllegalArgumentException("Unknown status: " + value);
        }
    }
}
