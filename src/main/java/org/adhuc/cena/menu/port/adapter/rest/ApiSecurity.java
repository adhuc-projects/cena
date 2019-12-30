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
package org.adhuc.cena.menu.port.adapter.rest;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import static org.adhuc.cena.menu.common.security.RolesDefinition.INGREDIENT_MANAGER_ROLE;
import static org.adhuc.cena.menu.common.security.RolesDefinition.SUPER_ADMINISTRATOR_ROLE;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

/**
 * Security configuration for REST API.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.1.0
 */
@Configuration
@Order(99)
public class ApiSecurity extends WebSecurityConfigurerAdapter {

    private static final String WILDCARD = "/**";
    private static final String BASE_API_PATH = "/api" + WILDCARD;
    private static final String INGREDIENTS_PATH = "/api/ingredients";
    private static final String BASE_INGREDIENTS_PATH = INGREDIENTS_PATH + WILDCARD;
    private static final String RECIPES_PATH = "/api/recipes";
    private static final String BASE_RECIPES_PATH = RECIPES_PATH + WILDCARD;
    private static final String RECIPE_INGREDIENTS_PATH = "/api/recipes/*/ingredients";
    private static final String BASE_RECIPE_INGREDIENTS_PATH = RECIPE_INGREDIENTS_PATH + WILDCARD;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        var authorizationConfigurer = http.authorizeRequests();
        authorizationConfigurer
                // Ingredients resources
                .mvcMatchers(POST, INGREDIENTS_PATH).hasAnyRole(INGREDIENT_MANAGER_ROLE, SUPER_ADMINISTRATOR_ROLE)
                .mvcMatchers(DELETE, INGREDIENTS_PATH).hasRole(SUPER_ADMINISTRATOR_ROLE)
                .mvcMatchers(DELETE, BASE_INGREDIENTS_PATH).hasAnyRole(INGREDIENT_MANAGER_ROLE, SUPER_ADMINISTRATOR_ROLE)
                // Recipe ingredients resources
                .mvcMatchers(POST, RECIPE_INGREDIENTS_PATH).authenticated()
                .mvcMatchers(DELETE, RECIPE_INGREDIENTS_PATH).authenticated()
                .mvcMatchers(DELETE, BASE_RECIPE_INGREDIENTS_PATH).authenticated()
                // Recipes resources
                .mvcMatchers(POST, RECIPES_PATH).authenticated()
                .mvcMatchers(DELETE, RECIPES_PATH).hasRole(SUPER_ADMINISTRATOR_ROLE)
                .mvcMatchers(DELETE, BASE_RECIPES_PATH).authenticated()
                // Other resources
                .mvcMatchers(BASE_API_PATH).permitAll();
        http.csrf().disable();
        http.httpBasic().authenticationEntryPoint(new HttpStatusEntryPoint(UNAUTHORIZED));
        http.sessionManagement().sessionCreationPolicy(STATELESS);
    }

}
