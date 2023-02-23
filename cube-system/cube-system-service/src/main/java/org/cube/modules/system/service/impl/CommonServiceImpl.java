package org.cube.modules.system.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.cube.application.config.properties.ResourcesProperties;
import org.cube.commons.base.BaseEnum;
import org.cube.commons.exception.CubeAppException;
import org.cube.modules.system.service.ICommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@Slf4j
@Service
public class CommonServiceImpl implements ICommonService {

    @Autowired
    private ResourcesProperties resourcesProperties;

    @Override
    public String saveFileToLocal(MultipartFile multipartFile, String path) throws IOException {
        String dirPath = resourcesProperties.getUploadPath() + File.separator;
        if (StrUtil.isNotEmpty(path) && ObjectUtil.notEqual("null", path)) {
            dirPath = dirPath + path + File.separator;
        }
        File file = new File(dirPath);
        if (!file.exists()) {
            boolean result = file.mkdirs();
            if (!result) {
                throw new CubeAppException("文件目录创建失败！");
            }
        }
        String originFileName = multipartFile.getOriginalFilename();
        if (StrUtil.isEmpty(originFileName)) {
            throw new CubeAppException("获取文件名失败！");
        }
        String fileName = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN) + "-" + RandomUtil.randomString(6) + originFileName.substring(originFileName.lastIndexOf("."));
        String savePath = file.getPath() + File.separator + fileName;
        log.info(savePath);
        File saveFile = new File(savePath);
        FileCopyUtils.copy(multipartFile.getBytes(), saveFile);
        String dbpath;
        if (StrUtil.isNotEmpty(path)) {
            dbpath = path + File.separator + fileName;
        } else {
            dbpath = fileName;
        }
        if (dbpath.contains("\\")) {
            dbpath = dbpath.replace("\\", "/");
        }
        return dbpath;
    }

    @Override
    public File viewStaticFile(String filePath) {
        if (StrUtil.isEmpty(filePath) || filePath.equals("null")) {
            return null;
        }
        filePath = filePath.replace("..", "");
        if (filePath.endsWith(",")) {
            filePath = filePath.substring(0, filePath.length() - 1);
        }
        String fileFullPath = resourcesProperties.getUploadPath() + File.separator + filePath;
        File file = new File(fileFullPath);
        if (!file.exists()) {
            return null;
        }
        return file;
    }

    @Override
    public JSONArray getEnumValueList(String enumClassPath) throws ClassNotFoundException {
        Class<?> cls = Class.forName(enumClassPath);
        if (!cls.isEnum()) {
            throw new CubeAppException("该类不是枚举类型！");
        }
        BaseEnum[] baseEnums = (BaseEnum[]) cls.getEnumConstants();
        JSONArray dataArray = JSONUtil.createArray();
        for (BaseEnum baseEnum : baseEnums) {
            JSONObject data = JSONUtil.createObj();
            data.set("label", baseEnum.getLabel());
            data.set("value", baseEnum.getValue());
            dataArray.add(data);
        }
        return dataArray;
    }
}
