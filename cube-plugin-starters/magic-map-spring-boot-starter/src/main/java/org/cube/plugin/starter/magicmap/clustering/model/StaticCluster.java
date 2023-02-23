package org.cube.plugin.starter.magicmap.clustering.model;

import org.cube.plugin.starter.magicmap.clustering.ICluster;
import org.cube.plugin.starter.magicmap.clustering.IClusterItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StaticCluster<T extends IClusterItem> implements ICluster<T> {
    private final LatLng mCenter;
    private final List<T> mItems = new ArrayList<T>();
    private String name;

    public StaticCluster(LatLng center) {
        mCenter = center;
    }

    public boolean add(T t) {
        return mItems.add(t);
    }

    @Override
    public LatLng getPosition() {
        return mCenter;
    }

    public boolean remove(T t) {
        return mItems.remove(t);
    }

    @Override
    public Collection<T> getItems() {
        return mItems;
    }

    @Override
    public int getSize() {
        return mItems.size();
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "StaticCluster{"
                + "mCenter=" + mCenter
                + ", mItems.size=" + mItems.size()
                + ",name=" + name + "}";
    }
}
