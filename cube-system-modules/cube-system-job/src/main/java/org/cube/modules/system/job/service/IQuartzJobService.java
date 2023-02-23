package org.cube.modules.system.job.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cube.modules.system.job.entity.QuartzJob;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * 定时任务在线管理
 *
 * @author 杨欣武
 * @version 1.1.2
 * @since 2021-03-26
 */
public interface IQuartzJobService extends IService<QuartzJob> {

    List<QuartzJob> findByJobClassName(String jobClassName);

    boolean saveAndScheduleJob(QuartzJob quartzJob);

    boolean editAndScheduleJob(QuartzJob quartzJob) throws SchedulerException;

    boolean deleteAndStopJob(QuartzJob quartzJob);

    boolean resumeJob(QuartzJob quartzJob);

    /**
     * 执行定时任务
     *
     * @param quartzJob
     */
    void execute(QuartzJob quartzJob) throws Exception;

    /**
     * 暂停任务
     *
     * @param quartzJob
     * @throws SchedulerException
     */
    void pause(QuartzJob quartzJob);
}
