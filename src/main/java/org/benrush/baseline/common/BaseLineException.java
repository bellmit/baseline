package org.benrush.baseline.common;

/**
 * @Description: 自定义代码生成过程中的异常
 * @author: 刘希晨
 * @date:  2020/11/15 9:47
 */
public class BaseLineException extends RuntimeException{
    public BaseLineException(String message){
        super(message);
    }
}
