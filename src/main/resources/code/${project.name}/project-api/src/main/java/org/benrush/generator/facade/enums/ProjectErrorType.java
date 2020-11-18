package org.benrush.generator.facade.enums;

import lombok.Getter;
/**
 * @Description: 错误产生来源分为 A/B/C，
 * A 表示错误来源于用户，比如参数错误，用户安装版本过低，用户支付超时等问题；
 * B 表示错误来源于当前系统，往往是业务逻辑出错，或程序健壮性差等问题；
 * C 表示错误来源于第三方服务，比如 CDN 服务出错，消息投递超时等问题；四位数字编号从 0001 到 9999，大类之间的
 * 步长间距预留 100
 * @author: 刘希晨
 * @date:  2020/11/7 15:31
 */
@Getter
public enum ProjectErrorType {
    UNKNOWN_ERROR("B00000","未知错误"),
    DATABASE_ERROR("B00001","数据库错误");

    private String code;
    private String message;

    ProjectErrorType(String code, String message) {
        this.code = code;
        this.message = message;
    }

}
