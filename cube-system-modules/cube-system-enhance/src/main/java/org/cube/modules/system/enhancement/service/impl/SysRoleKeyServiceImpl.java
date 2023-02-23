package org.cube.modules.system.enhancement.service.impl;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cube.commons.base.Result;
import org.cube.modules.system.enhancement.controller.response.AddKeyResponse;
import org.cube.modules.system.enhancement.entity.SysRoleKey;
import org.cube.modules.system.enhancement.mapper.SysRoleKeyMapper;
import org.cube.modules.system.enhancement.service.ISysRoleKeyService;
import org.springframework.stereotype.Service;

/**
 * 访问密钥即AK/SK
 *
 * @author cube
 * @version V2.0.0
 * @since 2022-08-02
 */
@Service
public class SysRoleKeyServiceImpl extends ServiceImpl<SysRoleKeyMapper, SysRoleKey> implements ISysRoleKeyService {

    @Override
    public AddKeyResponse addRoleKey(SysRoleKey sysRoleKey) {
        // 随机生成密钥
        String ak = HexUtil.encodeHexStr(RandomUtil.randomBytes(16));
        String sk = HexUtil.encodeHexStr(RandomUtil.randomBytes(16));
        sysRoleKey.setAccessKey(ak);
        sysRoleKey.setSecretKey(sk);
        baseMapper.insert(sysRoleKey);
        AddKeyResponse response = new AddKeyResponse();
        response.setAk(ak);
        response.setSk(sk);
        return response;
    }

    @Override
    public Result<?> sign(String ak, String sign) {
        QueryWrapper<SysRoleKey> wrapper = new QueryWrapper<>();
        wrapper.eq("access_key", ak);
        wrapper.eq("enabled", 1);
        SysRoleKey sysRoleKey = baseMapper.selectOne(wrapper);
        if (sysRoleKey == null) {
            return Result.error("签名异常！");
        }
        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, HexUtil.decodeHex(ak), HexUtil.decodeHex(sysRoleKey.getSecretKey()));
        String signParamsStr = aes.decryptStr(sign);
        if (!JSONUtil.isTypeJSONObject(signParamsStr)) {
            return Result.error("签名内容有误！");
        }
        JSONObject signParams = JSONUtil.parseObj(signParamsStr);
        Long expired = signParams.getLong("expired");
        Long signTime = signParams.getLong("signTime");
        if (expired == null || signTime == null) {
            return Result.error("签名内容有误！");
        }
        if (System.currentTimeMillis() - signTime > 15 * 60 * 1000) {
            return Result.error("签名失效！");
        }
        SaLoginModel saLoginModel = SaLoginModel.create();
        saLoginModel.setDevice("api");
        saLoginModel.setTimeout(expired);
        StpUtil.login(sysRoleKey.getUsername(), saLoginModel);
        String token = StpUtil.getTokenInfo().getTokenValue();
        return Result.ok(token);
    }
}