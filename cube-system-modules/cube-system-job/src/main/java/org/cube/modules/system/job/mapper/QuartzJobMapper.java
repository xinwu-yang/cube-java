package org.cube.modules.system.job.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.cube.modules.system.job.entity.QuartzJob;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuartzJobMapper extends BaseMapper<QuartzJob> {

    List<QuartzJob> findByJobClassName(@Param("jobClassName") String jobClassName);
}
