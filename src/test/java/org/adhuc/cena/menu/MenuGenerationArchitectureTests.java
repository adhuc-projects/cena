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

import static com.tngtech.archunit.base.DescribedPredicate.anyElementThat;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.*;
import static com.tngtech.archunit.core.domain.properties.CanBeAnnotated.Predicates.annotatedWith;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;
import static org.apache.commons.lang3.ArrayUtils.addAll;
import static org.apache.commons.lang3.ArrayUtils.removeElement;

import java.lang.annotation.Annotation;
import java.util.Collection;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchRules;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.GeneralCodingRules;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import org.adhuc.cena.menu.common.ApplicationService;
import org.adhuc.cena.menu.common.DomainService;
import org.adhuc.cena.menu.common.aggregate.Command;
import org.adhuc.cena.menu.common.aggregate.Query;
import org.adhuc.cena.menu.common.exception.CenaException;
import org.adhuc.cena.menu.ingredients.Ingredient;
import org.adhuc.cena.menu.menus.Menu;
import org.adhuc.cena.menu.recipes.Recipe;

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
    private static final String[] INCOMING_ADAPTERS_PACKAGES = new String[]{CONTROLLER_PACKAGES};
    private static final String[] OUTGOING_ADAPTERS_PACKAGES = new String[]{PERSISTENCE_PACKAGES};

    // Third party packages
    private static final String JAVA_PACKAGES = "java..";
    private static final String LOGGING_PACKAGES = "org.slf4j..";

    // Object types
    private static final String REPOSITORY_CLASSES_SUFFIX = "Repository";

    @ArchTest
    public static final ArchRules generalRules = ArchRules.in(GeneralRules.class);

    @ArchTest
    public static final ArchRules domainServiceRules = ArchRules.in(DomainServiceRules.class);

    @ArchTest
    public static final ArchRules domainModelRules = ArchRules.in(DomainModelRules.class);

    @ArchTest
    public static final ArchRules inMemoryRepositoriesRules = ArchRules.in(InMemoryRepositoryRules.class);

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
        public static final ArchRule commonShouldNotAccessOtherDomainPackages =
                noClasses().that().resideInAPackage(COMMON_PACKAGES)
                        .should().accessClassesThat().resideOutsideOfPackages(JAVA_PACKAGES, COMMON_PACKAGES, UTIL_PACKAGES)
                        .as("Common classes should not access other domain packages")
                        .because("Common classes are definition classes that participate only in the project structure");

        @ArchTest
        public static final ArchRule repositoryShouldFollowNamingConvention =
                classes().that().areNotInterfaces().and().areAnnotatedWith(Repository.class)
                        .should().haveSimpleNameEndingWith(REPOSITORY_CLASSES_SUFFIX)
                        .because("Repositories must respect naming convention");

        @ArchTest
        public static final ArchRule repositoryInterfacesShouldFollowNamingConvention =
                classes().that(isInterfaceImplementedByClassAnnotatedWith(Repository.class))
                        .should().haveSimpleNameEndingWith(REPOSITORY_CLASSES_SUFFIX)
                        .because("Repositories must respect naming convention");

    }

    public static class DomainServiceRules {

        @ArchTest
        public static final ArchRule domainServiceShouldNotBeSecuredWithPreAuthorize =
                noClasses().that().resideInAnyPackage(DOMAIN_PACKAGES).and().areNotInterfaces()
                        .should().beAnnotatedWith(PreAuthorize.class)
                        .because("Securing domain methods should be based on specific annotations using ubiquitous language");

        @ArchTest
        public static final ArchRule domainServiceMethodsShouldNotBeSecuredWithPreAuthorize =
                noMembers().that().areDeclaredInClassesThat().resideInAnyPackage(DOMAIN_PACKAGES)
                        .should().beAnnotatedWith(PreAuthorize.class)
                        .because("Securing domain methods should be based on specific annotations using ubiquitous language");

        @ArchTest
        public static final ArchRule serviceShouldNotBeAnnotatedWithSpringService =
                noClasses().that().resideInAnyPackage(DOMAIN_PACKAGES)
                        .should().beAnnotatedWith(Service.class)
                        .because("Application and domain services should be annotated with respective dedicated annotation");

        @ArchTest
        public static final ArchRule applicationServiceShouldBePartOfDomain =
                noClasses().that().resideOutsideOfPackages(DOMAIN_PACKAGES)
                        .should().beAnnotatedWith(ApplicationService.class)
                        .because("Application services are part of the domain model");

        @ArchTest
        public static final ArchRule applicationServicesImplementationsShouldNotBePublic =
                classes().that().resideInAnyPackage(DOMAIN_PACKAGES).and().areAnnotatedWith(ApplicationService.class)
                        .should().notBePublic()
                        .because("Application services should be exposed only through interfaces");

        @ArchTest
        public static final ArchRule applicationServicesInterfacesShouldBePublic =
                classes().that().resideInAnyPackage(DOMAIN_PACKAGES)
                        .and(isInterfaceImplementedByClassAnnotatedWith(ApplicationService.class))
                        .should().bePublic()
                        .because("Application services should be exposed only through interfaces");

        @ArchTest
        public static final ArchRule domainServiceShouldBePartOfDomain =
                noClasses().that().resideOutsideOfPackages(DOMAIN_PACKAGES)
                        .should().beAnnotatedWith(DomainService.class)
                        .because("Domain services are part of the domain model");

        @ArchTest
        public static final ArchRule domainServicesImplementationsShouldNotBePublic =
                classes().that().resideInAnyPackage(DOMAIN_PACKAGES).and().areAnnotatedWith(DomainService.class)
                        .should().notBePublic()
                        .because("Domain services should not be exposed outside of the domain model");

        @ArchTest
        public static final ArchRule repositoryInterfaceShouldBePartOfDomain =
                classes().that(isInterfaceImplementedByClassAnnotatedWith(Repository.class))
                        .should().resideInAnyPackage(addAll(DOMAIN_PACKAGES, COMMON_PACKAGES))
                        .because("Repositories interfaces are part of the domain model");

        @ArchTest
        public static final ArchRule repositoryInterfaceShouldBePublic =
                classes().that().resideInAnyPackage(DOMAIN_PACKAGES).and(isInterfaceImplementedByClassAnnotatedWith(Repository.class))
                        .should().bePublic()
                        .because("Repository interfaces should be exposed publicly for outgoing adapters implementations");

    }

    public static class DomainModelRules {

        @ArchTest
        public static final ArchRule commandShouldBePartOfDomain =
                noClasses().that().resideOutsideOfPackages(DOMAIN_PACKAGES)
                        .should().beAnnotatedWith(Command.class)
                        .because("Commands are part of the domain model");

        @ArchTest
        public static final ArchRule queryShouldBePartOfDomain =
                noClasses().that().resideOutsideOfPackages(DOMAIN_PACKAGES)
                        .should().beAnnotatedWith(Query.class)
                        .because("Queries are part of the domain model");

        @ArchTest
        public static final ArchRule commandObjectShouldBeImmutable =
                classes().that().resideInAnyPackage(DOMAIN_PACKAGES).and().areAnnotatedWith(Command.class)
                        .should().haveOnlyFinalFields()
                        .because("Command objects should be value objects encapsulating information to execute a command");

        @ArchTest
        public static final ArchRule queryObjectShouldBeImmutable =
                classes().that().resideInAnyPackage(DOMAIN_PACKAGES).and().areAnnotatedWith(Query.class)
                        .should().haveOnlyFinalFields()
                        .because("Query objects should be value objects encapsulating information to execute a query");

        @ArchTest
        public static final ArchRule commandHandlersShouldReturnVoid =
                methods().that().areDeclaredInClassesThat().resideInAnyPackage(DOMAIN_PACKAGES)
                        .and().haveRawParameterTypes(anyElementThat(annotatedWith(Command.class)))
                        .and().areNotPrivate()
                        .should().notHaveRawReturnType(assignableTo(Object.class))
                        .because("Command handlers in command-query separation should return void");

        @ArchTest
        public static final ArchRule queryHandlersShouldReturnQueryResult =
                methods().that().areDeclaredInClassesThat().resideInAnyPackage(DOMAIN_PACKAGES)
                        .and().haveRawParameterTypes(anyElementThat(annotatedWith(Query.class)))
                        .and().areNotPrivate()
                        .should().haveRawReturnType(Ingredient.class)
                        .orShould().haveRawReturnType(Recipe.class)
                        .orShould().haveRawReturnType(Menu.class)
                        .orShould().haveRawReturnType(assignableTo(Collection.class))
                        .because("Query handlers in command-query separation should return query result");

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

    }

    public static class InMemoryRepositoryRules {

        private static final String IN_MEMORY_REPO_PREFIX = "InMemory";

        @ArchTest
        public static final ArchRule inMemoryRepositoryShouldBePartOfDomain =
                noClasses().that().areAnnotatedWith(Repository.class).and().haveSimpleNameStartingWith(IN_MEMORY_REPO_PREFIX)
                        .should().resideOutsideOfPackages(DOMAIN_PACKAGES)
                        .because("In-memory repository implementations are part of the domain, for testability reasons");

        @ArchTest
        public static final ArchRule inMemoryRepositoryShouldImplementRepository =
                classes().that().areAnnotatedWith(Repository.class).and().haveSimpleNameStartingWith(IN_MEMORY_REPO_PREFIX)
                        .should().implement(simpleNameEndingWith(REPOSITORY_CLASSES_SUFFIX))
                        .because("In-memory repository implementation must implement a domain repository");

        @ArchTest
        public static final ArchRule inMemoryRepositoryShouldNotBePublic =
                classes().that().resideInAnyPackage(DOMAIN_PACKAGES)
                        .and().haveSimpleNameStartingWith(IN_MEMORY_REPO_PREFIX)
                        .and().areAnnotatedWith(Repository.class)
                        .should().notBePublic()
                        .because("In-memory repository implementations should not be exposed outside of the domain model");

        @ArchTest
        public static final ArchRule inMemoryRepositoryShouldHaveLimitedScope =
                classes().that().resideInAnyPackage(DOMAIN_PACKAGES).and().haveSimpleNameStartingWith(IN_MEMORY_REPO_PREFIX)
                        .should().beAnnotatedWith(Profile.class)
                        .because("In-memory repository implementations should have limited scope in spring context");

    }

    public static class AdaptersRules {

        @ArchTest
        public static final ArchRule adaptersShouldNotDependOnEachOther =
                slices().matching("org.adhuc.cena.menu.port.adapter.(*)..")
                        .should().notDependOnEachOther()
                        .because("Adapters must pass through the domain to communicate with each other");

        @ArchTest
        public static final ArchRule incomingAdaptersShouldNotAccessOutgoingAdapters =
                noClasses().that().resideInAnyPackage(INCOMING_ADAPTERS_PACKAGES)
                        .should().accessClassesThat().resideInAnyPackage(OUTGOING_ADAPTERS_PACKAGES)
                        .as("Controller classes should not access repositories")
                        .because("Controller adapters are incoming adapters and must rely only on services");

        @ArchTest
        public static final ArchRule outgoingAdaptersShouldNotAccessServices =
                noClasses().that().resideInAnyPackage(OUTGOING_ADAPTERS_PACKAGES)
                        .should().accessClassesThat(resideInAnyPackage(DOMAIN_PACKAGES)
                        .and(isInterfaceImplementedByClassAnnotatedWith(ApplicationService.class)))
                        .as("Persistence classes should not access services")
                        .because("Persistence adapters are outgoing adapters and must not rely on domain services");

    }

    private static DescribedPredicate<JavaClass> isInterfaceImplementedByClassAnnotatedWith(Class<? extends Annotation> annotationType) {
        return INTERFACES.and(assignableFrom(annotatedWith(annotationType)));
    }

}
