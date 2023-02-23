package org.cube.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cube.modules.system.mapper.SysDataLogMapper;
import org.cube.modules.system.service.ISysDataLogService;
import org.cube.modules.system.entity.SysDataLog;
import org.springframework.stereotype.Service;

@Service
public class SysDataLogServiceImpl extends ServiceImpl<SysDataLogMapper, SysDataLog> implements ISysDataLogService {
}