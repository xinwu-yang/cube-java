package org.cube.modules.system.model.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LogInfoResponse {

    /**
     * 总访问量
     */
    private long totalVisitCount;

    /**
     * 今日访问量
     */
    private long todayVisitCount;

    /**
     * 今日访问IP数量
     */
    private long todayIp;
}
