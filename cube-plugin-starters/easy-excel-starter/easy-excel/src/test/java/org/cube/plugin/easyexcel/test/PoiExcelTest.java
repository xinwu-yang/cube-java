package org.cube.plugin.easyexcel.test;

import cn.hutool.core.date.DateUtil;
import org.cube.plugin.easyexcel.EasyExcel;
import org.cube.plugin.easyexcel.impl.POIEasyExcel;
import org.cube.plugin.easyexcel.model.ImportExcel;
import org.cube.plugin.easyexcel.model.ImportMessage;
import org.cube.plugin.easyexcel.model.ImportReslt;
import org.junit.Before;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 描述
 *
 * @author xinwuy
 * @version V2.3.x
 * @since 2022/2/18
 */
public class PoiExcelTest {

    private EasyExcel easyExcel;

    @Before
    public void before() {
        easyExcel = new POIEasyExcel(true);
    }

    @Test
    public void read() throws IOException {
        ImportExcel excel = new ImportExcel();
        excel.setInputStream(Files.newInputStream(Paths.get("/Users/guojianwei/work/testMMM/kafka-out.xls")));
        List<Glances> glancesList = easyExcel.read(Glances.class, excel, null);
        glancesList.forEach(glances -> System.out.println(glances.getId() + " | " + DateUtil.format(glances.getDate(), "yyyy-MM-dd HH:mm:ss") + " | " + glances.getIp() + " | " + glances.getKfkIn()));
    }

    @Test
    public void importExcel() throws IOException {
        ImportExcel excel = new ImportExcel();
        excel.setInputStream(Files.newInputStream(Paths.get("/Users/guojianwei/work/testMMM/kafka-out.xls")));
        ImportReslt<Glances> glancesImportReslt = easyExcel.importExcel(Glances.class, excel, null);
        Boolean successful = glancesImportReslt.getSuccessful();
        List<Glances> data = glancesImportReslt.getData();
        ImportMessage importMessage = glancesImportReslt.getImportMessage();
    }
    @Test
    public void readToJson() throws IOException {
        ImportExcel excel = new ImportExcel();
        excel.setInputStream(Files.newInputStream(Paths.get("C:\\Users\\Yousinnmu\\Desktop\\kafka-out.xls")));
        String data = easyExcel.readToJson(Glances.class, excel, null);
        System.out.println(data);
    }

    @Test
    public void readToJsonNoClass() throws IOException {
        ImportExcel excel = new ImportExcel();
        excel.setInputStream(Files.newInputStream(Paths.get("C:\\Users\\Yousinnmu\\Desktop\\kafka-out.xlsx")));
        String data = easyExcel.readToJson(excel);
        System.out.println(data);
    }

    @Test
    public void export() {
        List<Glances> glancesList = new ArrayList<>();
        glancesList.add(new Glances(1L, new Date(), "25.30.15.85", 1234L));
        glancesList.add(new Glances(1L, new Date(), "25.30.15.85", 1234L));
        glancesList.add(new Glances(1L, new Date(), "25.30.15.85", 1234L));
        glancesList.add(new Glances(1L, new Date(), "25.30.15.85", 1234L));
        glancesList.add(new Glances(1L, new Date(), "25.30.15.85", 1234L));
        glancesList.add(new Glances(1L, new Date(), "25.30.15.85", 1234L));
        glancesList.add(new Glances(1L, new Date(), "25.30.15.85", 1234L));
        glancesList.add(new Glances(1L, new Date(), "25.30.15.85", 1234L));
        glancesList.add(new Glances(1L, new Date(), "25.30.15.85", 1234L));
        glancesList.add(new Glances(1L, new Date(), "25.30.15.85", 1234L));
        glancesList.add(new Glances(1L, new Date(), "25.30.15.85", 1234L));
        glancesList.add(new Glances(1L, new Date(), "25.30.15.85", 1234L));
        glancesList.add(new Glances(1L, new Date(), "25.30.15.85", 1234L));
        glancesList.add(new Glances(1L, new Date(), "25.30.15.85", 1234L));
        try (FileOutputStream fileOutputStream = new FileOutputStream("/Users/guojianwei/work/testMMM/kafka-out.xls")) {
            easyExcel.export(glancesList, fileOutputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
