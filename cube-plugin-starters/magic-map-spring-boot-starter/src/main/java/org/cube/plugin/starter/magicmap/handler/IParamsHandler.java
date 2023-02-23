package org.cube.plugin.starter.magicmap.handler;

import org.ssssssss.magicapi.core.model.ApiInfo;
import org.ssssssss.script.MagicScriptContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 查询参数处理
 */
public interface IParamsHandler {

    /**
     * magic-api 处理方法
     *
     * @param info     接口信息和参数
     * @param context  Magic脚本上下文
     * @param request  请求
     * @param response 相应
     */
    default void handle(ApiInfo info, MagicScriptContext context, HttpServletRequest request, HttpServletResponse response) {

    }

    /**
     * map-api处理方法
     *
     * @param request  请求
     * @param response 相应
     */
    default void handle(HttpServletRequest request, HttpServletResponse response) {

    }

    /**
     * 指定查询处理，未指定就是全局
     */
    default String query() {
        return "";
    }
}
