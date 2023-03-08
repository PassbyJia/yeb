//package com.rainfir.server;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//import java.util.ArrayList;
//
//@Configuration
//@EnableSwagger2
//public class TestSwagger {
//
//    //配置了Swagger的Docket的bean实例
//    @Bean
//    public Docket docket(){
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo());
//    }
//
//    //配置Swagger信息=apiInfo
//    private ApiInfo apiInfo(){
//        //作者信息
//        Contact contact = new Contact(
//                ""//name
//                ,""//url
//                ,"");//email
//
//        return new ApiInfo(
//                ""//title
//                ,""//description
//                ,""//version
//                ,""//termsOfServiceUrl
//                ,contact//contactName
//                ,""//license
//                ,""//licenseUrl
//                ,new ArrayList()//vendorExtensions
//        );
//    }
//}
