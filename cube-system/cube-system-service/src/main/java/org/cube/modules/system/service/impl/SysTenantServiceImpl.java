package org.cube.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cube.modules.system.mapper.SysTenantMapper;
import org.cube.modules.system.service.ISysTenantService;
import org.cube.modules.system.entity.SysTenant;
import org.springframework.stereotype.Service;

@Service
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements ISysTenantService {
}
