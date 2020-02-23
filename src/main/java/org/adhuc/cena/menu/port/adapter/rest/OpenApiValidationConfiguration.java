/*
 * Copyright (C) 2019 Alexandre Carbenay
 *
 * This file is part of Cena Project.
 *
 * Cena Project is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Cena Project is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Cena Project. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package org.adhuc.cena.menu.port.adapter.rest;

import static com.atlassian.oai.validator.whitelist.rule.WhitelistRules.*;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.nio.charset.Charset;
import javax.servlet.Filter;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.model.ApiOperation;
import com.atlassian.oai.validator.model.Request;
import com.atlassian.oai.validator.model.Response;
import com.atlassian.oai.validator.report.ValidationReport;
import com.atlassian.oai.validator.springmvc.OpenApiValidationFilter;
import com.atlassian.oai.validator.springmvc.OpenApiValidationInterceptor;
import com.atlassian.oai.validator.whitelist.ValidationErrorsWhitelist;
import com.atlassian.oai.validator.whitelist.rule.WhitelistRule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.adhuc.cena.menu.configuration.MenuGenerationProperties;

/**
 * Configures REST API requests and responses validation against its OpenAPI specification.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.1.0
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "cena.menu-generation", name = "rest.openApiValidation.enabled", matchIfMissing = true)
class OpenApiValidationConfiguration implements WebMvcConfigurer {

    private final OpenApiValidationInterceptor validationInterceptor;

    OpenApiValidationConfiguration(@Value("classpath:api/openapi.yml") Resource apiSpecificationResource,
                                   @Value("${management.endpoints.web.base-path}") String managementBasePath,
                                   MenuGenerationProperties properties
    ) throws IOException {
        var apiSpecification = IOUtils.toString(apiSpecificationResource.getInputStream(), Charset.defaultCharset());
        this.validationInterceptor = new OpenApiValidationInterceptor(
                OpenApiInteractionValidator.createForInlineApiSpecification(apiSpecification)
                        .withWhitelist(validationErrorsWhitelist(managementBasePath, properties)).build());
    }

    @Bean
    public Filter validationFilter() {
        return new OpenApiValidationFilter(true, true);
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(validationInterceptor);
    }

    private ValidationErrorsWhitelist validationErrorsWhitelist(String managementBasePath, MenuGenerationProperties properties) {
        log.info("Activate OpenAPI whitelist for Spring Boot management API");
        var whitelist = ValidationErrorsWhitelist.create()
                .withRule("Ignore validation on responses with content types not handled by API",
                        allOf(
                                isResponse(),
                                headerContains(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE).not(),
                                headerContains(HttpHeaders.CONTENT_TYPE, HAL_JSON_VALUE).not()
                        )
                ).withRule(
                        "Ignore Spring Boot management API",
                        allOf(
                                isRequest(),
                                requestPathStartsWith(managementBasePath)
                        )
                );
        if (properties.getDocumentation().isEnabled()) {
            log.info("Activate OpenAPI whitelist for documentation");
            whitelist = whitelist.withRule(
                    "Ignore documentation",
                    allOf(
                            isRequest(),
                            requestPathStartsWith(properties.getDocumentation().getPath())
                    )
            );
        }
        return whitelist;
    }

    private WhitelistRule requestPathStartsWith(final String pathPrefix) {
        return new WhitelistRule() {
            @Override
            public boolean matches(ValidationReport.Message message, ApiOperation operation, Request request, Response response) {
                return operation == null && request.getPath().startsWith(pathPrefix);
            }
        };
    }

}
