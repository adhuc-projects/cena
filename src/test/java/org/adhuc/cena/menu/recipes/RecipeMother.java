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
package org.adhuc.cena.menu.recipes;

/**
 * An object mother to create testing domain elements related to {@link Recipe}s.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @see https://www.martinfowler.com/bliki/ObjectMother.html
 * @since 0.2.0
 */
public class RecipeMother {

    public static final RecipeId TOMATO_CUCUMBER_MOZZA_SALAD_ID = new RecipeId(1);
    public static final String TOMATO_CUCUMBER_MOZZA_SALAD_NAME = "Tomato, cucumber and mozzarella salad";
    public static final String TOMATO_CUCUMER_MOZZA_SALAD_CONTENT = "Cut everything into dices, mix it, dress it";

    public static final RecipeId TOMATO_CUCUMBER_OLIVE_FETA_SALAD_ID = new RecipeId(2);
    public static final String TOMATO_CUCUMBER_OLIVE_FETA_SALAD_NAME = "Tomato, cucumber, olive and feta salad";
    public static final String TOMATO_CUCUMBER_OLIVE_FETA_SALAD_CONTENT = "Stone olives, cut everything into dices, mix it, dress it";

    public static final RecipeId ID = TOMATO_CUCUMBER_MOZZA_SALAD_ID;
    public static final String NAME = TOMATO_CUCUMBER_MOZZA_SALAD_NAME;
    public static final String CONTENT = TOMATO_CUCUMER_MOZZA_SALAD_CONTENT;

    public static Recipe recipe() {
        return recipe(ID, NAME, CONTENT);
    }

    public static Recipe recipe(RecipeId id, String name, String content) {
        return new Recipe(id, name, content);
    }

}
