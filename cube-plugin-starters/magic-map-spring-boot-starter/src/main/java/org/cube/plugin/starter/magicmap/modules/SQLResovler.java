package org.cube.plugin.starter.magicmap.modules;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;
import java.util.Map;

/**
 * 从XML中读取SQL
 */
@Slf4j
public class SQLResovler {

    /**
     * SQL缓存
     */
    private static final Map<String, String> SQL_CACHE = MapUtil.newHashMap();

    /**
     * 读取XML到配置
     */
    public void loadQueries(String path) {
        String xml = ResourceUtil.readUtf8Str(path);
        Document document = XmlUtil.readXML(xml);
        Element rootElement = XmlUtil.getRootElement(document);
        List<Element> elements = XmlUtil.getElements(rootElement, "query");
        for (Element element : elements) {
            String queryName = element.getAttribute("name");
            String queryXML = XmlUtil.toStr(element, CharsetUtil.UTF_8, false, true);
            String[] queryXMLData = queryXML.split("\\n");
            StrBuilder builder = StrBuilder.create();
            for (int i = 1; i < queryXMLData.length - 1; i++) {
                builder.append(queryXMLData[i].trim()).append(" ");
            }
            SQL_CACHE.put(queryName, builder.toString());
        }
        log.info("重新加载所有查询语句，加载路径：" + path);
    }

    /**
     * 通过名称获取执行SQL
     *
     * @param name SQL 名称
     * @return SQL
     */
    public String getSQL(String name) {
        return SQL_CACHE.get(name);
    }
}
