package org.cube.plugin.starter.magicmap.modules;

import org.cube.plugin.starter.magicmap.spring.boot.autoconfigure.MagicMapProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ssssssss.magicapi.core.annotation.MagicModule;
import org.ssssssss.script.annotation.Comment;

/**
 * 用于选择要执行的SQL
 */
@Component
@MagicModule("sql")
public class SQLModule {

    @Autowired
    private MagicMapProperties magicMapProperties;
    @Autowired
    private SQLResovler sqlResovler;

    @Comment("根据名称得到对应的SQL")
    public String select(@Comment("SQL名称") String name) {
        return sqlResovler.getSQL(name);
    }

    @Comment("重新加载所有查询语句")
    public void reloadQueries() {
        sqlResovler.loadQueries(magicMapProperties.getQueryDir());
    }
}
