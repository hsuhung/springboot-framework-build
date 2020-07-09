package com.springboot.framework.build.example.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**************************************************************
 * 创建日期：2020/1/19 10:17
 * 作    者：lixuhong
 * 功能描述：swagger2配置
 * swagger文档访问地址
 * 原界面：http://localhost:8080/swagger-ui.html
 * 增强型：http://localhost:8080/doc.html
 **************************************************************/
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI //第三方swagger增强API注解
//@Profile(value = {"local"}) // 指定环境下启用swagger
public class Swagger2Config {

    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .globalOperationParameters(headParam())
                .apiInfo(apiInfo())
                .select().apis(RequestHandlerSelectors.withMethodAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * swagger接口信息
     * @return
     */
    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("XXX-接口文档")
                .description("XXX-接口文档描述")
                .version("v.1.0.0")
                .build();
    }

    /**
     * 请求头参数
     * @return
     */
    private List<Parameter> headParam(){

        List<Parameter> params = new ArrayList<>();

        // 加入token字段
        params.add(new ParameterBuilder()
                .name("token")
                .description("认证token")
                .modelRef(new ModelRef("String"))
                .parameterType("header")
                .required(false)
                .build());

        return params;
    }
}
