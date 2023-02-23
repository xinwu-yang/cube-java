package org.cube.modules.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.cube.commons.model.LogDTO;
import org.cube.modules.system.entity.SysLog;
import org.cube.modules.system.model.api.response.VisitsInfo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 系统日志
 *
 * @author 杨欣武
 * @version 2.4.0
 * @since 2022-05-07
 */
public interface SysLogMapper extends BaseMapper<SysLog> {

    /**
     * 清空所有日志记录
     */
    void removeAll();

    /**
     * 获取系统总访问次数
     *
     * @return Long
     */
    Long findTotalVisitCount();

    //update-begin--Author:zhangweijian  Date:20190428 for：传入开始时间，结束时间参数

    /**
     * 获取系统今日访问次数
     *
     * @return Long
     */
    Long findTodayVisitCount(@Param("dayStart") Date dayStart, @Param("dayEnd") Date dayEnd);

    /**
     * 获取系统今日访问 IP数
     *
     * @return Long
     */
    Long findTodayIp(@Param("dayStart") Date dayStart, @Param("dayEnd") Date dayEnd);
    //update-end--Author:zhangweijian  Date:20190428 for：传入开始时间，结束时间参数

    /**
     * 首页：根据时间统计访问数量/ip数量
     */
    List<VisitsInfo> findVisitCount(@Param("dayStart") Date dayStart, @Param("dayEnd") Date dayEnd, @Param("dbType") String dbType);

    /**
     * 保存日志
     */
    @InterceptorIgnore(illegalSql = "true", tenantLine = "true")
    void saveLog(@Param("dto") LogDTO dto);
}
