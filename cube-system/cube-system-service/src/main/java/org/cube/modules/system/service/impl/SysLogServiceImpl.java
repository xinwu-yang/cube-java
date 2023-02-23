package org.cube.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cube.commons.model.LogDTO;
import org.cube.modules.system.entity.SysLog;
import org.cube.modules.system.mapper.SysLogMapper;
import org.cube.modules.system.service.ISysLogService;
import org.springframework.stereotype.Service;

@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

    /**
     * 清空所有日志记录
     */
    @Override
    public void removeAll() {
        baseMapper.removeAll();
    }

    /**
     * 保存日志
     */
    @Override
    public void saveLog(LogDTO dto) {
        baseMapper.saveLog(dto);
    }
}
