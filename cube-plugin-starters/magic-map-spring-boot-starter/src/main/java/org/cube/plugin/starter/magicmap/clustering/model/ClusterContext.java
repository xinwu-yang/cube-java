package org.cube.plugin.starter.magicmap.clustering.model;

import lombok.Data;

import java.util.List;

@Data
public class ClusterContext {
    private Double zoom;
    private LatLng southWest;
    private LatLng northEast;
    private List<String> clusterFields;
    private long[] consumption;
}
