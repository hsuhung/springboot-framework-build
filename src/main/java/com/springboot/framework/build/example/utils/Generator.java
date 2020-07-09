package com.springboot.framework.build.example.utils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * 生成器-自定义文件输出
 */
public class Generator {

    // 作者
    private static String author = "lixuhong";
    // 模块路径
    private static String modulePath = "com/parentpath";
    // 表名
    private static String[] tables = {"user_vip"};

    // 数据源
    private static String username = "root";
    private static String password = "HHXR!@#$QAZWSX";
    private static String url = "jdbc:mysql://47.110.145.22:3306/installment?serverTimezone=GMT%2B8&characterEncoding=UTF-8&allowMultiQueries=true";
    private static String driverClassName = "com.mysql.cj.jdbc.Driver";

    // 文件输出模板 可以自定义模板，放在templates文件夹下
    private static String entityTemplate = "templates/entity.java.ftl";
    private static String xmlTemplate = "templates/mapper.xml.ftl";
    private static String mapperTemplate = "templates/mapper.java.ftl";
    private static String serviceTemplate = "templates/service.java.ftl";
    private static String serviceImplTemplate = "templates/serviceImpl.java.ftl";
    private static String controllerTemplate = "templates/controller.java.ftl";


    public static void main(String[] args) {

        // 全局配置
        GlobalConfig globalConfig = globalConfig();
        // 数据源配置
        DataSourceConfig dataSourceConfig = dataSourceConfig();
        // 策略配置
        StrategyConfig strategyConfig = strategyConfig();
        // 包配置
        PackageConfig packageConfig = packageConfig();
        // 模板配置
        TemplateConfig templateConfig = templateConfig();
        // 自定义配置
        InjectionConfig injectionConfig = injectionConfig();

        // 执行
        new AutoGenerator().setGlobalConfig(globalConfig)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig)
                .setPackageInfo(packageConfig)
                .setTemplate(templateConfig)
                .setTemplateEngine(new FreemarkerTemplateEngine()) // 使用的模板引擎，如果不是默认模板引擎则需要添加模板依赖到pom
                .setCfg(injectionConfig)
                .execute();
    }

    /**
     * 全局配置
     */
    private static GlobalConfig globalConfig() {
        return new GlobalConfig()
                .setOutputDir(System.getProperty("user.dir") + "/src/main/java") // 输出目录
                .setOpen(false) // 打开文件
                .setFileOverride(true) // 文件覆盖
                .setActiveRecord(true) // 开启activeRecord模式
                .setBaseResultMap(true) // XML ResultMap: mapper.xml生成查询映射结果
                .setBaseColumnList(true) // XML ColumnList: mapper.xml生成查询结果列
                .setSwagger2(true) // swagger注解; 须添加swagger依赖
                .setAuthor(author); // 作者
    }

    /**
     * 数据源配置
     */
    private static DataSourceConfig dataSourceConfig() {
        return new DataSourceConfig()
                .setDbType(DbType.MYSQL) // 数据库类型
                .setDriverName(driverClassName) // 连接驱动
                .setUrl(url) // 地址
                .setUsername(username) // 用户名
                .setPassword(password); // 密码
    }

    /**
     * 策略配置
     */
    private static StrategyConfig strategyConfig() {
        return new StrategyConfig()
                .setNaming(NamingStrategy.underline_to_camel) // 表名生成策略：下划线连转驼峰
                .setColumnNaming(NamingStrategy.underline_to_camel) // 表字段生成策略：下划线连转驼峰
                .setInclude(tables) // 需要生成的表
                .setRestControllerStyle(true) // 生成controller
                .setControllerMappingHyphenStyle(true) // controller映射地址：驼峰转连字符
//                .setEntityLombokModel(true) // 是否为lombok模型; 需要lombok依赖
                .setEntityTableFieldAnnotationEnable(true); // 生成实体类字段注解
    }

    /**
     * 包配置
     * 设置包路径用于导包时使用，路径示例：com.path
     */
    private static PackageConfig packageConfig() {
        return new PackageConfig()
                .setParent(modulePath.replace('/', '.')); // 父包名
    }

    /**
     * 模板配置
     */
    private static TemplateConfig templateConfig() {
        return new TemplateConfig()
                // 置空后方便自定义输出位置
                .setEntity(null)
                .setXml(null)
                .setMapper(null)
                .setService(null)
                .setServiceImpl(null)
                .setController(null);
    }

    /**
     * 自定义配置
     */
    private static InjectionConfig injectionConfig() {
        return new InjectionConfig() {
            @Override
            public void initMap() {
                // 注入配置
            }
        }
        .setFileOutConfigList(fileOutConfigList()); // 自定义输出文件
    }

    /**
     * 自定义输出文件配置
     */
    private static List<FileOutConfig> fileOutConfigList() {
        List<FileOutConfig> list = new ArrayList<>();
        // 当前项目路径
        String projectPath = System.getProperty("user.dir");

        // 实体类文件输出
        list.add(new FileOutConfig(entityTemplate) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/src/main/java/" + modulePath + "/entity/" + tableInfo.getEntityName() + StringPool.DOT_JAVA;
            }
        });
        // mapper xml文件输出
        list.add(new FileOutConfig(xmlTemplate) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/src/main/resources/mapper/" + tableInfo.getMapperName() + StringPool.DOT_XML;
            }
        });
        // mapper文件输出
        list.add(new FileOutConfig(mapperTemplate) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/src/main/java/" + modulePath + "/mapper/" + tableInfo.getMapperName() + StringPool.DOT_JAVA;
            }
        });
        // service文件输出
        list.add(new FileOutConfig(serviceTemplate) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/src/main/java/" + modulePath + "/service/" + tableInfo.getServiceName() + StringPool.DOT_JAVA;
            }
        });
        // service impl文件输出
        list.add(new FileOutConfig(serviceImplTemplate) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/src/main/java/" + modulePath + "/service/impl/" + tableInfo.getServiceImplName() + StringPool.DOT_JAVA;
            }
        });
        // controller文件输出
        list.add(new FileOutConfig(controllerTemplate) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return projectPath + "/src/main/java/" + modulePath + "/controller/" + tableInfo.getControllerName() + StringPool.DOT_JAVA;
            }
        });

        return list;
    }
}
