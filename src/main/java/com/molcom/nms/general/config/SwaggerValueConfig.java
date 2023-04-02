package com.molcom.nms.general.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class SwaggerValueConfig {
    @Value("${swagger.version}")
    private String swaggerVersion;

    @Value("${swagger.title}")
    private String swaggerTitle;

    @Value("${swagger.description}")
    private String swaggerDescription;

    @Value("${swagger.base.package}")
    private String swaggerBasePackage;

    @Value("${swagger.useDefaultResponseMessages:true}")
    private String useDefaultResponseMessages;

    @Value("${swagger.enableUrlTemplating:true}")
    private String enableUrlTemplating;

    @Value("${swagger.deepLinking:true}")
    private String deepLinking;

    @Value("${swagger.defaultModelsExpandDepth}")
    private String defaultModelsExpandDepth;

    @Value("${swagger.defaultModelExpandDepth}")
    private String defaultModelExpandDepth;

    @Value("${swagger.displayOperationId:true}")
    private String displayOperationId;

    @Value("${swagger.displayRequestDuration:true}")
    private String displayRequestDuration;

    @Value("${swagger.filter:true}")
    private String filter;

    @Value("${swagger.maxDisplayedTags:1000}")
    private String maxDisplayedTags;

    @Value("${swagger.showExtensions:true}")
    private String showExtensions;

}
