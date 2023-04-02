package com.molcom.nms.general.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@RequiredArgsConstructor
@EnableSwagger2
public class SwaggerConfig {
    private final SwaggerValueConfig config;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(config.getSwaggerTitle())
                .description(config.getSwaggerDescription())
                .version(config.getSwaggerDescription()).build();
    }

    @Bean
    UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
                .deepLinking(Boolean.valueOf(config.getDeepLinking()))
                .displayOperationId(Boolean.valueOf(config.getDisplayOperationId()))
                .defaultModelsExpandDepth(Integer.valueOf(config.getDefaultModelsExpandDepth()))
                .defaultModelExpandDepth(Integer.valueOf(config.getDefaultModelExpandDepth()))
                .defaultModelRendering(ModelRendering.EXAMPLE)
                .displayRequestDuration(Boolean.valueOf(config.getDisplayRequestDuration()))
                .docExpansion(DocExpansion.NONE)
                .filter(Boolean.valueOf(config.getFilter()))
                .maxDisplayedTags(Integer.valueOf(config.getMaxDisplayedTags()))
                .operationsSorter(OperationsSorter.ALPHA)
                .showExtensions(Boolean.valueOf(config.getShowExtensions()))
                .tagsSorter(TagsSorter.ALPHA)
                .supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
                .validatorUrl(null).build();
    }

}