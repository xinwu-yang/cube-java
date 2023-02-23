package org.cube.modules.system.sms.service.impl;

import org.cube.commons.base.CubeServiceImpl;
import org.cube.modules.system.sms.entity.SysMessage;
import org.cube.modules.system.sms.mapper.SysMessageMapper;
import org.cube.modules.system.sms.service.ISysMessageService;
import org.springframework.stereotype.Service;

@Service
public class SysMessageServiceImpl extends CubeServiceImpl<SysMessageMapper, SysMessage> implements ISysMessageService {

}
