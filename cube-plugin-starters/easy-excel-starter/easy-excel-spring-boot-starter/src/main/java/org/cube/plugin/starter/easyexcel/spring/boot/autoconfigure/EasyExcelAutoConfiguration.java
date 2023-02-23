package org.cube.plugin.starter.easyexcel.spring.boot.autoconfigure;

import org.cube.plugin.easyexcel.EasyExcel;
import org.cube.plugin.easyexcel.impl.POIEasyExcel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class EasyExcelAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(name = "org.apache.poi.xssf.binary.XSSFBParser")
    public EasyExcel xssfEasyExcel() {
        log.info("Initializing EasyExcel with XSSF.");
        return new POIEasyExcel(true);
    }

    @Bean
    @ConditionalOnMissingBean
    public EasyExcel hssfEasyExcel() {
        log.info("Initializing EasyExcel with HSSF.");
        return new POIEasyExcel(false);
    }
}
