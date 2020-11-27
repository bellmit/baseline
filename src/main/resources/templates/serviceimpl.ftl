import ${project.groupId}.${project.name}.server.persistence.mapper.${mapperName};
import ${project.groupId}.${project.name}.server.persistence.po.${poName};
import ${project.groupId}.${project.name}.server.service.defination.${serviceName};
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("${baseName}Service")
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ${serviceImplName} extends ServiceImpl<${mapperName}, ${poName}> implements ${serviceName} {

}