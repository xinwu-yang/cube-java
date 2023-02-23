package org.cube.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.cube.modules.system.entity.SysDictItem;

import java.util.List;

/**
 * 字典项
 *
 * @author 杨欣武
 * @version 2.4.0
 * @since 2022-05-07
 */
public interface SysDictItemMapper extends BaseMapper<SysDictItem> {

    @Select("SELECT * FROM sys_dict_item WHERE DICT_ID = #{mainId} order by sort_order asc, item_value asc")
    List<SysDictItem> selectItemsByMainId(String mainId);
}
