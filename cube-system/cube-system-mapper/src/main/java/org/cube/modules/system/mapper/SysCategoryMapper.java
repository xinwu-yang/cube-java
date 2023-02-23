package org.cube.modules.system.mapper;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.cube.modules.system.model.TreeSelectModel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.cube.modules.system.entity.SysCategory;

import java.util.List;

/**
 * 分类字典
 *
 * @author 杨欣武
 * @version 2.4.0
 * @since 2022-05-07
 */
public interface SysCategoryMapper extends BaseMapper<SysCategory> {

    /**
     * 根据父级ID查询树节点数据
     */
    List<TreeSelectModel> queryListByPid(@Param("pid") Long pid, @Param("query") JSONObject query);

    @Select("SELECT ID FROM sys_category WHERE CODE = #{code, jdbcType=VARCHAR}")
    Long queryIdByCode(@Param("code") String code);
}
