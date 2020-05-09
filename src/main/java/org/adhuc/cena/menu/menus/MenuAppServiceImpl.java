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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import org.adhuc.cena.menu.common.security.AsAuthenticatedUser;

/**
 * A {@link MenuAppService} implementation.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
class MenuAppServiceImpl implements MenuAppService {

    @NonNull
    private MenuCreationService menuCreationService;
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

    @Override
    @AsAuthenticatedUser
    public void createMenu(@NonNull CreateMenu command) {
        log.info("Create menu from command {}", command);
        menuCreationService.createMenu(command);
    }

    @Override
    public void deleteMenu(@NonNull DeleteMenu command) {
        repository.delete(repository.findNotNullById(command.menuId()));
    }

}
