package org.cube.plugin.starter.magicmap.clustering.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import org.cube.plugin.starter.magicmap.clustering.IClusterItem;

public class LatLngClusterItem implements IClusterItem {
    private String id;
    @JsonAlias(value = "position")
    private LatLng latLng;
    private String bitmapDescriptor;
    private Object object;

    public LatLngClusterItem() {

    }

    public LatLngClusterItem(String id, double lat, double lng) {
        this.id = id;
        this.latLng = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPosition() {
        return latLng;
    }

    public String getBitmapDescriptor() {
        return bitmapDescriptor;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Object getObject() {
        return object;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPosition(LatLng latLng) {
        this.latLng = latLng;
    }

    public void setBitmapDescriptor(String bitmapDescriptor) {
        this.bitmapDescriptor = bitmapDescriptor;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
