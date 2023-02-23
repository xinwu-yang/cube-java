package org.cube.plugin.starter.magicmap.mybatis;

import cn.hutool.core.util.StrUtil;

import java.util.List;
import java.util.Map;

/**
 * 对应XML中 <trim>，注意prefixOverrides和suffixOverrides大小写敏感
 *
 * @author zhangxu
 * @version : 2020-12-05
 */
public class TrimSqlNode extends SqlNode {
    /**
     * 前缀  prefix
     */
    protected String prefix;
    /**
     * 后缀  suffix
     */
    protected String suffix;
    /**
     * 前缀 prefixOverrides
     */
    protected String prefixOverrides;
    /**
     * 后缀 suffixOverrides
     */
    protected String suffixOverrides;

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setSuffixOverrides(String suffixOverrides) {
        this.suffixOverrides = suffixOverrides == null ? null : suffixOverrides.toUpperCase();
    }

    public void setPrefixOverrides(String prefixOverrides) {
        this.prefixOverrides = prefixOverrides == null ? null : prefixOverrides.toUpperCase();
    }

    @Override
    public String getSql(Map<String, Object> paramMap, List<Object> parameters) {
        StringBuilder sqlBuffer = new StringBuilder();
        String childrenSql = executeChildren(paramMap, parameters).trim();
        // 如果子节点不为null，则转成数组
        if (StrUtil.isNotEmpty(childrenSql)) {
            String upperSql = childrenSql.toUpperCase();
            // 开始拼接SQL,
            sqlBuffer.append(StrUtil.nullToEmpty(this.prefix)).append(" ");
            //去掉prefixOverrides
            if (StrUtil.isNotEmpty(this.prefixOverrides)) {
                String[] overrideArray = this.prefixOverrides.split("\\|");
                for (String override : overrideArray) {
                    if (upperSql.startsWith(override)) {
                        childrenSql = childrenSql.substring(upperSql.indexOf(override) + override.length()).trim();
                        upperSql = childrenSql.toUpperCase();
                        break;
                    }
                }
            }
            //去掉suffixOverrides
            if (StrUtil.isNotBlank(this.suffixOverrides)) {
                String[] overrideArray = this.suffixOverrides.split("\\|");
                for (String override : overrideArray) {
                    if (upperSql.endsWith(override)) {
                        childrenSql = childrenSql.substring(0, upperSql.lastIndexOf(override));
                        break;
                    }
                }
            }
            sqlBuffer.append(childrenSql);
            // 拼接结束SQL
            sqlBuffer.append(" ").append(StrUtil.nullToEmpty(this.suffix));
        }
        return sqlBuffer.toString();
    }
}
