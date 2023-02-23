package org.cube.commons.utils;

import org.cube.modules.system.model.SysPermissionDataRuleModel;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据权限查询规则容器工具类
 *
 * @author 张代浩
 * @version V1.0.0
 * @since 2012-12-15
 */
public class DataAuthorizationUtil {

    public static final String MENU_DATA_AUTHOR_RULES = "MENU_DATA_AUTHOR_RULES";

    public static final String MENU_DATA_AUTHOR_RULE_SQL = "MENU_DATA_AUTHOR_RULE_SQL";

    /**
     * 往链接请求里面，传入数据查询条件
     */
    public static synchronized void installDataSearchCondition(HttpServletRequest request, List<SysPermissionDataRuleModel> dataRules) {
        // 1.先从request获取MENU_DATA_AUTHOR_RULES，如果存则获取到LIST
        List<SysPermissionDataRuleModel> list = loadDataSearchCondition();
        if (list == null) {
            // 2.如果不存在，则new一个list
            list = new ArrayList<>();
        }
        list.addAll(dataRules);
        request.setAttribute(MENU_DATA_AUTHOR_RULES, list); // 3.往list里面增量存指
    }

    /**
     * 获取请求对应的数据权限规则
     */
    @SuppressWarnings("unchecked")
    public static synchronized List<SysPermissionDataRuleModel> loadDataSearchCondition() {
        return (List<SysPermissionDataRuleModel>) SystemContextUtil.getHttpServletRequest().getAttribute(MENU_DATA_AUTHOR_RULES);
    }

    /**
     * 获取请求对应的数据权限SQL
     */
    public static synchronized String loadDataSearchConditionSQLString() {
        return (String) SystemContextUtil.getHttpServletRequest().getAttribute(MENU_DATA_AUTHOR_RULE_SQL);
    }

    /**
     * 往链接请求里面，传入数据查询条件
     */
    public static synchronized void installDataSearchCondition(HttpServletRequest request, String sql) {
        String ruleSql = loadDataSearchConditionSQLString();
        if (!StringUtils.hasText(ruleSql)) {
            request.setAttribute(MENU_DATA_AUTHOR_RULE_SQL, sql);
        }
    }
}
