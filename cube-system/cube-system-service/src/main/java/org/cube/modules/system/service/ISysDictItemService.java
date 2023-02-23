package org.cube.modules.system.service;

import org.cube.modules.system.entity.SysDictItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 字典项
 *
 * @author 杨欣武
 * @version 2.4.0
 * @since 2022-05-07
 */
public interface ISysDictItemService extends IService<SysDictItem> {

    List<SysDictItem> selectItemsByMainId(String mainId);
}
