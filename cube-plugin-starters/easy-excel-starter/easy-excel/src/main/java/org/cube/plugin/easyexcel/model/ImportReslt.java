package org.cube.plugin.easyexcel.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * ImportReslt
 *
 * @author gjw
 * @version 1.0
 * @description
 * @date 2022/11/10 14:59
 */
@Data
public class ImportReslt<T> {
    private List<T> data = new ArrayList<>();
    private ImportMessage importMessage;
    private Boolean successful =true;


}
