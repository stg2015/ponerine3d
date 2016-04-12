package com.sp.video.yi.data.server.retrofit1;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nd.hy.android.commons.data.ObjectMapperUtils;
import com.nd.hy.android.commons.util.IOUtils;

import java.io.IOException;
import java.lang.reflect.Type;

import retrofit.converter.ConversionException;
import retrofit.converter.JacksonConverter;
import retrofit.mime.TypedInput;

/**
 * 支持直接返回字符串的处理
 */
public class JsonConverter extends JacksonConverter {

    public JsonConverter(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public Object fromBody(TypedInput body, Type type) throws ConversionException {
        if (type.equals(String.class)) {
            try {
                return IOUtils.readToString(body.in());
            } catch (IOException e) {
                throw new ConversionException(e);
            }
        } else {
            try {
                return super.fromBody(body, type);
            } catch (ConversionException e) {
                ObjectMapper om = ObjectMapperUtils.getMapperInstance();
                try {
                    JavaType jt = ObjectMapperUtils.constructParametricType(BaseEntry.class, Object.class);
                    body.in().reset();
                    BaseEntry<Object> entry = om.readValue(body.in(), jt);
                    if (entry != null && entry.isError()) {
                        entry.setData(null);
                        String content = om.writeValueAsString(entry);
                        return om.readValue(content, om.getTypeFactory().constructType(type));
                    } else {
                        throw e;
                    }
                } catch (IOException e1) {
                    throw new ConversionException(e1);
                }
            }
        }
    }
}
