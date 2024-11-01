package com.freshfood.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = UserStatusDeserializer.class)
public enum UserStatus {
    @JsonProperty("active")
    ACTIVE,
    active,
    @JsonProperty("inactive")
    INACTIVE,
    @JsonProperty("none")
    NONE
}
