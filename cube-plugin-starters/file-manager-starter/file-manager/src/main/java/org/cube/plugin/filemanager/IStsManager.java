package org.cube.plugin.filemanager;

import org.cube.plugin.filemanager.models.SignStsCredentialsParam;
import org.cube.plugin.filemanager.models.StsCredentials;

public interface IStsManager<T> extends IClient<T> {

    /**
     * 根据策略获取零时凭证
     *
     * @param param 策略组
     * @return 临时凭证
     */
    StsCredentials signStsCredentials(SignStsCredentialsParam param);
}
