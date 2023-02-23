package org.cube.plugin.easyexcel.model;

import lombok.Data;

import java.io.InputStream;
import java.io.Serializable;

/**
 * 描述
 *
 * @author xinwuy
 * @version V1.0.0
 * @since 2022/2/16
 */
@Data
public class ImportExcel implements Serializable {

    /**
     * 读取的表格编号
     */
    private int sheetIndex = 0;

    /**
     * 起始行号
     */
    private int startRow = 1;

    private int startLine = 0;

    /**
     * 是否启用字典翻译
     */
    private boolean enableDictTranslate = true;

    /**
     * Excel输入流
     */
    private InputStream inputStream;
}
