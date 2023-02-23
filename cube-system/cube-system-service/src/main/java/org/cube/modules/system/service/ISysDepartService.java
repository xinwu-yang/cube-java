package org.cube.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cube.commons.base.Result;
import org.cube.modules.system.model.DepartIdModel;
import org.cube.modules.system.model.SysDepartTreeModel;
import org.cube.modules.system.entity.SysDepart;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 部门表
 *
 * @author 杨欣武
 * @version 2.4.0
 * @since 2022-05-07
 */
public interface ISysDepartService extends IService<SysDepart> {

    /**
     * 查询我的部门信息,并分节点进行显示
     */
    List<SysDepartTreeModel> queryMyDeptTreeList(String departIds);

    /**
     * 查询所有部门信息,并分节点进行显示
     */
    List<SysDepartTreeModel> queryTreeList();

    /**
     * 查询所有部门DepartId信息,并分节点进行显示
     */
    List<DepartIdModel> queryDepartIdTreeList();

    /**
     * 保存部门数据
     */
    Result<?> saveDepartData(SysDepart sysDepart);

    /**
     * 更新depart数据
     */
    void updateDepartDataById(SysDepart sysDepart, String username);

    /**
     * 根据关键字搜索相关的部门数据
     */
    List<SysDepartTreeModel> searchBy(String keyWord, String myDeptSearch, String departIds);

    /**
     * 根据部门id删除并删除其可能存在的子级部门
     */
    boolean delete(String id);

    /**
     * 查询SysDepart集合
     */
    List<SysDepart> queryUserDeparts(String userId);

    /**
     * 根据用户名查询部门
     */
    List<SysDepart> queryDepartsByUsername(String username);

    /**
     * 根据部门id批量删除并删除其可能存在的子级部门
     */
    void deleteBatchWithChildren(List<String> ids);

    /**
     * 根据部门Id查询,当前和下级所有部门IDS
     */
    List<String> getSubDepIdsByDepId(String departId);

    /**
     * 获取我的部门下级所有部门IDS
     */
    List<String> getMySubDepIdsByDepId(String departIds);

    /**
     * 根据关键字获取部门信息（通讯录）
     */
    List<SysDepartTreeModel> queryTreeByKeyWord(String keyWord);

    SysDepart getParentDepartId(String departId);

    String queryDepartIdByOrgCode(String orgCode);

    List<String> getSubDepIdsByOrgCodes(@Param("orgCodes") String[] orgCodes);
}
