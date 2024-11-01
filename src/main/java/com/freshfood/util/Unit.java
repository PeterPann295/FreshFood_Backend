package com.freshfood.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Unit {
    @JsonProperty("kg")
    KG,
    @JsonProperty("g")
    G,
    @JsonProperty("l")
    L,
    @JsonProperty("ml")
    ML,
    @JsonProperty("box")
    BOX,
    @JsonProperty("can")
    CAN,
    @JsonProperty("bottle")
    BOTTLE,
    @JsonProperty("piece")
    PIECE,
    @JsonProperty("bag")
    BAG,
    @JsonProperty("bundle")
    BUNDLE,
    @JsonProperty("pack")
    PACK
}
