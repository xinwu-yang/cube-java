package org.cube.application.config.mybatis;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import org.cube.commons.utils.SystemContextUtil;
import org.cube.modules.system.model.LoginUser;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * MyBatis拦截器：自动注入创建人、创建时间、修改人、修改时间、删除默认值
 *
 * @author xinwuy
 * @since 2021-11-23
 */
@Slf4j
@Component
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class AutoInjectBasicDataInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        Object parameter = invocation.getArgs()[1];
        if (parameter == null) {
            return invocation.proceed();
        }
        switch (sqlCommandType) {
            case INSERT:
                insert(parameter);
                break;
            case UPDATE:
                update(parameter);
                break;
        }
        return invocation.proceed();
    }

    /**
     * 防止因为没有登录而报错
     * 防止因为没有Web环境而报错
     */
    private LoginUser getLoginUser() {
        LoginUser loginUser = null;
        try {
            loginUser = SystemContextUtil.currentLoginUser();
        } catch (Exception ignored) {
        }
        return loginUser;
    }

    /**
     * 执行插入时
     *
     * @param parameter 插入的数据
     */
    @SneakyThrows
    private void insert(Object parameter) {
        LoginUser sysUser = getLoginUser();
        Field[] fields = ReflectUtil.getFieldsDirectly(parameter.getClass(), false);
        for (Field field : fields) {
            if (sysUser != null) {
                // 注入创建人
                if ("createBy".equals(field.getName())) {
                    field.setAccessible(true);
                    Object createBy = field.get(parameter);
                    field.setAccessible(false);
                    if (ObjectUtil.isEmpty(createBy)) {
                        // 登录人账号
                        field.setAccessible(true);
                        field.set(parameter, sysUser.getUsername());
                        field.setAccessible(false);
                    }
                    continue;
                }
                // 注入部门编码
                if ("sysOrgCode".equals(field.getName())) {
                    field.setAccessible(true);
                    Object sysOrgCode = field.get(parameter);
                    field.setAccessible(false);
                    if (ObjectUtil.isEmpty(sysOrgCode)) {
                        // 获取登录用户信息
                        field.setAccessible(true);
                        field.set(parameter, sysUser.getOrgCode());
                        field.setAccessible(false);
                    }
                    continue;
                }
            }
            // 注入创建时间
            if ("createTime".equals(field.getName())) {
                field.setAccessible(true);
                Object createTime = field.get(parameter);
                field.setAccessible(false);
                if (ObjectUtil.isEmpty(createTime)) {
                    field.setAccessible(true);
                    field.set(parameter, new Date());
                    field.setAccessible(false);
                }
                continue;
            }
            if ("delFlag".equals(field.getName())) {
                field.setAccessible(true);
                Object delFlag = field.get(parameter);
                field.setAccessible(false);
                if (ObjectUtil.isEmpty(delFlag)) {
                    // 是否删除，默认不删除
                    field.setAccessible(true);
                    field.set(parameter, 0);
                    field.setAccessible(false);
                }
            }
        }
    }

    /**
     * 执行更新时
     *
     * @param parameter 更新的数据
     */
    @SneakyThrows
    private void update(Object parameter) {
        if (parameter == null) {
            // 某些情况下拿不到要更新的对象值，所以不做处理
            return;
        }
        LoginUser sysUser = getLoginUser();
        Field[] fields;
        if (parameter instanceof ParamMap) {
            ParamMap<?> p = (ParamMap<?>) parameter;
            if (p.containsKey("et")) {
                parameter = p.get("et");
            } else {
                parameter = p.get("param1");
            }
        }
        fields = ReflectUtil.getFieldsDirectly(parameter.getClass(), false);
        for (Field field : fields) {
            if ("updateBy".equals(field.getName())) {
                //获取登录用户信息
                if (sysUser != null) {
                    // 登录账号
                    field.setAccessible(true);
                    field.set(parameter, sysUser.getUsername());
                    field.setAccessible(false);
                }
            }
            if ("updateTime".equals(field.getName())) {
                field.setAccessible(true);
                field.set(parameter, new Date());
                field.setAccessible(false);
            }
        }
    }
}
