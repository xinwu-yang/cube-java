package org.cube.plugin.easyexcel.model;

import lombok.Data;

/**
 * ErrorMessage
 *
 * @author gjw
 * @version 1.0
 * @description
 * @date 2022/11/10 09:50
 */
@Data
public class ImportMessage {
    private Integer row;
    private Integer index;
    private String msg;
}
