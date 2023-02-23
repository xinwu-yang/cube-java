package org.cube.modules.system.enhancement.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.cube.modules.system.entity.SysDictItem;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CubeDictItemMapper extends BaseMapper<SysDictItem> {

    @Select("SELECT * FROM sys_dict_item WHERE DICT_ID = #{mainId} order by sort_order asc, item_value asc")
    List<SysDictItem> selectItemsByMainId(String mainId);
}
