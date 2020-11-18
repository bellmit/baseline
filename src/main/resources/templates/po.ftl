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
public class ${tableClass}Po extends BasePo {

<#list poItemList as poItem>
    /**
    * ${poItem.comment}
    */
    private ${poItem.javaType} ${poItem.propertyName};

</#list>
}