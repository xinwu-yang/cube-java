package org.cube.plugin.sensitive;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.cube.plugin.sensitive.impl.DefaultSensitiveImpl;
import lombok.*;

import java.util.Objects;

/**
 * 脱敏插件-主要的业务逻辑
 *
 * @author xinwuy
 * @version 1.0.0
 * @since 2021-05-06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SensitiveSerializer extends JsonSerializer<String> implements ContextualSerializer {
    private SensitiveType sensitiveType;
    private Class<ISensitiveCustom> customClass;
    private int startIndex;
    private int length;

    @Override
    @SneakyThrows
    public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
        // 是否开启脱敏插件
        if (!SensitiveConfig.ENABLE) {
            jsonGenerator.writeString(value);
            return;
        }
        // 权限验证
        if (SensitiveConfig.PERMISSION_CHECK) {
            String role = SensitiveConfig.CURRENT_PERMISSION.get();
            boolean havePermission = SensitiveConfig.WHITE_LIST.contains(role);
            if (havePermission) {
                jsonGenerator.writeString(value);
                return;
            }
        }
        // 选择脱敏方式：默认和自定义
        ISensitiveCustom sensitiveMethod;
        if (Objects.equals(this.sensitiveType, SensitiveType.DEFAULT)) {
            sensitiveMethod = new DefaultSensitiveImpl(this.startIndex, this.length);
        } else {
            sensitiveMethod = this.customClass.getDeclaredConstructor().newInstance();
        }
        value = sensitiveMethod.sensitive(value);
        jsonGenerator.writeString(value);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) {
        Sensitive sensitive = property.getAnnotation(Sensitive.class);
        Class<?> customClass = sensitive.custom();
        return new SensitiveSerializer(sensitive.value(), (Class<ISensitiveCustom>) customClass, sensitive.start(), sensitive.length());
    }
}
