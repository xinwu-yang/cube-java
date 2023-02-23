package org.cube.cloud.starter.core.feign;

public interface IFeignService {

    /**
     * 初始化FeignService
     *
     * @param clientClass Feign Client Class
     * @param name    Service Name
     * @param <T>     Type
     */
    <T> T newInstance(Class<T> clientClass, String name);
}
