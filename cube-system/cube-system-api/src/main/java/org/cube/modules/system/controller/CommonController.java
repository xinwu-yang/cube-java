package org.cube.modules.system.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.http.Header;
import cn.hutool.json.JSONArray;
import org.cube.commons.base.Result;
import org.cube.modules.system.service.ICommonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 通用API
 *
 * @author 杨欣武
 * @version 2.5.2
 * @apiNote 提供常用API接口（错误提示页面，上传接口）
 * @since 2022-08-11
 */
@Slf4j
@Tag(name = "通用接口")
@RestController
@RequestMapping("/sys/common")
public class CommonController {

    @Autowired
    private ICommonService commonService;

    /**
     * 403
     *
     * @apiNote 没有权限，则会提示该消息
     */
    @GetMapping("/403")
    @Operation(summary = "403")
    public Result<?> noAuth() {
        return Result.error("没有权限，请联系管理员授权！");
    }

    /**
     * 全局默认异常返回数据
     */
    @GetMapping("/error")
    @Operation(summary = "全局默认异常返回数据")
    public Result<?> error() {
        return Result.error("默认异常错误，请联系管理员！");
    }

    /**
     * 文件上传
     *
     * @param biz 上传目录
     * @return 存储路径
     * @apiNote 上传到系统指定文件目录
     */
    @PostMapping("/upload")
    @Operation(summary = "文件上传")
    public Result<?> upload(@Parameter(name = "上传目录") @RequestParam(required = false) String biz,
                            @Parameter(name = "文件") @RequestParam(required = false) MultipartFile file) throws IOException {
        if (file == null) {
            return Result.error("没有上传文件！");
        }
        String dbPath = commonService.saveFileToLocal(file, biz);
        return Result.ok(dbPath);
    }

    /**
     * 静态资源访问
     */
    @GetMapping("/static/**")
    @Operation(summary = "静态资源访问")
    public void view(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        String filePath = new AntPathMatcher().extractPathWithinPattern(bestMatchPattern, path);
        File file = commonService.viewStaticFile(filePath);
        if (file == null) {
            response.setStatus(404);
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            IoUtil.write(response.getOutputStream(), true, ("该文件不存在！").getBytes(StandardCharsets.UTF_8));
            return;
        }
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.addHeader(Header.CONTENT_DISPOSITION.getValue(), "attachment;fileName=" + file.getName());
        FileUtil.writeToStream(file, response.getOutputStream());
    }

    /**
     * 获取枚举类的值列表
     *
     * @param enumClassPath 枚举类的类路径
     * @throws ClassNotFoundException 找不到这个类
     */
    @GetMapping("/enumValueList")
    @Operation(summary = "获取枚举类的值列表")
    public Result<?> getEnumValueList(@Parameter(name = "类路径") String enumClassPath) throws ClassNotFoundException {
        JSONArray dataArray = commonService.getEnumValueList(enumClassPath);
        return Result.ok(dataArray);
    }
}
