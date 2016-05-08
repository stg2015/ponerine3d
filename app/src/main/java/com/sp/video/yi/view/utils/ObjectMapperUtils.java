package com.sp.video.yi.view.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * ObjectMapperUtils
 *
 * @author Yangz
 *         2014/10/8
 */
public class ObjectMapperUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static ObjectMapper getMapperInstance() {
        return OBJECT_MAPPER;
    }

    public static JavaType constructParametricType(Class<?> collectionClass, Class<?>... elementClasses) {
        return OBJECT_MAPPER.getTypeFactory()
                .constructParametricType(collectionClass, elementClasses);
    }
}
