package org.cube.modules.system.model;

import cn.hutool.core.util.StrUtil;
import org.cube.modules.system.entity.SysDepart;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 部门表 存储树结构数据的实体类
 * <p>
 *
 * @author Steve
 * @since 2019-01-22
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class SysDepartTreeModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 对应SysDepart中的id字段,前端数据树中的key
     */
    private String key;

    /**
     * 对应SysDepart中的id字段,前端数据树中的value
     */
    private String value;

    /**
     * 对应depart_name字段,前端数据树中的title
     */
    private String title;

    private boolean isLeaf;

    // 以下所有字段均与SysDepart相同
    private String id;

    private String parentId;

    private String departName;

    private String departNameEn;

    private String departNameAbbr;

    private Integer departOrder;

    private String description;

    private String orgCategory;

    private String orgType;

    private String orgCode;

    private String mobile;

    private String fax;

    private String address;

    private String memo;

    private String status;

    private String areaId;

    private String gbCode;

    private Integer delFlag;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

    private List<SysDepartTreeModel> children = new ArrayList<>();

    /**
     * 将SysDepart对象转换成SysDepartTreeModel对象
     */
    public SysDepartTreeModel(SysDepart sysDepart) {
        this.key = sysDepart.getId();
        this.value = sysDepart.getId();
        this.title = sysDepart.getDepartName();
        this.id = sysDepart.getId();
        this.parentId = sysDepart.getParentId();
        this.departName = sysDepart.getDepartName();
        this.departNameEn = sysDepart.getDepartNameEn();
        this.departNameAbbr = sysDepart.getDepartNameAbbr();
        this.departOrder = sysDepart.getDepartOrder();
        this.description = sysDepart.getDescription();
        this.orgCategory = sysDepart.getOrgCategory();
        this.orgType = sysDepart.getOrgType();
        this.orgCode = sysDepart.getOrgCode();
        this.mobile = sysDepart.getMobile();
        this.fax = sysDepart.getFax();
        this.address = sysDepart.getAddress();
        this.memo = sysDepart.getMemo();
        this.areaId = sysDepart.getAreaId() != null ? sysDepart.getAreaId().toString() : null;
        this.gbCode = sysDepart.getGbCode();
        this.status = sysDepart.getStatus();
        this.delFlag = sysDepart.getDelFlag();
        this.createBy = sysDepart.getCreateBy();
        this.createTime = sysDepart.getCreateTime();
        this.updateBy = sysDepart.getUpdateBy();
        this.updateTime = sysDepart.getUpdateTime();
    }

    public void setChildren(List<SysDepartTreeModel> children) {
        if (children == null) {
            this.isLeaf = true;
        }
        this.children = children;
    }

    /**
     * queryTreeList的子方法 ====1=====
     * 该方法是s将SysDepart类型的list集合转换成SysDepartTreeModel类型的集合
     */
    public static List<SysDepartTreeModel> toTreeList(List<SysDepart> recordList) {
        // 在该方法每请求一次,都要对全局list集合进行一次清理
        //idList.clear();
        List<DepartIdModel> idList = new ArrayList<>();
        List<SysDepartTreeModel> records = new ArrayList<>();
        for (SysDepart depart : recordList) {
            records.add(new SysDepartTreeModel(depart));
        }
        List<SysDepartTreeModel> tree = findChildren(records, idList);
        setEmptyChildrenAsNull(tree);
        return tree;
    }

    /**
     * 获取 DepartIdModel
     */
    public static List<DepartIdModel> toIdTreeList(List<SysDepart> recordList) {
        // 在该方法每请求一次,都要对全局list集合进行一次清理
        //idList.clear();
        List<DepartIdModel> idList = new ArrayList<>();
        List<SysDepartTreeModel> records = new ArrayList<>();
        for (SysDepart depart : recordList) {
            records.add(new SysDepartTreeModel(depart));
        }
        findChildren(records, idList);
        return idList;
    }

    /**
     * queryTreeList的子方法 ====2=====
     * 该方法是找到并封装顶级父类的节点到TreeList集合
     */
    private static List<SysDepartTreeModel> findChildren(List<SysDepartTreeModel> recordList, List<DepartIdModel> departIdList) {
        List<SysDepartTreeModel> treeList = new ArrayList<>();
        for (SysDepartTreeModel branch : recordList) {
            if (StrUtil.isEmpty(branch.getParentId())) {
                treeList.add(branch);
                DepartIdModel departIdModel = new DepartIdModel().convert(branch);
                departIdList.add(departIdModel);
            }
        }
        getGrandChildren(treeList, recordList, departIdList);
        return treeList;
    }

    /**
     * queryTreeList的子方法====3====
     * 该方法是找到顶级父类下的所有子节点集合并封装到TreeList集合
     */
    private static void getGrandChildren(List<SysDepartTreeModel> treeList, List<SysDepartTreeModel> recordList, List<DepartIdModel> idList) {
        for (int i = 0; i < treeList.size(); i++) {
            SysDepartTreeModel model = treeList.get(i);
            DepartIdModel idModel = idList.get(i);
            for (SysDepartTreeModel m : recordList) {
                if (m.getParentId() != null && m.getParentId().equals(model.getId())) {
                    model.getChildren().add(m);
                    DepartIdModel dim = new DepartIdModel().convert(m);
                    idModel.getChildren().add(dim);
                }
            }
            getGrandChildren(treeList.get(i).getChildren(), recordList, idList.get(i).getChildren());
        }
    }

    /**
     * queryTreeList的子方法 ====4====
     * 该方法是将子节点为空的List集合设置为Null值
     */
    private static void setEmptyChildrenAsNull(List<SysDepartTreeModel> treeList) {
        for (SysDepartTreeModel model : treeList) {
            if (model.getChildren().size() == 0) {
                model.setChildren(null);
                model.setLeaf(true);
            } else {
                setEmptyChildrenAsNull(model.getChildren());
                model.setLeaf(false);
            }
        }
    }
}
