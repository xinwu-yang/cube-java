package org.cube.commons.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 杨欣武
 */
@Slf4j
public class EntityConvertUtil {

    /**
     * 将EntityList转换成DTOModelList
     */
    public static <F, T> List<T> entityListToModelList(List<F> fromList, Class<T> tClass) {
        if (fromList == null || fromList.isEmpty()) {
            return null;
        }
        List<T> tList = new ArrayList<>();
        for (F f : fromList) {
            T t = entityToModel(f, tClass);
            tList.add(t);
        }
        return tList;
    }

    /**
     * 将Entity转换成DTOModel
     */
    public static <F, T> T entityToModel(F entity, Class<T> modelClass) {
        log.debug("entityToModel : Entity属性的值赋值到Model");
        T model = null;
        if (entity == null || modelClass == null) {
            return null;
        }
        try {
            model = modelClass.newInstance();
        } catch (InstantiationException e) {
            log.error("entityToModel : 实例化异常", e);
        } catch (IllegalAccessException e) {
            log.error("entityToModel : 安全权限异常", e);
        }
        BeanUtils.copyProperties(entity, model);
        return model;
    }
}
