package org.benrush.generator.server.common.exception;

import lombok.Getter;
import lombok.Setter;
import org.benrush.generator.facade.enums.ProjectErrorType;

@Getter
@Setter
public class GeneratorException extends RuntimeException{

    private final String code;
    private final String message;

    public GeneratorException(ProjectErrorType projectErrorType){
        this.code = projectErrorType.getCode();
        this.message = projectErrorType.getMessage();
    }

    public GeneratorException(String code,String message){
        this.code = code;
        this.message = message;
    }

    public GeneratorException(String message){
        this("500",message);
    }
}
