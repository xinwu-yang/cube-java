package org.cube.modules.system.oss.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cube.commons.mybatisplus.QueryGenerator;
import org.cube.commons.base.Result;
import org.cube.modules.system.oss.entity.OSSFile;
import org.cube.modules.system.oss.service.IOSSFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * OSS API
 */
@Slf4j
@RestController
@RequestMapping("/sys/oss/file")
public class OSSFileController {

    @Autowired
    private IOSSFileService ossFileService;

    /**
     * 分页列表查询
     *
     * @param file     查询参数
     * @param pageNo   页码
     * @param pageSize 每页数量
     */
    @GetMapping("/list")
    public Result<IPage<OSSFile>> queryPageList(OSSFile file, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        QueryWrapper<OSSFile> queryWrapper = QueryGenerator.initQueryWrapper(file, req.getParameterMap());
        Page<OSSFile> page = new Page<>(pageNo, pageSize);
        IPage<OSSFile> pageList = ossFileService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    public Result<?> upload(@RequestParam("file") MultipartFile file) {
        ossFileService.upload(file);
        return Result.ok();
    }

    /**
     * 通过id删除
     *
     * @param id 主键id
     */
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam String id) {
        OSSFile file = ossFileService.getById(id);
        if (file == null) {
            return Result.error("未找到对应实体！");
        }
        ossFileService.delete(file);
        return Result.ok();
    }

    /**
     * 通过id查询.
     *
     * @param id 主键id
     */
    @GetMapping("/queryById")
    public Result<OSSFile> queryById(@RequestParam String id) {
        OSSFile file = ossFileService.getById(id);
        if (file == null) {
            return Result.error("未找到对应实体！");
        }
        return Result.ok(file);
    }
}
