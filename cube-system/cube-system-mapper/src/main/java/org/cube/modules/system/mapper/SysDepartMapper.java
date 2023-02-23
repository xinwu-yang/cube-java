package org.cube.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.cube.modules.system.entity.SysDepart;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 部门管理
 *
 * @author 杨欣武
 * @version 2.4.0
 * @since 2022-05-07
 */
public interface SysDepartMapper extends BaseMapper<SysDepart> {

    /**
     * 根据用户ID查询部门集合
     */
    List<SysDepart> queryUserDeparts(@Param("userId") String userId);

    /**
     * 根据用户名查询部门
     */
    List<SysDepart> queryDepartsByUsername(@Param("username") String username);

    /**
     * 根据用户名查询所属部门的所有OrgCode
     *
     * @param username 用户名
     * @return orgCode列表
     */
    List<String> queryDepartOrgCodesByUsername(@Param("username") String username);

    /**
     * 根据部门编码查询部门id
     */
    @Select("select id from sys_depart where org_code = #{orgCode}")
    String queryDepartIdByOrgCode(@Param("orgCode") String orgCode);

    /**
     * 根据部门id查询部门父级id
     */
    @Select("select id,parent_id from sys_depart where id = #{departId}")
    SysDepart getParentDepartId(@Param("departId") String departId);

    /**
     * 根据部门Id查询,当前和下级所有部门
     */
    List<String> getSubDepIdsByDepId(@Param("departId") String departId);

    /**
     * 根据部门编码获取部门下所有部门
     */
    List<String> getSubDepIdsByOrgCodes(@Param("orgCodes") String[] orgCodes);
}
