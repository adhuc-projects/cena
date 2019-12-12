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

import java.util.*;

import lombok.NonNull;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import org.adhuc.cena.menu.recipes.Recipe;
import org.adhuc.cena.menu.recipes.RecipeId;
import org.adhuc.cena.menu.recipes.RecipeRepository;

/**
 * An in-memory {@link RecipeRepository} implementation.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
@Repository
@Profile("in-memory")
public class InMemoryRecipeRepository implements RecipeRepository {

    private Map<RecipeId, Recipe> recipes = new HashMap<>();

    @Override
    public Collection<Recipe> findAll() {
        return Collections.unmodifiableCollection(recipes.values());
    }

    @Override
    public boolean exists(RecipeId id) {
        return recipes.containsKey(id);
    }

    @Override
    public Optional<Recipe> findById(@NonNull RecipeId recipeId) {
        return Optional.ofNullable(recipes.get(recipeId));
    }

    @Override
    public <S extends Recipe> S save(@NonNull S recipe) {
        recipes.put(recipe.id(), recipe);
        return recipe;
    }

    @Override
    public void deleteAll() {
        recipes.clear();
    }

    @Override
    public void delete(@NonNull Recipe recipe) {
        recipes.remove(recipe.id());
    }

}
