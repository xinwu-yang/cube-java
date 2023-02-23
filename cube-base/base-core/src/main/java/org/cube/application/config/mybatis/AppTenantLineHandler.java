package org.cube.application.config.mybatis;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import lombok.AllArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;

import java.util.List;

@AllArgsConstructor
public class AppTenantLineHandler implements TenantLineHandler {
    /**
     * 有哪些表需要做多租户 这些表需要添加一个字段 ，字段名和tenant_field对应的值一样
     */
    private List<String> tenantTables;

    @Override
    public Expression getTenantId() {
        String tenantId = TenantContext.getTenant();
        return new LongValue(tenantId);
    }

    @Override
    public boolean ignoreTable(String tableName) {
        if (tenantTables == null) {
            return true;
        }
        return !this.tenantTables.contains(tableName);
    }
}
