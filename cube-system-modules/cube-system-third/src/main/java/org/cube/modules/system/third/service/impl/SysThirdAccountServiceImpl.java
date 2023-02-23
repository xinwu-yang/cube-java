package org.cube.modules.system.third.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cube.commons.constant.CommonConst;
import org.cube.commons.system.api.ISysBaseAPI;
import org.cube.commons.utils.crypto.PasswordUtil;
import org.cube.commons.utils.spring.RedisUtil;
import org.cube.commons.base.Result;
import org.cube.modules.system.entity.SysRole;
import org.cube.modules.system.entity.SysUser;
import org.cube.modules.system.entity.SysUserRole;
import org.cube.modules.system.mapper.SysRoleMapper;
import org.cube.modules.system.mapper.SysUserMapper;
import org.cube.modules.system.mapper.SysUserRoleMapper;
import org.cube.modules.system.third.entity.SysThirdAccount;
import org.cube.modules.system.third.mapper.SysThirdAccountMapper;
import org.cube.modules.system.third.model.ThirdLoginModel;
import org.cube.modules.system.third.service.ISysThirdAccountService;
import com.xkcoding.justauth.AuthRequestFactory;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

/**
 * 第三方登录账号
 *
 * @author xinwuy
 * @version v2.3.1
 * @since 2020-11-17
 */
@Slf4j
@Service
public class SysThirdAccountServiceImpl extends ServiceImpl<SysThirdAccountMapper, SysThirdAccount> implements ISysThirdAccountService {

    @Autowired
    private SysThirdAccountMapper sysThirdAccountMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private AuthRequestFactory factory;
    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Override
    public void updateThirdUserId(SysUser sysUser, String thirdUserUuid) {
        //修改第三方登录账户表使其进行添加用户id
        LambdaQueryWrapper<SysThirdAccount> query = new LambdaQueryWrapper<>();
        query.eq(SysThirdAccount::getThirdUserUuid, thirdUserUuid);
        SysThirdAccount account = sysThirdAccountMapper.selectOne(query);
        SysThirdAccount sysThirdAccount = new SysThirdAccount();
        sysThirdAccount.setSysUserId(sysUser.getId());
        //根据当前用户id和登录方式查询第三方登录表
        LambdaQueryWrapper<SysThirdAccount> thirdQuery = new LambdaQueryWrapper<>();
        thirdQuery.eq(SysThirdAccount::getSysUserId, sysUser.getId());
        thirdQuery.eq(SysThirdAccount::getThirdType, account.getThirdType());
        SysThirdAccount sysThirdAccounts = sysThirdAccountMapper.selectOne(thirdQuery);
        if (sysThirdAccounts != null) {
            sysThirdAccountMapper.deleteById(sysThirdAccounts.getId());
        }
        //更新用户账户表sys_user_id
        sysThirdAccountMapper.update(sysThirdAccount, query);
    }

    @Override
    public SysUser createUser(String phone, String thirdUserUuid) {
        //先查询第三方，获取登录方式
        LambdaQueryWrapper<SysThirdAccount> query = new LambdaQueryWrapper<>();
        query.eq(SysThirdAccount::getThirdUserUuid, thirdUserUuid);
        sysThirdAccountMapper.selectOne(query);
        //添加用户
        SysUser user = new SysUser();
        user.setDelFlag(CommonConst.NOT_DELETED);
        user.setStatus(CommonConst.USER_UNFREEZE);
        user.setUsername(thirdUserUuid);
        user.setPhone(phone);
        user.setFirstLogin(true);
        //设置初始密码
        String salt = RandomUtil.randomString(8);
        user.setSalt(salt);
        String passwordEncode = PasswordUtil.encrypt(user.getUsername(), "123456", salt);
        user.setPassword(passwordEncode);
        String userId = this.saveThirdUser(user);
        //更新用户第三方账户表的userId
        SysThirdAccount sysThirdAccount = new SysThirdAccount();
        sysThirdAccount.setSysUserId(userId);
        sysThirdAccountMapper.update(sysThirdAccount, query);
        return user;
    }

