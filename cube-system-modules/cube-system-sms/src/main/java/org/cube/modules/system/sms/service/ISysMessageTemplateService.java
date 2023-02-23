package org.cube.modules.system.sms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cube.commons.base.CubeService;
import org.cube.modules.system.sms.entity.SysMessageTemplate;

import java.util.List;

/**
 * 消息模板
 *
 * @author xinwuy
 * @version 2.3.0
 * @since 2021-09-09
 */
public interface ISysMessageTemplateService extends CubeService<SysMessageTemplate>, IService<SysMessageTemplate> {

    List<SysMessageTemplate> selectByCode(String code);
}
