package ${project.groupId}.${project.name}.server.common.exception;

import lombok.Getter;
import lombok.Setter;
import ${project.groupId}.${project.name}.facade.enums.${project.class}ErrorType;

@Getter
@Setter
public class ${project.name}Exception extends RuntimeException{

    private final String code;
    private final String message;

    public ${project.name}Exception(ProjectErrorType projectErrorType){
        this.code = projectErrorType.getCode();
        this.message = projectErrorType.getMessage();
    }

    public ${project.name}Exception(String code,String message){
        this.code = code;
        this.message = message;
    }

    public ${project.name}Exception(String message){
        this("500",message);
    }
}
