package org.cube.modules.system.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.cube.commons.constant.CacheConst;
import org.cube.commons.constant.CommonConst;
import org.cube.commons.base.CubeController;
import org.cube.commons.mybatisplus.QueryGenerator;
import org.cube.commons.annotations.AutoLog;
import org.cube.commons.base.Result;
import org.cube.modules.system.service.ISysDictItemService;
import org.cube.modules.system.service.ISysDictService;
import org.cube.plugin.easyexcel.model.ImportExcel;
import org.cube.commons.utils.SqlInjectionUtil;
import org.cube.commons.utils.SystemContextUtil;
import org.cube.commons.utils.web.HttpServletUtil;
import org.cube.modules.system.entity.SysDict;
import org.cube.modules.system.entity.SysDictItem;
import org.cube.modules.system.model.DictModel;
import org.cube.modules.system.model.SysDictPage;
import org.cube.modules.system.model.SysDictTree;
import org.cube.modules.system.model.TreeSelectModel;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 数据字典
 *
 * @author 杨欣武
 * @since 2021-07-12
 */
@Slf4j
@Tag(name = "数据字典相关接口")
@RestController
@RequestMapping("/sys/dict")
public class SysDictController extends CubeController<SysDict, ISysDictService> {

    @Autowired
    private ISysDictItemService sysDictItemService;

