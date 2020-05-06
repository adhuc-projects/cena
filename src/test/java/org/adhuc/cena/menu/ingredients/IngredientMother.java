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
package org.adhuc.cena.menu.ingredients;

import static org.adhuc.cena.menu.ingredients.MeasurementType.COUNT;
import static org.adhuc.cena.menu.ingredients.MeasurementType.WEIGHT;

import java.util.List;

import org.adhuc.cena.menu.common.entity.Name;

/**
 * An object mother to create testing domain elements related to {@link Ingredient}s.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @see https://www.martinfowler.com/bliki/ObjectMother.html
 * @since 0.1.0
 */
public class IngredientMother {

    public static final IngredientId TOMATO_ID = new IngredientId("3fa85f64-5717-4562-b3fc-2c963f66afa6");
    public static final IngredientId CUCUMBER_ID = new IngredientId("168c9f6d-dda3-4fde-a0b0-28934fe9eb9b");

    public static final Name TOMATO = new Name("Tomato");
    public static final Name CUCUMBER = new Name("Cucumber");

    public static final List<MeasurementType> TOMATO_MEASUREMENT_TYPES = List.of(WEIGHT, COUNT);
    public static final List<MeasurementType> CUCUMBER_MEASUREMENT_TYPES = List.of(COUNT);

    public static final IngredientId ID = TOMATO_ID;
    public static final Name NAME = TOMATO;
    public static final List<MeasurementType> MEASUREMENT_TYPES = TOMATO_MEASUREMENT_TYPES;

    public static CreateIngredient createCommand() {
        return createCommand(ingredient());
    }

    public static CreateIngredient createCommand(Name name) {
        return createCommand(ingredient(ID, name, List.of()));
    }

    public static CreateIngredient createCommand(Ingredient ingredient) {
        return new CreateIngredient(ingredient.id(), ingredient.name(), ingredient.measurementTypes());
    }

    public static DeleteIngredient deleteCommand() {
        return deleteCommand(TOMATO_ID);
    }

    public static DeleteIngredient deleteCommand(IngredientId ingredientId) {
        return new DeleteIngredient(ingredientId);
    }

    public static Ingredient ingredient() {
        return ingredient(ID, NAME, MEASUREMENT_TYPES);
    }

    public static Ingredient ingredient(IngredientId id, Name name, List<MeasurementType> measurementTypes) {
        return new Ingredient(id, name, measurementTypes);
    }

}
