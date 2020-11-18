package org.benrush.generator.facade.common;

import lombok.Data;
import lombok.experimental.Accessors;
import org.benrush.generator.facade.enums.ProjectErrorType;

@Data
@Accessors(chain = true)
public final class ResultDto<T>{

    private String code;

    private String msg;

    private T data;

    public ResultDto() {

    }

    public ResultDto(String code) {
        this.code = code;
    }

    public ResultDto(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultDto(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ResultDto<T> ok(T data) {
        ResultDto<T> dto = new ResultDto<>();
        dto.msg = "Success";
        dto.code = "000000";
        dto.data = data;
        return dto;
    }

    public static <T> ResultDto<T> ok() {
        ResultDto<T> dto = new ResultDto<>();
        dto.msg = "Success";
        dto.code = "000000";
        return dto;
    }

    public static <T> ResultDto<T> ok(String code, String msg, T data) {
        ResultDto<T> dto = new ResultDto<>();
        dto.code = code;
        dto.msg = msg;
        dto.data = data;
        return dto;
    }

    public static <T> ResultDto<T> error() {
        ResultDto<T> dto = new ResultDto<>();
        dto.msg = "Fail";
        dto.code = "B00000";
        return dto;
    }

    public static <T> ResultDto<T> error(String msg) {
        ResultDto<T> dto = new ResultDto<>();
        dto.msg = msg;
        dto.code = "B00000";
        return dto;
    }

    public static <T> ResultDto<T> error(String code, String msg) {
        ResultDto<T> dto = new ResultDto<>();
        dto.msg = msg;
        dto.code = code;
        return dto;
    }

    public static <T> ResultDto<T> error(ProjectErrorType projectErrorType) {
        ResultDto<T> dto = new ResultDto<>();
        dto.msg = projectErrorType.getCode();
        dto.code = projectErrorType.getMessage();
        return dto;
    }
}
