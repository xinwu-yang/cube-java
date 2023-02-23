package org.cube.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cube.commons.model.LogDTO;
import org.cube.modules.system.entity.SysLog;

/**
 * 系统日志
 *
 * @author 杨欣武
 * @version 2.4.0
 * @since 2022-05-07
 */
public interface ISysLogService extends IService<SysLog> {

    /**
     * 清空所有日志记录
     */
    void removeAll();

    /**
     * 保存日志
     */
    void saveLog(LogDTO dto);
}
