package org.cube.plugin.starter.magicmap.clustering.baidu.projection;

import org.cube.plugin.starter.magicmap.clustering.model.LatLng;

public class SphericalMercatorProjection {
    final double mWorldWidth;

    public SphericalMercatorProjection(final double worldWidth) {
        mWorldWidth = worldWidth;
    }

    /**
     * 经纬度转像素点
     * @param latLng
     * @return
     */
    @SuppressWarnings("deprecation")
    public Point toPoint(final LatLng latLng) {
        final double x = latLng.getLongitude() / 360 + .5;
        final double siny = Math.sin(Math.toRadians(latLng.getLatitude()));
        final double y = 0.5 * Math.log((1 + siny) / (1 - siny)) / - (2 * Math.PI) + .5;

        if (x < 0 || y < 0) {
            System.out.println("lat:" + latLng.getLatitude() + ",lng:" + latLng.getLongitude() + " overflow");
        }

        return new Point(x * mWorldWidth, y * mWorldWidth);
    }

    /**
     * 像素点转经纬度
     * @param point
     * @return
     */
    public LatLng toLatLng(Point point) {
        final double x = point.getX() / mWorldWidth - 0.5;
        final double lng = x * 360;

        double y = .5 - (point.getY() / mWorldWidth);
        final double lat = 90 - Math.toDegrees(Math.atan(Math.exp(-y * 2 * Math.PI)) * 2);

        return new LatLng(lat, lng);
    }
}
