package com.kelvin.psbs.common.config;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.beans.BeanProperty;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {


    @Bean
    public Docket searchApi() {
        return getDocket("검색", Predicates.or( PathSelectors.regex("/search.*")));
    }

    @Bean
    public Docket placeSearchApi() {
        return getDocket("장소검색", Predicates.or( PathSelectors.regex("/place.*")));
    }

    @Bean
    public Docket allApi() {
        return getDocket("전체", Predicates.or(PathSelectors.regex("/*.*")));
    }

    //swagger 설정.
    public Docket getDocket(String groupName, Predicate<String> predicate) {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .groupName(groupName).select()
                .apis(RequestHandlerSelectors.basePackage("com.kelvin.psbs"))
                .paths(predicate)
                .apis(RequestHandlerSelectors.any()).build();
    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("Place Search-Based Service(PSBS) API Doc")
                .description("키워드 기반의 장소 검색 서비스입니다. 카카오 네이버의 장소를 키워드 검색으로 제공하는 API 를 통해 데이터를 정제하고, 실시간 키워드 랭킹 연산을 통해 목록 제공 및 실시간 검색 키워드 랭킹을 제공합니다. ")
                .version("1.0.0")
                .build();
    }

    /* swagger-ui 페이지 연결 핸들러 설정 */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry
                .addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

        super.addResourceHandlers(registry);
    }
}
