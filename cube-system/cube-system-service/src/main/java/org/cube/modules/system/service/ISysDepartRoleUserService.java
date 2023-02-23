package org.cube.modules.system.service;

import org.cube.modules.system.entity.SysDepartRoleUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 部门角色人员信息
 *
 * @author 杨欣武
 * @version 2.4.0
 * @since 2022-05-07
 */
public interface ISysDepartRoleUserService extends IService<SysDepartRoleUser> {

    void deptRoleUserAdd(String userId,String newRoleId,String oldRoleId);

    /**
     * 取消用户与部门关联，删除关联关系
     */
    void removeDeptRoleUser(List<String> userIds,String depId);
}
