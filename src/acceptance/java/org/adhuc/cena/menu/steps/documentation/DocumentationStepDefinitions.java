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
package org.adhuc.cena.menu.steps.documentation;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.runtime.java.StepDefAnnotation;
import net.thucydides.core.annotations.Steps;

import org.adhuc.cena.menu.steps.serenity.DocumentationSteps;

/**
 * The documentation steps definitions for rest-services acceptance tests.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.0.1
 */
@StepDefAnnotation
public class DocumentationStepDefinitions {

    @Steps
    private DocumentationSteps documentation;

    @When("^I access to the documentation$")
    public void accessToDocumentation() {
        documentation.getDocumentation();
    }

    @When("^I access to the OpenAPI specification$")
    public void accessToOpenApiSpecification() {
        documentation.getOpenApiSpecification();
    }

    @Then("^the documentation is available$")
    public void documentationIsAvailable() {
        documentation.assertDocumentationIsAvailable();
    }

    @Then("^the OpenAPI specification is available$")
    public void openApiSpecificationIsAvailable() {
        documentation.assertOpenApiSpecificationIsAvailable();
    }

}
