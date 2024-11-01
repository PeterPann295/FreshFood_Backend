package com.freshfood.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ProductStatus {
    @JsonProperty("available")
    AVAILABLE,
    @JsonProperty("out_of_stock")
    OUT_OF_STOCK,
    @JsonProperty("discontinued")
    DISCONTINUED,
    @JsonProperty("pre_order")
    PRE_ORDER,
    @JsonProperty("archived")
    ARCHIVED
}
