package org.cube.modules.system.monitor.controller;

import org.cube.commons.base.Result;
import org.cube.modules.system.monitor.domain.RedisInfo;
import org.cube.modules.system.monitor.service.RedisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统监控
 *
 * @author 杨欣武
 */
@Slf4j
@Tag(name = "系统监控")
@RestController
@RequestMapping("/sys/actuator/redis")
public class ActuatorRedisController {

    @Autowired
    private RedisService redisService;

    /**
     * Redis详细信息
     */
    @GetMapping("/info")
    @Operation(summary = "Redis详细信息")
    public Result<List<RedisInfo>> getRedisInfo() throws Exception {
        List<RedisInfo> infoList = this.redisService.getRedisInfo();
        return Result.ok(infoList);
    }

    /**
     * 获取Redis键的数量
     */
    @GetMapping("/keysSize")
    @Operation(summary = "获取Redis键的数量")
    public Map<String, Object> getKeysSize() throws Exception {
        return redisService.getKeysSize();
    }

    /**
     * 获取内存信息
     */
    @GetMapping("/memoryInfo")
    @Operation(summary = "获取内存信息")
    public Map<String, Object> getMemoryInfo() throws Exception {
        return redisService.getMemoryInfo();
    }

    /**
     * 获取磁盘信息
     *
     * @apiNote 每个磁盘的详细信息
     */
    @Operation(summary = "获取磁盘信息")
    @GetMapping("/queryDiskInfo")
    public Result<?> queryDiskInfo() {
        // 当前文件系统类
        FileSystemView fsv = FileSystemView.getFileSystemView();
        // 列出所有windows 磁盘
        File[] fs = File.listRoots();
        log.info("查询磁盘信息:" + fs.length + "个");
        List<Map<String, Object>> list = new ArrayList<>();
        for (File f : fs) {
            if (f.getTotalSpace() == 0) {
                continue;
            }
            Map<String, Object> map = new HashMap<>(4);
            map.put("name", fsv.getSystemDisplayName(f));
            map.put("max", f.getTotalSpace());
            map.put("rest", f.getFreeSpace());
            map.put("restPPT", (f.getTotalSpace() - f.getFreeSpace()) * 100 / f.getTotalSpace());
            list.add(map);
        }
        return Result.ok(list);
    }
}
