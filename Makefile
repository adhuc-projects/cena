ifeq ($(OS),Windows_NT)
	gradle = gradlew.cmd
else
	gradle = ./gradlew
endif

build: ## Build the application
	$(gradle) test bootJar docker

run: ## Run the application
	$(gradle) bootRun

up: ## Start the application and its dependencies
	docker-compose -f docker-compose.yml up -d

down: ## Stop the application and its dependencies
	docker-compose -f docker-compose.yml down

acceptance: ## Run acceptance testing
	$(gradle) acceptance aggregate checkOutcomes

clean: ## Clean the project folder
	$(gradle) clean

help: ## This help dialog
	@echo "Usage: make [target]. Find the available targets below:"
	@echo "$$(grep -hE '^\S+:.*##' $(MAKEFILE_LIST) | sed 's/:.*##\s*/:/' | column -c2 -t -s :)"
