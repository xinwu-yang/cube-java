package org.cube.plugin.starter.magicmap.clustering.grid;

import org.cube.plugin.starter.magicmap.clustering.AbstractClusterAlgorithm;
import org.cube.plugin.starter.magicmap.clustering.ICluster;
import org.cube.plugin.starter.magicmap.clustering.IClusterAlgorithm;
import org.cube.plugin.starter.magicmap.clustering.IClusterItem;
import org.cube.plugin.starter.magicmap.clustering.baidu.projection.Bounds;
import org.cube.plugin.starter.magicmap.clustering.model.ClusterContext;
import org.cube.plugin.starter.magicmap.clustering.model.LatLng;
import org.cube.plugin.starter.magicmap.clustering.model.StaticCluster;

import java.util.*;

public class GridBasedAlgorithm<T extends IClusterItem> extends AbstractClusterAlgorithm<T> implements IClusterAlgorithm<T> {
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
        double zoom = ctx.getZoom();
        LatLng southWest = ctx.getSouthWest();
        LatLng northEast = ctx.getNorthEast();

        Bounds bounds = new Bounds(southWest.getLongitude(), northEast.getLongitude(), southWest.getLatitude(), northEast.getLatitude());

        int zoomLevel = (int)zoom;
        double gridWidh = (northEast.getLongitude() - southWest.getLongitude()) / zoomLevel;
        double gridHeight = (northEast.getLatitude() - southWest.getLatitude()) / zoomLevel;

        ArrayList<T>[][] gridList = new ArrayList[zoomLevel][zoomLevel];
        /**
         * 遍历每个点，按经纬度坐标划分到方格中
         */
        for (T item : items) {
            if (bounds.contains(item.getPosition().getLongitude(), item.getPosition().getLatitude())) {
                // 如果该点在显示范围内则参与聚合
                int x = (int) ((item.getPosition().getLongitude() - southWest.getLongitude()) / gridWidh);
                int y = (int) ((item.getPosition().getLatitude() - southWest.getLatitude()) / gridHeight);
                ArrayList<T> list = gridList[x][y];
                if (null == list) {
                    list = new ArrayList<>();
                    gridList[x][y] = list;
                }

                list.add(item);
            }
        }

        /**
         * 处理聚合结果，计算中心质点坐标
         */
        Set<ICluster<T>> set = new HashSet<>();
        for (int i = 0; i < zoomLevel; i++) {
            for (int j = 0; j < zoomLevel; j++) {
                ArrayList<T> list = gridList[i][j];
                if ((null != list) && (list.size() > 0)) {
                    LatLng latLng = new LatLng();
                    double lat = 0.0D;
                    double lng = 0.0D;
                    StaticCluster<T> cluster = new StaticCluster<>(latLng);
                    for (T item : list) {
                        cluster.add(item);
                        lat += item.getPosition().getLatitude();
                        lng += item.getPosition().getLongitude();
                    }
                    lat = lat / list.size();
                    lng = lng / list.size();
                    latLng.setLatitude(lat);
                    latLng.setLongitude(lng);
                    set.add(cluster);
                }
            }
        }

        return set;
    }


    @Override
    public Collection<T> getItems() {
        return items;
    }
}
