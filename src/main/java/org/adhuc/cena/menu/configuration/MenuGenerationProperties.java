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

import lombok.Builder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties to configure the menu generation application.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.0.1
 */
@Data
@ConfigurationProperties(prefix = "cena.menu-generation")
public class MenuGenerationProperties {

    private Documentation documentation = new Documentation();
    private Management management = new Management();
    private Security security = new Security();
    private Features features = new Features();

    @Data
    public static class Documentation {
        /**
         * Enable API documentation endpoint. Default is {@code true}.
         */
        private boolean enabled = true;
        /**
         * Path to documentation resources. Default is '/api/docs'.
         */
        private String path = "/api/docs";
    }

    @Data
    static class Management {
        private Security security = new Security();

        @Data
        static class Security {
            /**
             * Defines management services basic authentication username. Default is 'actuator'.
             */
            private String username = "actuator";
            /**
             * Defines management services basic authentication password. Default is 'actuator'.
             */
            private String password = "actuator";
        }
    }

    @Data
    public static class Security {

        /**
         * Defines user credentials. Default is 'user' username and 'password' password.
         */
        private Credentials user = Credentials.builder().username("user").password("password").build();

        /**
         * Defines ingredient manager credentials. Default is 'ingredient-manager' username and 'ingredient-manager' password.
         */
        private Credentials ingredientManager = Credentials.builder().username("ingredient-manager").password("ingredient-manager").build();

        /**
         * Defines super administrator credentials. Default is 'super-admin' username and 'super-admin' password.
         */
        private Credentials superAdministrator = Credentials.builder().username("super-admin").password("super-admin").build();

        @Data
        @Builder
        public static class Credentials {
            /**
             * The username.
             */
            private String username;
            /**
             * The password.
             */
            private String password;
        }
    }

    @Data
    public static class Features {
        /**
         * Enable ingredients deletion feature. Default is {@code true}.
         */
        private boolean ingredientsDeletion = true;
        /**
         * Enable recipes deletion feature. Default is {@code true}.
         */
        private boolean recipesDeletion = true;
    }

}
