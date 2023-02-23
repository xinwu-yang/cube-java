package org.cube.plugin.sqlexport.test;

import org.cube.plugin.sqlexport.SQLExporter;
import org.cube.plugin.sqlexport.mysql.MySQLExporter;
import org.cube.plugin.sqlexport.utils.NameStrategy;
import org.junit.Test;

import java.util.Date;

public class MySQLExporterTest {

    @Test
    public void exportInsert() {
        SysArea sysArea = new SysArea();
        sysArea.setId(1L);
        sysArea.setName("成都市");
        sysArea.setParentId(0L);
        sysArea.setPostCode("6100041");
        sysArea.setGbCode("GB2312");
        sysArea.setLevel(1);
        sysArea.setPath("A01");
        sysArea.setHasChildren(false);
        sysArea.setDelFlag(0);
        sysArea.setCreateTime(new Date());

        SQLExporter exporter = new MySQLExporter();
        String sql = exporter.insert(sysArea);
        System.out.println(sql);
    }

    @Test
    public void nameConvert() {
        String tableName = "SysUserTestMama";
        System.out.println(NameStrategy.toUnderline(tableName));
    }

    @Test
    public void exportDelete() {
        SysArea sysArea = new SysArea();
        sysArea.setId(1L);
        sysArea.setName("成都市");
        sysArea.setParentId(0L);
        sysArea.setPostCode("6100041");
        sysArea.setGbCode("GB2312");
        sysArea.setLevel(1);
        sysArea.setPath("A01");
        sysArea.setHasChildren(false);
        sysArea.setDelFlag(0);
        sysArea.setCreateTime(new Date());

        SQLExporter exporter = new MySQLExporter();
        String sql = exporter.delete(sysArea);
        System.out.println(sql);
    }
}