    /**
     * 分页列表查询
     *
     * @param pageNo   页码
     * @param pageSize 每页数量
     * @param sysDict  查询参数
     */
    @GetMapping("/list")
    public Result<IPage<SysDict>> queryPageList(@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize, SysDict sysDict, HttpServletRequest req) {
        QueryWrapper<SysDict> queryWrapper = QueryGenerator.initQueryWrapper(sysDict, req.getParameterMap());
        Page<SysDict> page = new Page<>(pageNo, pageSize);
        IPage<SysDict> pageList = service.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 列表查询
     *
     * @param sysDict 查询参数
     * @apiNote 树形结构
     */
    @GetMapping("/treeList")
    public Result<List<SysDictTree>> treeList(SysDict sysDict) {
        LambdaQueryWrapper<SysDict> query = new LambdaQueryWrapper<>();
        // 构造查询条件
        String dictName = sysDict.getDictName();
        if (StrUtil.isNotEmpty(dictName)) {
            query.like(true, SysDict::getDictName, dictName);
        }
        query.orderByDesc(true, SysDict::getCreateTime);
        List<SysDict> list = service.list(query);
        List<SysDictTree> treeList = new ArrayList<>();
        for (SysDict node : list) {
            treeList.add(new SysDictTree(node));
        }
        return Result.ok(treeList);
    }

    /**
     * 获取字典数据
     *
     * @param dictCode 字典code
     * @apiNote 表字典：表名,文本字段,code字段,sql条件 举例：sys_user,realname,id,where id in ('1', '2')
     * 数据字典：字典code
     */
    @GetMapping("/getDictItems/{dictCode}")
    public Result<List<DictModel>> getDictItems(@PathVariable String dictCode) {
        List<DictModel> dictModels;
        // 是否为表字典
        if (dictCode.contains(",")) {
            List<String> params = StrUtil.split(dictCode, ",", 4, true, false);
            int paramSize = params.size();
            if (paramSize < 3) {
                return Result.error("字典Code格式不正确！");
            }
            //SQL注入校验（只限制非法串改数据库）
            SqlInjectionUtil.filterContent(params.get(0), params.get(1), params.get(2));
            if (paramSize == 4) {
                //SQL注入校验（查询条件SQL 特殊check，此方法仅供此处使用）
                SqlInjectionUtil.filterContentForTableDict(params.get(3));
                dictModels = service.queryTableDictItemsByCodeAndFilter(params.get(0), params.get(1), params.get(2), params.get(3));
            } else {
                dictModels = service.queryTableDictItemsByCode(params.get(0), params.get(1), params.get(2));
            }
        } else {
            // 数据字典
            dictModels = service.queryDictItemsByCode(dictCode);
        }
        return Result.ok(dictModels);
    }

    /**
     * 获取全部字典数据
     */
    @GetMapping("/queryAllDictItems")
    public Result<?> queryAllDictItems() {
        Map<String, List<DictModel>> res = service.queryAllDictItems();
        return Result.ok(res);
    }

    /**
     * 查询指定key对应的字典值
     *
     * @param dictCode 字典code
     * @param key      键值
     * @return 显示值
     * @apiNote 只支持数据字典
     */
    @Deprecated
    @GetMapping("/getDictText/{dictCode}/{key}")
    public Result<String> getDictText(@PathVariable String dictCode, @PathVariable String key) {
        String text = service.queryDictTextByKey(dictCode, key);
        return Result.ok(text);
    }

    /**
     * 通过关键词查询表字典
     *
     * @param dictCode 表字典code
     * @param keyword  关键词
     * @apiNote 只支持表字典
     */
    @GetMapping("/loadDict/{dictCode}")
    public Result<List<DictModel>> loadDict(@PathVariable String dictCode, @RequestParam String keyword) {
        String[] params = dictCode.split(",");
        if (params.length < 3) {
            return Result.error("字典Code格式不正确！");
        }
        String filterSql = null;
        if (params.length == 4) {
            filterSql = params[3];
        }
        List<DictModel> list = service.queryTableDictItems(params[0], params[1], params[2], keyword, filterSql);
        return Result.ok(list);
    }

    /**
     * 查询多个key对应的值
     *
     * @param dictCode 字典code
     * @param keys     键值（逗号分隔）
     * @apiNote 只支持表字典
     */
    @GetMapping("/loadDictItem/{dictCode}")
    public Result<?> loadDictItem(@PathVariable String dictCode, @RequestParam(name = "key") String keys) {
        String[] params = dictCode.split(",");
        // 兼容字典 sql filter 条件
        if (params.length < 3) {
            return Result.error("字典Code格式不正确！");
        }
        List<String> texts = service.queryTableDictByKeys(params[0], params[1], params[2], keys);
        return Result.ok(texts);
    }

    /**
     * 查询指定父id下的所有子节点
     *
     * @param pid           父id
     * @param pidField      父id对应的字段
     * @param tableName     表名
     * @param text          要显示的字段
     * @param code          要显示的键值
     * @param hasChildField 是否有子节点
     * @param condition     查询条件
     * @apiNote JTreeTable的组件接口
     */
    @GetMapping("/loadTreeData")
    public Result<List<TreeSelectModel>> loadTreeData(@RequestParam String pid, @RequestParam String pidField, @RequestParam String tableName, @RequestParam String text, @RequestParam String code, @RequestParam String hasChildField, @RequestParam String condition) {
        Map<String, String> query = null;
        if (StrUtil.isNotEmpty(condition)) {
            query = JSONUtil.toBean(condition, Map.class);
        }
        // SQL注入漏洞 sign签名校验(表名,label字段,val字段,条件)
        String dictCode = tableName + "," + text + "," + code + "," + condition;
        SqlInjectionUtil.filterContent(dictCode);
        List<TreeSelectModel> ls = service.queryTreeList(query, tableName, text, code, pidField, pid, hasChildField);
        return Result.ok(ls);
    }

    /**
     * 新增
     *
     * @param sysDict 字典内容
     */
    @AutoLog("数据字段-新增字典")
    @PostMapping("/add")
    public Result<?> add(@RequestBody SysDict sysDict) {
        service.save(sysDict);
        return Result.ok();
    }

    /**
     * 编辑
     *
     * @param sysDict 字典内容
     */
    @AutoLog("数据字段-编辑字典")
    @PutMapping("/edit")
    public Result<?> edit(@RequestBody SysDict sysDict) {
        SysDict sysdict = service.getById(sysDict.getId());
        if (sysdict != null) {
            service.updateById(sysDict);
        }
        return Result.ok();
    }

    /**
     * 删除字典
     *
     * @param id 主键id
     */
    @AutoLog("数据字段-删除字典")
    @DeleteMapping("/delete")
    @CacheEvict(value = CacheConst.SYS_DICT_CACHE, allEntries = true)
    public Result<?> delete(@RequestParam String id) {
        service.removeById(id);
        return Result.ok();
    }

    /**
     * 批量删除字典
     *
     * @param ids 主键id（多个逗号分隔）
     */
    @Deprecated
    @AutoLog("数据字段-批量删除字典")
    @DeleteMapping("/deleteBatch")
    @CacheEvict(cacheNames = CacheConst.SYS_DICT_CACHE, allEntries = true)
    public Result<?> deleteBatch(@RequestParam String ids) {
        if (StrUtil.isEmpty(ids)) {
            return Result.error("参数不识别！");
        }
        service.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok();
    }

    /**
     * 清空字典缓存
     */
    @AutoLog("数据字段-清空字典缓存")
    @GetMapping("/refreshCache")
    @CacheEvict(cacheNames = {CacheConst.SYS_DICT_CACHE, CacheConst.SYS_DEPARTS_CACHE}, allEntries = true)
    public Result<?> refreshCache() {
        return Result.ok();
    }

    /**
     * 导出数据字典为Excel
     *
     * @param sysDict 查询参数
     */
    @AutoLog("数据字段-导出数据字典为Excel")
    @GetMapping("/exportXls")
    public void exportXls(SysDict sysDict, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Step.1 组装查询条件
        QueryWrapper<SysDict> queryWrapper = QueryGenerator.initQueryWrapper(sysDict, request.getParameterMap());
        //Step.2 AutoPoi 导出Excel
        List<SysDictPage> pageList = new ArrayList<>();
        List<SysDict> sysDictList = service.list(queryWrapper);
        for (SysDict dictMain : sysDictList) {
            SysDictPage vo = new SysDictPage();
            BeanUtil.copyProperties(dictMain, vo);
            // 查询机票
            List<SysDictItem> sysDictItemList = sysDictItemService.selectItemsByMainId(dictMain.getId());
            vo.setSysDictItemList(sysDictItemList);
            pageList.add(vo);
        }
        //导出文件名称
        String date = DateUtil.format(new Date(), "yyyyMMddHHmmss");
        HttpServletUtil.addDownloadHeader(response, "字典数据-" + date + easyExcel.getExtension());
        easyExcel.export(pageList, response.getOutputStream(), SystemContextUtil.dictTranslator());
    }

    /**
     * 通过Excel导入数据字典
     */
    @AutoLog("数据字段-通过Excel导入数据字典")
    @PostMapping("/importExcel")
    public Result<?> importExcel(HttpServletRequest request) throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            try (InputStream inputStream = file.getInputStream()) {
                ImportExcel excel = new ImportExcel();
                excel.setInputStream(inputStream);
                List<SysDictPage> list = easyExcel.read(SysDictPage.class, excel, SystemContextUtil.dictTranslator());
                // 错误信息
                List<String> errorMessages = new ArrayList<>();
                int successLines = 0, errorLines = 0;
                for (int i = 0; i < list.size(); i++) {
                    SysDict po = new SysDict();
                    BeanUtil.copyProperties(list.get(i), po);
                    po.setDelFlag(CommonConst.NOT_DELETED);
                    try {
                        Integer integer = service.saveMain(po, list.get(i).getSysDictItemList());
                        if (integer > 0) {
                            successLines++;
                        } else {
                            errorLines++;
                            int lineNumber = i + 1;
                            errorMessages.add("第 " + lineNumber + " 行：字典编码已经存在，忽略导入！");
                        }
                    } catch (Exception e) {
                        errorLines++;
                        int lineNumber = i + 1;
                        errorMessages.add("第 " + lineNumber + " 行：字典编码已经存在，忽略导入！");
                    }
                }
                JSONObject returnData = JSONUtil.createObj();
                returnData.set("errorLines", errorLines);
                returnData.set("successLines", successLines);
                returnData.set("errorMessages", errorMessages);
                return Result.ok(returnData);
            }
        }
        return Result.error("文件导入失败！");
    }

    /**
     * 查询被删除的列表
     */
    @GetMapping("/deleteList")
    public Result<List<SysDict>> deleteList() {
        List<SysDict> sysDictList = this.service.queryDeleteList();
        return Result.ok(sysDictList);
    }

    /**
     * 物理删除数据字典
     *
     * @param id 主键id
     */
    @AutoLog("数据字典-物理删除数据字典")
    @DeleteMapping("/deletePhysic/{id}")
    public Result<?> deletePhysic(@PathVariable String id) {
        service.deleteOneDictPhysically(id);
        return Result.ok();
    }

    /**
     * 撤销删除
     *
     * @param id 主键id
     */
    @AutoLog("数据字典-撤销删除")
    @PutMapping("/back/{id}")
    public Result<?> back(@PathVariable String id) {
        service.updateDictDelFlag(0, id);
        return Result.ok();
    }
}
