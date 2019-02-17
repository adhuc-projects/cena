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
package org.adhuc.cena.menu.steps.serenity;

import static net.serenitybdd.rest.SerenityRest.rest;

import net.thucydides.core.annotations.Step;

/**
 * The documentation service client steps.
 *
 * @author Alexandre Carbenay
 * @version 0.0.1
 * @since 0.0.1
 */
public class DocumentationServiceClientSteps extends AbstractServiceClientSteps {

    @Step("Get documentation")
    public void getDocumentation() {
        rest().get(getDocumentationResourceUrl()).andReturn();
    }

    @Step("Assert documentation is available")
    public void assertDocumentationIsAvailable() {
        assertOk();
    }

    private String getDocumentationResourceUrl() {
        return getApiClientResource().getDocumentation();
    }

}
