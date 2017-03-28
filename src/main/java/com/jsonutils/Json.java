package com.jsonutils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by CM on 12/20/2014.
 */
public class Json {
    private static void checkError(ObjectMapper mapper, JsonParser parser) throws IOException {
        JsonNode errorNode = mapper.readValue(parser, JsonNode.class);
        checkError(errorNode);
    }

    public static void checkError(JsonNode errorNode) throws RequestException {
        JsonNode error = errorNode.get("error");
        if (error != null) {
            ExceptionInfo info = new ExceptionInfo();
            info.setError(error.asText());
            JsonNode message = errorNode.get("message");
            if (message != null) {
                info.setMessage(message.asText());
            }
            JsonNode cause = errorNode.get("cause");
            if (cause != null) {
                info.setCause(cause.asText());
            }
            throw new RequestException(info);
        }
    }

    public static JsonNode toJsonNode(String json) throws IOException {
        ObjectMapper mapper = createMapper();
        return getJsonNode(json, mapper);
    }

    public static JsonNode getJsonNode(String json, ObjectMapper mapper) throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser(json);
        return mapper.readValue(parser, JsonNode.class);
    }

    public static long getIdOrThrow(String json) throws IOException {
        JsonNode jsonNode = Json.toJsonNode(json);
        Json.checkError(jsonNode);
        return jsonNode.get("id").asInt();
    }

    public static void checkError(String json) throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser(json);

        checkError(new ObjectMapper(), parser);
    }

    public static ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    public static <T> T readNoThrow(String json, Class<T> aClass) {
        try {
            return read(json, aClass);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T read(String json, Class<T> aClass) throws IOException {
        ObjectMapper mapper = createMapper();
        JsonNode jsonNode = getJsonNode(json, mapper);
        checkError(jsonNode);

        return mapper.readValue(jsonNode.traverse(), aClass);
    }

    public static <T> List<T> readList(String json, String key, Class<? extends T> aClass) throws IOException {
        ObjectMapper mapper = createMapper();
        JsonNode jsonNode = getJsonNode(json, mapper);
        checkError(jsonNode);

        JavaType type = getListType(aClass, mapper);
        return mapper.readValue(jsonNode.get(key).traverse(), type);
    }

    private static <T> JavaType getListType(Class<T> aClass, ObjectMapper mapper) {
        TypeFactory typeFactory = mapper.getTypeFactory();
        return typeFactory.constructCollectionType(List.class, aClass);
    }

    public static <T> List<T> parseJsonArray(String json, Class<? extends T> aClass) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser parser = jsonFactory.createParser(json);

        JavaType type = getListType(aClass, mapper);

        return mapper.readValue(parser, type);
    }

    public static List<Long> toLongArray(ArrayNode arrayNode) {
        int size = arrayNode.size();
        List<Long> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            result.add(arrayNode.get(i).asLong());
        }

        return result;
    }

    public static List<String> toStringArray(ArrayNode arrayNode) {
        int size = arrayNode.size();
        List<String> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            result.add(arrayNode.get(i).asText());
        }

        return result;
    }

    public static List<Long> parseLongArray(String json, String key) throws IOException {
        JsonNode jsonNode = Json.toJsonNode(json);
        checkError(jsonNode);
        return toLongArray((ArrayNode) jsonNode.get(key));
    }

    public static List<String> parseStringArray(String json, String key) throws IOException {
        JsonNode jsonNode = Json.toJsonNode(json);
        checkError(jsonNode);
        return toStringArray((ArrayNode) jsonNode.get(key));
    }
}
