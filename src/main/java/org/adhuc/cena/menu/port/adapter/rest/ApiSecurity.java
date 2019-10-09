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

import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

/**
 * Security configuration for REST API.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
@Configuration
@Order(99)
public class ApiSecurity extends WebSecurityConfigurerAdapter {

    private static final String BASE_API_PATH = "/api/**";
    private static final String BASE_INGREDIENTS_PATH = "/api/ingredients/**";

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        var authorizationConfigurer = http.authorizeRequests();
        authorizationConfigurer.mvcMatchers(HttpMethod.POST, BASE_INGREDIENTS_PATH).authenticated();
        authorizationConfigurer.mvcMatchers(BASE_API_PATH).permitAll();
        http.csrf().disable();
        http.httpBasic().authenticationEntryPoint(new HttpStatusEntryPoint(UNAUTHORIZED));
        http.sessionManagement().sessionCreationPolicy(STATELESS);
    }

}
