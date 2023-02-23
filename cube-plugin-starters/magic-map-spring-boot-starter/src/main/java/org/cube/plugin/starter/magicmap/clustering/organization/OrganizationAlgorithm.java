package org.cube.plugin.starter.magicmap.clustering.organization;

import cn.hutool.core.util.ReflectUtil;
import org.cube.plugin.starter.magicmap.clustering.AbstractClusterAlgorithm;
import org.cube.plugin.starter.magicmap.clustering.ICluster;
import org.cube.plugin.starter.magicmap.clustering.IClusterAlgorithm;
import org.cube.plugin.starter.magicmap.clustering.IClusterItem;
import org.cube.plugin.starter.magicmap.clustering.model.ClusterContext;
import org.cube.plugin.starter.magicmap.clustering.model.LatLng;
import org.cube.plugin.starter.magicmap.clustering.model.StaticCluster;
import org.cube.plugin.starter.magicmap.model.MapPoint;

import java.util.*;

public class OrganizationAlgorithm <T extends IClusterItem> extends AbstractClusterAlgorithm<T> implements IClusterAlgorithm<T> {
    private final String FIELD_EXTRA = "extra.";

    protected List<T> items = new ArrayList<>();

    @Override
    public void addItem(T item) {
        items.add(item);
    }

    @Override
    public void addItems(Collection<T> items) {
        this.items.addAll(items);
    }

    @Override
    public void clearItems() {
        items = null;
    }

    @Override
    public void removeItem(T item) {
        throw new UnsupportedOperationException("GridBasedAlgorithm.remove not implemented");
    }

    @Override
    public Set<? extends ICluster<T>> getClusters(ClusterContext ctx) {
        List<String> clusterFields = ctx.getClusterFields();

        /**
         * 遍历每个点，按聚合字段进行聚合
         */
        Map<String, ICluster<T>> map = new HashMap<>();
        for (T item : items) {
            String clusterName = "";
            for (String field : clusterFields) {
                String fieldName = field;
                String fieldValue = null;
                Object obj = null;
                if (field.startsWith(FIELD_EXTRA)) {
                    fieldName = field.substring(FIELD_EXTRA.length());
                    obj = ((MapPoint)item.getObject()).getExtra().getObj(fieldName);
                } else {
                    obj = ReflectUtil.getFieldValue(item.getObject(), fieldName);
                }

                if (null != obj) {
                    fieldValue = obj.toString();
                } else {
                    fieldValue = "";
                }

                clusterName += fieldValue;
            }

            ICluster<T> cluster = map.get(clusterName);
            if (null == cluster) {
                LatLng latLng = new LatLng(0.0, 0.0);
                cluster = new StaticCluster<>(latLng);
                ((StaticCluster)cluster).setName(clusterName);
                map.put(clusterName, cluster);
            }

            cluster.getItems().add(item);
        }

        Set<? extends ICluster<T>> set = new HashSet<>(map.values());

        return set;
    }

    @Override
    public Collection<T> getItems() {
        return items;
    }
}
