package org.cube.commons.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.cube.commons.base.BaseEnum;

import java.io.IOException;

/**
 * 序列化枚举值
 */
public class EnumSerializer extends JsonSerializer<BaseEnum> {

    public static final EnumSerializer instance = new EnumSerializer();

    @Override
    public void serialize(BaseEnum value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("label", value.getLabel());
        gen.writeStringField("value", value.getValue());
        // 显式结束操作
        gen.writeEndObject();
    }
}
