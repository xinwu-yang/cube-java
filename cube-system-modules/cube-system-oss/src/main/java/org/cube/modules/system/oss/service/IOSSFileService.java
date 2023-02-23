package org.cube.modules.system.oss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cube.modules.system.oss.entity.OSSFile;
import org.springframework.web.multipart.MultipartFile;

public interface IOSSFileService extends IService<OSSFile> {

    void upload(MultipartFile file);

    boolean delete(OSSFile ossFile);
}
