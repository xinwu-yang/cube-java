package org.cube.plugin.crypto.intercepts;

import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import org.cube.plugin.crypto.annotations.Crypto;
import org.cube.plugin.crypto.handler.AlgorithmHandler;
import org.cube.plugin.crypto.model.Algorithm;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;

import java.lang.reflect.Field;

@Slf4j
@NoArgsConstructor
public class CryptoInterceptor implements InnerInterceptor {

    private AlgorithmHandler algorithmHandler;

    public CryptoInterceptor(AlgorithmHandler algorithmHandler) {
        this.algorithmHandler = algorithmHandler;
    }

    @SneakyThrows
    @Override
    public void beforeUpdate(Executor executor, MappedStatement ms, Object parameter) {
        Field[] fields = parameter.getClass().getDeclaredFields();
        for (Field field : fields) {
            String ciphertext;
            Crypto crypto = field.getDeclaredAnnotation(Crypto.class);
            String fieldType = field.getType().getSimpleName();
            // 字段Java类型是String才处理
            if (crypto != null && "String".equals(fieldType)) {
                field.setAccessible(true);
                Object plainTextObj = field.get(parameter);
                if (plainTextObj == null) {
                    log.warn("{} 字段值为 null, 不能进行加密!", field.getName());
                    return;
                }
                String plaintext = plainTextObj.toString();
                Algorithm algorithm = crypto.value();
                ciphertext = algorithmHandler.encrypt(algorithm, plaintext);
                field.set(parameter, ciphertext);
                field.setAccessible(false);
            }
        }
    }
}
