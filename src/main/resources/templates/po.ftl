package ${project.groupId}.${project.name}.server.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import lombok.EqualsAndHashCode;
import ${project.groupId}.${project.name}.server.common.orm.BasePo;


@TableName("${tableName}")
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("${tableComment}")
public class ${poName} extends BasePo {

<#list poItemList as poItem>
    <#if poItem.comment??>
    /**
    * ${poItem.comment}
    */
    </#if>
    private ${poItem.javaType} ${poItem.propertyName};

</#list>
}