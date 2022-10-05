package com.dongdong.usercenter.config;

import com.dongdong.usercenter.interceptors.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @program: user-center
 * @description: WebMvc配置对象
 * @author: Mr.Ye
 * @create: 2022-09-18 19:52
 **/
@Configuration
public class MvcConfig implements WebMvcConfigurer {
	@Resource
	private StringRedisTemplate stringRedisTemplate;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginInterceptor(stringRedisTemplate))
				.excludePathPatterns(
						"/user/login/**",
						"/user/code",
//						"/user/recommend",
						"/user/register",
						"/**/webjars/**",
						// knife4j的页面
						"/**/doc.html",
						// 防止控制台报错，对功能没有影响，实际没啥用
						"/**/favicon.ico",
						// 请求接口信息
						"/**/swagger-resources",
						// 请求接口信息
						"/**/api-docs"
				);
	}
//
//	/***
//	 * 配置静态资源访问拦截
//	 * @param registry
//	 */
//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry){
//		registry.addResourceHandler("swagger-ui.html", "doc.html")
//				.addResourceLocations("classpath:/META-INF/resources/");
//		registry.addResourceHandler("/webjars/**")
//				.addResourceLocations("classpath:/META-INF/resources/webjars/");
//	}

//	/**
//	 * 配置跨域
//	 *
//	 * @param registry 注册对象
//	 */
//	@Override
//	public void addCorsMappings(CorsRegistry registry) {
//		registry.addMapping("/**")
//				.allowedOrigins("http://localhost:8001")
//				.allowCredentials(true)
//				.allowedMethods("GET", "POST", "DELETE", "PUT")
//				.maxAge(3600);
//	}
//	private CorsConfiguration addCorsConfig() {
//		CorsConfiguration corsConfiguration = new CorsConfiguration();
//		List<String> list = new ArrayList<>();
//		list.add("*");
//		corsConfiguration.setAllowedOrigins(list);
//    /*
//    // 请求常用的三种配置，*代表允许所有，当时你也可以自定义属性（比如header只能带什么，只能是post方式等等）
//    */
//		corsConfiguration.addAllowedOrigin("*");
//		corsConfiguration.addAllowedHeader("*");
//		corsConfiguration.addAllowedMethod("*");
//		return corsConfiguration;
//	}
//	@Bean
//	public CorsFilter corsFilter() {
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/user/**", this.addCorsConfig());
//		return new CorsFilter(source);
//	}
}
