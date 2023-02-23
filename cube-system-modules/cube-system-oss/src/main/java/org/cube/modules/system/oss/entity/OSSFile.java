package org.cube.modules.system.oss.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import org.cube.commons.base.CubeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("sys_oss_file")
@EqualsAndHashCode(callSuper = true)
public class OSSFile extends CubeEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件地址
     */
    private String url;
}
