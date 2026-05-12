.PHONY: setup
setup: ## Start database and build the application
	docker compose up -d postgres pgweb
	@[ -f src/main/resources/application-local.properties ] || \
		cp src/main/resources/application-local.properties.template src/main/resources/application-local.properties
	./mvnw clean package -DskipTests

.PHONY: run
run: ## Run the application on http://localhost:8100
	./mvnw spring-boot:run -Dspring-boot.run.profiles=local

.PHONY: test
test: ## Run all tests
	./mvnw test
