package org.cube.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.cube.modules.system.model.api.request.DuplicateCheckRequest;
import org.cube.modules.system.model.TreeSelectModel;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.cube.modules.system.model.DictModel;
import org.cube.modules.system.entity.SysDict;

import java.util.List;
import java.util.Map;

/**
 * 字典
 *
 * @author 杨欣武
 * @version 2.4.0
 * @since 2022-05-07
 */
public interface SysDictMapper extends BaseMapper<SysDict> {

    /**
     * 重复检查SQL
     */
    Long duplicateCheckCountSql(DuplicateCheckRequest duplicateCheckRequest);

    List<DictModel> queryDictItemsByCode(@Param("code") String code);

    List<DictModel> queryTableDictItemsByCodeAndFilter(@Param("table") String table, @Param("text") String text, @Param("code") String code, @Param("filterSql") String filterSql);

    String queryDictTextByKey(@Param("code") String code, @Param("key") String key);

    String queryDictKeyByText(@Param("code") String code, @Param("text") String text);

    List<DictModel> queryTableDictByKeys(@Param("table") String table, @Param("text") String text, @Param("code") String code, @Param("keyArray") List<String> keyArray);

    String queryTableDictByText(@Param("table") String table, @Param("text") String text, @Param("code") String code, @Param("value") String value);

    /**
     * 查询所有部门 作为字典信息 id -->value,departName -->text
     */
    List<DictModel> queryAllDepartBackDictModel();

    /**
     * 根据表名、显示字段名、存储字段名 查询树
     */
    List<TreeSelectModel> queryTreeList(@Param("query") Map<String, String> query, @Param("table") String table, @Param("text") String text, @Param("code") String code, @Param("pidField") String pidField, @Param("pid") String pid, @Param("hasChildField") String hasChildField);

    /**
     * 物理删除
     */
    @Select("delete from sys_dict where id = #{id}")
    void physicalDeleteById(@Param("id") String id);

    /**
     * 查询被逻辑删除的数据
     */
    @Select("select * from sys_dict where del_flag = 1")
    List<SysDict> queryDeleteList();

    /**
     * 修改状态值
     */
    @Update("update sys_dict set del_flag = #{flag,jdbcType=INTEGER} where id = #{id,jdbcType=VARCHAR}")
    void updateDictDelFlag(@Param("flag") int delFlag, @Param("id") String id);
}
