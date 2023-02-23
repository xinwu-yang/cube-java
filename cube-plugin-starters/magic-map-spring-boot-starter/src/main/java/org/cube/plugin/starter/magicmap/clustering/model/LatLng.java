package org.cube.plugin.starter.magicmap.clustering.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LatLng {
    // 纬度
    private Double latitude;
    // 经度
    private Double longitude;

    public LatLng(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "LatLng{"
                + "lat=" + latitude
                +  ", lng=" + longitude
                + '}';
    }
}
