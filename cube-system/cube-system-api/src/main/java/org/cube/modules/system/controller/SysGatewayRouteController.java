package org.cube.modules.system.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.cube.commons.base.CubeController;
import org.cube.commons.base.Result;
import org.cube.modules.system.entity.SysGatewayRoute;
import org.cube.modules.system.service.ISysGatewayRouteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 网关路由管理
 *
 * @author xinwuy
 * @version V1.0.0
 * @since 2020-05-26
 */
@Slf4j
@Tag(name = "网关路由管理相关接口")
@RestController
@RequestMapping("/sys/gateway/route")
public class SysGatewayRouteController extends CubeController<SysGatewayRoute, ISysGatewayRouteService> {

    /**
     * 更新所有路由配置
     *
     * @param json 参数
     */
    @PostMapping("/updateAll")
    public Result<?> updateAll(@RequestBody JsonNode json) {
        service.updateAll(json);
        return Result.ok();
    }

    /**
     * 查询路由列表
     */
    @GetMapping("/list")
    public Result<?> queryPageList() {
        LambdaQueryWrapper<SysGatewayRoute> query = new LambdaQueryWrapper<>();
        List<SysGatewayRoute> ls = service.list(query);
        JSONArray array = JSONUtil.createArray();
        for (SysGatewayRoute rt : ls) {
            JSONObject obj = JSONUtil.parseObj(rt);
            if (StrUtil.isNotEmpty(rt.getPredicates())) {
                obj.set("predicates", JSONUtil.parseArray(rt.getPredicates()));
            }
            if (StrUtil.isNotEmpty(rt.getFilters())) {
                obj.set("filters", JSONUtil.parseArray(rt.getFilters()));
            }
            array.add(obj);
        }
        return Result.ok(array);
    }

    /**
     * 清除路由缓存
     */
    @GetMapping("/clearRedis")
    public Result<?> clearRedis() {
        service.clearRedis();
        return Result.ok();
    }

    /**
     * 通过id删除
     *
     * @param id 主键id
     */
    @DeleteMapping("/delete")
    public Result<?> delete(@RequestParam String id) {
        service.deleteById(id);
        return Result.ok();
    }
}
