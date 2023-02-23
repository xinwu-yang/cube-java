package org.cube.modules.system.sms.service.impl;

import org.cube.commons.base.CubeServiceImpl;
import org.cube.modules.system.sms.entity.SysMessageTemplate;
import org.cube.modules.system.sms.mapper.SysMessageTemplateMapper;
import org.cube.modules.system.sms.service.ISysMessageTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysMessageTemplateServiceImpl extends CubeServiceImpl<SysMessageTemplateMapper, SysMessageTemplate> implements ISysMessageTemplateService {

    @Autowired
    private SysMessageTemplateMapper sysMessageTemplateMapper;

    @Override
    public List<SysMessageTemplate> selectByCode(String code) {
        return sysMessageTemplateMapper.selectByCode(code);
    }
}
