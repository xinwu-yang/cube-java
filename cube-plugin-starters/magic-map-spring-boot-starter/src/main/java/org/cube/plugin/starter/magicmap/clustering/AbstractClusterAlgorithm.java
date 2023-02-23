package org.cube.plugin.starter.magicmap.clustering;

import org.cube.plugin.starter.magicmap.clustering.model.ClusterContext;

import java.util.Set;

public abstract class AbstractClusterAlgorithm<T extends IClusterItem> implements IClusterAlgorithm<T> {
    @Override
    public Set<? extends ICluster<T>> getClustersWithConsumption(ClusterContext ctx) {
        long timeStart = System.currentTimeMillis();
        Set<? extends ICluster<T>> set = getClusters(ctx);
        long timeEnd = System.currentTimeMillis();
        long[] consumption = ctx.getConsumption();
        if ((null != consumption) && (consumption.length > 0)) {
            consumption[0] = timeEnd - timeStart;
        }

        return set;
    }
}
