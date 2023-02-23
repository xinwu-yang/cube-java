package org.cube.modules.system.third.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cube.commons.base.Result;
import org.cube.modules.system.entity.SysUser;
import org.cube.modules.system.third.entity.SysThirdAccount;
import org.cube.modules.system.third.model.ThirdLoginModel;
import me.zhyd.oauth.model.AuthCallback;
import org.springframework.ui.ModelMap;

/**
 * 第三方登录账号表
 *
 * @author jeecg-boot
 * @version V1.0
 * @since 2020-11-17
 */
public interface ISysThirdAccountService extends IService<SysThirdAccount> {
    /**
     * 更新第三方账户信息
     */
    void updateThirdUserId(SysUser sysUser, String thirdUserUuid);

    /**
     * 创建第三方用户
     */
    SysUser createUser(String phone, String thirdUserUuid);

    /**
     * 第三方登录
     **/
    String loginThird(String source, AuthCallback callback, ModelMap modelMap);

    /**
     * 创建第三方用户
     */
    Result<?> thirdUserCreate(ThirdLoginModel model);

    /**
     * 检查密码
     */
    Result<?> checkPassword(JSONObject json);

    /**
     * 第三方账号绑定手机号码
     */
    Result<?> bindingThirdPhone(JSONObject jsonObject);

    /**
     * 获取第三方登录用户信息
     *
     * @param token     登录凭证
     * @param thirdType 来源
     */
    Result<?> getThirdLoginUser(String token, String thirdType);
}
