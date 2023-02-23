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
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@Form(description = "测试设备")
@QueryFields({
        @QueryField(label = "设备名称", value = "name")
})
public class Device implements Serializable {
    @FormField(title = "主键")
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    @FormField(title = "设备名称", sort = 1)
    private String name;
    @ForeignKey(value = Tester.class)
    @FormField(title = "测试人", sort = 1)
    private Long testerId;
    @TableLogic
    @FormField(title = "是否删除", showInList = false)
    private Integer delFlag;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @FormField(title = "创建时间")
    private Date createTime;
}
