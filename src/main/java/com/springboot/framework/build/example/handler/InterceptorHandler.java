package com.springboot.framework.build.example.handler;

import com.springboot.framework.build.example.enums.ReturnCode;
import com.springboot.framework.build.example.utils.annotation.LoginValid;
import com.springboot.framework.build.example.utils.component.GlobalException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**************************************************************
 * 创建日期：2020/1/19 10:06
 * 作    者：lixuhong
 * 功能描述：拦截处理器
 **************************************************************/
public class InterceptorHandler implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 登录拦截
        // 取得方法注解
        LoginValid loginValid = ((HandlerMethod) handler).getMethodAnnotation(LoginValid.class);
        if (loginValid != null && loginValid.required()) {
            // 验证登录
        } else {
            throw new GlobalException(ReturnCode.NOT_LOGIN);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
