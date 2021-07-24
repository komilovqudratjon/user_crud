package com.example.backent.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
@EnableWebMvc
@Import(BeanValidatorPluginsConfiguration.class)
public class SpringFoxConfig extends WebMvcConfigurerAdapter {
  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo())
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.example.backent"))
        .paths(PathSelectors.any())
        .build();
  }

  private ApiInfo apiInfo() {
    //    return new ApiInfo( "Manager",
    //            "manage api documenation",
    //            "1.0",
    //            "https://www.google.com/",
    //            new Contact( "Qudratjon","https://www.google.com/","komilovqudrtajon@gmail.com" ),
    //            "License of API",
    //            "API license URL",
    //            Collections.emptyList( ) );
    return new ApiInfoBuilder()
        .title("Manager")
        .description("manage api documenation")
        .version("1.0.0")
        .license("web side")
        .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0\"")
        .contact(new Contact("Qudratjon", "https://www.google.com/", "komilovqudrtajon@gmail.com"))
        .build();
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry
        .addResourceHandler("swagger-ui.html")
        .addResourceLocations("classpath:/META-INF/resources/");

    registry
        .addResourceHandler("/webjars/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/");
  }
}
