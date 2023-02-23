package org.cube.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cube.commons.base.Result;
import org.cube.modules.system.entity.SysPosition;

import java.util.List;

/**
 * 职务
 *
 * @author 杨欣武
 * @version 2.4.0
 * @since 2022-05-07
 */
public interface ISysPositionService extends IService<SysPosition> {

    /**
     * 删除职务
     *
     * @param positionIds 职务id列表
     * @return 结果
     */
    Result<?> delete(List<String> positionIds);
}
