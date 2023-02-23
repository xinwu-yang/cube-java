package org.cube.plugin.crypto.handler;

import org.cube.plugin.crypto.model.Algorithm;
import org.cube.plugin.crypto.model.CryptoField;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@MappedTypes({CryptoField.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class CryptoTypeHandler extends BaseTypeHandler<CryptoField> {
    private final AlgorithmHandler algorithmHandler;

    public CryptoTypeHandler() {
        algorithmHandler = AlgorithmHandler.INSTANCE;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, CryptoField parameter, JdbcType jdbcType) throws SQLException {
        if (parameter.getAlgorithm() == null) {
            log.warn("CryptoField {} {} 没有设置算法，使用明文存储！", i, jdbcType);
            ps.setString(i, parameter.getPlaintext());
            return;
        }
        String ciphertext = parameter.getAlgorithm().name() + "@" + algorithmHandler.encrypt(parameter.getAlgorithm(), parameter.getPlaintext());
        ps.setString(i, ciphertext);
    }

    @Override
    public CryptoField getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String ciphertext = rs.getString(columnName);
        return makeCryptoField(ciphertext);
    }

    @Override
    public CryptoField getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String ciphertext = rs.getString(columnIndex);
        return makeCryptoField(ciphertext);
    }

    @Override
    public CryptoField getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String ciphertext = cs.getString(columnIndex);
        return makeCryptoField(ciphertext);
    }

    private CryptoField makeCryptoField(String ciphertext) {
        String[] ciphertextArray = ciphertext.split("@");
        Algorithm algorithm = Algorithm.valueOf(ciphertextArray[0]);
        CryptoField cryptoField = new CryptoField();
        cryptoField.setAlgorithm(algorithm);
        cryptoField.setCiphertext(ciphertextArray[1]);
        cryptoField.setPlaintext(algorithmHandler.decrypt(algorithm, ciphertextArray[1]));
        return cryptoField;
    }
}
