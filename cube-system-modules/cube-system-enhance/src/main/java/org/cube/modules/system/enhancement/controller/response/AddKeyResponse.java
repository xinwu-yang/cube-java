package org.cube.modules.system.enhancement.controller.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class AddKeyResponse implements Serializable {

    /**
     * Access Key ID
     */
    private String ak;

    /**
     * Secret Access Key
     */
    private String sk;
}
