package org.cube.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.cube.modules.system.model.api.request.DuplicateCheckRequest;
import org.cube.modules.system.model.TreeSelectModel;
import org.cube.modules.system.model.DictModel;
import org.cube.modules.system.entity.SysDict;
import org.cube.modules.system.entity.SysDictItem;

import java.util.List;
import java.util.Map;

/**
 * 字典表
 *
 * @author 杨欣武
 * @version 2.4.0
 * @since 2022-05-07
 */
public interface ISysDictService extends IService<SysDict> {

    List<DictModel> queryDictItemsByCode(String code);

    Map<String, List<DictModel>> queryAllDictItems();

    List<DictModel> queryTableDictItemsByCode(String table, String text, String code);

    List<DictModel> queryTableDictItemsByCodeAndFilter(String table, String text, String code, String filterSql);

    String queryDictTextByKey(String code, String key);

    List<String> queryTableDictByKeys(String table, String text, String code, String keys);

    /**
     * 添加一对多
     */
    Integer saveMain(SysDict sysDict, List<SysDictItem> sysDictItemList);

    /**
     * 通过关键字查询字典表
     */
    List<DictModel> queryTableDictItems(String table, String text, String code, String keyword, String filterSql);

    /**
     * 根据表名、显示字段名、存储字段名 查询树
     */
    List<TreeSelectModel> queryTreeList(Map<String, String> query, String table, String text, String code, String pidField, String pid, String hasChildField);

    /**
     * 真实删除
     */
    void deleteOneDictPhysically(String id);

    /**
     * 修改delFlag
     */
    void updateDictDelFlag(int delFlag, String id);

    /**
     * 查询被逻辑删除的数据
     */
    List<SysDict> queryDeleteList();

    Long duplicateCheckCountSql(DuplicateCheckRequest duplicateCheckRequest);
}
