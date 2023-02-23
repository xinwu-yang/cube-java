package org.cube.modules.system.job.example;

import org.cube.commons.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

/**
 * 示例带参定时任务
 *
 * @author 杨欣武
 */
@Slf4j
public class SampleParamJob implements Job {

    /**
     * 若参数变量名修改 QuartzJobController中也需对应修改
     */
    private String parameter;

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        log.info(String.format("welcome %s! Cube-Boot 带参数定时任务 SampleParamJob !   时间:" + DateUtils.now(), this.parameter));
    }
}
