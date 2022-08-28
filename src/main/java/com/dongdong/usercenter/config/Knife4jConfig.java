package com.dongdong.usercenter.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @program: user-center
 * @description: 整合Knife4j和Swagger3
 * @author: Mr.Ye
 * @create: 2022-08-25 07:45
 **/
@Configuration
@EnableSwagger2
@EnableKnife4j
@Profile("dev")
public class Knife4jConfig {
	@Bean(value = "defaultApi3")
	public Docket createRestApi() {
		return new Docket(DocumentationType.OAS_30)
				.useDefaultResponseMessages(false)
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.dongdong.usercenter.controller"))
				.paths(PathSelectors.any())
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("用户中心接口文档")
				.description("利用knife4j生成用户中心接口文档")
				.contact(new Contact("Winter's冬", "https://github.com/TuwenMark/user-center.git", "m18698577021@163.com"))
				.version("v3.0.0")
				.build();
	}

}

