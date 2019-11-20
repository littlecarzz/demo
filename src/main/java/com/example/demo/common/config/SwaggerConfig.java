package com.example.demo.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 描述：集成swagger2的配置
 * @author littlecar
 * @date 2019/11/20 13:23
 */
@Configuration
public class SwaggerConfig {
    @Bean
    public Docket buildDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(buildApiInfo())
                //controller路径
                .select().apis(RequestHandlerSelectors.basePackage("web"))
                .paths(PathSelectors.any())
                .build();
    }
    //配置API文档标题、描述、作者等信息。
    public ApiInfo buildApiInfo() {
        return new ApiInfoBuilder()
                .title("前后端分离接口文档")
                .termsOfServiceUrl("")
                .description("使用swagger2构建Restful API")
                .build();
    }
}