    @Override
    public String loginThird(String source, AuthCallback callback, ModelMap modelMap) {
        log.info("第三方登录进入callback：" + source + " params：" + JSONObject.toJSONString(callback));
        AuthRequest authRequest = factory.get(source);
        AuthResponse<?> response = authRequest.login(callback);
        log.info(JSONObject.toJSONString(response));
        Result<JSONObject> result = new Result<>();
        if (response.getCode() == 2000) {
            JSONObject data = JSONObject.parseObject(JSONObject.toJSONString(response.getData()));
            String username = data.getString("username");
            String avatar = data.getString("avatar");
            String uuid = data.getString("uuid");
            //构造第三方登录信息存储对象
            ThirdLoginModel thirdLoginModel = new ThirdLoginModel(source, uuid, username, avatar);
            //判断有没有这个人
            //update-begin-author:wangshuai date:20201118 for:修改成查询第三方账户表
            LambdaQueryWrapper<SysThirdAccount> query = new LambdaQueryWrapper<>();
            query.eq(SysThirdAccount::getThirdUserUuid, uuid);
            query.eq(SysThirdAccount::getThirdType, source);
            SysThirdAccount user = sysThirdAccountMapper.selectOne(query);
            if (user == null) {
                //否则直接创建新账号
                user = saveThirdUser(thirdLoginModel);
            }
            // 生成token
            //update-begin-author:wangshuai date:20201118 for:从第三方登录查询是否存在用户id，不存在绑定手机号
            if (StrUtil.isNotEmpty(user.getSysUserId())) {
                String sysUserId = user.getSysUserId();
                SysUser sysUser = sysUserMapper.selectById(sysUserId);
                String token = saveToken(sysUser);
                modelMap.addAttribute("token", token);
            } else {
                modelMap.addAttribute("token", "绑定手机号," + "" + uuid);
            }
            //update-end-author:wangshuai date:20201118 for:从第三方登录查询是否存在用户id，不存在绑定手机号
            //update-begin--Author:wangshuai  Date:20200729 for：接口在签名校验失败时返回失败的标识码 issues#1441--------------------
        } else {
            modelMap.addAttribute("token", "登录失败");
        }
        //update-end--Author:wangshuai  Date:20200729 for：接口在签名校验失败时返回失败的标识码 issues#1441--------------------
        result.setSuccess(false);
        result.setMessage("第三方登录异常,请联系管理员！");
        return "thirdLogin";
    }

    @Override
    public Result<?> thirdUserCreate(ThirdLoginModel model) {
        Object operateCode = redisUtil.get(CommonConst.THIRD_LOGIN_CODE);
        if (operateCode == null || !operateCode.toString().equals(model.getOperateCode())) {
            return Result.error("校验失败");
        }
        //创建新账号
        //update-begin-author:wangshuai date:20201118 for:修改成从第三方登录查出来的user_id，在查询用户表尽行token
        SysThirdAccount user = saveThirdUser(model);
        String token = null;
        if (StrUtil.isNotEmpty(user.getSysUserId())) {
            String sysUserId = user.getSysUserId();
            SysUser sysUser = sysUserMapper.selectById(sysUserId);
            // 生成token
            token = saveToken(sysUser);
            //update-end-author:wangshuai date:20201118 for:修改成从第三方登录查出来的user_id，在查询用户表尽行token
        }
        return Result.ok(token);
    }

    @Override
    public Result<?> checkPassword(JSONObject json) {
        Object operateCode = redisUtil.get(CommonConst.THIRD_LOGIN_CODE);
        if (operateCode == null || !operateCode.toString().equals(json.getString("operateCode"))) {
            return Result.error("校验失败");
        }
        String username = json.getString("uuid");
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        SysUser user = sysUserMapper.selectOne(wrapper);
        if (user == null) {
            return Result.error("用户未找到");
        }
        String password = json.getString("password");
        String salt = user.getSalt();
        String passwordEncode = PasswordUtil.encrypt(user.getUsername(), password, salt);
        if (!passwordEncode.equals(user.getPassword())) {
            return Result.error("密码不正确");
        }
        sysUserMapper.updateById(user);
        // 生成token
        String token = saveToken(user);
        return Result.ok(token);
    }

