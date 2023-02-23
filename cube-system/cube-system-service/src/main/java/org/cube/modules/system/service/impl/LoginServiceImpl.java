package org.cube.modules.system.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.math.Calculator;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.cube.application.config.properties.CaptchaConfigProperties;
import org.cube.commons.system.api.ISysBaseAPI;
import org.cube.commons.base.Result;
import org.cube.commons.constant.CacheConst;
import org.cube.commons.exception.CubeAppException;
import org.cube.commons.intf.ISmsSender;
import org.cube.commons.utils.CommonUtil;
import org.cube.commons.utils.SystemContextUtil;
import org.cube.commons.utils.captcha.CaptchaGenerator;
import org.cube.commons.utils.crypto.PasswordTransportUtil;
import org.cube.commons.utils.crypto.PasswordUtil;
import org.cube.commons.utils.spring.RedisUtil;
import org.cube.modules.system.entity.SysDepart;
import org.cube.modules.system.entity.SysUser;
import org.cube.modules.system.mapper.SysDepartMapper;
import org.cube.modules.system.mapper.SysLogMapper;
import org.cube.modules.system.mapper.SysUserMapper;
import org.cube.modules.system.model.SmsType;
import org.cube.modules.system.model.api.response.LogInfoResponse;
import org.cube.modules.system.model.api.response.LoginResponse;
import org.cube.modules.system.model.api.response.VisitsInfo;
import org.cube.modules.system.service.ILoginService;
import org.cube.modules.system.service.ISysDictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Slf4j
@Service
public class LoginServiceImpl implements ILoginService {

    @Autowired
    private SysDepartMapper sysDepartMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysLogMapper sysLogMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ISysBaseAPI sysBaseAPI;
    @Autowired
    private ISysDictService sysDictService;
    @Autowired
    private CaptchaConfigProperties captchaConfigProperties;
    @Autowired
    private CaptchaGenerator captchaGenerator;
    @Autowired(required = false)
    private ISmsSender smsSender;

    @Override
    public LoginResponse getUserInfo(SysUser sysUser) {
        String username = sysUser.getUsername();
        // 获取用户部门信息
        LoginResponse loginResponse = new LoginResponse();
        List<SysDepart> departs = sysDepartMapper.queryUserDeparts(sysUser.getId());
        loginResponse.setDeparts(departs);
        if (departs.size() == 1) {
            SysDepart sysDepart = departs.get(0);
            sysUser.setOrgCode(sysDepart.getOrgCode());
            sysUser.setOrgCodeTxt(sysDepart.getDepartName());
            sysUserMapper.updateUserDepart(username, sysDepart.getOrgCode());
        }
        // 生成token
        StpUtil.login(username);
        SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
        loginResponse.setToken(saTokenInfo.getTokenValue());
        loginResponse.setTokenTimeout(saTokenInfo.getTokenTimeout());
        loginResponse.setUserInfo(sysUser);
        loginResponse.setUserRoles(sysBaseAPI.queryUserRoles(sysUser.getUsername()));
        loginResponse.setSysAllDictItems(sysDictService.queryAllDictItems());
        // 登录日志
        SystemContextUtil.log("登录成功，登录凭证：" + saTokenInfo.getTokenValue());
        // 删除原有用户信息缓存
        redisUtil.del(String.format("%s::%s", CacheConst.SYS_USERS_CACHE, username));
        return loginResponse;
    }

    @Override
    public LoginResponse loginWithPassword(String username, String password, String captcha, String checkKey) {
        //AES CBC 解密
        password = PasswordTransportUtil.getPlaintext(password);
        if (captchaConfigProperties.isEnable()) {
            if (StrUtil.isEmpty(captcha)) {
                throw new CubeAppException("请填写验证码！");
            }
            String lowerCaseCaptcha = captcha.toLowerCase();
            String realKey = SecureUtil.md5(lowerCaseCaptcha + checkKey);
            Object checkCode = redisUtil.get(realKey);
            //当进入登录页时，有一定几率出现验证码错误 #1714
            if (checkCode == null || !String.valueOf(checkCode).equals(lowerCaseCaptcha)) {
                throw new CubeAppException("验证码错误！");
            }
        }
        //1. 校验用户是否有效
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
        Result<?> userState = sysBaseAPI.checkUserState(sysUser);
        if (!userState.isSuccess()) {
            throw new CubeAppException(userState.getMessage());
        }
        //2. 校验用户名或密码是否正确
        String userInputPassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
        String userPassword = sysUser.getPassword();
        if (!userPassword.equals(userInputPassword)) {
            throw new CubeAppException("用户名或密码错误！");
        }
        return this.getUserInfo(sysUser);
    }

