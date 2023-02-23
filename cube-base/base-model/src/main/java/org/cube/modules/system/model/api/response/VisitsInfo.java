package org.cube.modules.system.model.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisitsInfo implements Serializable {

    /**
     * IP数量
     */
    private Long ip;

    /**
     * 年月日
     */
    private String tian;

    /**
     * 月日
     */
    private String type;

    /**
     * 游览量
     */
    private Long visit;
}
