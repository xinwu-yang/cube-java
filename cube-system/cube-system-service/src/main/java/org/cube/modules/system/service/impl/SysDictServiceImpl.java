package org.cube.modules.system.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cube.commons.constant.CacheConst;
import org.cube.modules.system.entity.SysDict;
import org.cube.modules.system.entity.SysDictItem;
import org.cube.modules.system.mapper.SysDictItemMapper;
import org.cube.modules.system.mapper.SysDictMapper;
import org.cube.modules.system.model.DictModel;
import org.cube.modules.system.model.api.request.DuplicateCheckRequest;
import org.cube.modules.system.model.TreeSelectModel;
import org.cube.modules.system.service.ISysDictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements ISysDictService {

    @Autowired
    private SysDictItemMapper sysDictItemMapper;

    /**
     * 通过查询指定code 获取字典
     */
    @Override
    @Cacheable(value = CacheConst.SYS_DICT_CACHE, key = "#code")
    public List<DictModel> queryDictItemsByCode(String code) {
        log.debug("无缓存dictCache的时候调用这里！");
        return baseMapper.queryDictItemsByCode(code);
    }

    @Override
    public Map<String, List<DictModel>> queryAllDictItems() {
        Map<String, List<DictModel>> res = new HashMap<>();
        List<SysDict> ls = baseMapper.selectList(null);
        LambdaQueryWrapper<SysDictItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDictItem::getStatus, 1);
        queryWrapper.orderByAsc(SysDictItem::getSortOrder);
        List<SysDictItem> sysDictItemList = sysDictItemMapper.selectList(queryWrapper);
        for (SysDict d : ls) {
            List<DictModel> dictModelList = sysDictItemList.stream().filter(s -> d.getId().equals(s.getDictId())).map(item -> {
                DictModel dictModel = new DictModel();
                dictModel.setText(item.getItemText());
                dictModel.setValue(item.getItemValue());
                return dictModel;
            }).collect(Collectors.toList());
            res.put(d.getDictCode(), dictModelList);
        }
        return res;
    }

    /**
     * 通过查询指定code 获取字典值text
     */
    @Override
    @Cacheable(value = CacheConst.SYS_DICT_CACHE, key = "#code+':'+#key")
    public String queryDictTextByKey(String code, String key) {
        return baseMapper.queryDictTextByKey(code, key);
    }

    /**
     * 通过查询指定table的 text code 获取字典
     * dictTableCache采用redis缓存有效期10分钟
     */
    @Override
    //@Cacheable(value = CacheConstant.SYS_DICT_TABLE_CACHE)
    public List<DictModel> queryTableDictItemsByCode(String table, String text, String code) {
        log.debug("无缓存dictTableList的时候调用这里！");
        return baseMapper.queryTableDictItemsByCodeAndFilter(table, text, code, null);
    }

    @Override
    public List<DictModel> queryTableDictItemsByCodeAndFilter(String table, String text, String code, String filterSql) {
        log.debug("无缓存dictTableList的时候调用这里！");
        return baseMapper.queryTableDictItemsByCodeAndFilter(table, text, code, filterSql);
    }

    /**
     * 通过查询指定table的 text code 获取字典，包含text和value
     * dictTableCache采用redis缓存有效期10分钟
     *
     * @param keys (逗号分隔)
     */
    @Override
    @Cacheable(value = CacheConst.SYS_DICT_TABLE_BY_KEYS_CACHE)
    public List<String> queryTableDictByKeys(String table, String text, String code, String keys) {
        if (StrUtil.isEmpty(keys)) {
            return null;
        }
        String[] keyArray = keys.split(",");
        List<DictModel> dictModels = baseMapper.queryTableDictByKeys(table, text, code, ListUtil.of(keyArray));
        List<String> texts = new ArrayList<>(dictModels.size());
        // 查询出来的顺序可能是乱的，需要排个序
        for (String key : keyArray) {
            for (DictModel dict : dictModels) {
                if (key.equals(dict.getValue())) {
                    texts.add(dict.getText());
                    break;
                }
            }
        }
        return texts;
    }

    @Override
    @Transactional
    public Integer saveMain(SysDict sysDict, List<SysDictItem> sysDictItemList) {
        int insert = 0;
        try {
            insert = baseMapper.insert(sysDict);
            if (sysDictItemList != null) {
                for (SysDictItem entity : sysDictItemList) {
                    entity.setDictId(sysDict.getId());
                    entity.setStatus(1);
                    sysDictItemMapper.insert(entity);
                }
            }
        } catch (Exception e) {
            return insert;
        }
        return insert;
    }

    @Override
    public List<DictModel> queryTableDictItems(String table, String text, String code, String keyword, String filterSql) {
        StringBuilder sql = new StringBuilder();
        if (StrUtil.isNotEmpty(keyword)) {
            sql.append(text).append(" like '%").append(keyword).append("%'");
        }
        if (StrUtil.isNotEmpty(filterSql)) {
            if (sql.length() > 0) {
                sql.append(" and ").append(filterSql);
            } else {
                sql.append(filterSql);
            }
        }
        return baseMapper.queryTableDictItemsByCodeAndFilter(table, text, code, sql.toString());
    }

    @Override
    public List<TreeSelectModel> queryTreeList(Map<String, String> query, String table, String text, String code, String pidField, String pid, String hasChildField) {
        return baseMapper.queryTreeList(query, table, text, code, pidField, pid, hasChildField);
    }

    @Override
    public void deleteOneDictPhysically(String id) {
        this.baseMapper.physicalDeleteById(id);
        this.sysDictItemMapper.delete(new LambdaQueryWrapper<SysDictItem>().eq(SysDictItem::getDictId, id));
    }

    @Override
    public void updateDictDelFlag(int delFlag, String id) {
        baseMapper.updateDictDelFlag(delFlag, id);
    }

    @Override
    public List<SysDict> queryDeleteList() {
        return baseMapper.queryDeleteList();
    }

    @Override
    public Long duplicateCheckCountSql(DuplicateCheckRequest duplicateCheckRequest) {
        return baseMapper.duplicateCheckCountSql(duplicateCheckRequest);
    }
}
