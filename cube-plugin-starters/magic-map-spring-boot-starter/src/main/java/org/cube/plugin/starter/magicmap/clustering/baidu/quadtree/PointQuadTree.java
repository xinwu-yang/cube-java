package org.cube.plugin.starter.magicmap.clustering.baidu.quadtree;

import org.cube.plugin.starter.magicmap.clustering.baidu.projection.Bounds;
import org.cube.plugin.starter.magicmap.clustering.baidu.projection.Point;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PointQuadTree<T extends PointQuadTree.Item> {
    /**
     * 本四分树的边界
     */
    private final Bounds mBounds;

    /**
     * 本四分树的深度
     */
    private final int mDepth;

    /**
     * 在拆分前保存的最大元素数量
     */
    private static final int MAX_ELEMENTS = 50;

    /**
     * The elements inside this quad, if any.
     */
    private List<T> mItems;

    /**
     * 最大深度
     */
    private static final int MAX_DEPTH = 40;

    /**
     * 子树
     */
    private List<PointQuadTree<T>> mChildren = null;

    /**
     * 以指定边界创建一个新的四分树
     *
     * @param minX
     * @param maxX
     * @param minY
     * @param maxY
     */
    public PointQuadTree(double minX, double maxX, double minY, double maxY) {
        this(new Bounds(minX, maxX, minY, maxY));
    }

    public PointQuadTree(Bounds bounds) {
        this(bounds, 0);
    }

    private PointQuadTree(double minX, double maxX, double minY, double maxY, int depth) {
        this(new Bounds(minX, maxX, minY, maxY), depth);
    }

    private PointQuadTree(Bounds bounds, int depth) {
        mBounds = bounds;
        mDepth = depth;
    }

    /**
     * 插入一个元素
     */
    public void add(T item) {
        Point point = item.getPoint();
        // 这里不做边界控制判断，否则可能导致出现空的聚合
        if (this.mBounds.contains(point.getX(), point.getY())) {
            insert(point.getX(), point.getY(), item);
        } else {
            System.out.println("bounds on contains point:" + point.getX() + "," + point.getY());
        }
    }

    private void insert(double x, double y, T item) {
        if (this.mChildren != null) {
            // 四个区域进行控制
            if (y < mBounds.midY) {
                if (x < mBounds.midX) { // top left
                    mChildren.get(0).insert(x, y, item);
                } else { // top right
                    mChildren.get(1).insert(x, y, item);
                }
            } else {
                if (x < mBounds.midX) { // bottom left
                    mChildren.get(2).insert(x, y, item);
                } else {
                    mChildren.get(3).insert(x, y, item);
                }
            }
            return;
        }
        if (mItems == null) {
            mItems = new ArrayList<T>();
        }
        mItems.add(item);
        if (mItems.size() > MAX_ELEMENTS && mDepth < MAX_DEPTH) {
            split();
        }
    }

    /**
     * 拆分子树
     */
    private void split() {
        mChildren = new ArrayList<PointQuadTree<T>>(4);
        mChildren.add(new PointQuadTree<T>(mBounds.minX, mBounds.midX, mBounds.minY, mBounds.midY, mDepth + 1));
        mChildren.add(new PointQuadTree<T>(mBounds.midX, mBounds.maxX, mBounds.minY, mBounds.midY, mDepth + 1));
        mChildren.add(new PointQuadTree<T>(mBounds.minX, mBounds.midX, mBounds.midY, mBounds.maxY, mDepth + 1));
        mChildren.add(new PointQuadTree<T>(mBounds.midX, mBounds.maxX, mBounds.midY, mBounds.maxY, mDepth + 1));

        List<T> items = mItems;
        mItems = null;

        for (T item : items) {
            // re-insert items into child quads.
            insert(item.getPoint().getX(), item.getPoint().getY(), item);
        }
    }

    /**
     * Remove the given item from the set.
     *
     * @return whether the item was removed.
     */
    public boolean remove(T item) {
        Point point = item.getPoint();
        if (this.mBounds.contains(point.getX(), point.getY())) {
            return remove(point.getX(), point.getY(), item);
        } else {
            return false;
        }
    }

    private boolean remove(double x, double y, T item) {
        if (this.mChildren != null) {
            if (y < mBounds.midY) {
                if (x < mBounds.midX) { // top left
                    return mChildren.get(0).remove(x, y, item);
                } else { // top right
                    return mChildren.get(1).remove(x, y, item);
                }
            } else {
                if (x < mBounds.midX) { // bottom left
                    return mChildren.get(2).remove(x, y, item);
                } else {
                    return mChildren.get(3).remove(x, y, item);
                }
            }
        }
        else {
            return mItems.remove(item);
        }
    }

    /**
     * Removes all points from the quadTree
     */
    public void clear() {
        mChildren = null;
        if (mItems != null) {
            mItems.clear();
        }
    }

    public interface Item {

        public Point getPoint();

    }

    /**
     * Search for all items within a given bounds.
     */
    public Collection<T> search(Bounds searchBounds) {
        final List<T> results = new ArrayList<T>();
        search(searchBounds, results);
        return results;
    }

    private void search(Bounds searchBounds, Collection<T> results) {
        if (!mBounds.intersects(searchBounds)) {
            return;
        }

        if (this.mChildren != null) {
            for (PointQuadTree<T> quad : mChildren) {
                quad.search(searchBounds, results);
            }
        } else if (mItems != null) {
            if (searchBounds.contains(mBounds)) {
                results.addAll(mItems);
            } else {
                for (T item : mItems) {
                    if (searchBounds.contains(item.getPoint())) {
                        results.add(item);
                    }
                }
            }
        }
    }
}
