package org.cube.modules.system.model.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class HaveReadRequest implements Serializable {

    @NotNull
    @Schema(title = "通告id")
    private Long anntId;
}
