package com.ne.autotest.tencent.fca.boot.common.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jayway.jsonpath.*;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.ne.autotest.tencent.fca.boot.common.exception.NEException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JsonUtil {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Configuration conf;

    public JsonUtil() {
    }

    public static String writeValueAsString(Object value) {
        if (value == null) {
            return null;
        } else {
            String result = null;

            try {
                result = objectMapper.writeValueAsString(value);
                return result;
            } catch (JsonProcessingException var3) {
                throw new NEException(var3);
            }
        }
    }

    public static String writeValueAsPrettyString(Object value) {
        if (value == null) {
            return null;
        } else {
            String result = null;

            try {
                result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
                return result;
            } catch (JsonProcessingException var3) {
                throw new NEException(var3);
            }
        }
    }

    public static <T> T readValue(String content, Class<T> valueType) {
        if (content == null) {
            return null;
        } else {
            Object result = null;

            try {
                result = objectMapper.readValue(content, valueType);
                return (T) result;
            } catch (IOException var4) {
                throw new NEException(var4);
            }
        }
    }

    public static <T> T readValue(String content, TypeReference valueTypeRef) {
        if (content == null) {
            return null;
        } else {
            Object result = null;

            try {
                result = objectMapper.readValue(content, valueTypeRef);
                return (T)result;
            } catch (IOException var4) {
                throw new NEException(var4);
            }
        }
    }

    public static <T> T readValue(String json, String jsonPath) {
        try {
            return JsonPath.read(json, jsonPath, new Predicate[0]);
        } catch (PathNotFoundException var3) {
            logger.debug(var3.getMessage());
            return null;
        } catch (Exception var4) {
            throw new NEException(var4);
        }
    }

    public static <T> T readValue(String json, String jsonPath, TypeRef<T> typeRef) {
        try {
            return JsonPath.using(conf).parse(json).read(jsonPath, typeRef);
        } catch (PathNotFoundException var4) {
            logger.debug(var4.getMessage());
            return null;
        } catch (Exception var5) {
            throw new NEException(var5);
        }
    }

    public static <T> T readValue(String json, String jsonPath, Predicate... filters) {
        try {
            return JsonPath.read(json, jsonPath, filters);
        } catch (PathNotFoundException var4) {
            logger.debug(var4.getMessage());
            return null;
        } catch (Exception var5) {
            throw new NEException(var5);
        }
    }

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        objectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        conf = Configuration.builder().mappingProvider(new JacksonMappingProvider(objectMapper)).jsonProvider(new JacksonJsonProvider(objectMapper)).build();
    }
}
