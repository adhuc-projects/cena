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

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.http.HttpStatus.OK;

import lombok.NonNull;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.adhuc.cena.menu.configuration.MenuGenerationProperties;

/**
 * A REST controller providing links to all general resources.
 *
 * @author Alexandre Carbenay
 * @version 0.0.1
 * @since 0.0.1
 */
@RestController
@RequestMapping(value = "/api", produces = HAL_JSON_VALUE)
class ApiIndexController {

    private final MenuGenerationProperties.Documentation documentation;

    ApiIndexController(@NonNull MenuGenerationProperties menuGenerationProperties) {
        documentation = menuGenerationProperties.getDocumentation();
    }

    @GetMapping
    @ResponseStatus(OK)
    ResourceSupport index() {
        final var index = new ResourceSupport();
        if (documentation.isEnabled()) {
            index.add(linkTo(ApiIndexController.class).slash("docs").slash("api-guide.html").withRel("documentation"));
        }
        index.add(linkTo(ApiIndexController.class).slash("management").withRel("management"));
        return index;
    }

}