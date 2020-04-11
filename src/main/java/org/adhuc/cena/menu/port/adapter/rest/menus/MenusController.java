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
package org.adhuc.cena.menu.port.adapter.rest.menus;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.net.URI;
import java.net.URISyntaxException;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A REST controller exposing /api/menus resource.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@ExposesResourceFor(MenuModel.class)
@RequestMapping(path = "/api/menus", produces = {HAL_JSON_VALUE, APPLICATION_JSON_VALUE})
public class MenusController {

    private final EntityLinks links;

    /**
     * Creates a menu.
     */
    @PostMapping(consumes = {APPLICATION_JSON_VALUE, HAL_JSON_VALUE})
    ResponseEntity<Void> createIngredient(@RequestBody @Valid CreateMenuRequest request, Errors errors) throws URISyntaxException {
        return ResponseEntity.created(new URI(links.linkToItemResource(MenuModel.class,
                String.format("%s-%s", request.getDate(), request.getMealType())).getHref())).build();
    }

}
