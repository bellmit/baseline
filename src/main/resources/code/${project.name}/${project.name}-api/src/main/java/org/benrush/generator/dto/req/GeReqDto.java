package org.benrush.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("Ge请求体")
public class GeReqDto {

    @ApiModelProperty("值")
    @NotBlank(message = "v不能为空")
    private String v;

}
