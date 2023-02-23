package org.cube.commons.mybatisplus;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.NamingCase;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.cube.commons.annotations.EnableOrgCodeView;
import org.cube.commons.constant.CommonConst;
import org.cube.commons.constant.DataBaseConst;
import org.cube.commons.utils.*;
import org.cube.modules.system.model.LoginUser;
import org.cube.modules.system.model.SysPermissionDataRuleModel;
import org.springframework.util.NumberUtils;

import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class QueryGenerator {

    public static final String SQL_RULES_COLUMN = "SQL_RULES_COLUMN";
    private static final String BEGIN = "_begin";
    private static final String END = "_end";

    /**
     * 数字类型字段，拼接此后缀 接受多值参数
     */
    private static final String MULTI = "_MultiString";
    private static final String STAR = "*";
    private static final String COMMA = ",";
    private static final String NOT_EQUAL = "!";

    /**
     * 页面带有规则值查询，空格作为分隔符
     */
    private static final String QUERY_SEPARATE_KEYWORD = " ";

    /**
     * 高级查询前端传来的参数名
     */
    private static final String SUPER_QUERY_PARAMS = "superQueryParams";

    /**
     * 高级查询前端传来的拼接方式参数名
     */
    private static final String SUPER_QUERY_MATCH_TYPE = "superQueryMatchType";

    /**
     * 单引号
     */
    public static final String SQL_SQ = "'";

    /**
     * 排序列
     */
    private static final String ORDER_COLUMN = "column";

    /**
     * 排序方式
     */
    private static final String ORDER_TYPE = "order";
    private static final String ORDER_TYPE_ASC = "ASC";

    /**
     * mysql 模糊查询之特殊字符下划线 （_、\）
     */
    public static final String[] LIKE_MYSQL_SPECIAL_STR = new String[]{"_", "%"};

    /**
     * 时间格式化
     */
    private static final ThreadLocal<SimpleDateFormat> local = new ThreadLocal<>();

    private static SimpleDateFormat getTime() {
        SimpleDateFormat time = local.get();
        if (time == null) {
            time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            local.set(time);
        }
        return time;
    }

    /**
     * 获取查询条件构造器QueryWrapper实例 通用查询条件已被封装完成
     *
     * @param searchObj    查询实体
     * @param parameterMap request.getParameterMap()
     * @return QueryWrapper实例
     */
    public static <T> QueryWrapper<T> initQueryWrapper(T searchObj, Map<String, String[]> parameterMap) {
        long start = System.currentTimeMillis();
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        paramsInit(queryWrapper, searchObj, parameterMap);
        log.debug("查询条件构造器初始化完成，耗时：" + (System.currentTimeMillis() - start) + "ms");
        return queryWrapper;
    }

    /**
     * 组装Mybatis Plus 查询条件
     * <p>使用此方法 需要有如下几点注意:
     * <br>1.使用QueryWrapper 而非LambdaQueryWrapper;
     * <br>2.实例化QueryWrapper时不可将实体传入参数
     * <br>错误示例:如QueryWrapper<Demo> queryWrapper = new QueryWrapper<Demo>(demo);
     * <br>正确示例:QueryWrapper<Demo> queryWrapper = new QueryWrapper<Demo>();
     * <br>3.也可以不使用这个方法直接调用 {@link #initQueryWrapper}直接获取实例
     */
    @SneakyThrows
    public static void paramsInit(QueryWrapper<?> queryWrapper, Object searchObj, Map<String, String[]> parameterMap) {
		/*
		 * 注意:权限查询由前端配置数据规则 当一个人有多个所属部门时候 可以在规则配置包含条件 orgCode 包含 #{sys_org_code}
		但是不支持在自定义SQL中写orgCode in #{sys_org_code} 
		当一个人只有一个部门 就直接配置等于条件: orgCode 等于 #{sys_org_code} 或者配置自定义SQL: orgCode = '#{sys_org_code}'
		*/

        //区间条件组装 模糊查询 高级查询组装 简单排序 权限查询
        PropertyDescriptor[] origDescriptors = BeanUtil.getPropertyDescriptors(searchObj.getClass());
        Map<String, SysPermissionDataRuleModel> ruleMap = getRuleMap();

        //权限规则自定义SQL表达式
        for (String c : ruleMap.keySet()) {
            if (StrUtil.isNotEmpty(c) && c.startsWith(SQL_RULES_COLUMN)) {
                queryWrapper.and(i -> i.apply(getSqlRuleValue(ruleMap.get(c).getRuleValue())));
            }
        }
        String name, type, column;
        //定义实体字段和数据库字段名称的映射 高级查询中 只能获取实体字段 如果设置TableField注解 那么查询条件会出问题
        Map<String, String> fieldColumnMap = new HashMap<>(origDescriptors.length);
        for (PropertyDescriptor origDescriptor : origDescriptors) {
            //aliasName = origDescriptors[i].getName();  mybatis  不存在实体属性 不用处理别名的情况
            name = origDescriptor.getName();
            type = origDescriptor.getPropertyType().toString();
            if (judgedIsUselessField(name) || !BeanUtil.isReadableBean(searchObj.getClass())) {
                continue;
            }
            Object value = BeanUtil.getFieldValue(searchObj, name);
            column = getTableFieldName(searchObj.getClass(), name);
            if (column == null) {
                //column为null只有一种情况 那就是 添加了注解@TableField(exist = false) 后续都不用处理了
                continue;
            }
            fieldColumnMap.put(name, column);
            //数据权限查询
            if (ruleMap.containsKey(name)) {
                addRuleToQueryWrapper(ruleMap.get(name), column, origDescriptor.getPropertyType(), queryWrapper);
            }
            //区间查询
            doIntervalQuery(queryWrapper, parameterMap, type, name, column);
            //判断单值  参数带不同标识字符串 走不同的查询
            //TODO 这种前后带逗号的支持分割后模糊查询需要否 使多选字段的查询生效
            if (null != value && value.toString().startsWith(COMMA) && value.toString().endsWith(COMMA)) {
                String multiLikeVal = value.toString().replace(",,", COMMA);
                String[] values = multiLikeVal.substring(1).split(COMMA);
                final String field = NamingCase.toUnderlineCase(column).toLowerCase();
                if (values.length > 1) {
                    queryWrapper.and(j -> {
                        j = j.like(field, values[0]);
                        for (int k = 1; k < values.length; k++) {
                            j = j.or().like(field, values[k]);
                        }
                    });
                } else {
                    queryWrapper.and(j -> j.like(field, values[0]));
                }
            } else {
                //根据参数值带什么关键字符串判断走什么类型的查询
                QueryRuleEnum rule = convert2Rule(value);
                value = replaceValue(rule, value);
                // 添加判断为字符串时默认设为全模糊查询
                if ((rule == null || QueryRuleEnum.EQ.equals(rule)) && "class java.lang.String".equals(type)) {
                    // 可以设置左右模糊或全模糊，因人而异
                    rule = QueryRuleEnum.LIKE;
                    if (ObjectUtil.isNotEmpty(value)) {
                        // 特殊字符转义处理
                        value = specialStrConvert(String.valueOf(value));
                    }
                }
                addEasyQuery(queryWrapper, column, rule, value);
            }
        }
        // 排序逻辑 处理
        doMultiFieldsOrder(queryWrapper, parameterMap);
        // 高级查询
        doSuperQuery(queryWrapper, parameterMap, fieldColumnMap);
        // 开启视图权限控制
        EnableOrgCodeView view = searchObj.getClass().getAnnotation(EnableOrgCodeView.class);
        if (view != null && ruleMap.containsKey("orgCode")) {
            addRuleToQueryWrapper(ruleMap.get("orgCode"), "orgCode", String.class, queryWrapper);
        }
    }

    /**
     * 区间查询
     *
     * @param queryWrapper query对象
     * @param parameterMap 参数map
     * @param type         字段类型
     * @param filedName    字段名称
     * @param columnName   列名称
     */
    private static void doIntervalQuery(QueryWrapper<?> queryWrapper, Map<String, String[]> parameterMap, String type, String filedName, String columnName) throws ParseException {
        // 添加 判断是否有区间值
        String endValue, beginValue;
        if (parameterMap != null && parameterMap.containsKey(filedName + BEGIN)) {
            beginValue = parameterMap.get(filedName + BEGIN)[0].trim();
            addQueryByRule(queryWrapper, columnName, type, beginValue, QueryRuleEnum.GE);
        }
        if (parameterMap != null && parameterMap.containsKey(filedName + END)) {
            endValue = parameterMap.get(filedName + END)[0].trim();
            addQueryByRule(queryWrapper, columnName, type, endValue, QueryRuleEnum.LE);
        }
        //多值查询
        if (parameterMap != null && parameterMap.containsKey(filedName + MULTI)) {
            endValue = parameterMap.get(filedName + MULTI)[0].trim();
            addQueryByRule(queryWrapper, columnName.replace(MULTI, ""), type, endValue, QueryRuleEnum.IN);
        }
    }

    //多字段排序 TODO 需要修改前端
    public static void doMultiFieldsOrder(QueryWrapper<?> queryWrapper, Map<String, String[]> parameterMap) {
        String column = null, order = null;
        if (parameterMap != null && parameterMap.containsKey(ORDER_COLUMN)) {
            column = parameterMap.get(ORDER_COLUMN)[0];
        }
        if (parameterMap != null && parameterMap.containsKey(ORDER_TYPE)) {
            order = parameterMap.get(ORDER_TYPE)[0];
        }
        log.debug("排序规则>>列:" + column + ",排序方式:" + order);
        if (StrUtil.isNotEmpty(column) && StrUtil.isNotEmpty(order)) {
            //字典字段，去掉字典翻译文本后缀
            if (column.endsWith(CommonConst.DICT_TEXT_SUFFIX)) {
                column = column.substring(0, column.lastIndexOf(CommonConst.DICT_TEXT_SUFFIX));
            }
            //SQL注入check
            SqlInjectionUtil.filterContent(column);
            String orderBy = NamingCase.toUnderlineCase(column).toLowerCase();
            if (order.toUpperCase().contains(ORDER_TYPE_ASC)) {
                queryWrapper.orderByAsc(orderBy);
            } else {
                queryWrapper.orderByDesc(orderBy);
            }
        }
    }

    /**
     * 高级查询
     *
     * @param queryWrapper   查询对象
     * @param parameterMap   参数对象
     * @param fieldColumnMap 实体字段和数据库列对应的map
     */
    public static void doSuperQuery(QueryWrapper<?> queryWrapper, Map<String, String[]> parameterMap, Map<String, String> fieldColumnMap) {
        if (parameterMap != null && parameterMap.containsKey(SUPER_QUERY_PARAMS)) {
            String superQueryParams = parameterMap.get(SUPER_QUERY_PARAMS)[0];
            String superQueryMatchType = parameterMap.get(SUPER_QUERY_MATCH_TYPE) != null ? parameterMap.get(SUPER_QUERY_MATCH_TYPE)[0] : MatchTypeEnum.AND.getValue();
            MatchTypeEnum matchType = MatchTypeEnum.getByValue(superQueryMatchType);
            try {
                superQueryParams = URLDecoder.decode(superQueryParams, "UTF-8");
                ObjectMapper mapper = new ObjectMapper();
                // 忽略dictCode
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                List<QueryCondition> conditions = mapper.readValue(superQueryParams, new TypeReference<List<QueryCondition>>() {
                });
                if (conditions == null || conditions.size() == 0) {
                    return;
                }
                log.info("高级查询参数：{}", conditions);
                queryWrapper.and(andWrapper -> {
                    for (int i = 0; i < conditions.size(); i++) {
                        QueryCondition rule = conditions.get(i);
                        if (StrUtil.isNotEmpty(rule.getField()) && StrUtil.isNotEmpty(rule.getRule()) && StrUtil.isNotEmpty(rule.getVal())) {
                            log.debug("SuperQuery ==> " + rule);
                            addEasyQuery(andWrapper, fieldColumnMap.get(rule.getField()), QueryRuleEnum.getByValue(rule.getRule()), rule.getVal());
                            // 如果拼接方式是OR，就拼接OR
                            if (MatchTypeEnum.OR == matchType && i < (conditions.size() - 1)) {
                                andWrapper.or();
                            }
                        }
                    }
                });
            } catch (UnsupportedEncodingException e) {
                log.error("高级查询参数转码失败：" + superQueryParams, e);
            } catch (Exception e) {
                log.error("高级查询拼接失败：" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据所传的值 转化成对应的比较方式
     * 支持><= like in !
     */
    private static QueryRuleEnum convert2Rule(Object value) {
        // 避免空数据
        if (value == null) {
            return null;
        }
        String val = (value + "").trim();
        if (val.length() == 0) {
            return null;
        }
        QueryRuleEnum rule = null;

        //update-begin--Author:scott  Date:20190724 for：initQueryWrapper组装sql查询条件错误 #284-------------------
        //TODO 此处规则，只适用于 le lt ge gt
        // step 2 .>= =<
        if (val.length() >= 3) {
            if (QUERY_SEPARATE_KEYWORD.equals(val.substring(2, 3))) {
                rule = QueryRuleEnum.getByValue(val.substring(0, 2));
            }
        }
        // step 1 .> <
        if (rule == null && val.length() >= 2) {
            if (QUERY_SEPARATE_KEYWORD.equals(val.substring(1, 2))) {
                rule = QueryRuleEnum.getByValue(val.substring(0, 1));
            }
        }
        //update-end--Author:scott  Date:20190724 for：initQueryWrapper组装sql查询条件错误 #284---------------------

        // step 3 like
        if (rule == null && val.contains(STAR)) {
            if (val.startsWith(STAR) && val.endsWith(STAR)) {
                rule = QueryRuleEnum.LIKE;
            } else if (val.startsWith(STAR)) {
                rule = QueryRuleEnum.LEFT_LIKE;
            } else if (val.endsWith(STAR)) {
                rule = QueryRuleEnum.RIGHT_LIKE;
            }
        }
        // step 4 in
        if (rule == null && val.contains(COMMA)) {
            //TODO in 查询这里应该有个bug  如果一字段本身就是多选 此时用in查询 未必能查询出来
            rule = QueryRuleEnum.IN;
        }
        // step 5 !=
        if (rule == null && val.startsWith(NOT_EQUAL)) {
            rule = QueryRuleEnum.NE;
        }
        return rule != null ? rule : QueryRuleEnum.EQ;
    }

    /**
     * 替换掉关键字字符
     */
    private static Object replaceValue(QueryRuleEnum rule, Object value) {
        if (rule == null) {
            return null;
        }
        if (!(value instanceof String)) {
            return value;
        }
        String val = (value + "").trim();
        if (rule == QueryRuleEnum.LIKE) {
            if (val.length() > 1) {
                value = val.substring(1, val.length() - 1);
                //mysql 模糊查询之特殊字符下划线 （_、\）
                value = specialStrConvert(value.toString());
            } else {
                value = "";
            }
        } else if (rule == QueryRuleEnum.LEFT_LIKE || rule == QueryRuleEnum.NE) {
            value = val.substring(1);
            //mysql 模糊查询之特殊字符下划线 （_、\）
            value = specialStrConvert(value.toString());
        } else if (rule == QueryRuleEnum.RIGHT_LIKE) {
            value = val.substring(0, val.length() - 1);
            //mysql 模糊查询之特殊字符下划线 （_、\）
            value = specialStrConvert(value.toString());
        } else if (rule == QueryRuleEnum.IN) {
            value = val.split(",");
        } else {
            //update-begin--Author:scott  Date:20190724 for：initQueryWrapper组装sql查询条件错误 #284-------------------
            if (val.startsWith(rule.getValue())) {
                //TODO 此处逻辑应该注释掉-> 如果查询内容中带有查询匹配规则符号，就会被截取的（比如：>=您好）
                value = val.replaceFirst(rule.getValue(), "");
            } else if (val.startsWith(rule.getCondition() + QUERY_SEPARATE_KEYWORD)) {
                value = val.replaceFirst(rule.getCondition() + QUERY_SEPARATE_KEYWORD, "").trim();
            }
            //update-end--Author:scott  Date:20190724 for：initQueryWrapper组装sql查询条件错误 #284-------------------
        }
        return value;
    }

    private static void addQueryByRule(QueryWrapper<?> queryWrapper, String name, String type, String value, QueryRuleEnum rule) throws ParseException {
        if (StrUtil.isNotEmpty(value)) {
            Object temp;
            // 针对数字类型字段，多值查询
            if (value.contains(COMMA)) {
                temp = value;
                addEasyQuery(queryWrapper, name, rule, temp);
                return;
            }
            switch (type) {
                case "class java.lang.Integer":
                    temp = Integer.parseInt(value);
                    break;
                case "class java.math.BigDecimal":
                    temp = new BigDecimal(value);
                    break;
                case "class java.lang.Short":
                    temp = Short.parseShort(value);
                    break;
                case "class java.lang.Long":
                    temp = Long.parseLong(value);
                    break;
                case "class java.lang.Float":
                    temp = Float.parseFloat(value);
                    break;
                case "class java.lang.Double":
                    temp = Double.parseDouble(value);
                    break;
                case "class java.util.Date":
                    temp = getDateQueryByRule(value, rule);
                    break;
                default:
                    temp = value;
                    break;
            }
            addEasyQuery(queryWrapper, name, rule, temp);
        }
    }

    /**
     * 获取日期类型的值
     */
    private static Date getDateQueryByRule(String value, QueryRuleEnum rule) throws ParseException {
        Date date = null;
        if (value.length() == 10) {
            if (rule == QueryRuleEnum.GE) {
                //比较大于
                date = getTime().parse(value + " 00:00:00");
            } else if (rule == QueryRuleEnum.LE) {
                //比较小于
                date = getTime().parse(value + " 23:59:59");
            }
            //TODO 日期类型比较特殊 可能oracle下不一定好使
        }
        if (date == null) {
            date = getTime().parse(value);
        }
        return date;
    }

    /**
     * 根据规则走不同的查询
     *
     * @param queryWrapper QueryWrapper
     * @param name         字段名字
     * @param rule         查询规则
     * @param value        查询条件值
     */
    private static void addEasyQuery(QueryWrapper<?> queryWrapper, String name, QueryRuleEnum rule, Object value) {
        if (value == null || rule == null || "".equals(value)) {
            return;
        }
        name = NamingCase.toUnderlineCase(name).toLowerCase();
        log.debug("查询规则：" + name + " " + rule.getValue() + " " + value);
        switch (rule) {
            case GT:
                queryWrapper.gt(name, value);
                break;
            case GE:
                queryWrapper.ge(name, value);
                break;
            case LT:
                queryWrapper.lt(name, value);
                break;
            case LE:
                queryWrapper.le(name, value);
                break;
            case EQ:
                queryWrapper.eq(name, value);
                break;
            case NE:
                queryWrapper.ne(name, value);
                break;
            case IN:
                if (value instanceof String) {
                    queryWrapper.in(name, (Object[]) value.toString().split(","));
                } else if (value instanceof String[]) {
                    queryWrapper.in(name, (Object[]) value);
                }
                //update-begin-author:taoyan date:20200909 for:【bug】in 类型多值查询 不适配postgresql #1671
                else if (value.getClass().isArray()) {
                    queryWrapper.in(name, (Object[]) value);
                } else {
                    queryWrapper.in(name, value);
                }
                //update-end-author:taoyan date:20200909 for:【bug】in 类型多值查询 不适配postgresql #1671
                break;
            case LIKE:
                queryWrapper.like(name, value);
                break;
            case LEFT_LIKE:
                queryWrapper.likeLeft(name, value);
                break;
            case RIGHT_LIKE:
                queryWrapper.likeRight(name, value);
                break;
            default:
                log.warn("查询规则未匹配到！");
                break;
        }
    }

    private static boolean judgedIsUselessField(String name) {
        return "class".equals(name) || "ids".equals(name) || "page".equals(name) || "rows".equals(name) || "sort".equals(name) || "order".equals(name);
    }

    /**
     * 获取请求对应的数据权限规则
     */
    public static Map<String, SysPermissionDataRuleModel> getRuleMap() {
        List<SysPermissionDataRuleModel> list = DataAuthorizationUtil.loadDataSearchCondition();
        if (list == null || list.size() < 1 || list.get(0) == null) {
            return new HashMap<>(1);
        }
        Map<String, SysPermissionDataRuleModel> ruleMap = new HashMap<>(list.size());
        for (SysPermissionDataRuleModel rule : list) {
            String column = rule.getRuleColumn();
            if (QueryRuleEnum.SQL_RULES.getValue().equals(rule.getRuleConditions())) {
                column = SQL_RULES_COLUMN + rule.getId();
            }
            ruleMap.put(column, rule);
        }
        return ruleMap;
    }

    /**
     * 获取请求对应的数据权限规则
     */
    public static Map<String, SysPermissionDataRuleModel> getRuleMap(List<SysPermissionDataRuleModel> list) {
        if (list == null) {
            list = DataAuthorizationUtil.loadDataSearchCondition();
        }
        if (list == null || list.size() < 1 || list.get(0) == null) {
            return new HashMap<>(1);
        }
        Map<String, SysPermissionDataRuleModel> ruleMap = new HashMap<>(list.size());
        for (SysPermissionDataRuleModel rule : list) {
            String column = rule.getRuleColumn();
            if (QueryRuleEnum.SQL_RULES.getValue().equals(rule.getRuleConditions())) {
                column = SQL_RULES_COLUMN + rule.getId();
            }
            ruleMap.put(column, rule);
        }
        return ruleMap;
    }

    private static void addRuleToQueryWrapper(SysPermissionDataRuleModel dataRule, String name, Class propertyType, QueryWrapper<?> queryWrapper) {
        QueryRuleEnum rule = QueryRuleEnum.getByValue(dataRule.getRuleConditions());
        if (QueryRuleEnum.IN.equals(rule) && !propertyType.equals(String.class)) {
            String[] values = dataRule.getRuleValue().split(",");
            Object[] objs = new Object[values.length];
            for (int i = 0; i < values.length; i++) {
                objs[i] = NumberUtils.parseNumber(values[i], propertyType);
            }
            addEasyQuery(queryWrapper, name, rule, objs);
        } else {
            if (propertyType.equals(String.class)) {
                addEasyQuery(queryWrapper, name, rule, convertRuleValue(dataRule.getRuleValue()));
            } else if (propertyType.equals(Date.class)) {
                String dateStr = convertRuleValue(dataRule.getRuleValue());
                if (dateStr.length() == 10) {
                    addEasyQuery(queryWrapper, name, rule, DateUtils.str2Date(dateStr, DateUtils.date_sdf.get()));
                } else {
                    addEasyQuery(queryWrapper, name, rule, DateUtils.str2Date(dateStr, DateUtils.datetimeFormat.get()));
                }
            } else {
                addEasyQuery(queryWrapper, name, rule, NumberUtils.parseNumber(dataRule.getRuleValue(), propertyType));
            }
        }
    }

    public static String convertRuleValue(String ruleValue) {
        String value = getUserSystemData(ruleValue);
        return value != null ? value : ruleValue;
    }

    /**
     * 从当前用户中获取变量
     */
    private static String getUserSystemData(String key) {
        LoginUser sysUser = SystemContextUtil.currentLoginUser();
        String moshi = "";
        if (key.contains("}")) {
            moshi = key.substring(key.indexOf("}") + 1);
        }
        String returnValue = null;
        //针对特殊标示处理#{sysOrgCode}，判断替换
        if (key.contains("#{")) {
            key = key.substring(2, key.indexOf("}"));
        }
        //替换为系统登录用户帐号
        if (key.equals(DataBaseConst.SYS_USER_CODE) || key.equalsIgnoreCase(DataBaseConst.SYS_USER_CODE_TABLE)) {
            returnValue = sysUser.getUsername();
        }
        //替换为系统登录用户真实名字
        else if (key.equals(DataBaseConst.SYS_USER_NAME) || key.equalsIgnoreCase(DataBaseConst.SYS_USER_NAME_TABLE)) {
            returnValue = sysUser.getRealname();
        }
        //替换为系统用户登录所使用的机构编码
        else if (key.equals(DataBaseConst.SYS_ORG_CODE) || key.equalsIgnoreCase(DataBaseConst.SYS_ORG_CODE_TABLE)) {
            returnValue = sysUser.getOrgCode();
        }
        //替换为系统用户所拥有的所有机构编码
        else if (key.equals(DataBaseConst.SYS_MULTI_ORG_CODE) || key.equalsIgnoreCase(DataBaseConst.SYS_MULTI_ORG_CODE_TABLE)) {
            returnValue = sysUser.getOrgCodes();
        }
        //替换为当前系统时间(年月日)
        else if (key.equals(DataBaseConst.SYS_DATE) || key.equalsIgnoreCase(DataBaseConst.SYS_DATE_TABLE)) {
            returnValue = DateUtils.formatDate();
        }
        //替换为当前系统时间（年月日时分秒）
        else if (key.equals(DataBaseConst.SYS_TIME) || key.equalsIgnoreCase(DataBaseConst.SYS_TIME_TABLE)) {
            returnValue = DateUtils.now();
        }
        if (returnValue != null) {
            returnValue = returnValue + moshi;
        }
        return returnValue;
    }

    /**
     * 去掉值前后单引号
     *
     * @author scott
     * @since 2020/3/19 21:26
     */
    public static String trimSingleQuote(String ruleValue) {
        if (StrUtil.isEmpty(ruleValue)) {
            return "";
        }
        if (ruleValue.startsWith(QueryGenerator.SQL_SQ)) {
            ruleValue = ruleValue.substring(1);
        }
        if (ruleValue.endsWith(QueryGenerator.SQL_SQ)) {
            ruleValue = ruleValue.substring(0, ruleValue.length() - 1);
        }
        return ruleValue;
    }

    public static String getSqlRuleValue(String sqlRule) {
        try {
            Set<String> varParams = getSqlRuleParams(sqlRule);
            for (String var : varParams) {
                String tempValue = convertRuleValue(var);
                sqlRule = sqlRule.replace("#{" + var + "}", tempValue);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return sqlRule;
    }

    /**
     * 获取sql中的#{key} 这个key组成的set
     */
    public static Set<String> getSqlRuleParams(String sql) {
        if (StrUtil.isEmpty(sql)) {
            return null;
        }
        Set<String> varParams = new HashSet<>();
        String regex = "#\\{\\w+}";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(sql);
        while (m.find()) {
            String var = m.group();
            varParams.add(var.substring(var.indexOf("{") + 1, var.indexOf("}")));
        }
        return varParams;
    }

    /**
     * 获取查询条件
     */
    public static String getSingleQueryConditionSql(String field, String alias, Object value, boolean isString) {
        if (value == null) {
            return "";
        }
        field = alias + NamingCase.toUnderlineCase(field).toLowerCase();
        QueryRuleEnum rule = QueryGenerator.convert2Rule(value);
        return getSingleSqlByRule(rule, field, value, isString);
    }

    public static String getSingleSqlByRule(QueryRuleEnum rule, String field, Object value, boolean isString) {
        String res;
        switch (rule) {
            case GT:
            case GE:
            case LT:
            case LE:
            case EQ:
                res = field + rule.getValue() + getFieldConditionValue(value, isString);
                break;
            case NE:
                res = field + " <> " + getFieldConditionValue(value, isString);
                break;
            case IN:
                res = field + " in " + getInConditionValue(value, isString);
                break;
            case LIKE:
            case LEFT_LIKE:
            case RIGHT_LIKE:
                res = field + " like " + getLikeConditionValue(value);
                break;
            default:
                res = field + " = " + getFieldConditionValue(value, isString);
                break;
        }
        return res;
    }

    private static String getFieldConditionValue(Object value, boolean isString) {
        String str = value.toString().trim();
        if (str.startsWith("!")) {
            str = str.substring(1);
        } else if (str.startsWith(">=")) {
            str = str.substring(2);
        } else if (str.startsWith("<=")) {
            str = str.substring(2);
        } else if (str.startsWith(">")) {
            str = str.substring(1);
        } else if (str.startsWith("<")) {
            str = str.substring(1);
        }
        if (isString) {
            if (DataBaseConst.DB_TYPE_SQLSERVER.equals(getDbType())) {
                return " N'" + str + "' ";
            } else {
                return " '" + str + "' ";
            }
        } else {
            // 如果不是字符串 有一种特殊情况 popup调用都走这个逻辑 参数传递的可能是“‘admin’”这种格式的
            if (DataBaseConst.DB_TYPE_SQLSERVER.equals(getDbType()) && str.endsWith("'") && str.startsWith("'")) {
                return " N" + str;
            }
            return value.toString();
        }
    }

    private static String getInConditionValue(Object value, boolean isString) {
        if (isString) {
            String[] temp = value.toString().split(",");
            StringBuilder res = new StringBuilder();
            for (String string : temp) {
                if (DataBaseConst.DB_TYPE_SQLSERVER.equals(getDbType())) {
                    res.append(",N'").append(string).append("'");
                } else {
                    res.append(",'").append(string).append("'");
                }
            }
            return "(" + res.substring(1) + ")";
        } else {
            return "(" + value.toString() + ")";
        }
    }

    private static String getLikeConditionValue(Object value) {
        String str = value.toString().trim();
        if (str.startsWith("*") && str.endsWith("*")) {
            if (DataBaseConst.DB_TYPE_SQLSERVER.equals(getDbType())) {
                return "N'%" + str.substring(1, str.length() - 1) + "%'";
            } else {
                return "'%" + str.substring(1, str.length() - 1) + "%'";
            }
        } else if (str.startsWith("*")) {
            if (DataBaseConst.DB_TYPE_SQLSERVER.equals(getDbType())) {
                return "N'%" + str.substring(1) + "'";
            } else {
                return "'%" + str.substring(1) + "'";
            }
        } else if (str.endsWith("*")) {
            if (DataBaseConst.DB_TYPE_SQLSERVER.equals(getDbType())) {
                return "N'" + str.substring(0, str.length() - 1) + "%'";
            } else {
                return "'" + str.substring(0, str.length() - 1) + "%'";
            }
        } else {
            if (str.contains("%")) {
                if (DataBaseConst.DB_TYPE_SQLSERVER.equals(getDbType())) {
                    if (str.startsWith("'") && str.endsWith("'")) {
                        return "N" + str;
                    } else {
                        return "N" + "'" + str + "'";
                    }
                } else {
                    if (str.startsWith("'") && str.endsWith("'")) {
                        return str;
                    } else {
                        return "'" + str + "'";
                    }
                }
            } else {
                if (DataBaseConst.DB_TYPE_SQLSERVER.equals(getDbType())) {
                    return "N'%" + str + "%'";
                } else {
                    return "'%" + str + "%'";
                }
            }
        }
    }

    /**
     * 根据权限相关配置生成相关的SQL 语句
     */
    public static String installAuthJdbc(Class<?> clazz) {
        StringBuilder sb = new StringBuilder();
        //权限查询
        Map<String, SysPermissionDataRuleModel> ruleMap = getRuleMap();
        PropertyDescriptor[] origDescriptors = BeanUtil.getPropertyDescriptors(clazz);
        String sql_and = " and ";
        for (String c : ruleMap.keySet()) {
            if (StrUtil.isNotEmpty(c) && c.startsWith(SQL_RULES_COLUMN)) {
                sb.append(sql_and).append(getSqlRuleValue(ruleMap.get(c).getRuleValue()));
            }
        }
        String name, column;
        for (PropertyDescriptor origDescriptor : origDescriptors) {
            name = origDescriptor.getName();
            if (judgedIsUselessField(name)) {
                continue;
            }
            if (ruleMap.containsKey(name)) {
                column = getTableFieldName(clazz, name);
                if (column == null) {
                    continue;
                }
                SysPermissionDataRuleModel dataRule = ruleMap.get(name);
                QueryRuleEnum rule = QueryRuleEnum.getByValue(dataRule.getRuleConditions());
                Class propType = origDescriptor.getPropertyType();
                boolean isString = propType.equals(String.class);
                Object value;
                if (isString) {
                    value = convertRuleValue(dataRule.getRuleValue());
                } else {
                    value = NumberUtils.parseNumber(dataRule.getRuleValue(), propType);
                }
                String filedSql = getSingleSqlByRule(rule, NamingCase.toUnderlineCase(column).toLowerCase(), value, isString);
                sb.append(sql_and).append(filedSql);
            }
        }
        log.info("query auth sql is:" + sb);
        return sb.toString();
    }

    /**
     * 根据权限相关配置 组装mp需要的权限
     */
    public static void installAuthMplus(QueryWrapper<?> queryWrapper, Class<?> clazz) {
        //权限查询
        Map<String, SysPermissionDataRuleModel> ruleMap = getRuleMap();
        PropertyDescriptor[] origDescriptors = BeanUtil.getPropertyDescriptors(clazz);
        for (String c : ruleMap.keySet()) {
            if (StrUtil.isNotEmpty(c) && c.startsWith(SQL_RULES_COLUMN)) {
                queryWrapper.and(i -> i.apply(getSqlRuleValue(ruleMap.get(c).getRuleValue())));
            }
        }
        String name, column;
        for (PropertyDescriptor origDescriptor : origDescriptors) {
            name = origDescriptor.getName();
            if (judgedIsUselessField(name)) {
                continue;
            }
            column = getTableFieldName(clazz, name);
            if (column == null) {
                continue;
            }
            if (ruleMap.containsKey(name)) {
                addRuleToQueryWrapper(ruleMap.get(name), column, origDescriptor.getPropertyType(), queryWrapper);
            }
        }
    }

    /**
     * 转换sql中的系统变量
     */
    public static String convertSystemVariables(String sql) {
        return getSqlRuleValue(sql);
    }

    /**
     * 获取所有配置的权限 返回sql字符串 不受字段限制 配置什么就拿到什么
     */
    public static String getAllConfigAuth() {
        StringBuilder sb = new StringBuilder();
        //权限查询
        Map<String, SysPermissionDataRuleModel> ruleMap = getRuleMap();
        String sql_and = " and ";
        for (String c : ruleMap.keySet()) {
            SysPermissionDataRuleModel dataRule = ruleMap.get(c);
            String ruleValue = dataRule.getRuleValue();
            if (StrUtil.isEmpty(ruleValue)) {
                continue;
            }
            if (StrUtil.isNotEmpty(c) && c.startsWith(SQL_RULES_COLUMN)) {
                sb.append(sql_and).append(getSqlRuleValue(ruleValue));
            } else {
                boolean isString = false;
                ruleValue = ruleValue.trim();
                if (ruleValue.startsWith("'") && ruleValue.endsWith("'")) {
                    isString = true;
                    ruleValue = ruleValue.substring(1, ruleValue.length() - 1);
                }
                QueryRuleEnum rule = QueryRuleEnum.getByValue(dataRule.getRuleConditions());
                String value = convertRuleValue(ruleValue);
                String filedSql = getSingleSqlByRule(rule, c, value, isString);
                sb.append(sql_and).append(filedSql);
            }
        }
        log.info("query auth sql is = " + sb);
        return sb.toString();
    }

    /**
     * 获取系统数据库类型
     */
    private static String getDbType() {
        return CommonUtil.getDatabaseType();
    }

    /**
     * 获取class的 包括父类的
     */
    private static List<Field> getClassFields(Class<?> clazz) {
        List<Field> list = new ArrayList<>();
        Field[] fields;
        do {
            fields = clazz.getDeclaredFields();
            Collections.addAll(list, fields);
            clazz = clazz.getSuperclass();
        } while (clazz != Object.class && clazz != null);
        return list;
    }

    /**
     * 获取表字段名
     */
    private static String getTableFieldName(Class<?> clazz, String name) {
        //如果字段加注解了@TableField(exist = false),不走DB查询
        Field field = null;
        try {
            field = clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            //如果异常，则去父类查找字段
            List<Field> allFields = getClassFields(clazz);
            List<Field> searchFields = allFields.stream().filter(a -> a.getName().equals(name)).collect(Collectors.toList());
            if (searchFields.size() > 0) {
                field = searchFields.get(0);
            }
        }
        if (field != null) {
            TableField tableField = field.getAnnotation(TableField.class);
            if (tableField != null) {
                if (!tableField.exist()) {
                    //如果设置了TableField false 这个字段不需要处理
                    return null;
                } else {
                    String column = tableField.value();
                    //如果设置了TableField value 这个字段是实体字段
                    if (!"".equals(column)) {
                        return column;
                    }
                }
            }
        }
        return name;
    }

    /**
     * mysql模糊查询之特殊字符下划线（_%）
     */
    public static String specialStrConvert(String value) {
        if (DataBaseConst.DB_TYPE_MYSQL.equals(getDbType())) {
            for (String str : QueryGenerator.LIKE_MYSQL_SPECIAL_STR) {
                if (value != null && value.contains(str)) {
                    value = value.replace(str, "\\" + str);
                }
            }
        }
        return value;
    }
}
