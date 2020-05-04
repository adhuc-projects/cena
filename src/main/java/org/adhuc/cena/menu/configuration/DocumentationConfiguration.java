/*
 * Copyright (C) 2019-2020 Alexandre Carbenay
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
package org.adhuc.cena.menu.configuration;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * A configuration to enable documentation endpoint.
 *
 * @author Alexandre Carbenay
 * @version 0.0.1
 * @since 0.0.1
 */
@Slf4j
@Configuration
class DocumentationConfiguration implements WebMvcConfigurer {

    private static final String DOCUMENTATION_RESOURCE_LOCATION = "classpath:/static/docs/";

    private final MenuGenerationProperties.Documentation documentation;

    DocumentationConfiguration(@NonNull MenuGenerationProperties menuGenerationProperties) {
        documentation = menuGenerationProperties.getDocumentation();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (documentation.isEnabled()) {
            log.info("Enable documentation endpoint");
            registry.addResourceHandler(documentation.getPath() + "/**")
                    .addResourceLocations(DOCUMENTATION_RESOURCE_LOCATION);
        }
    }

}
