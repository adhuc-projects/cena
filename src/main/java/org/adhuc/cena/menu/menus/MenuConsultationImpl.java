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
package org.adhuc.cena.menu.menus;

import java.util.List;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;

import org.adhuc.cena.menu.common.ApplicationService;

/**
 * A {@link MenuConsultation} implementation.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@RequiredArgsConstructor
@ApplicationService("menuConsultation")
class MenuConsultationImpl implements MenuConsultation {

    @NonNull
    private MenuRepository repository;

    @Override
    @AsMenuOwner
    public List<Menu> getMenus(@P("ownedBy") @NonNull ListMenus query) {
        return List.copyOf(repository.findByOwnerAndDateBetween(query.owner(), query.since(), query.until()));
    }

    @Override
    @AsMenuOwner
    public Menu getMenu(@P("ownedBy") @NonNull MenuId menuId) {
        return repository.findNotNullById(menuId);
    }

}
