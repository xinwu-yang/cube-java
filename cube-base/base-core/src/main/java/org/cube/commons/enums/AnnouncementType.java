package org.cube.commons.enums;

import cn.hutool.core.util.StrUtil;

/**
 * 系统公告自定义跳转方式
 */
public enum AnnouncementType {
    /**
     * 邮件跳转组件
     */
    EMAIL("email", "component", "modules/eoa/email/modals/EoaEmailInForm"),
    /**
     * 工作流跳转链接我的办公
     */
    BPM("bpm", "url", "/bpm/task/MyTaskList");

    /**
     * 业务类型(email:邮件 bpm:流程)
     */
    private String type;
    /**
     * 打开方式 组件：component 路由：url
     */
    private String openType;
    /**
     * 组件/路由 地址
     */
    private String openPage;

    AnnouncementType(String type, String openType, String openPage) {
        this.type = type;
        this.openType = openType;
        this.openPage = openPage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOpenType() {
        return openType;
    }

    public void setOpenType(String openType) {
        this.openType = openType;
    }

    public String getOpenPage() {
        return openPage;
    }

    public void setOpenPage(String openPage) {
        this.openPage = openPage;
    }

    public static AnnouncementType getByType(String type) {
        if (StrUtil.isEmpty(type)) {
            return null;
        }
        for (AnnouncementType val : values()) {
            if (val.getType().equals(type)) {
                return val;
            }
        }
        return null;
    }
}
