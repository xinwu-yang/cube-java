package org.cube.modules.system.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DictModel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 字典值
     */
    private String value;

    /**
     * 字典文本
     */
    private String text;
}
