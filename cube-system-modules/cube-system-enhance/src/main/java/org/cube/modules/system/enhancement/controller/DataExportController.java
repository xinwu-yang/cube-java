package org.cube.modules.system.enhancement.controller;

import org.cube.commons.utils.web.HttpServletUtil;
import org.cube.modules.system.enhancement.model.IdParams;
import org.cube.modules.system.enhancement.service.IDataExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 数据导出成SQL
 *
 * @author xinwuy
 */
@Tag(name = "数据导出成SQL")
@Controller
@RequestMapping("/sys/export")
public class DataExportController {
    @Autowired
    private IDataExportService dataExportService;

    /**
     * 导出基础数据
     *
     * @param idParams 要导出的id
     */
    @PostMapping("sql/insert")
    @Operation(summary = "导出基础数据")
    public void exportSQL(@RequestBody IdParams idParams, HttpServletResponse response) throws IOException {
        String fileName = "SQLExport-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".sql";
        HttpServletUtil.addDownloadHeader(response, fileName);
        try (OutputStream os = response.getOutputStream()) {
            List<String> sqlData = dataExportService.exportInserts(idParams.getPermissionIds(), idParams.getUserIds(), idParams.getRoleIds(), idParams.getDictIds());
            for (String sql : sqlData) {
                os.write(sql.concat("\n").getBytes(StandardCharsets.UTF_8));
            }
        }
    }
}
