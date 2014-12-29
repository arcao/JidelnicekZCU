package com.arcao.menza.volley;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;

public enum JsonMapper {
	INSTANCE;

	private final ObjectMapper mapper;

	private JsonMapper() {
		mapper = new ObjectMapper();
		VisibilityChecker<?> visibilityChecker = mapper.getSerializationConfig().getDefaultVisibilityChecker();
		mapper.setVisibilityChecker(visibilityChecker
						.withFieldVisibility(Visibility.ANY)
						.withCreatorVisibility(Visibility.NONE)
						.withGetterVisibility(Visibility.NONE)
						.withSetterVisibility(Visibility.NONE)
						.withIsGetterVisibility(Visibility.NONE));
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	public ObjectMapper mapper() {
		return mapper;
	}
}