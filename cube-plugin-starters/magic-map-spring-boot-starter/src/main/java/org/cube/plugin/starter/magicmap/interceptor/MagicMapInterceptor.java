package org.cube.plugin.starter.magicmap.interceptor;

import cn.hutool.core.util.StrUtil;
import org.cube.plugin.starter.magicmap.handler.IParamsHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ssssssss.magicapi.core.interceptor.RequestInterceptor;
import org.ssssssss.magicapi.core.model.ApiInfo;
import org.ssssssss.script.MagicScriptContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
public class MagicMapInterceptor implements RequestInterceptor {

    @Autowired(required = false)
    private List<IParamsHandler> paramsHandlers;

    @Override
    public Object preHandle(ApiInfo info, MagicScriptContext context, HttpServletRequest request, HttpServletResponse response) {
        String query = request.getParameter("query");
        if (paramsHandlers != null && paramsHandlers.size() > 0 && StrUtil.isNotEmpty(query)) {
            for (IParamsHandler paramsHandler : paramsHandlers) {
                if (StrUtil.isNotEmpty(paramsHandler.query())) {
                    if (paramsHandler.query().equals(query)) {
                        paramsHandler.handle(info, context, request, response);
                    }
                } else {
                    paramsHandler.handle(info, context, request, response);
                }
            }
        }
        return null;
    }
}
