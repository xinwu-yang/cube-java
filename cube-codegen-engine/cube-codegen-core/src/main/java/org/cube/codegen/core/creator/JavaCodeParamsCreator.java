package org.cube.codegen.core.creator;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import org.cube.codegen.annotations.Form;
import org.cube.codegen.annotations.JavaPackage;
import org.cube.codegen.core.models.JavaCodeParams;
import org.cube.codegen.core.models.Table;

import java.util.List;

/**
 * 表单信息构建器（是否分组，表名，包名，实体名）
 */
public class JavaCodeParamsCreator extends AbstractCreator<JavaCodeParams> {
    private static final List<String> ENTITY_PKG_NAME = ListUtil.of("entity", "domain");

    public JavaCodeParamsCreator(Class<?> cls, Table table) {
        super(cls, table);
    }

    @Override
    public JavaCodeParams create() {
        // 分析单表参数
        JavaCodeParams javaCodeParams = new JavaCodeParams();
        String packageName = this.getCls().getPackage().getName();
        javaCodeParams.setEntityPackage(packageName);
        String[] pkgs = packageName.split("\\.");
        if (pkgs.length < 3) {
            throw new IllegalStateException("该类的类路径长度不够，请检查配置！");
        }
        // 如果是在实体的包内
        if (ENTITY_PKG_NAME.contains(pkgs[pkgs.length - 1])) {
            javaCodeParams.setModulePackage(pkgs[pkgs.length - 2]);
            javaCodeParams.setBusinessPackage(packageName.substring(0, packageName.lastIndexOf(pkgs[pkgs.length - 2]) - 1));
        } else {
            javaCodeParams.setModulePackage(pkgs[pkgs.length - 1]);
            javaCodeParams.setBusinessPackage(packageName.substring(0, packageName.lastIndexOf(pkgs[pkgs.length - 1]) - 1));
        }
        String entityName = this.getCls().getSimpleName();
        javaCodeParams.setEntityName(entityName);
        javaCodeParams.setEntityNameLower(StrUtil.subPre(entityName, 1).toLowerCase() + StrUtil.subSuf(entityName, 1));
        javaCodeParams.setDescription(this.getCls().getSimpleName());

        JavaPackage javaPackage = this.getCls().getDeclaredAnnotation(JavaPackage.class);
        if (javaPackage != null) {
            if (StrUtil.isNotEmpty(javaPackage.businessPackage())) {
                javaCodeParams.setBusinessPackage(javaPackage.businessPackage());
            }
            if (StrUtil.isNotEmpty(javaPackage.modulePackage())) {
                javaCodeParams.setModulePackage(javaPackage.modulePackage());
            }
        }

        Form form = cls.getDeclaredAnnotation(Form.class);
        if (form != null) {
            table.setGrouped(form.isGroup());
            javaCodeParams.setDescription(form.description());
            if (StrUtil.isNotEmpty(form.entityPackage())) {
                javaCodeParams.setEntityPackage(form.entityPackage());
            }
            if (StrUtil.isNotEmpty(form.businessPackage())) {
                javaCodeParams.setBusinessPackage(form.businessPackage());
            }
        }
        table.setJavaCodeParams(javaCodeParams);
        return javaCodeParams;
    }
}
