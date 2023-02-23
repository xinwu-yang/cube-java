package org.cube.plugin.sensitive.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cube.plugin.sensitive.ISensitiveCustom;
import org.cube.plugin.sensitive.SensitiveResolver;
import org.cube.plugin.sensitive.impl.DefaultSensitiveImpl;
import org.cube.plugin.sensitive.test.entity.SysUser;
import org.junit.Test;

public class DefaultSensitiveTest {

    @Test
    public void sensitive() {
        ISensitiveCustom sensitiveCustom = new DefaultSensitiveImpl(7, 12);
        String phone = "18180423321";
        phone = sensitiveCustom.sensitive(phone);
        System.out.println("phone = " + phone);
    }

    @Test
    public void testAnnotation() throws JsonProcessingException {
        SensitiveResolver resolver = new SensitiveResolver();
        resolver.enable(true);
        SysUser sysUser = new SysUser();
        sysUser.setUsername("xinwuy");
        sysUser.setPhone("18180423321");
        sysUser.setEmail("834812353@qq.com");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(sysUser);
        System.out.println("json = " + json);
    }

    @Test
    public void testAnnotationError() throws JsonProcessingException {
        SensitiveResolver resolver = new SensitiveResolver();
        resolver.enable(true);
        SysUser sysUser = new SysUser();
        sysUser.setUsername("xinwuy");
        sysUser.setPhone("18180423321");
        sysUser.setEmail("83");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(sysUser);
        System.out.println("json = " + json);
    }

    @Test
    public void testEnable() throws JsonProcessingException {
        SysUser sysUser = new SysUser();
        sysUser.setUsername("xinwuy");
        sysUser.setPhone("18180423321");
        sysUser.setEmail("83");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(sysUser);
        System.out.println("json = " + json);
    }
}
