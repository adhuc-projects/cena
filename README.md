# Cena - Menu generation

This project contains all the sources for the cena's menu generation.

## [TL;DR]

Considering you have installed the [prerequesite tools](#general-prerequisites) `Make`, `Docker` and `Java` follow these steps to get started:

- To build the project, from the root folder, run:
`make`

- To start up the application and its dependencies, run:
`make up`

- You can access the API documentation page at the following address: [`http://localhost:8080/api/docs/api-guide.html`](http://localhost:8080/api/docs/api-guide.html)

- To stop the application and its dependencies, run: `make down`

## Project resources

- [Github](https://github.com/adhuc-projects/cena)
- [Travis CI](https://travis-ci.com/adhuc-projects/cena/)
- [Codacy](https://app.codacy.com/project/adhuc-projects/cena/dashboard)

## General prerequisites

### Tools installation

- [Git](http://help.github.com/set-up-git-redirect)
- [Open JDK 11](https://openjdk.java.net/install/)
- [Docker](https://www.docker.com/) and [docker-compose](https://docs.docker.com/compose/install/)

### Makefile

The following sections propose to use `Makefile` targets in the form of`make` commands to abstract development lifecycle goals from technologies (gradle, docker,...) and practical details of implementation (locations, profiles,...).

## Usage

This project is designed to be usable in both development situation and in classical gradle build.

### Development Usage

Execute `org.adhuc.cena.menu.MenuGenerationApplication` in your favorite IDE. The application uses [Spring Devtools](https://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-devtools.html), which means that the application does not need to be restarted after each modification in the project source, but will be re-compiled and reloaded each time.

### Build

From `cena` project folder, execute either `make` or `make build` command.

This build results in a `cena/build/libs/menu-generation.jar` JAR file and a `menu-generation:latest` docker image, based on a `openjdk:11-slim` image. More information about this built image can be found in `cena/Dockerfile` file.

See [Running jar](#running-jar) section to know how to run directly from JAR file, or [Running with docker-compose](#running-with-docker-compose) section to know how to run the dockerized environment.

### Execution

#### Environment Configuration

The project contains a `Makefile` to execute different targets for build and execution. Execution targets enable configuring local environment. To override default values, create a `.env` file in the project root folder, then complete the file with the following (with default values) and adapt :

```
PORT=8080
```

#### Running with gradle

The application can run directly from the compiled classes using [Gradle Spring Boot plugin](https://docs.spring.io/spring-boot/docs/2.1.2.RELEASE/gradle-plugin/reference/html/). From `cena` project folder, execute `make run` command.

Once started, the application will be available at [http://localhost:8080](http://localhost:8080), or the URL corresponding to the port configured in the `.env` file.

#### Running jar

Once the build ends successfully (see [Build](#build) section), the application can be started using `make runJar` command.

Once started, the application will be available at [http://localhost:8080](http://localhost:8080), or the URL corresponding to the port configured in the `.env` file.

#### Running with docker-compose

Once the build ends successfully (see [Build](#build) section), a docker environment can be executed using `make up` command. The `docker-compose` configuration files are located at `./docker-compose.yml` and `./docker-compose.port.yml`. The latter contains port binding to ensure that application is available at [http://localhost:8080](http://localhost:8080), or the URL corresponding to the port configured in the `.env` file.

The docker environment can be stopped using `make down` command.

## Acceptance tests

From `cena` project folder, execute `make acceptance` command.

The acceptance tests are run against a docker-compose environment (with random ports). The acceptance tests results are available through `cena/build/reports/serenity/index.html` page.

## Restful API documentation

The API documentation is generated using [Spring-RestDocs](http://projects.spring.io/spring-restdocs/).

Once the application is started (see [Running with docker-compose](#running-with-docker-compose) section), the Rest API documentation will be available at [`http://localhost:8080/api/docs/api-guide.html`](http://localhost:8080/api/docs/api-guide.html).