    @Override
    public LoginResponse loginWithCode(String phone, String code) {
        //校验用户有效性
        SysUser sysUser = sysUserMapper.getUserByPhone(phone);
        Result<?> userState = sysBaseAPI.checkUserState(sysUser);
        if (!userState.isSuccess()) {
            throw new CubeAppException(userState.getMessage());
        }
        Object verificationCode = redisUtil.get(phone);
        if (!code.equals(verificationCode)) {
            throw new CubeAppException("手机验证码错误！");
        }
        return this.getUserInfo(sysUser);
    }

    @Override
    public void logout() {
        // 删除用户的缓存信息（包括部门信息），例如sys:cache:user::<username>
        redisUtil.del(String.format("%s::%s", CacheConst.SYS_USERS_CACHE, StpUtil.getLoginIdAsString()));
        StpUtil.logout();
        SystemContextUtil.log("登出成功，登录凭证：" + StpUtil.getTokenInfo().getTokenValue());
    }

    @Override
    public LogInfoResponse visitsToday() {
        // 获取一天的开始和结束时间
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date dayStart = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date dayEnd = calendar.getTime();
        // 获取系统访问记录
        Long totalVisitCount = sysLogMapper.findTotalVisitCount();
        Long todayVisitCount = sysLogMapper.findTodayVisitCount(dayStart, dayEnd);
        Long todayIp = sysLogMapper.findTodayIp(dayStart, dayEnd);
        return new LogInfoResponse(totalVisitCount, todayVisitCount, todayIp);
    }

    @Override
    public List<VisitsInfo> visitsSevenDay() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date dayEnd = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        Date dayStart = calendar.getTime();
        String dbType = CommonUtil.getDatabaseType();
        return sysLogMapper.findVisitCount(dayStart, dayEnd, dbType);
    }

    @Override
    public SysUser selectDepart(String orgCode) {
        String username = StpUtil.getLoginIdAsString();
        // 重新选择部门时删除原有缓存
        redisUtil.del(String.format("%s::%s", CacheConst.SYS_USERS_CACHE, username));
        if (StrUtil.isNotEmpty(orgCode)) {
            sysUserMapper.updateUserDepart(username, orgCode);
        }
        SysUser sysUser = sysUserMapper.getUserByName(username);
        String departName = sysBaseAPI.translateDictFromTable("sys_depart", "depart_name", "org_code", orgCode);
        sysUser.setOrgCodeTxt(departName);
        return sysUser;
    }

    @Override
    public void sendSms(String phone, String mode) {
        String captcha = RandomUtil.randomNumbers(6);
        SmsType smsType = SmsType.SECURITY_TEMPLATE;
        switch (mode) {
            case "0":
                smsType = SmsType.LOGIN_TEMPLATE;
                break;
            case "1":
                SysUser sysUser = sysUserMapper.getUserByPhone(phone);
                if (sysUser != null) {
                    throw new CubeAppException("手机号已经注册，请直接登录！");
                }
                break;
            case "2":
                smsType = SmsType.FORGET_PASSWORD_TEMPLATE;
                break;
        }
        redisUtil.set(phone, captcha, 600);
        if (smsSender != null) {
            JSONObject params = JSONUtil.createObj();
            params.set("code", captcha);
            smsSender.send(phone, smsType, params);
        }
        log.info("Send SMS template={} code={} phone={} !", smsType.name(), captcha, phone);
    }

    @Override
    public String randomImage(String key) {
        String base64 = captchaGenerator.generate();
        String lowerCaseCode = captchaGenerator.getCode().toLowerCase();
        if (captchaConfigProperties.isMath()) {
            int calculateResult = (int) Calculator.conversion(lowerCaseCode);
            lowerCaseCode = String.valueOf(calculateResult);
        }
        String realKey = SecureUtil.md5(lowerCaseCode + key);
        redisUtil.set(realKey, lowerCaseCode, 60 * 2);
        return base64;
    }

    @Override
    public void checkCaptcha(String captcha, String checkKey) {
        String lowerCaseCaptcha = captcha.toLowerCase();
        String realKey = SecureUtil.md5(lowerCaseCaptcha + checkKey);
        Object checkCode = redisUtil.get(realKey);
        if (checkCode == null || !checkCode.equals(lowerCaseCaptcha)) {
            throw new CubeAppException("验证码错误！");
        }
    }
}
