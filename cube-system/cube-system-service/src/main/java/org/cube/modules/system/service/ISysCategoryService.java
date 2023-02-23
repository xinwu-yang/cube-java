package org.cube.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cube.modules.system.model.TreeSelectModel;
import org.cube.commons.exception.CubeAppException;
import org.cube.modules.system.entity.SysCategory;

import java.util.List;

/**
 * 分类字典
 *
 * @author 杨欣武
 * @version 2.4.0
 * @since 2022-05-07
 */
public interface ISysCategoryService extends IService<SysCategory> {

    /**
     * 根节点父ID的值
     */
    Long ROOT_PID_VALUE = 0L;

    void addSysCategory(SysCategory sysCategory);

    void updateSysCategory(SysCategory sysCategory);

    /**
     * 根据父级编码加载分类字典的数据
     */
    List<TreeSelectModel> queryListByCode(String pcode) throws CubeAppException;

    /**
     * 根据pid查询子节点集合
     */
    List<TreeSelectModel> queryListByPid(Long pid);

    /**
     * 根据code查询id
     */
    Long queryIdByCode(String code);

    /**
     * 分类字典树控件专用接口
     */
    List<TreeSelectModel> loadTreeData(Long pid, String pcode, String condition);
}
