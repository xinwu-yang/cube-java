package org.cube.modules.system.third.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.cube.modules.system.third.entity.SysThirdAccount;
import org.springframework.stereotype.Repository;

/**
 * 第三方登录账号表
 *
 * @author jeecg-boot
 * @version V1.0
 * @since 2020-11-17
 */
@Repository
public interface SysThirdAccountMapper extends BaseMapper<SysThirdAccount> {
}
