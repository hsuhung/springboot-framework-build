package com.springboot.framework.build.example.utils.component;

import com.springboot.framework.build.example.enums.ReturnCode;

import java.io.Serializable;

/**************************************************************
 * 创建日期：2020/1/10 11:08
 * 作    者：lixuhong
 * 功能描述：返回数据
 **************************************************************/
public class ReturnJson implements Serializable {

    /** 状态码 */
    private int code;
    /** 消息 */
    private String msg;
    /** 数据 */
    private Object data;

    /**
     * 执行成功
     */
    public static Object ok(){
        return new ReturnJson();
    }

    /**
     * 执行成功
     * @param data 返回数据
     */
    public static Object ok(Object data){
        return new ReturnJson(data);
    }

    /**
     * 执行失败
     * @param code  返回码
     * @param msg   消息
     */
    public static Object err(int code, String msg){
        return new ReturnJson(code, msg);
    }

    /**
     * 执行失败
     * @param returnCode 返回码（枚举）
     */
    public static Object err(ReturnCode returnCode){
        return err(returnCode.getCode(), returnCode.getMsg());
    }

    /**
     * 执行失败
     * @param code  返回码
     * @param msg   消息
     * @param data   返回数据
     */
    public static Object err(int code, String msg, Object data){
        return new ReturnJson(code, msg, data);
    }

    public ReturnJson() {
        this.code = ReturnCode.SUCCESS.getCode();
        this.msg = ReturnCode.SUCCESS.getMsg();
    }

    public ReturnJson(Object data) {
        this.code = ReturnCode.SUCCESS.getCode();
        this.msg = ReturnCode.SUCCESS.getMsg();
        this.data = data;
    }

    public ReturnJson(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ReturnJson(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
