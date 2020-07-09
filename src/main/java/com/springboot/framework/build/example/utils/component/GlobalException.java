package com.springboot.framework.build.example.utils.component;

/**************************************************************
 * 创建日期：2019/12/15 11:08
 * 作    者：lixuhong
 * 功能描述：自定义全局异常
 **************************************************************/
public class GlobalException extends RuntimeException{

    private Object object;

    public GlobalException(Object object){
        super(object.toString());
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
