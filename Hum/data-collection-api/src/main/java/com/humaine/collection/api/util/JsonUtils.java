package com.humaine.collection.api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonUtils {

	public static String json(Object value) throws JsonProcessingException {
		 ObjectMapper mapper = new ObjectMapper();
		 mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		 ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		 return ow.writeValueAsString(value);
	}
}
