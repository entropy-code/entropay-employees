.PHONY: setup
setup: ## Start database and build the application
	docker-compose up -d postgres pgweb
	./mvnw clean package -DskipTests

.PHONY: run
run: ## Run the application on http://localhost:8100
	./mvnw spring-boot:run

.PHONY: test
test: ## Run all tests
	./mvnw test
