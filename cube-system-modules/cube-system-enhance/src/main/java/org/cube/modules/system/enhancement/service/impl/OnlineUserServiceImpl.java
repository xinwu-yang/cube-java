package org.cube.modules.system.enhancement.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.cube.commons.utils.SystemContextUtil;
import org.cube.commons.base.Result;
import org.cube.commons.utils.web.HttpServletUtil;
import org.cube.modules.system.enhancement.model.OnlineUser;
import org.cube.modules.system.enhancement.service.IOnlineUserService;
import org.cube.modules.system.entity.SysLog;
import org.cube.modules.system.entity.SysUser;
import org.cube.modules.system.mapper.SysLogMapper;
import org.cube.modules.system.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class OnlineUserServiceImpl implements IOnlineUserService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public Result<?> getOnlineUserList(String username) {
        Set<String> keys = stringRedisTemplate.keys(OnlineUser.ONLINE_USER_CACHE + "*");
        if (keys != null) {
            List<String> records;
            List<String> results = stringRedisTemplate.opsForValue().multiGet(keys);
            if (results != null && results.size() > 0 && StrUtil.isNotEmpty(username)) {
                records = results.stream().filter(record -> {
                    JSONObject root = JSONUtil.parseObj(record);
                    String name = root.getStr("username");
                    return name.contains(username);
                }).collect(Collectors.toList());
                return Result.ok(records);
            }
            return Result.ok(results);
        }
        return Result.ok();
    }

    @Override
    public Result<?> onlineHeart(String username, HttpServletRequest request) {
        if (!StpUtil.isLogin()) {
            return Result.error("未登录，不统计数据！");
        }
        UserAgent userAgent = UserAgentUtil.parse(request.getHeader("USER-AGENT"));
        String ip = HttpServletUtil.getIpAddr(request);
        String browse = userAgent.getBrowser() + " " + userAgent.getVersion();
        String os = userAgent.getOs().getName();
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        LoginUser user = SystemContextUtil.currentLoginUser();
        String token = StpUtil.getTokenInfo().getTokenValue();
        QueryWrapper<SysLog> wrapper = new QueryWrapper<>();
        wrapper.eq("userid", user.getUsername()).like("log_content", token).orderByDesc("create_time").last("limit 1");
        SysLog sysLog = sysLogMapper.selectOne(wrapper);
        OnlineUser onlineUser = new OnlineUser(username, token, ip, browse, os, sysLog.getCreateTime());
        stringRedisTemplate.opsForValue().set(OnlineUser.ONLINE_USER_CACHE + user.getUsername() + ":" + token, JSONUtil.toJsonStr(onlineUser), 1, TimeUnit.MINUTES);
        return Result.ok();
    }
}
