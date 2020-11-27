import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("${tableComment}表请求体")
public class ${reqName} {
<#list reqItemList as reqItem>
    @ApiModelProperty("${reqItem.comment!reqItem.propertyName}")
    @${(reqItem.javaType == "String")?then('NotBlank','NotNull')}(message = "${reqItem.comment!reqItem.propertyName}不能为空")
    private ${reqItem.javaType} ${reqItem.propertyName};

</#list>

}