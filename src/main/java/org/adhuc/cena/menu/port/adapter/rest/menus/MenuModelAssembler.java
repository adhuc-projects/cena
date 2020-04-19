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

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import org.adhuc.cena.menu.menus.Menu;

/**
 * A {@link org.springframework.hateoas.server.RepresentationModelAssembler RepresentationModelAssembler} implementation
 * allowing building {@link MenuModel}s.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Component
class MenuModelAssembler extends RepresentationModelAssemblerSupport<Menu, MenuModel> {

    private EntityLinks links;
    private MenuIdConverter menuIdConverter;

    /**
     * Creates a model assembler for menus.
     */
    MenuModelAssembler(EntityLinks links, MenuIdConverter menuIdConverter) {
        super(MenusController.class, MenuModel.class);
        this.links = links;
        this.menuIdConverter = menuIdConverter;
    }

    @Override
    public MenuModel toModel(Menu menu) {
        return instantiateModel(menu)
                .add(links.linkToItemResource(MenuModel.class, menuIdConverter.convert(menu)).withSelfRel());
    }

    @Override
    public CollectionModel<MenuModel> toCollectionModel(Iterable<? extends Menu> entities) {
        return super.toCollectionModel(entities)
                .add(links.linkToCollectionResource(MenuModel.class).withSelfRel());
    }

    @Override
    protected MenuModel instantiateModel(Menu menu) {
        return new MenuModel(menu);
    }

}
