package org.cube.plugin.easyexcel;

import org.cube.plugin.easyexcel.dict.IDictTranslator;
import org.cube.plugin.easyexcel.model.ImportExcel;
import org.cube.plugin.easyexcel.model.ImportReslt;

import java.io.OutputStream;
import java.util.List;

/**
 * EasyExcel 主要接口
 *
 * @author xinwuy
 * @version V2.3.x
 * @since 2022/2/16
 */
public interface EasyExcel {

    /**
     * 将Excel数据转化成Java对象集合
     *
     * @param cls            要转化的类
     * @param excel          excel
     * @param dictTranslator 字典转化接口
     * @param <T>            泛型
     * @return 数据集合
     */
    <T> List<T> read(Class<T> cls, ImportExcel excel, IDictTranslator dictTranslator);

    /**
     * 读取excel为json数据
     *
     * @param cls         自定义实体类数据
     * @param excel       excel输入
     * @param dictHandler 字典转化接口
     * @return json
     */
    String readToJson(Class<?> cls, ImportExcel excel, IDictTranslator dictHandler);

    /**
     * 读取excel为json数据
     *
     * @param excel excel输入
     * @return json字符串
     */
    String readToJson(ImportExcel excel);

    /**
     * 数据导出成Excel
     *
     * @param list           要导出的数据
     * @param outputStream   输出流
     * @param dictTranslator 字典转化接口
     */
    void export(List<?> list, OutputStream outputStream, IDictTranslator dictTranslator);

    /**
     * 将Excel数据导入
     *
     * @param cls            要转化的类
     * @param excel          excel
     * @param dictTranslator 字典转化接口
     * @param <T>            泛型
     */
    <T> ImportReslt<T> importExcel(Class<T> cls, ImportExcel excel, IDictTranslator dictTranslator);


    /**
     * 是否支持 XLSX
     *
     * @return 是否支持
     */
    boolean supportXlsx();

    /**
     * 获取文件后缀
     *
     * @return 后缀名
     * @apiNote 带. 如.xls
     */
    String getExtension();
}
