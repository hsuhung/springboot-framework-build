package com.springboot.framework.build.example.utils.annotation;

import java.lang.annotation.*;

/**************************************************************
 * 创建日期：2020/1/13 15:06
 * 作    者：lixuhong
 * 功能描述：Excel表头字段
 **************************************************************/
@Documented
@Target({ElementType.FIELD}) // 在字段上使用
@Retention(RetentionPolicy.RUNTIME) // 运行时有效
public @interface ExcelField {

    /**
     * 列
     * Excel表头位置
     * 从左往右排列位置
     */
    int column() default 0;

    /**
     * 值
     *Excel表头的值
     */
    String value() default "";
}
