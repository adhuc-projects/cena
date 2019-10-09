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
package org.adhuc.cena.menu.configuration;

import static org.adhuc.cena.menu.common.security.RolesDefinition.INGREDIENT_MANAGER_ROLE;

import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Configures application security, by providing a {@link UserDetailsService}.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.1.0
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class ApplicationSecurityConfiguration {

    @Bean
    UserDetailsService userDetailsService(@NonNull PasswordEncoder passwordEncoder,
                                          @NonNull MenuGenerationProperties menuGenerationProperties) {
        var users = User.builder().passwordEncoder(passwordEncoder::encode);
        var manager = new InMemoryUserDetailsManager();
        // TODO configure ingredient manager from properties
        manager.createUser(users.username("ingredientManager").password("ingredientManager").roles(INGREDIENT_MANAGER_ROLE).build());
        manager.createUser(users.username(menuGenerationProperties.getManagement().getSecurity().getUsername())
                .password(menuGenerationProperties.getManagement().getSecurity().getPassword())
                .roles("ACTUATOR").build());
        return manager;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
