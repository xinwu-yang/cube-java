package org.cube.modules.system.service;

import cn.hutool.json.JSONArray;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 通用API的Service
 *
 * @author 杨欣武
 * @version 2.5.2
 * @since 2022-08-10
 */
public interface ICommonService {

    /**
     * 保存上传上来的文件到本地
     *
     * @param multipartFile 文件内容
     * @param path          路径
     * @return 路径
     */
    String saveFileToLocal(MultipartFile multipartFile, String path) throws IOException;

    /**
     * 预览图片&下载文件
     *
     * @param filePath 请求的文件路径
     * @return 文件
     */
    File viewStaticFile(String filePath);

    /**
     * 获取枚举内容列表
     *
     * @param enumClassPath 枚举类路径
     * @return 枚举内容列表（JSONArray）
     */
    JSONArray getEnumValueList(String enumClassPath) throws ClassNotFoundException;
}
