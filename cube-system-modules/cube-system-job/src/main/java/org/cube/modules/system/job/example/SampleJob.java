package org.cube.modules.system.job.example;

import org.cube.commons.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

/**
 * 示例不带参定时任务
 *
 * @author 杨欣武
 */
@Slf4j
public class SampleJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        log.info("魔方普通定时任务 SampleJob !  时间:" + DateUtils.getTimestamp());
    }
}
