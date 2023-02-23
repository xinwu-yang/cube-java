package org.cube.modules.system.job.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cube.commons.mybatisplus.QueryGenerator;
import org.cube.commons.utils.SystemContextUtil;
import org.cube.commons.base.Result;
import org.cube.modules.system.job.entity.QuartzJob;
import org.cube.plugin.easyexcel.EasyExcel;
import org.cube.plugin.easyexcel.model.ImportExcel;
import org.cube.commons.utils.web.HttpServletUtil;
import org.cube.modules.system.job.service.IQuartzJobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 定时任务在线管理
 *
 * @author 杨欣武
 * @version 1.1.2
 * @since 2021-03-26
 */
@Slf4j
@RestController
@RequestMapping("/sys/quartzJob")
public class QuartzJobController {

    @Autowired
    private IQuartzJobService quartzJobService;
    @Autowired
    private EasyExcel easyExcel;

    /**
     * 分页列表查询
     */
    @GetMapping("/list")
    public Result<?> queryPageList(QuartzJob quartzJob, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        QueryWrapper<QuartzJob> queryWrapper = QueryGenerator.initQueryWrapper(quartzJob, req.getParameterMap());
        Page<QuartzJob> page = new Page<>(pageNo, pageSize);
        IPage<QuartzJob> pageList = quartzJobService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加定时任务
     *
     * @param quartzJob 参数
     */
    @PostMapping("/add")
    public Result<?> add(@RequestBody QuartzJob quartzJob) {
        List<QuartzJob> list = quartzJobService.findByJobClassName(quartzJob.getJobClassName());
        if (list != null && list.size() > 0) {
            return Result.error("该定时任务类名已存在！");
        }
        quartzJobService.saveAndScheduleJob(quartzJob);
        return Result.ok("创建定时任务成功！");
    }

    /**
     * 更新定时任务
     *
     * @param quartzJob 参数
     */
    @PutMapping("/edit")
    public Result<?> edit(@RequestBody QuartzJob quartzJob) throws SchedulerException {
        quartzJobService.editAndScheduleJob(quartzJob);
        return Result.ok();
    }

    /**
     * 通过id删除
     *
     * @param id 主键id
     */
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam String id) {
        QuartzJob quartzJob = quartzJobService.getById(id);
        if (quartzJob == null) {
            return Result.error("未找到对应实体！");
        }
        quartzJobService.deleteAndStopJob(quartzJob);
        return Result.ok();

    }

    /**
     * 批量删除
     *
     * @param ids 主键id（多个逗号分隔）
     */
    @DeleteMapping("/deleteBatch")
    public Result<?> deleteBatch(@RequestParam String ids) {
        if (ids == null || "".equals(ids.trim())) {
            return Result.error("参数不识别！");
        }
        for (String id : ids.split(",")) {
            QuartzJob job = quartzJobService.getById(id);
            quartzJobService.deleteAndStopJob(job);
        }
        return Result.ok();
    }

    /**
     * 暂停定时任务
     *
     * @param jobClassName 任务类名
     */
    @GetMapping("/pause")
    public Result<?> pauseJob(@RequestParam String jobClassName) {
        QuartzJob job = quartzJobService.getOne(new LambdaQueryWrapper<QuartzJob>().eq(QuartzJob::getJobClassName, jobClassName));
        if (job == null) {
            return Result.error("定时任务不存在！");
        }
        quartzJobService.pause(job);
        return Result.ok();
    }

    /**
     * 恢复定时任务
     *
     * @param jobClassName 任务类名
     */
    @GetMapping("/resume")
    public Result<?> resumeJob(@RequestParam String jobClassName) {
        QuartzJob job = quartzJobService.getOne(new LambdaQueryWrapper<QuartzJob>().eq(QuartzJob::getJobClassName, jobClassName));
        if (job == null) {
            return Result.error("定时任务不存在！");
        }
        quartzJobService.resumeJob(job);
        return Result.ok("");
    }

    /**
     * 通过id查询
     *
     * @param id 主键id
     */
    @GetMapping("/queryById")
    public Result<?> queryById(@RequestParam String id) {
        QuartzJob quartzJob = quartzJobService.getById(id);
        return Result.ok(quartzJob);
    }

    /**
     * 导出excel
     */
    @RequestMapping("/exportXls")
    public void exportXls(QuartzJob quartzJob, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Step.1 组装查询条件
        QueryWrapper<QuartzJob> queryWrapper = QueryGenerator.initQueryWrapper(quartzJob, request.getParameterMap());
        // Step.2 AutoPoi 导出Excel
        List<QuartzJob> pageList = quartzJobService.list(queryWrapper);
        HttpServletUtil.addDownloadHeader(response, "定时任务列表数据-" + DateUtil.format(new Date(), "yyyyMMdd") + easyExcel.getExtension());
        easyExcel.export(pageList, response.getOutputStream(), SystemContextUtil.dictTranslator());
    }

    /**
     * 通过excel导入数据
     */
    @PostMapping("/importExcel")
    public Result<?> importExcel(HttpServletRequest request) throws IOException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        // 错误信息
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();
            ImportExcel excel = new ImportExcel();
            excel.setInputStream(file.getInputStream());
            List<QuartzJob> quartzJobs = easyExcel.read(QuartzJob.class, excel, SystemContextUtil.dictTranslator());
            quartzJobService.saveBatch(quartzJobs);
        }
        return Result.ok();
    }

    /**
     * 立即执行
     *
     * @param id 主键id
     */
    @GetMapping("/execute")
    public Result<?> execute(@RequestParam String id) throws Exception {
        QuartzJob quartzJob = quartzJobService.getById(id);
        if (quartzJob == null) {
            return Result.error("未找到对应实体！");
        }
        quartzJobService.execute(quartzJob);
        return Result.ok();
    }
}
