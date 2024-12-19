package com.aixohub.algotrader.base.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class JsonUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);

    public static final ObjectMapper OBJECT_MAPPER = JsonMapper.builder()
            .build();

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    }

    public static String toJson(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOGGER.warn("json 转化异常 ", e);
            return null;
        }
    }

    public static String toJsonDefault(Object object, String defaultString) {
        try {
            return Objects.isNull(object) ? defaultString : OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOGGER.warn("json 转化异常 ", e);
            return defaultString;
        }
    }

    public static <T> T parse(String json, TypeReference<T> typeReference) {
        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (IOException e) {
            LOGGER.warn("json 解析异常 ", e);
            return null;
        } catch (Exception e) {
            LOGGER.warn("json 解析异常 {} , e is", e.getMessage(), e);
            return null;
        }
    }

    public static <T> T parse(String json, Class<T> t) {
        try {
            return OBJECT_MAPPER.readValue(json, t);
        } catch (IOException e) {
            LOGGER.warn("json 解析异常 ", e);
            return null;
        } catch (Exception e) {
            LOGGER.warn("json 解析异常 {} , e is", e.getMessage(), e);
            return null;
        }
    }

    public static <T> List<T> readList(String str, Class<T> type) {
        return readList(str, ArrayList.class, type);
    }

    public static <T> List<T> readList(String str, Class<? extends Collection> type,
                                       Class<T> elementType) {

        try {
            return OBJECT_MAPPER.readValue(str,
                    OBJECT_MAPPER.getTypeFactory().constructCollectionType(type, elementType));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T convertValue(Object fromValue, Class<T> t) {
        return OBJECT_MAPPER.convertValue(fromValue, t);
    }

    public static <T> T convertValue(Object fromValue, TypeReference<T> typeReference) {
        return OBJECT_MAPPER.convertValue(fromValue, typeReference);
    }

    public static <T> T mapToObject(List<Map<String, Object>> mapList, TypeReference<T> typeRef) {
        try {
            OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return OBJECT_MAPPER.convertValue(mapList, typeRef);
        } catch (Exception e) {
            LOGGER.warn("mapToObject warn", e);
        }
        return null;
    }


    public static JsonNode toJsonNode(String listStr) {
        try {
            return OBJECT_MAPPER.readTree(listStr);
        } catch (Exception e) {
            LOGGER.warn("mapToObject warn", e);
        }
        return null;
    }

    public static ObjectNode newObjectNode(String listStr) {
        return OBJECT_MAPPER.createObjectNode();
    }

}
