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
package org.adhuc.cena.menu.port.adapter.rest.recipes;

import java.util.Collections;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.NonNull;
import lombok.ToString;

import org.adhuc.cena.menu.common.aggregate.Name;
import org.adhuc.cena.menu.recipes.*;

/**
 * A request to create a recipe.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.2.0
 */
@ToString
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class CreateRecipeRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String content;
    @Positive
    private Integer servings;
    private Set<CourseType> courseTypes;

    /**
     * Converts this request to a {@code CreateRecipe} command.
     *
     * @param id         the recipe identity.
     * @param authorName the recipe author name.
     * @return the recipe creation command.
     */
    CreateRecipe toCommand(@NonNull RecipeId id, @NonNull String authorName) {
        Set<CourseType> courseTypes = this.courseTypes != null ? this.courseTypes : Collections.emptySet();
        return servings != null
                ? new CreateRecipe(id, new Name(name), content, new RecipeAuthor(authorName), new Servings(servings), courseTypes)
                : new CreateRecipe(id, new Name(name), content, new RecipeAuthor(authorName), courseTypes);
    }

}