    @Override
    public Result<?> bindingThirdPhone(JSONObject jsonObject) {
        String phone = jsonObject.getString("mobile");
        String thirdUserUuid = jsonObject.getString("thirdUserUuid");
        //校验用户有效性
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("phone", phone);
        SysUser sysUser = sysUserMapper.selectOne(wrapper);
        if (sysUser != null) {
            this.updateThirdUserId(sysUser, thirdUserUuid);
        } else {
            // 不存在手机号，创建用户
            String smscode = jsonObject.getString("captcha");
            Object code = redisUtil.get(phone);
            if (!smscode.equals(code)) {
                return Result.error("手机验证码错误");
            }
            //创建用户
            sysUser = this.createUser(phone, thirdUserUuid);
        }
        String token = saveToken(sysUser);
        return Result.ok(token);
    }

    @Override
    public Result<?> getThirdLoginUser(String token, String thirdType) {
        String username = StpUtil.getLoginIdByToken(token).toString();
        //1. 校验用户是否有效
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        SysUser sysUser = sysUserMapper.selectOne(wrapper);
        Result<?> result = sysBaseAPI.checkUserState(sysUser);
        if (!result.isSuccess()) {
            return result;
        }
        //update-begin-author:wangshuai date:20201118 for:如果真实姓名和头像不存在就取第三方登录的
        LambdaQueryWrapper<SysThirdAccount> query = new LambdaQueryWrapper<>();
        query.eq(SysThirdAccount::getSysUserId, sysUser.getId());
        query.eq(SysThirdAccount::getThirdType, thirdType);
        SysThirdAccount account = sysThirdAccountMapper.selectOne(query);
        if (StrUtil.isEmpty(sysUser.getRealname())) {
            sysUser.setRealname(account.getRealname());
        }
        if (StrUtil.isEmpty(sysUser.getAvatar())) {
            sysUser.setAvatar(account.getAvatar());
        }
        //update-end-author:wangshuai date:20201118 for:如果真实姓名和头像不存在就取第三方登录的
        JSONObject obj = new JSONObject();
        //用户登录信息
        obj.put("userInfo", sysUser);
        //token 信息
        obj.put("token", token);
        return Result.ok(obj);
    }

    public String saveThirdUser(SysUser sysUser) {
        //保存用户
        String userId = UUID.randomUUID().toString();
        sysUser.setId(userId);
        sysUserMapper.insert(sysUser);
        //获取第三方角色
        SysRole sysRole = sysRoleMapper.selectOne(new LambdaQueryWrapper<SysRole>().eq(SysRole::getRoleCode, "third_role"));
        //保存用户角色
        SysUserRole userRole = new SysUserRole();
        userRole.setRoleId(sysRole.getId());
        userRole.setUserId(userId);
        sysUserRoleMapper.insert(userRole);
        return userId;
    }

    private SysThirdAccount saveThirdUser(ThirdLoginModel tlm) {
        SysThirdAccount user = new SysThirdAccount();
        user.setDelFlag(CommonConst.NOT_DELETED);
        user.setStatus(1);
        user.setThirdType(tlm.getSource());
        user.setAvatar(tlm.getAvatar());
        user.setRealname(tlm.getUsername());
        user.setThirdUserUuid(tlm.getUuid());
        sysThirdAccountMapper.insert(user);
        return user;
    }

    private String saveToken(SysUser sysUser) {
        // 生成token
        StpUtil.login(sysUser.getUsername());
        SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
        return saTokenInfo.getTokenValue();
    }
}
