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
package org.adhuc.cena.menu.port.adapter.persistence.memory;

import java.util.*;

import lombok.NonNull;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import org.adhuc.cena.menu.common.entity.Name;
import org.adhuc.cena.menu.ingredients.Ingredient;
import org.adhuc.cena.menu.ingredients.IngredientId;
import org.adhuc.cena.menu.ingredients.IngredientRepository;

/**
 * An in-memory {@link IngredientRepository} implementation.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.1.0
 */
@Repository
@Profile("in-memory")
public class InMemoryIngredientRepository implements IngredientRepository {

    private Map<IngredientId, Ingredient> ingredients = new HashMap<>();

    @Override
    public Collection<Ingredient> findAll() {
        return Collections.unmodifiableCollection(ingredients.values());
    }

    @Override
    public boolean exists(IngredientId ingredientId) {
        return ingredients.containsKey(ingredientId);
    }

    @Override
    public Optional<Ingredient> findById(@NonNull IngredientId ingredientId) {
        return Optional.ofNullable(ingredients.get(ingredientId));
    }

    @Override
    public Optional<Ingredient> findByName(Name ingredientName) {
        return ingredients.values().stream().filter(i -> i.name().equals(ingredientName)).findFirst();
    }

    @Override
    public Optional<Ingredient> findByNameIgnoreCase(Name ingredientName) {
        return ingredients.values().stream().filter(i -> i.name().equalsIgnoreCase(ingredientName)).findFirst();
    }

    @Override
    public <S extends Ingredient> S save(@NonNull S ingredient) {
        ingredients.put(ingredient.id(), ingredient);
        return ingredient;
    }

    @Override
    public void deleteAll() {
        ingredients.clear();
    }

    @Override
    public void delete(@NonNull Ingredient ingredient) {
        ingredients.remove(ingredient.id());
    }

}
