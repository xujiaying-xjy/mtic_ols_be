package com.mantoo.mtic.aop;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName: WebMvcConfig
 * @Description: 登录接口跨域开放、静态资源映射
 * @Author: renjt
 * @Date: 2019-11-25 15:46
 */

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

//    @Autowired
//    FilePathConfig filePathConfig;
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/login").allowedOrigins("*")
//                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
//                .allowCredentials(false).maxAge(3600);
//    }
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        String separator = File.separator;
//        String path = null;
//        if ("\\".equals(separator)) {
//            //win系统
//            path = filePathConfig.getWinsPath() + separator;
//        } else {
//            //linux系统
//            path = filePathConfig.getLinuxPath() + separator;
//        }
//        /*registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
//        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
//        registry.addResourceHandler("/attachment/**").addResourceLocations("classpath:/attachment/");
//        registry.addResourceHandler("/**").addResourceLocations("classpath:/public/");*/
//        registry.addResourceHandler("/image/**").addResourceLocations("file:" + path);
//        registry.addResourceHandler("/video/**").addResourceLocations("file:" + path);
//    }
}