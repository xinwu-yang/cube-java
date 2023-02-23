package org.cube.modules.system.extra.fillrules;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.cube.commons.intf.IFillRuleHandler;
import org.cube.commons.utils.YouBianCodeUtil;
import org.cube.modules.system.entity.SysCategory;
import org.cube.modules.system.mapper.SysCategoryMapper;

import java.util.List;

/**
 * 分类字典编码生成规则
 */
public class CategoryCodeRule implements IFillRuleHandler {

    public static final String ROOT_PID_VALUE = "0";

    @Override
    public Object execute(JSONObject params, String formData) {
        String categoryPid = ROOT_PID_VALUE;
        String categoryCode;
        JSONObject root = JSONUtil.parseObj(formData);
        if (StrUtil.isNotEmpty(formData)) {
            categoryPid = root.getStr("pid");
        } else if (params.size() > 0) {
            categoryPid = params.getStr("pid");
        }
        /*
         * 分成三种情况
         * 1.数据库无数据 调用YouBianCodeUtil.getNextYouBianCode(null);
         * 2.添加子节点，无兄弟元素 YouBianCodeUtil.getSubYouBianCode(parentCode,null);
         * 3.添加子节点有兄弟元素 YouBianCodeUtil.getNextYouBianCode(lastCode);
         * */
        //找同类 确定上一个最大的code值
        LambdaQueryWrapper<SysCategory> query = new LambdaQueryWrapper<>();
        query.eq(SysCategory::getPid, categoryPid);
        query.isNotNull(SysCategory::getCode);
        query.orderByDesc(SysCategory::getCode);
        SysCategoryMapper baseMapper = SpringUtil.getBean("sysCategoryMapper");
        List<SysCategory> list = baseMapper.selectList(query);
        if (list == null || list.size() == 0) {
            if (ROOT_PID_VALUE.equals(categoryPid)) {
                //情况1
                categoryCode = YouBianCodeUtil.getNextYouBianCode(null);
            } else {
                //情况2
                SysCategory parent = baseMapper.selectById(categoryPid);
                categoryCode = YouBianCodeUtil.getSubYouBianCode(parent.getCode(), null);
            }
        } else {
            //情况3
            categoryCode = YouBianCodeUtil.getNextYouBianCode(list.get(0).getCode());
        }
        return categoryCode;
    }
}
