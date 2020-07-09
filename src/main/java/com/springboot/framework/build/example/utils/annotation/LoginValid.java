package com.springboot.framework.build.example.utils.annotation;

import java.lang.annotation.*;

/**************************************************************
 * 创建日期：2020/1/19 9:57
 * 作    者：lixuhong
 * 功能描述：登录验证
 **************************************************************/
@Target({ElementType.METHOD, ElementType.TYPE}) // 此注解可作用在方法、类、包、接口、枚举
@Retention(RetentionPolicy.RUNTIME) // 运行时有效
@Documented // 写入javadoc
public @interface LoginValid {

    /**
     * 是否验证登录
     */
    boolean required() default true;
}