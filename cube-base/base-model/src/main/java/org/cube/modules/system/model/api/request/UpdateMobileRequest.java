package org.cube.modules.system.model.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class UpdateMobileRequest implements Serializable {

    @NotBlank
    @Schema(title = "验证码")
    private String code;

    @NotBlank
    @Schema(title = "手机号")
    private String phone;
}
