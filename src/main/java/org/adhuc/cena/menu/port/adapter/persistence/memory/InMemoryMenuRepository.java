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
package org.adhuc.cena.menu.port.adapter.persistence.memory;

import static java.util.stream.Collectors.toUnmodifiableSet;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import lombok.NonNull;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import org.adhuc.cena.menu.menus.Menu;
import org.adhuc.cena.menu.menus.MenuId;
import org.adhuc.cena.menu.menus.MenuOwner;
import org.adhuc.cena.menu.menus.MenuRepository;

/**
 * An in-memory {@link MenuRepository} implementation.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.3.0
 */
@Repository
@Profile("in-memory")
public class InMemoryMenuRepository implements MenuRepository {

    private Map<MenuId, Menu> menus = new HashMap<>();

    @Override
    public Collection<Menu> findByOwner(@NonNull MenuOwner owner) {
        return menus.values().stream().filter(m -> m.owner().equals(owner)).collect(toUnmodifiableSet());
    }

    @Override
    public Collection<Menu> findByOwnerAndDateBetween(@NonNull MenuOwner owner, @NonNull LocalDate since, @NonNull LocalDate until) {
        return menus.values().stream()
                .filter(m -> m.owner().equals(owner))
                .filter(m -> m.date().compareTo(since) >= 0 && m.date().compareTo(until) <= 0)
                .collect(toUnmodifiableSet());
    }

    @Override
    public boolean exists(MenuId menuId) {
        return menus.containsKey(menuId);
    }

    @Override
    public Optional<Menu> findById(@NonNull MenuId menuId) {
        return Optional.ofNullable(menus.get(menuId));
    }

    @Override
    public <S extends Menu> S save(@NonNull S menu) {
        menus.put(menu.id(), menu);
        return menu;
    }

    @Override
    public void deleteAll() {
        menus.clear();
    }

    @Override
    public void delete(@NonNull Menu menu) {
        menus.remove(menu.id());
    }

}
