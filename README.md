This is a simple eCommerce POC API built with:

Java 21
Spring Boot
Spring Security
JWT Authentication
PostgreSQL
Docker
Swagger/OpenAPI

The API allows users to:

create an account
authenticate using JWT
browse products
search and filter products
manage a shopping cart
manage a wishlist

The project also includes:

pagination
validation
global exception handling
DTO projections
unit testing with Spock
Swagger documentation


Running The Project Locally
1. Download The Project

Download and extract the project zip file.

2. Start Docker Containers

Navigate to the docker folder:

cd docker

Run:

docker compose up

This will start:

PostgreSQL database
pgAdmin (optional if configured)

3. Add Jwt Secret
Go to application.yaml and change the jwtSecret property to an actual secret key

4. Start The Spring Boot Application

Go back to the root project folder and run:

./mvnw spring-boot:run

Or using Maven:

mvn spring-boot:run

5. Swagger Documentation
Once the application is running:

http://localhost:8080/swagger-ui/index.html
Authentication Flow
Create an account using:
POST /api/auth/account
Authenticate using:
POST /api/auth/token
Copy the returned JWT token.
Click the Authorize button in Swagger and paste:
YOUR_TOKEN

6. Default Admin User
For simplicity in this POC:

admin@admin.com

is considered an administrator and can:
create products
update products
delete products

7. POC Products API — Future Improvements & Scalability Roadmap
The current implementation already includes:

JWT authentication
Swagger/OpenAPI documentation
Product management
Cart management
Wishlist management
Pagination & filtering
DTO projections
Validation
Global exception handling
PostgreSQL persistence
Clean REST architecture

The following improvements would further strengthen scalability, maintainability, resilience, observability, and performance.
- Search Engine Integration (Elasticsearch / OpenSearch)
- Redis Caching
- Spring Batch for Bulk Product Import
- Product Image Management (Google Cloud Storage, AWS S3 , Azure Blob Storage)
- API Versioning
- Role-Based Access Control (RBAC)
- CI/CD Pipeline
- Advanced Security (Refresh Tokens, OAuth2/OpenID Connect, Secret Management... etc)
- Database Improvements (Database Indexing, Flyway/Liquibase for schema versioning.. etc)
- Testing Improvements
- Microservices Architecture (Product Service, Cart Service, User Service, Search Service)
- API Gateway (ex: apigee)