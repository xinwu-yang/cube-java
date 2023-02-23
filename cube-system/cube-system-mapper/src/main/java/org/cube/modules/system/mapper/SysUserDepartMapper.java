package org.cube.modules.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.cube.modules.system.entity.SysUserDepart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 用户部门关系
 *
 * @author 杨欣武
 * @version 2.4.0
 * @since 2022-05-07
 */
public interface SysUserDepartMapper extends BaseMapper<SysUserDepart>{
	
	List<SysUserDepart> getUserDepartByUid(@Param("userId") String userId);
}
