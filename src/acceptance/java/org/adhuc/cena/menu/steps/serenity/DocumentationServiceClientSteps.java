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

import lombok.experimental.Delegate;
import net.thucydides.core.annotations.Step;

import org.adhuc.cena.menu.steps.serenity.support.ResourceUrlResolverDelegate;
import org.adhuc.cena.menu.steps.serenity.support.RestClientDelegate;
import org.adhuc.cena.menu.steps.serenity.support.StatusAssertionDelegate;

/**
 * The documentation service client steps.
 *
 * @author Alexandre Carbenay
 * @version 0.1.0
 * @since 0.0.1
 */
public class DocumentationServiceClientSteps {

    @Delegate
    private final RestClientDelegate restClientDelegate = new RestClientDelegate();
    @Delegate
    private final ResourceUrlResolverDelegate resourceUrlResolverDelegate = new ResourceUrlResolverDelegate();
    @Delegate
    private final StatusAssertionDelegate statusAssertionDelegate = new StatusAssertionDelegate();

    @Step("Get documentation")
    public void getDocumentation() {
        var documentationUrl = getDocumentationResourceUrl();
        rest().get(documentationUrl);
    }

    @Step("Get OpenAPI specification")
    public void getOpenApiSpecification() {
        var openApiUrl = getOpenApiResourceUrl();
        rest().get(openApiUrl);
    }

    @Step("Assert documentation is available")
    public void assertDocumentationIsAvailable() {
        assertOk();
    }

    @Step("Assert OpenAPI specification is available")
    public void assertOpenApiSpecificationIsAvailable() {
        assertOk();
    }

    private String getDocumentationResourceUrl() {
        return apiClientResource().getDocumentation();
    }

    private String getOpenApiResourceUrl() {
        return apiClientResource().getOpenApi();
    }
}
