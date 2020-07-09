package com.springboot.framework.build.example.enums;

/**
 * 返回码
 */
public enum ReturnCode {

    PARAM_ERROR(100, "参数错误"),
    SUCCESS(200, "执行成功"),
    NOT_LOGIN(300, "未登录"),

    EXCEL_ERROR(1050, "Excel错误"),
    EXCEL_HEAD_ERROR(1051, "Excel表头错误"),

    UNKNOWN_ERROR(6000, "未知错误");

    /** 状态码 */
    private int code;
    /** 消息 */
    private String msg;

    ReturnCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
