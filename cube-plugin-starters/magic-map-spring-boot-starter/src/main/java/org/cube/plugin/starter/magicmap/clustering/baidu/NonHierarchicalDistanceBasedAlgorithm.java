package org.cube.plugin.starter.magicmap.clustering.baidu;

import org.cube.plugin.starter.magicmap.clustering.AbstractClusterAlgorithm;
import org.cube.plugin.starter.magicmap.clustering.ICluster;
import org.cube.plugin.starter.magicmap.clustering.IClusterAlgorithm;
import org.cube.plugin.starter.magicmap.clustering.IClusterItem;
import org.cube.plugin.starter.magicmap.clustering.baidu.projection.Bounds;
import org.cube.plugin.starter.magicmap.clustering.baidu.projection.Point;
import org.cube.plugin.starter.magicmap.clustering.baidu.projection.SphericalMercatorProjection;
import org.cube.plugin.starter.magicmap.clustering.baidu.quadtree.PointQuadTree;
import org.cube.plugin.starter.magicmap.clustering.model.ClusterContext;
import org.cube.plugin.starter.magicmap.clustering.model.LatLng;
import org.cube.plugin.starter.magicmap.clustering.model.StaticCluster;

import java.util.*;

/**
 * 百度地图采用了和Google一样的方格+距离的算法
 * 即把每一个扩展成一个矩形区域，区域之间只要有重叠，那就可以聚合在一起，然后计算点到聚合点的距离，聚合到最近的聚合点
 * @param <T>
 */
public class NonHierarchicalDistanceBasedAlgorithm<T extends IClusterItem> extends AbstractClusterAlgorithm<T> implements IClusterAlgorithm<T> {
    public static final int MAX_DISTANCE_AT_ZOOM = 200;

    private final Collection<QuadItem<T>> mItems = new ArrayList<QuadItem<T>>();
    private final PointQuadTree<QuadItem<T>> mQuadTree = new PointQuadTree<QuadItem<T>>(0, 1, 0, 1);
    private static final SphericalMercatorProjection PROJECTION = new SphericalMercatorProjection(1);
    private Bounds bounds;

    public NonHierarchicalDistanceBasedAlgorithm(LatLng southWest, LatLng northEast) {
        if ((null != southWest) && (null != northEast)) {
            bounds = new Bounds(southWest.getLongitude(), northEast.getLongitude(), southWest.getLatitude(), northEast.getLatitude());
        }
    }

    @Override
    public void addItem(T item) {
        // 如果指定了可视化范围则忽略不在可视范围的点
        if ((null != bounds) && !bounds.contains(item.getPosition().getLongitude(), item.getPosition().getLatitude())) {
            return;
        }

        final QuadItem<T> quadItem = new QuadItem<T>(item);
        mItems.add(quadItem);
        mQuadTree.add(quadItem);
    }

    @Override
    public void addItems(Collection<T> items) {
        for (T item : items) {
            addItem(item);
        }
    }

    @Override
    public void clearItems() {
        mItems.clear();
        mQuadTree.clear();
    }

    @Override
    public void removeItem(T item) {
        throw new UnsupportedOperationException("NonHierarchicalDistanceBasedAlgorithm.remove not implemented");
    }

    @Override
    public Set<? extends ICluster<T>> getClusters(ClusterContext ctx) {
        double zoom = ctx.getZoom();
        final int discreteZoom = (int) zoom;
        final double zoomSpecificSpan = MAX_DISTANCE_AT_ZOOM / Math.pow(2, discreteZoom) / 256;

        final Set<QuadItem<T>> visitedCandidates = new HashSet<QuadItem<T>>();
        final Set<ICluster<T>> results = new HashSet<ICluster<T>>();
        final Map<QuadItem<T>, Double> distanceToCluster = new HashMap<QuadItem<T>, Double>();
        final Map<QuadItem<T>, StaticCluster<T>> itemToCluster = new HashMap<QuadItem<T>, StaticCluster<T>>();

        for (QuadItem<T> candidate : mItems) {
            if (visitedCandidates.contains(candidate)) {
                /**
                 * 如果侯选点已经是其它聚合中的点
                 */
                continue;
            }

            Bounds searchBounds = createBoundsFromSpan(candidate.getPoint(), zoomSpecificSpan);
            Collection<QuadItem<T>> clusterItems;
            // search 某边界范围内的clusterItems
            clusterItems = mQuadTree.search(searchBounds);
            if (clusterItems.size() == 1) {
                /**
                 * 如果范围内只有一个点，不需要聚合
                 */
                results.add(candidate);
                visitedCandidates.add(candidate);
                distanceToCluster.put(candidate, 0d);
                continue;
            }
            StaticCluster<T> cluster = new StaticCluster<T>(candidate.mClusterItem.getPosition());
            results.add(cluster);

            for (QuadItem<T> clusterItem : clusterItems) {
                Double existingDistance = distanceToCluster.get(clusterItem);
                double distance = distanceSquared(clusterItem.getPoint(), candidate.getPoint());
                if (existingDistance != null) {
                    /**
                     * 如果该点已经属于其它聚合，计算距离，看它离哪个更近
                     */
                    if (existingDistance < distance) {
                        continue;
                    }
                    /**
                     * 把它移动到更近的聚合
                     */
                    itemToCluster.get(clusterItem).remove(clusterItem.mClusterItem);
                }
                distanceToCluster.put(clusterItem, distance);
                cluster.add(clusterItem.mClusterItem);
                itemToCluster.put(clusterItem, cluster);
            }
            visitedCandidates.addAll(clusterItems);
        }

        return results;
    }

    @Override
    public Collection<T> getItems() {
        final List<T> items = new ArrayList<T>();
        for (QuadItem<T> quadItem : mItems) {
            items.add(quadItem.mClusterItem);
        }

        return items;
    }

    /**
     * 计算两点间的距离
     * @param a
     * @param b
     * @return
     */
    private double distanceSquared(Point a, Point b) {
        return (a.getX() - b.getX()) * (a.getX() - b.getX()) + (a.getY() - b.getY()) * (a.getY() - b.getY());
    }

    /**
     * 扩充一个点成为一个边界区域
     * @param p
     * @param span
     * @return
     */
    private Bounds createBoundsFromSpan(Point p, double span) {
        // TODO: Use a span that takes into account the visual size of the marker, not just its
        // LatLng.
        double halfSpan = span / 2;
        return new Bounds(
                p.getX() - halfSpan, p.getX() + halfSpan,
                p.getY() - halfSpan, p.getY() + halfSpan);
    }

    public static class QuadItem<T extends IClusterItem> implements PointQuadTree.Item, ICluster<T> {
        private final T mClusterItem;
        private final Point mPoint;
        private final LatLng mPosition;
        private Set<T> singletonSet;

        private QuadItem(T item) {
            mClusterItem = item;
            mPosition = item.getPosition();
            mPoint = PROJECTION.toPoint(mPosition);
            singletonSet = Collections.singleton(mClusterItem);
        }

        @Override
        public Point getPoint() {
            return mPoint;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        @Override
        public Set<T> getItems() {
            return singletonSet;
        }

        @Override
        public int getSize() {
            return 1;
        }

        @Override
        public String getName() {
            return null;
        }
    }
}
