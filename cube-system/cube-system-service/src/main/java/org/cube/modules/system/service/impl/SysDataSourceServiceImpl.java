package org.cube.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cube.modules.system.mapper.SysDataSourceMapper;
import org.cube.modules.system.service.ISysDataSourceService;
import org.cube.modules.system.entity.SysDataSource;
import org.springframework.stereotype.Service;

@Service
public class SysDataSourceServiceImpl extends ServiceImpl<SysDataSourceMapper, SysDataSource> implements ISysDataSourceService {
}
