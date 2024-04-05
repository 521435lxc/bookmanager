package com.example.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebConfig implements  WebMvcConfigurer {

    @Resource
    private JwtInterceptor jwtInterceptor;

    // 加自定义拦截器JwtInterceptor，设置拦截规则
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 对swagger的请求不进行拦截
        String[] excludePatterns = new String[]{"/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**",
                "/api", "/api-docs", "/api-docs/**", "/doc.html/**"};


        registry.addInterceptor(jwtInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/")
                .excludePathPatterns("/login")
                .excludePathPatterns("/register")
                .excludePathPatterns("/files/**")
                .excludePathPatterns("/department/selectAll")
                .excludePathPatterns("/v3/api-docs/swagger-config")
                .excludePathPatterns("/v3/api-docs/admin-api")
                .excludePathPatterns("/swagger-ui/oauth2-redirect.html")
                .excludePathPatterns("/doc.html")
                .excludePathPatterns("/**/doc.*",
                "/**/swagger-ui.*",
                "/**/swagger-resources",
                "/**/webjars/**",
                "/**/v2/api-docs/**").excludePathPatterns(excludePatterns);

    }
}