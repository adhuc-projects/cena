PORT=8080
-include .env

DOCKER_COMPOSE_ENV=COMPOSE_PROJECT_NAME=cena API_PORT=$(PORT)

gradle = ./gradlew
ifeq ($(OS),Windows_NT)
	gradle = gradlew.cmd
endif

build: ## Build the application
	$(gradle) test bootJar docker

run: ## Run the application
	$(gradle) bootRun -DAPI_PORT=$(PORT)

runJar: build ## Run the application from the JAR file
	API_PORT=$(PORT) java -jar build/libs/menu-generation.jar

up: ## Start the application and its dependencies
	$(DOCKER_COMPOSE_ENV) docker-compose -f docker-compose.yml -f docker-compose.port.yml up -d

down: ## Stop the application and its dependencies
	$(DOCKER_COMPOSE_ENV) docker-compose -f docker-compose.yml -f docker-compose.port.yml down

acceptance: ## Run acceptance testing
	$(gradle) acceptance aggregate checkOutcomes

clean: ## Clean the project folder
	$(gradle) clean

help: ## This help dialog
	@echo "Usage: make [target]. Find the available targets below:"
	@echo "$$(grep -hE '^\S+:.*##' $(MAKEFILE_LIST) | sed 's/:.*##\s*/:/' | column -c2 -t -s :)"
