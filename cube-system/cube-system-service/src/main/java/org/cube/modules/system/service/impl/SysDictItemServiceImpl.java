package org.cube.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cube.modules.system.entity.SysDictItem;
import org.cube.modules.system.mapper.SysDictItemMapper;
import org.cube.modules.system.service.ISysDictItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysDictItemServiceImpl extends ServiceImpl<SysDictItemMapper, SysDictItem> implements ISysDictItemService {

    @Override
    public List<SysDictItem> selectItemsByMainId(String mainId) {
        return baseMapper.selectItemsByMainId(mainId);
    }
}
