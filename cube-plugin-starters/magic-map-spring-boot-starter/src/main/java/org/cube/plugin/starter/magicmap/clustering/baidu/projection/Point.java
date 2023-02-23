package org.cube.plugin.starter.magicmap.clustering.baidu.projection;

import lombok.Data;

/**
 * 墨卡托投影点
 */
@Data
public class Point {
    private final double x;
    private final double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Point{"
                + "x=" + x
                +  ", y=" + y
                + '}';
    }
}
