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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import lombok.NonNull;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import org.adhuc.cena.menu.ingredients.Ingredient;
import org.adhuc.cena.menu.ingredients.IngredientRepository;

/**
 * An in-memory {@link IngredientRepository} implementation.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
@Repository
@Profile("in-memory")
public class InMemoryIngredientRepository implements IngredientRepository {

    private List<Ingredient> ingredients = new ArrayList<>();

    @Override
    public Collection<Ingredient> findAll() {
        return Collections.unmodifiableList(ingredients);
    }

    @Override
    public <S extends Ingredient> S save(@NonNull S ingredient) {
        ingredients.add(ingredient);
        return ingredient;
    }

    @Override
    public void deleteAll() {
        ingredients.clear();
    }

}
