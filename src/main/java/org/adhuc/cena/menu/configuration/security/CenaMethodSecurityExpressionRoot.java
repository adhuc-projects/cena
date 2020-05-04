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
package org.adhuc.cena.menu.configuration.security;

import static org.adhuc.cena.menu.common.security.RolesDefinition.SUPER_ADMINISTRATOR_ROLE;

import lombok.NonNull;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import org.adhuc.cena.menu.recipes.RecipeId;
import org.adhuc.cena.menu.recipes.RecipeRepository;

/**
 * A custom {@link MethodSecurityExpressionOperations} implementation for specific access controls. This implementation
 * is based on non-public {@link org.springframework.security.access.expression.method.MethodSecurityExpressionRoot MethodSecurityExpressionRoot}
 * class.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.2.0
 */
public class CenaMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private Object filterObject;
    private Object returnObject;
    private Object target;

    private RecipeRepository recipeRepository;

    CenaMethodSecurityExpressionRoot(@NonNull Authentication authentication, @NonNull RecipeRepository recipeRepository) {
        super(authentication);
        this.recipeRepository = recipeRepository;
    }

    public boolean isSuperAdministrator() {
        return isAuthenticated() && hasRole(SUPER_ADMINISTRATOR_ROLE);
    }

    public boolean isRecipeAuthor(RecipeId recipeId) {
        if (!(isAuthenticated() && UserDetails.class.isAssignableFrom(getPrincipal().getClass()))) {
            return false;
        }
        var user = (UserDetails) getPrincipal();
        var recipe = recipeRepository.findNotNullById(recipeId);
        return isSuperAdministrator() || recipe.author().authorName().equals(user.getUsername());
    }

    // Copy pasted code from org.springframework.security.access.expression.method.MethodSecurityExpressionRoot
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    public Object getFilterObject() {
        return filterObject;
    }

    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    public Object getReturnObject() {
        return returnObject;
    }

    void setThis(Object target) {
        this.target = target;
    }

    public Object getThis() {
        return target;
    }

}
