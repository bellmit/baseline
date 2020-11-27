package ${project.groupId}.${project.name}.facade.dto.res;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ${resName} {
<#list resItemList as resItem>
    <#if resItem.comment??>
        /**
        * ${resItem.comment}
        */
    </#if>
    private ${resItem.javaType} ${resItem.propertyName};

</#list>
}
