package org.cube.commons.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;

/**
 * ServiceImpl基类
 *
 * @author 杨欣武
 * @version 1.0.0
 * @since 2019-4-21 8:13
 */
@Slf4j
public class CubeServiceImpl<M extends BaseMapper<T>, T extends CubeEntity> extends ServiceImpl<M, T> implements CubeService<T> {
}
