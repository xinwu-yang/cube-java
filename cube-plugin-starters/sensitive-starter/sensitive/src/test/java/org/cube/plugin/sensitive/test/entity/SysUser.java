package org.cube.plugin.sensitive.test.entity;

import org.cube.plugin.sensitive.Sensitive;
import org.cube.plugin.sensitive.SensitiveType;
import org.cube.plugin.sensitive.test.custom.PhoneSensitive;

import java.io.Serializable;

/**
 * 用户表
 *
 * @author scott
 * @since 2018-12-20
 */
public class SysUser implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    @Sensitive(value = SensitiveType.CUSTOM, custom = PhoneSensitive.class)
    private String username;
    @Sensitive
    private String email;
    @Sensitive(start = 10, length = 4)
    private String phone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public SysUser() {
    }
}
