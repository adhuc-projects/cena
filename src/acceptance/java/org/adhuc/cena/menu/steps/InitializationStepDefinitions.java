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
package org.adhuc.cena.menu.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.cucumber.java.Before;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.rest.SerenityRest;

import org.adhuc.cena.menu.steps.serenity.support.authentication.AuthenticationProvider;

/**
 * An initialization step definitions, configuring rest-assured properties.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.0.1
 */
public class InitializationStepDefinitions {

    @Before
    public void init() {
        Serenity.initializeTestSession();
        initializeRestClient();

        AuthenticationProvider.instance().clean();
    }

    private void initializeRestClient() {
        SerenityRest.reset();
        RestAssured.config = RestAssuredConfig.config().objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory(
                (type, s) -> {
                    ObjectMapper om = new ObjectMapper().findAndRegisterModules();
                    om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
                    return om;
                }
        ));
    }

}
