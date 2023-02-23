package org.cube.commons.utils;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.cube.commons.api.CommonAPI;
import org.cube.commons.enums.LogType;
import org.cube.commons.enums.OperateLogType;
import org.cube.plugin.easyexcel.EasyExcel;
import org.cube.plugin.easyexcel.dict.IDictTranslator;
import org.cube.modules.system.model.LoginUser;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 魔方系统上下文工具类
 *
 * @author 杨欣武
 * @version 2.5.0
 * @since 2022-07-12
 */
public class SystemContextUtil {

    /**
     * 获取HttpServletRequest
     */
    public static HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            throw new RuntimeException("在非Web上下文中获取Request信息！");
        }
        return servletRequestAttributes.getRequest();
    }

    /**
     * 获取当前登录用户信息
     *
     * @return 用户信息
     */
    public static LoginUser currentLoginUser() {
        CommonAPI commonAPI = SpringUtil.getBean(CommonAPI.class);
        return commonAPI.getUserByName(StpUtil.getLoginIdAsString());
    }

    /**
     * 获取上下文中的字典翻译器
     *
     * @return 字典翻译器
     */
    public static IDictTranslator dictTranslator() {
        return SpringUtil.getBean(IDictTranslator.class);
    }

    /**
     * 获取上下文中的Excel工具类
     */
    public static EasyExcel easyExcel() {
        return SpringUtil.getBean(EasyExcel.class);
    }

    /**
     * 添加登录日志
     */
    public static void log(String content) {
        CommonAPI commonAPI = SpringUtil.getBean(CommonAPI.class);
        commonAPI.addLog(content, LogType.LOGIN.getValue(), null);
    }

    /**
     * 添加操作日志
     */
    public static void log(String content, OperateLogType operateType) {
        CommonAPI commonAPI = SpringUtil.getBean(CommonAPI.class);
        commonAPI.addLog(content, LogType.OPERATE.getValue(), operateType.getValue());
    }
}
