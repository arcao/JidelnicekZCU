package com.arcao.menza.volley;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public enum JsonMapper {
    INSTANCE;

    private final ObjectMapper mapper;

    JsonMapper() {
        mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public ObjectMapper mapper() {
        return mapper;
    }
}