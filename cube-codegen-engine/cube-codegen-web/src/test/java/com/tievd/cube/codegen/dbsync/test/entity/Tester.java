package com.tievd.cube.codegen.dbsync.test.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.cube.codegen.annotations.*;
import org.cube.codegen.annotations.db.ForcedSync;
import org.cube.codegen.annotations.models.ComponentType;
import org.cube.codegen.core.models.ComponentParamKey;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@SubTables(Device.class)
@Form(description = "测试人员", isGroup = true)
@QueryFields({
        @QueryField(label = "姓名", value = "name"),
        @QueryField(value = "sex", useFieldComponent = true),
        @QueryField(value = "createTime", component = ComponentType.DATE)
})
@ForcedSync
@Groups({@Group(id = 1, name = "基础信息"), @Group(id = 2, name = "权限信息")})
public class Tester implements Serializable {
    @FormField(title = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    @FormField(title = "真实姓名", groupId = 1, sort = 1)
    private String name;
    @FormField(title = "性别", groupId = 1, sort = 2, component = ComponentType.DICT_SELECT_TAG, componentParams = @ComponentParam(key = ComponentParamKey.DICT_CODE, value = "sex"))
    private Integer sex;
    @FormField(title = "管理的菜单", groupId = 2, sort = 3, component = ComponentType.TREE_SELECT,
            componentParams = {
                    @ComponentParam(key = ComponentParamKey.DICT_CODE, value = "sys_permission,name,id"),
                    @ComponentParam(key = "pidField", value = "parent_id"),
                    @ComponentParam(key = "condition", value = "{\"del_flag\":\"0\"}")
            })
    private String permissionId;
    @FormField(title = "角色", groupId = 2, sort = 4, component = ComponentType.MULTI_SELECT_TAG, componentParams = @ComponentParam(key = ComponentParamKey.DICT_CODE, value = "sys_role,role_name,id"))
    private String role;
    @TableLogic
    @FormField(title = "是否删除", showInList = false)
    private Integer delFlag;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @FormField(title = "创建时间")
    private Date createTime;
}
