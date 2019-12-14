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
package org.adhuc.cena.menu;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * The menu generation architecture tests.
 *
 * @author Alexandre Carbenay
 * @version 0.2.0
 * @since 0.1.0
 */
@AnalyzeClasses(packages = "org.adhuc.cena.menu", importOptions = ImportOption.DoNotIncludeTests.class)
class MenuGenerationArchitectureTests {

    // Menu generation packages
    private static final String COMMON_DOMAIN_PACKAGE = "org.adhuc.cena.menu.common..";
    private static final String INGREDIENT_DOMAIN_PACKAGES = "org.adhuc.cena.menu.ingredients..";
    private static final String RECIPE_DOMAIN_PACKAGES = "org.adhuc.cena.menu.recipes..";

    private static final String UTIL_PACKAGES = "org.adhuc.cena.menu.util..";
    private static final String CONFIGURATION_PACKAGES = "org.adhuc.cena.menu.configuration..";
    private static final String ADAPTER_PACKAGES = "org.adhuc.cena.menu.port.adapter..";
    private static final String PERSISTENCE_PACKAGES = "org.adhuc.cena.menu.port.adapter.persistence..";
    private static final String CONTROLLER_PACKAGES = "org.adhuc.cena.menu.port.adapter.rest..";

    // Third party packages
    private static final String JAVA_PACKAGES = "java..";
    private static final String LOGGING_PACKAGES = "org.slf4j..";

    // Object types
    private static final String SERVICE_CLASSES_SUFFIX = "Service";

    @ArchTest
    public static final ArchRule domainShouldNotAccessElementsOutsideOfDomain =
            noClasses().that()
                    .resideInAnyPackage(
                            COMMON_DOMAIN_PACKAGE,
                            INGREDIENT_DOMAIN_PACKAGES,
                            RECIPE_DOMAIN_PACKAGES)
                    .should()
                    .accessClassesThat()
                    .resideInAnyPackage(
                            CONFIGURATION_PACKAGES,
                            ADAPTER_PACKAGES);

    @ArchTest
    public static final ArchRule persistenceShouldNotAccessServices =
            noClasses().that()
                    .resideInAPackage(PERSISTENCE_PACKAGES)
                    .should()
                    .accessClassesThat()
                    .haveSimpleNameEndingWith(SERVICE_CLASSES_SUFFIX);

    @ArchTest
    public static final ArchRule persistenceShouldNotAccessControllers =
            noClasses().that()
                    .resideInAPackage(PERSISTENCE_PACKAGES)
                    .should()
                    .accessClassesThat()
                    .resideInAPackage(CONTROLLER_PACKAGES);

    @ArchTest
    public static final ArchRule controllersShouldNotAccessPersistence =
            noClasses().that()
                    .resideInAPackage(CONTROLLER_PACKAGES)
                    .should()
                    .accessClassesThat()
                    .resideInAPackage(PERSISTENCE_PACKAGES);

    @ArchTest
    public static final ArchRule domainShouldAccessToLimitedThirdPartyDependencies =
            noClasses().that()
                    .resideInAnyPackage(
                            COMMON_DOMAIN_PACKAGE,
                            INGREDIENT_DOMAIN_PACKAGES,
                            RECIPE_DOMAIN_PACKAGES)
                    .should()
                    .accessClassesThat()
                    .resideOutsideOfPackages(
                            COMMON_DOMAIN_PACKAGE,
                            INGREDIENT_DOMAIN_PACKAGES,
                            RECIPE_DOMAIN_PACKAGES,
                            UTIL_PACKAGES,
                            JAVA_PACKAGES,
                            LOGGING_PACKAGES);

    @ArchTest
    public static final ArchRule ingredientsShouldNotAccessToRecipes =
            noClasses().that()
                    .resideInAPackage(INGREDIENT_DOMAIN_PACKAGES)
                    .should()
                    .accessClassesThat()
                    .resideInAPackage(RECIPE_DOMAIN_PACKAGES);

}
