package com.humaine.portal.api.util;

import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.humaine.portal.api.model.AIUserGroup;
import com.humaine.portal.api.olap.model.OLAPUserGroup;

public class JsonUtils {

	public static String json(Object value) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		return ow.writeValueAsString(value);
	}

	public static <T> T parse(String jsonString, Class<T> cls) throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(jsonString, cls);
	}
	
	public static <T> T parseResponse(String jsonString, Class<T> cls) throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode newNode = objectMapper.readTree(jsonString);
		newNode = newNode.get("responseData");
		return objectMapper.readValue(newNode.toPrettyString(),cls);
	}

	public static <T> T parseAndGetResponseData(String jsonString, Class<?> cls)
			throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode newNode = objectMapper.readTree(jsonString);
		newNode = newNode.get("responseData");
		return objectMapper.readValue(newNode.toPrettyString(),
				objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, cls));
	}
}
