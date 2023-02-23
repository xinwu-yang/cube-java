package org.cube.modules.system.third.controller;

import com.alibaba.fastjson.JSONObject;
import org.cube.commons.base.Result;
import org.cube.modules.system.third.model.ThirdLoginModel;
import org.cube.modules.system.third.service.ISysThirdAccountService;
import com.xkcoding.justauth.AuthRequestFactory;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xinwuy
 * @since 2021-05-24
 */
@Slf4j
@Controller
@RequestMapping("/sys/thirdLogin")
public class ThirdLoginController {
    @Autowired
    private ISysThirdAccountService sysThirdAccountService;
    @Autowired
    private AuthRequestFactory factory;

    @RequestMapping("/render/{source}")
    public void render(@PathVariable("source") String source, HttpServletResponse response) throws IOException {
        log.info("第三方登录进入render：{}", source);
        AuthRequest authRequest = factory.get(source);
        String authorizeUrl = authRequest.authorize(AuthStateUtils.createState());
        log.info("第三方登录认证地址：{}", authorizeUrl);
        response.sendRedirect(authorizeUrl);
    }

    @RequestMapping("/{source}/callback")
    public String loginThird(@PathVariable String source, AuthCallback callback, ModelMap modelMap) {
        return sysThirdAccountService.loginThird(source, callback, modelMap);
    }

    /**
     * 创建新账号
     */
    @ResponseBody
    @PostMapping("/user/create")
    public Result<?> thirdUserCreate(@RequestBody ThirdLoginModel model) {
        return sysThirdAccountService.thirdUserCreate(model);
    }

    /**
     * 绑定账号 需要设置密码 需要走一遍校验
     */
    @ResponseBody
    @PostMapping("/user/checkPassword")
    public Result<?> checkPassword(@RequestBody JSONObject json) {
        return sysThirdAccountService.checkPassword(json);
    }

    @ResponseBody
    @GetMapping("/getLoginUser/{token}/{thirdType}")
    public Result<?> getThirdLoginUser(@PathVariable String token, @PathVariable String thirdType) {
        return sysThirdAccountService.getThirdLoginUser(token, thirdType);
    }

    /**
     * 第三方绑定手机号返回token
     */
    @ResponseBody
    @PostMapping("/bindingThirdPhone")
    public Result<?> bindingThirdPhone(@RequestBody JSONObject jsonObject) {
        return sysThirdAccountService.bindingThirdPhone(jsonObject);
    }
}