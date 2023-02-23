package org.cube.application.config.mybatis;

import cn.hutool.core.util.StrUtil;
import org.cube.application.config.properties.OrgCodeViewProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;

/**
 * MyBatis拦截器：自动修改指定表名
 *
 * @author xinwuy
 * @since 2021-11-23
 */
@Slf4j
@Component
@Intercepts({@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}), @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})})
public class OrgCodeViewInterceptor implements Interceptor {

    @Autowired
    private OrgCodeViewProperties orgCodeViewProperties;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameter = args[1];
        BoundSql boundSql;
        if (args.length == 4) {
            boundSql = ms.getBoundSql(parameter);
        } else {
            boundSql = (BoundSql) args[5];
        }
        String sql = boundSql.getSql();
        List<String> tables = orgCodeViewProperties.getTables();
        if (tables != null) {
            for (String table : tables) {
                if (sql.contains(table)) {
                    String newSql = StrUtil.replace(sql, table, table + orgCodeViewProperties.getSuffix());
                    Field field = boundSql.getClass().getDeclaredField("sql");
                    field.setAccessible(true);
                    field.set(boundSql, newSql);
                    if (orgCodeViewProperties.isReplaceAll()) {
                        sql = newSql;
                    } else {
                        break;
                    }
                }
            }
        }
        return invocation.proceed();
    }
}
