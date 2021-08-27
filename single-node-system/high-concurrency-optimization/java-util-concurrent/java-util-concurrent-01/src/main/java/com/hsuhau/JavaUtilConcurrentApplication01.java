package com.hsuhau;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <img src="img/SpringBoot启动类解析.png"/>
 *
 * @SpringBootConfiguration
 * springboot的配置类
 *      @Configuration
 *      标明这是一个配置类
 *
 * @EnableAutoConfiguration
 * 开启自动配置功能
 *      @AutoConfigurationPackage
 *      自动配置包
 *          @Import(AutoConfigurationPackages.Registrar.class)
 *          把启动类所在的包进行默认扫描，该包下的所有类都会被扫到spring容器中，
 *      @Import(AutoConfigurationImportSelector.class)
 *      加载指定的类到spring容器中去
 *          AutoConfigurationImportSelector.class
 *          根据项目来判断你的项目需要哪些配置信息然后把默认的配置内容导入到Spring容器中进行管理
 * @ComponentScan
 * 组件扫描和自动装配，用来指定扫描容器的范围
 *
 * @author hsuhau
 * @date 2020/7/18 15:48
 */
@SpringBootApplication
public class JavaUtilConcurrentApplication01 {
    public static void main(String[] args) {
        SpringApplication.run(JavaUtilConcurrentApplication01.class, args);
    }
}
