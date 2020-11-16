package org.benrush.server.config;

import lombok.extern.slf4j.Slf4j;
import ${project.groupId}.${project.artifactId}.server.common.exception.GeneratorException;
import ${project.groupId}.${project.artifactId}.common.ResultDto;
import org.springframework.util.StringUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandlerConfig {
    @ExceptionHandler(value = GeneratorException.class)
    @ResponseBody
    public ResultDto<String> jsonHandler(HttpServletRequest request, GeneratorException e) {
        String message = e.getMessage();
        if (StringUtils.isEmpty(message)) {
            message = "Unknown Exception";
        }
        logException(e, request);
        return ResultDto.error(e.getCode(),e.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public ResultDto<String> valid(HttpServletRequest request, MethodArgumentNotValidException e) {
        StringBuilder message = new StringBuilder();
        List<ObjectError> objectErrors = e.getBindingResult().getAllErrors();
        objectErrors.forEach(mes -> {
            message.append(mes.getDefaultMessage() + ";");
        });
        Exception exception = new Exception(message.toString());
        logException(exception, request);
        return ResultDto.error(message.toString());
    }

    public void logException(Exception e, HttpServletRequest request) {
        log.error("request url:" + request.getRequestURL());
        Enumeration<String> enumeration = request.getParameterNames();
        log.error("request params:");
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            log.error(name + ":" + request.getParameter(name));
        }
        log.error("exception:" + e);
    }

}
