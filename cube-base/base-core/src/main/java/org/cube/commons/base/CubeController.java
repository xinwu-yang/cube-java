package org.cube.commons.base;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.cube.commons.mybatisplus.QueryGenerator;
import org.cube.plugin.easyexcel.EasyExcel;
import org.cube.plugin.easyexcel.model.ImportExcel;
import org.cube.commons.utils.SystemContextUtil;
import org.cube.commons.utils.web.HttpServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller 基础类
 *
 * @author xinwuy
 * @version 1.2.0
 * @apiNote 提供基于实体类的导入导出
 * @since 2021-05-31
 */
@Slf4j
public class CubeController<T, S extends IService<T>> {

    @Autowired
    protected S service;
    @Autowired
    protected EasyExcel easyExcel;

    /**
     * 导出excel
     */
    protected void exportXls(HttpServletRequest request, HttpServletResponse response, T object, String title) throws IOException {
        // Step.1 组装查询条件
        QueryWrapper<T> queryWrapper = QueryGenerator.initQueryWrapper(object, request.getParameterMap());
        // Step.2 获取导出数据
        List<T> pageList = service.list(queryWrapper);
        List<T> exportList;
        // 过滤选中数据
        String selections = request.getParameter("selections");
        if (StrUtil.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            exportList = pageList.stream().filter(item -> selectionList.contains(getId(item))).collect(Collectors.toList());
        } else {
            exportList = pageList;
        }
        HttpServletUtil.addDownloadHeader(response, title);
        easyExcel.export(exportList, response.getOutputStream(), SystemContextUtil.dictTranslator());
    }

    /**
     * 获取对象ID
     */
    private String getId(T item) {
        try {
            return BeanUtil.getFieldValue(item, "id").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过excel导入数据
     */
    protected Result<?> importExcel(HttpServletRequest request, Class<T> clazz) throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportExcel excel = new ImportExcel();
            excel.setInputStream(file.getInputStream());
            List<T> list = easyExcel.read(clazz, excel, SystemContextUtil.dictTranslator());
            service.saveBatch(list);
        }
        return Result.ok("文件导入成功！");
    }
}
