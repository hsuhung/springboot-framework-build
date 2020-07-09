package com.springboot.framework.build.example.handler;

import com.springboot.framework.build.example.enums.ReturnCode;
import com.springboot.framework.build.example.utils.component.GlobalException;
import com.springboot.framework.build.example.utils.component.ReturnJson;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**************************************************************
 * 创建日期：2020/1/19 9:50
 * 作    者：lixuhong
 * 功能描述：异常处理
 **************************************************************/
@ResponseBody
@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
    public Object handleException(HttpServletRequest request, Exception e){

        Object object;
        ReturnJson returnJson = null;

        // 全局异常处理 运行时异常
        if(e instanceof GlobalException){
            GlobalException globalException = (GlobalException) e;
            returnJson = (ReturnJson) globalException.getObject();
            object = returnJson;
        }
        // @valid 方法参数异常;JSON方式：校验失败
        else if(e instanceof MethodArgumentNotValidException){
            MethodArgumentNotValidException validException = (MethodArgumentNotValidException) e;

            // 取得消息
            List<ObjectError> errors = validException.getBindingResult().getAllErrors();
            String message = errors.get(0).getDefaultMessage();

            object = ReturnJson.err(ReturnCode.PARAM_ERROR.getCode(), message);
        }
        else{
            object = ReturnJson.err(ReturnCode.UNKNOWN_ERROR);
        }

        // 异常输出
        e.printStackTrace();

        return object;
    }
}
