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
package org.adhuc.cena.menu.steps.serenity.ingredients;

import java.util.Collection;
import java.util.Optional;

import lombok.experimental.Delegate;
import net.thucydides.core.annotations.Step;

import org.adhuc.cena.menu.steps.serenity.support.ResourceUrlResolverDelegate;

/**
 * The ingredients list rest-service client steps definition.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.1.0
 */
public class IngredientListSteps {

    private final ResourceUrlResolverDelegate resourceUrlResolverDelegate = new ResourceUrlResolverDelegate();
    private final IngredientListClientDelegate listClient = new IngredientListClientDelegate(
            resourceUrlResolverDelegate.ingredientsResourceUrl());
    @Delegate
    private final IngredientStorageDelegate ingredientStorage = new IngredientStorageDelegate();

    @Step("Get ingredients list")
    public Collection<IngredientValue> getIngredients() {
        return listClient.fetchIngredients();
    }

    @Step("Get ingredient corresponding to ingredient {0}")
    public Optional<IngredientValue> getCorrespondingIngredient(IngredientValue ingredient) {
        return listClient.getFromIngredientsList(ingredient);
    }

    public String ingredientsResourceUrl() {
        return resourceUrlResolverDelegate.ingredientsResourceUrl();
    }

}
