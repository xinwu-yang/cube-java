package org.cube.modules.system.enhancement.service;

import org.cube.commons.base.Result;
import org.cube.modules.system.enhancement.controller.response.AddKeyResponse;
import org.cube.modules.system.enhancement.entity.SysRoleKey;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 访问密钥即AK/SK
 *
 * @author cube
 * @version V2.0.0
 * @since 2022-08-02
 */
public interface ISysRoleKeyService extends IService<SysRoleKey> {

    /**
     * 新增一个role key
     *
     * @param sysRoleKey role 参数
     * @return SK
     */
    AddKeyResponse addRoleKey(SysRoleKey sysRoleKey);

    /**
     * 签发一个API凭证
     *
     * @param ak   AK
     * @param sign 签名
     * @return API凭证
     */
    Result<?> sign(String ak, String sign);
}