package com.springboot.framework.build.example.utils;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**************************************************************
 * 创建日期：2019/12/15 11:08
 * 作    者：lixuhong
 * 功能描述：分页参数获取
 **************************************************************/
@Component
public class PageUtils {
    // 页面
    private static final String PAGE_STR = "page";
    private static final Integer DEFAULT_PAGE = 1;
    // 页面数据数量
    private static final String SIZE_STR = "size";
    private static final Integer DEFAULT_SIZE = 10;
    // 排序
    private static final String SORT_STR = "sort";
    private static final Integer DEFAULT_SORT = 0;
    private static final String SORT_NAME_STR = "sortName";

    @Autowired
    private HttpServletRequest request;

    /**
     * 获取分页数据
     */
    public Page page(){

        Page page = new Page();
        page.setCurrent(getPage());
        page.setSize(getSize());
        List<OrderItem> orderItems = getOrders();
        if(orderItems != null && orderItems.size() > 0){
            page.setOrders(orderItems);
        }

        return page;
    }

    /**
     * 排序
     */
    private List<OrderItem> getOrders(){

        // 排序字段
        String sortName = getSortName();
        if(StringUtils.isBlank(sortName)){
            return null;
        }
        // 排序方式
        boolean sort = DEFAULT_SORT.equals(getSort()) ? false : true;

        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItem.setColumn(sortName);
        orderItem.setAsc(sort);

        orderItems.add(orderItem);

        return orderItems;
    }

    /**
     * 获取page参数
     */
    private Integer getPage(){
        String page = request.getParameter(PAGE_STR);
        return StringUtils.isBlank(page) ? DEFAULT_PAGE : Integer.valueOf(page);
    }

    /**
     * 获取size参数
     */
    private Integer getSize(){
        String page = request.getParameter(SIZE_STR);
        return StringUtils.isBlank(page) ? DEFAULT_SIZE : Integer.valueOf(page);
    }

    /**
     * 获取sortName参数
     */
    private String getSortName(){
        return request.getParameter(SORT_NAME_STR);
    }

    /**
     * 获取sort参数
     */
    private Integer getSort(){
        String sort = request.getParameter(SORT_STR);
        return StringUtils.isBlank(sort) ? 0 : Integer.valueOf(sort);
    }
}
