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

import java.security.Principal;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.adhuc.cena.menu.menus.DeleteMenu;
import org.adhuc.cena.menu.menus.MenuAppService;
import org.adhuc.cena.menu.menus.MenuOwner;

/**
 * A REST controller exposing /api/menus/{menuId} resource.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/menus/{menuId}", produces = {HAL_JSON_VALUE, APPLICATION_JSON_VALUE})
class MenuController {

    private final MenuModelAssembler modelAssembler;
    private final MenuIdConverter menuIdConverter;
    private final MenuAppService menuAppService;

    /**
     * Gets the menu information for the menu corresponding to the specified identity.
     *
     * @param menuId the menu identity.
     * @return the menu information.
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public MenuModel getMenu(@PathVariable String menuId, Principal principal) {
        var id = menuIdConverter.parse(menuId, new MenuOwner(principal.getName()));
        var menu = menuAppService.getMenu(id);
        return modelAssembler.toModel(menu);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMenu(@PathVariable String menuId, Principal principal) {
        var id = menuIdConverter.parse(menuId, new MenuOwner(principal.getName()));
        menuAppService.deleteMenu(new DeleteMenu(id));
    }

}
