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
package org.adhuc.cena.menu;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;
import static org.apache.commons.lang3.ArrayUtils.addAll;
import static org.apache.commons.lang3.ArrayUtils.removeElement;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchRules;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.GeneralCodingRules;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.security.access.prepost.PreAuthorize;

import org.adhuc.cena.menu.common.exception.CenaException;

/**
 * The menu generation architecture tests.
 *
 * @author Alexandre Carbenay
 * @version 0.3.0
 * @since 0.1.0
 */
@AnalyzeClasses(packages = "org.adhuc.cena.menu", importOptions = ImportOption.DoNotIncludeTests.class)
class MenuGenerationArchitectureTests {

    // Menu generation packages
    private static final String CENA_PACKAGES = "org.adhuc.cena.menu..";
    private static final String UTIL_PACKAGES = "org.adhuc.cena.menu.util..";
    private static final String COMMON_PACKAGES = "org.adhuc.cena.menu.common..";
    private static final String INGREDIENT_DOMAIN_PACKAGES = "org.adhuc.cena.menu.ingredients..";
    private static final String RECIPE_DOMAIN_PACKAGES = "org.adhuc.cena.menu.recipes..";
    private static final String MENU_DOMAIN_PACKAGES = "org.adhuc.cena.menu.menus..";
    private static final String[] DOMAIN_PACKAGES = new String[]{
            INGREDIENT_DOMAIN_PACKAGES, RECIPE_DOMAIN_PACKAGES, MENU_DOMAIN_PACKAGES
    };
    private static final String CONFIGURATION_PACKAGES = "org.adhuc.cena.menu.configuration..";
    private static final String ADAPTER_PACKAGES = "org.adhuc.cena.menu.port.adapter..";
    private static final String PERSISTENCE_PACKAGES = "org.adhuc.cena.menu.port.adapter.persistence..";
    private static final String CONTROLLER_PACKAGES = "org.adhuc.cena.menu.port.adapter.rest..";

    // Third party packages
    private static final String JAVA_PACKAGES = "java..";
    private static final String LOGGING_PACKAGES = "org.slf4j..";

    // Object types
    private static final String SERVICE_CLASSES_SUFFIX = "Service";
    private static final String REPOSITORY_CLASSES_SUFFIX = "Repository";

    @ArchTest
    public static final ArchRules generalRules = ArchRules.in(GeneralRules.class);

    @ArchTest
    public static final ArchRules domainDependenciesRules = ArchRules.in(DomainDependenciesRules.class);

    @ArchTest
    public static final ArchRules adaptersRules = ArchRules.in(AdaptersRules.class);

    public static class GeneralRules {

        @ArchTest
        public ArchRule projectShouldNotUseJavaUtilLogging =
                GeneralCodingRules.NO_CLASSES_SHOULD_USE_JAVA_UTIL_LOGGING
                        .because("Project uses SLF4J");

        @ArchTest
        public ArchRule projectShouldNotThrowGenericExceptions =
                GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS
                        .because("Project defines an exception hierarchy");

        @ArchTest
        public static final ArchRule exceptionsShouldDeriveFromCenaException =
                classes().that().haveSimpleNameEndingWith("Exception").and().areNotInterfaces()
                        .should().beAssignableTo(CenaException.class)
                        .because("Project defines an exception hierarchy, with CenaException as a base class");

        @ArchTest
        public ArchRule packagesShouldBeFreeOfCycles =
                slices().matching(CENA_PACKAGES).should().beFreeOfCycles()
                        .because("Project enforces low package coupling through acyclic dependencies principle");

        @ArchTest
        public static final ArchRule commonShouldAccessOtherPackages =
                noClasses().that().resideInAPackage(COMMON_PACKAGES)
                        .should().accessClassesThat().resideOutsideOfPackages(JAVA_PACKAGES, COMMON_PACKAGES, UTIL_PACKAGES)
                        .as("Common classes should not access other domain packages")
                        .because("Common classes are definition classes that participate only in the project structure");

    }

    public static class DomainDependenciesRules {

        @ArchTest
        public static final ArchRule domainShouldNotAccessElementsOutsideOfDomain =
                noClasses().that().resideInAnyPackage(DOMAIN_PACKAGES)
                        .should().accessClassesThat().resideInAnyPackage(CONFIGURATION_PACKAGES, ADAPTER_PACKAGES)
                        .as("Domain classes should not access elements outside of domain packages")
                        .because("Domain must be self contained and not depend on technical aspects");

