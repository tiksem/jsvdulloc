package com.jsonutils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by CM on 12/20/2014.
 */
public class Json {
    private static void checkError(ObjectMapper mapper, JsonParser parser) throws IOException {
        JsonNode errorNode = mapper.readValue(parser, JsonNode.class);

        JsonNode error = errorNode.get("error");
        if (error != null) {
            ExceptionInfo info = new ExceptionInfo();
            info.setError(error.asText());
            info.setMessage(errorNode.get("message").asText());
            info.setCause(errorNode.get("cause").asText());
            throw new RequestException(info);
        }
    }

    public static <T> T read(String json, Class<T> aClass) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser(json);

        checkError(mapper, parser);

        return mapper.readValue(jsonFactory.createParser(json), aClass);
    }

    public static <T> List<T> readList(String json, String key, Class<T> aClass) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser(json);

        checkError(mapper, parser);

        TypeFactory typeFactory = mapper.getTypeFactory();
        JavaType type = typeFactory.constructMapType(Map.class,
                typeFactory.constructType(String.class),
                typeFactory.constructCollectionType(List.class, aClass));
        Map<String, List<T>> map =
                mapper.readValue(jsonFactory.createParser(json), type);
        return map.get(key);
    }

    public static <T> List<T> parseJsonArray(String json, Class<T> aClass) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser(json);

        TypeFactory typeFactory = mapper.getTypeFactory();
        JavaType type = typeFactory.constructCollectionLikeType(List.class, aClass);

        return mapper.readValue(parser, type);
    }
}
