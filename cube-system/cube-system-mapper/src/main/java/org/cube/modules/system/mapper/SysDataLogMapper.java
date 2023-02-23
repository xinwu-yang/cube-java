package org.cube.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.cube.modules.system.entity.SysDataLog;

/**
 * 数据日志
 *
 * @author 杨欣武
 * @version 2.4.0
 * @since 2022-05-07
 */
public interface SysDataLogMapper extends BaseMapper<SysDataLog> {

    /**
     * 通过表名及数据Id获取最大版本
     */
    String queryMaxDataVer(@Param("tableName") String tableName, @Param("dataId") String dataId);
}