        @ArchTest
        public static final ArchRule domainShouldAccessLimitedThirdPartyDependencies =
                noClasses().that().resideInAnyPackage(DOMAIN_PACKAGES)
                        .should().accessClassesThat()
                        .resideOutsideOfPackages(addAll(DOMAIN_PACKAGES, JAVA_PACKAGES, LOGGING_PACKAGES,
                                UTIL_PACKAGES, COMMON_PACKAGES))
                        .as("Domain classes should access limited third party dependencies")
                        .because("Domain must not depend on technical aspects");

        @ArchTest
        public static final ArchRule ingredientsShouldNotAccessOtherDomains =
                noClasses().that().resideInAPackage(INGREDIENT_DOMAIN_PACKAGES)
                        .should().accessClassesThat().resideInAnyPackage(
                        removeElement(ArrayUtils.clone(DOMAIN_PACKAGES), INGREDIENT_DOMAIN_PACKAGES))
                        .as("Ingredient domain package should not access other domain packages")
                        .because("Ingredient domain is not dependent on other domains");

        @ArchTest
        public static final ArchRule recipesShouldNotAccessMenus =
                noClasses().that().resideInAPackage(RECIPE_DOMAIN_PACKAGES)
                        .should().accessClassesThat().resideInAPackage(MENU_DOMAIN_PACKAGES)
                        .as("Recipe domain classes should not access menu domain classes")
                        .because("Recipe domain is not dependent on menu domain");

        @ArchTest
        public static final ArchRule menusShouldNotAccessIngredients =
                noClasses().that().resideInAPackage(MENU_DOMAIN_PACKAGES)
                        .should().accessClassesThat().resideInAPackage(INGREDIENT_DOMAIN_PACKAGES)
                        .as("Menu domain classes should not access ingredient domain classes")
                        .because("Menu domain is not directly dependent on ingredient domain");

        @ArchTest
        public static final ArchRule domainClassesShouldNotBeSecuredWithPreAuthorize =
                noClasses().that().resideInAnyPackage(DOMAIN_PACKAGES).and().areNotInterfaces()
                        .should().beAnnotatedWith(PreAuthorize.class)
                        .because("Securing domain methods should be based on specific annotations using ubiquitous language");

        @ArchTest
        public static final ArchRule domainMethodsShouldNotBeSecuredWithPreAuthorize =
                noMembers().that().areDeclaredInClassesThat().resideInAnyPackage(DOMAIN_PACKAGES)
                        .should().beAnnotatedWith(PreAuthorize.class)
                        .because("Securing domain methods should be based on specific annotations using ubiquitous language");

        @ArchTest
        public static final ArchRule domainServicesImplementationsShouldNotBePublic =
                classes().that().resideInAnyPackage(DOMAIN_PACKAGES).and().areNotInterfaces()
                        .and().haveSimpleNameContaining(SERVICE_CLASSES_SUFFIX)
                        .should().notBePublic()
                        .because("Domain services should be exposed only through interfaces");

    }

    public static class AdaptersRules {

        @ArchTest
        public static final ArchRule adaptersShouldNotDependOnEachOther =
                slices().matching("org.adhuc.cena.menu.port.adapter.(*)..")
                        .should().notDependOnEachOther()
                        .because("Adapters must pass through the domain to communicate with each other");

        @ArchTest
        public static final ArchRule controllerShouldNotAccessRepositories =
                noClasses().that().resideInAPackage(CONTROLLER_PACKAGES)
                        .should().accessClassesThat().haveSimpleNameEndingWith(REPOSITORY_CLASSES_SUFFIX)
                        .as("Controller classes should not access repositories")
                        .because("Controller adapters are incoming adapters and must rely only on services");

        @ArchTest
        public static final ArchRule persistenceShouldNotAccessServices =
                noClasses().that().resideInAPackage(PERSISTENCE_PACKAGES)
                        .should().accessClassesThat().haveSimpleNameEndingWith(SERVICE_CLASSES_SUFFIX)
                        .as("Persistence classes should not access services")
                        .because("Persistence adapters are outgoing adapters and must not rely on domain services");

    }

}
