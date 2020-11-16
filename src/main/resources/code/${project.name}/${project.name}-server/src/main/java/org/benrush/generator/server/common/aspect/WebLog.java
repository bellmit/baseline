package org.benrush.server.common.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import ${project.groupId}.${project.artifactId}.server.common.exception.GeneratorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @Description: 打印接口调用日志切面
 * @author: 刘希晨
 * @date:  2020/11/11 15:09
 */
@Aspect
@Component
@Slf4j
public class WebLog {
    /** 以 controller 包下定义的所有请求为切入点 */
    @Pointcut("execution(public * ${project.groupId}.server.controller..*.*(..))")
    public void webLog() {
        //切面自动注入controller中的方法
    }

    private ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 在切点之前织入
     * @param joinPoint
     * @throws Throwable
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        // 开始打印请求日志
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(attributes == null){
            throw new GeneratorException("未获取到合法的请求");
        }
        HttpServletRequest request = attributes.getRequest();

        // 打印请求相关参数
        log.info("============ 请求体 ============");
        // 打印请求 url
        log.info("URL            : {}", request.getRequestURL().toString());
        // 打印 Http method
        log.info("HTTP Method    : {}", request.getMethod());
        // 打印调用 controller 的全路径以及执行方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        log.info("Class Method   : {}.{}", signature.getDeclaringTypeName(), signature.getName());
        // 打印入参类型
        log.info("Parameter Type : {}",signature.getParameterNames());
        // 打印请求入参
        log.info("Request Args   : {}", Arrays.toString(joinPoint.getArgs()));
    }

    /**
     * 环绕
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        // 打印出参
        log.info("Response Args  : {}", objectMapper.writeValueAsString(result));
        // 执行耗时
        log.info("Time-Consuming : {} ms", System.currentTimeMillis() - startTime);
        return result;
    }

}
