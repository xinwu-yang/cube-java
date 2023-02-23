package org.cube.modules.system.sms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.cube.modules.system.sms.entity.SysMessageTemplate;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 消息模板
 *
 * @author xinwuy
 * @version 2.3.0
 * @since 2021-09-09
 */
public interface SysMessageTemplateMapper extends BaseMapper<SysMessageTemplate> {
    @Select("SELECT * FROM SYS_SMS_TEMPLATE WHERE TEMPLATE_CODE = #{code}")
    List<SysMessageTemplate> selectByCode(String code);
}
