.PHONY: setup
setup: ## Start database and build the application
ifeq ($(SKIP_DOCKER),true)
	@echo "Skipping Docker services; using existing local dependencies"
else
	docker compose up -d postgres pgweb
endif
	@[ -f src/main/resources/application-local.properties ] || \
		touch src/main/resources/application-local.properties
	./mvnw clean package -DskipTests

.PHONY: run
run: ## Run the application on http://localhost:8100
	./mvnw spring-boot:run -Dspring-boot.run.profiles=local

.PHONY: test
test: ## Run all tests
	./mvnw test
