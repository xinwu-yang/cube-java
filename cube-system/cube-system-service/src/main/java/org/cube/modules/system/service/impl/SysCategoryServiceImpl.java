package org.cube.modules.system.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.cube.commons.exception.CubeAppException;
import org.cube.commons.utils.FillRuleUtil;
import org.cube.modules.system.entity.SysCategory;
import org.cube.modules.system.mapper.SysCategoryMapper;
import org.cube.modules.system.model.TreeSelectModel;
import org.cube.modules.system.service.ISysCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysCategoryServiceImpl extends ServiceImpl<SysCategoryMapper, SysCategory> implements ISysCategoryService {

    @Override
    public void addSysCategory(SysCategory sysCategory) {
        String categoryCode;
        Long categoryPid = ISysCategoryService.ROOT_PID_VALUE;
        if (sysCategory.getPid() != null) {
            categoryPid = sysCategory.getPid();
            //PID 不是根节点 说明需要设置父节点 hasChild 为1
            if (!ISysCategoryService.ROOT_PID_VALUE.equals(categoryPid)) {
                SysCategory parent = baseMapper.selectById(categoryPid);
                if (!"1".equals(parent.getHasChild())) {
                    parent.setHasChild("1");
                    baseMapper.updateById(parent);
                }
            }
        }
        //update-begin--Author:baihailong  Date:20191209 for：分类字典编码规则生成器做成公用配置
        JSONObject formData = JSONUtil.createObj();
        formData.set("pid", categoryPid);
        categoryCode = (String) FillRuleUtil.executeRule("category_code_rule", formData.toString());
        //update-end--Author:baihailong  Date:20191209 for：分类字典编码规则生成器做成公用配置
        sysCategory.setCode(categoryCode);
        sysCategory.setPid(categoryPid);
        baseMapper.insert(sysCategory);
    }

    @Override
    public void updateSysCategory(SysCategory sysCategory) {
        if (sysCategory.getPid() != null) {
            sysCategory.setPid(ISysCategoryService.ROOT_PID_VALUE);
        } else {
            //如果当前节点父ID不为空 则设置父节点的hasChild 为1
            SysCategory parent = baseMapper.selectById(sysCategory.getPid());
            if (parent != null && !"1".equals(parent.getHasChild())) {
                parent.setHasChild("1");
                baseMapper.updateById(parent);
            }
        }
        baseMapper.updateById(sysCategory);
    }

    @Override
    public List<TreeSelectModel> queryListByCode(String pcode) throws CubeAppException {
        Long pid = ROOT_PID_VALUE;
        if (StrUtil.isNotEmpty(pcode)) {
            List<SysCategory> list = baseMapper.selectList(new LambdaQueryWrapper<SysCategory>().eq(SysCategory::getCode, pcode));
            if (list == null || list.size() == 0) {
                throw new CubeAppException("该编码【" + pcode + "】不存在，请核实!");
            }
            if (list.size() > 1) {
                throw new CubeAppException("该编码【" + pcode + "】存在多个，请核实!");
            }
            pid = list.get(0).getId();
        }
        return baseMapper.queryListByPid(pid, null);
    }

    @Override
    public List<TreeSelectModel> queryListByPid(Long pid) {
        if (pid == null) {
            pid = ROOT_PID_VALUE;
        }
        return baseMapper.queryListByPid(pid, null);
    }

    @Override
    public Long queryIdByCode(String code) {
        return baseMapper.queryIdByCode(code);
    }

    @Override
    public List<TreeSelectModel> loadTreeData(Long pid, String pcode, String condition) {
        if (pid == null) {
            if (StrUtil.isEmpty(pcode)) {
                throw new CubeAppException("pcode参数有误！");
            }
            if ("0".equals(pcode)) {
                pid = ISysCategoryService.ROOT_PID_VALUE;
            } else {
                pid = baseMapper.queryIdByCode(pcode);
            }
            if (pid == null) {
                throw new CubeAppException("pid参数有误！");
            }
        }
        JSONObject query = null;
        if (StrUtil.isNotEmpty(condition)) {
            query = JSONUtil.parseObj(condition);
        }
        return baseMapper.queryListByPid(pid, query);
    }
}
