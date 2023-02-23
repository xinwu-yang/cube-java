package org.cube.modules.system.oss.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cube.modules.system.oss.entity.OSSFile;
import org.cube.modules.system.oss.mapper.OSSFileMapper;
import org.cube.modules.system.oss.service.IOSSFileService;
import org.cube.modules.system.oss.utils.OssBootUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Service("ossFileService")
public class OSSFileServiceImpl extends ServiceImpl<OSSFileMapper, OSSFile> implements IOSSFileService {

    @Override
    public void upload(MultipartFile file) {
        String originFileName = file.getOriginalFilename();
        String fileName = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN) + "-" + RandomUtil.randomString(6) + originFileName.substring(originFileName.lastIndexOf("."));
        OSSFile ossFile = new OSSFile();
        ossFile.setFileName(fileName);
        String url = OssBootUtil.upload(file, "upload/test");
        ossFile.setUrl(url);
        this.save(ossFile);
    }

    @Override
    public boolean delete(OSSFile ossFile) {
        try {
            this.removeById(ossFile.getId());
            OssBootUtil.deleteUrl(ossFile.getUrl());
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
}
