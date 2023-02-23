package org.cube.plugin.starter.magicmap.interceptor;

import cn.hutool.core.util.StrUtil;
import org.cube.plugin.starter.magicmap.handler.IParamsHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 为MapApi相关接口添加前置处理
 */
public class MapApiInterceptor implements HandlerInterceptor {

    @Autowired(required = false)
    private List<IParamsHandler> paramsHandlers;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String query = request.getParameter("query");
        if (paramsHandlers != null && paramsHandlers.size() > 0 && StrUtil.isNotEmpty(query)) {
            for (IParamsHandler paramsHandler : paramsHandlers) {
                if (StrUtil.isNotEmpty(paramsHandler.query())) {
                    if (paramsHandler.query().equals(query)) {
                        paramsHandler.handle(request, response);
                    }
                } else {
                    paramsHandler.handle(request, response);
                }
            }
        }
        return true;
    }
}
