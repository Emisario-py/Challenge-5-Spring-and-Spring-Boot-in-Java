# MELI App (Spring Boot)

A small, production-ready REST API for managing **clients**, **orders**, and **order items**. It’s built with **Spring Boot 3**, **JPA/Hibernate**, and supports **H2 (dev)** and **MySQL (prod)** via environment-driven profiles. The project uses DTOs, validation, and integration tests with MockMvc.

---
Table of contents

* [Prerequisites](#prerequisites)
* [Features](#features)
* [Tech Stack](#tech-stack)
* [Project Structure](#project-structure)
* [Configuration & Profiles](#configuration--profiles)
* [Running the Application](#running-the-application)
* [Validation & Error Handling](#validation--error-handling)
* [Testing](#testing)
* [Api Documentation (Swagger)](#api-documentation-swagger)

---
## Prerequisites

- **JDK 17**  
  Verify: `java -version` → must report 17.x

- **Maven 3.9+**  
  Verify: `mvn -v` → Apache Maven 3.9.x

- **Git** (optional but recommended)  
  For cloning and version control.

- **IDE** (optional)  
  IntelliJ IDEA / VS Code with Java extensions.

## Features

- CRUD for **Clients**, **Orders**, and **Items**
- DTOs for clean request/response contracts
- Bean Validation on inputs
- Environment-driven config with `.env` (via `spring-dotenv`)
- Dev profile with in-memory **H2**
- Prod profile with **MySQL**

---

## Tech Stack

- **Language:** Java 17
- **Framework:** Spring Boot 3.5 (Web, Validation, Data JPA)
- **Persistence:** Hibernate/JPA
- **Databases:** H2 (dev), MySQL (prod)
- **Build:** Maven
- **JSON:** Jackson (JSR-310 support for Java time)
- **Env:** `spring-dotenv` to load `.env`
- **Testing:** Spring Boot Test, JUnit 5, MockMvc
- **Documentation**: Swagger

---

## Project Structure
```md
meli-app/
└─src/
  └─main/
    ├─ java/
    │ └─org/
    │   └─digitalnao/
    │     └─meli/
    │       ├─controller/
    │       │ ├─ClientController.java
    │       │ ├─ItemController.java
    │       │ └─OrderController.java
    │       ├─domain/
    │       │ ├─Client.java
    │       │ ├─Item.java
    │       │ └─Order.java
    │       ├─dto/
    │       │ ├─client/
    │       │ │ └─ClientResponse.java
    │       │ ├─error/
    │       │ │ └─ErrorResponse.java
    │       │ ├─item/
    │       │ │ ├─CreateItemRequest.java
    │       │ │ └─ItemResponse.java
    │       │ └─order/
    │       │   ├─CreateOrderRequest.java
    │       │   └─OrderResponse.java
    │       ├─exception/
    │       │ ├─ClientNotFoundException.java
    │       │ ├─GlobalExceptionHandler.java
    │       │ ├─ItemNotFoundException.java
    │       │ └─OrderNotFoundException.java
    │       ├─mapper/
    │       │ ├─ClientMapper.java
    │       │ ├─ItemMapper.java
    │       │ └─OrderMapper.java
    │       ├─repository/
    │       │ ├─ClientRepository.java
    │       │ ├─ItemRepository.java
    │       │ └─OrderRepository.java
    │       ├─service/
    │       │ ├─ClientService.java
    │       │ ├─ItemService.java
    │       │ └─OrderService.java
    │       ├─web/
    │       │ └─RestExceptionHandler.java
    │       └─MeliAppApplication.java
    └─resources/
      ├─ application.yml
      ├─ application-dev.yml 
      ├─ application-prod.yml
      ├─application-test.yml
      └─data.sql
```
---

## Configuration & Profiles

This app reads configuration from `application.yml` + profile overrides and from your `.env`.

### `.env` (example)
```md
DB_URL=jdbc:mysql://localhost:3306/YOURDB
DB_USER=USER
DB_PASSWORD=PASSWORD
SERVER_PORT=PORT (default 8080)
```

### Profiles

### `dev`
- **Purpose**: Development environment profile  
- **Database**: Uses H2 in-memory database for rapid development and testing without external dependencies  
- **Configuration**: Configured for quick iterations with auto-restart and detailed logging

### `prod` 
Make sure you configure your .env file to be able to run this profile
- **Purpose**: Production environment profile  
- **Database**: Uses MySQL database for persistent data storage in production environments  
- **Configuration**: Optimized for performance, security, and stability with production-grade settings  
> **Run profile**  
`.\start.bat prod`

### `test`
- **Purpose**: Testing environment profile  
- **Database**: Uses H2 in-memory database for isolated test execution  
- **Configuration**: Runs integration tests to verify application functionality and component interactions  
> **Run profile**  
`.\start.bat test`
---

## Running the Application

### Prerequisites

- Java 17
- Maven 3.9+

### Command
For running the application you can use three different commands depending on which profile you are going to use:
### `dev`
>**Run profile**  
**Windows**: `.\start.bat dev`  
**Linux/Mac**: `./start.sh dev`

### `prod`
>**Run profile**  
**Windows**: `.\start.bat prod`  
**Linux/Mac**: `./start.sh prod`

### `test`
>**Run profile**  
**Windows**: `.\start.bat test`  
**Linux/Mac**: `./start.sh test`

---
## Validation & Error Handling

- DTOs use Jakarta Bean Validation annotations (@NotNull, @Positive, etc.).

- Typical error responses return a 4xx/5xx with a message.

- Common pitfalls handled:

- Client must exist for order creation.

- Order items must include productId, quantity, and unitPrice.

- When updating an order with a new items array, existing items are cleared (due to orphanRemoval = true) and replaced.

---

## Testing

### Overview
This project includes comprehensive integration tests for all API endpoints using Spring Boot Test framework with MockMvc. Tests verify the complete functionality of the application including database interactions, request/response handling, and business logic validation.

### Test Structure

#### Test Configuration
All integration tests use the following configuration:
- **@SpringBootTest**: Loads the complete application context
- **@AutoConfigureMockMvc**: Configures MockMvc for HTTP request simulation
- **@Transactional**: Ensures database rollback after each test for isolation
- **@DisplayName**: Provides descriptive test names for better readability

#### Test Profile
Tests run using the `test` profile which is configured with:
- **H2 In-Memory Database**: Provides isolated, fast test execution
- **Clean State**: Database is cleared before each test class execution
- **Auto-configuration**: Automatically sets up test-specific beans and configurations

### Test Files

#### 1. ClientIntegrationTest
**Purpose**: Validates all CRUD operations for the Client API endpoints.

**Test Cases**:
- ✅ **GET /api/clients**: Retrieves all clients from the database
    - Creates multiple test clients
    - Verifies response contains correct number of clients
    - Validates client names in response

- ✅ **POST /api/clients**: Creates a new client
    - Sends client data with manual ID assignment
    - Verifies HTTP 200 response with correct data
    - Confirms persistence in database using repository

- ✅ **GET /api/clients/{id}**: Retrieves a specific client by ID
    - Creates test client and retrieves by ID
    - Validates all client fields in response
    - Tests complete client data structure

- ✅ **GET /api/clients/{id} - Not Found**: Handles non-existent client
    - Attempts to retrieve client with invalid ID (9999)
    - Expects 5xx server error response

- ✅ **PUT /api/clients/{id}**: Updates existing client
    - Creates client with original data
    - Sends update request with new values
    - Verifies updated fields in response and database

- ✅ **DELETE /api/clients/{id}**: Deletes a client
    - Creates test client
    - Sends delete request
    - Confirms HTTP 204 No Content response
    - Verifies client no longer exists in database

---

#### 2. ItemIntegrationTest
**Purpose**: Validates all CRUD operations for the Item API endpoints with proper order associations.

**Setup**:
- Creates test client and order before each test
- Ensures proper parent-child relationship for item creation

**Test Cases**:
- ✅ **GET /api/items**: Retrieves all items across all orders
    - Creates multiple test items linked to test order
    - Verifies response contains correct items count
    - Validates product IDs and names in response

- ✅ **GET /api/items/{id}**: Retrieves a specific item by ID
    - Creates test item linked to order
    - Validates all item fields including orderId reference
    - Verifies quantity, unit price, and product details

- ✅ **POST /api/items/order/{orderId}**: Creates a new item for an order
    - Sends item data with order association
    - Verifies HTTP 200 response with complete item data
    - Confirms item includes correct orderId reference
    - Validates persistence in database

- ✅ **PUT /api/items/{id}**: Updates existing item
    - Creates item with original data
    - Sends update request with new product details
    - Verifies all updated fields (productId, name, quantity, price)
    - Confirms changes persisted in database

- ✅ **DELETE /api/items/{id}**: Deletes an item
    - Creates test item
    - Sends delete request
    - Confirms HTTP 204 No Content response
    - Verifies item removed from database

---

#### 3. OrderIntegrationTest
**Purpose**: Validates all CRUD operations for the Order API endpoints including complex item associations.

**Setup**:
- Creates test client before each test
- Provides client reference for order creation

**Test Cases**:
- ✅ **GET /api/orders**: Retrieves all orders
    - Creates multiple test orders
    - Verifies response contains correct number of orders

- ✅ **GET /api/orders/{id}**: Retrieves a specific order by ID
    - Creates test order with client association
    - Validates order ID and clientId in response

- ✅ **POST /api/orders**: Creates a new order with items
    - Prepares CreateOrderRequest with order ID, client ID, and items list
    - **IMPORTANT**: Manually assigns IDs to both order and items (no auto-generation)
    - Sends complete order structure with multiple items
    - Verifies HTTP 200 response with order data
    - Validates items count and product IDs in response
    - Confirms cascading persistence of items

- ✅ **PUT /api/orders/{id}**: Updates existing order
    - Creates test order
    - Sends update request with new timestamp
    - Verifies order updated successfully

- ✅ **DELETE /api/orders/{id}**: Deletes an order
    - Creates test order
    - Sends delete request
    - Confirms HTTP 204 No Content response
    - Verifies order removed from database
    - Confirms cascade deletion of associated items

**Helper Methods**:
- `createAndSaveOrder(Client client, Long orderId)`: Creates and persists test order with client association

---

### Running Tests

#### Run All Tests
**Windows**:
```bash
.\start.bat test
```

**Linux/Mac**:
```bash
./start.sh test
```

#### Run Specific Test Class
```bash
mvn test -Dtest=ClientIntegrationTest
mvn test -Dtest=ItemIntegrationTest
mvn test -Dtest=OrderIntegrationTest
```

#### Run Single Test Method
```bash
mvn test -Dtest=ClientIntegrationTest#testCreateClient
```

#### Run Tests with Maven
```bash
mvn test
```


### Test Coverage

#### API Endpoints Tested
| Endpoint       | GET All | GET By ID | POST | PUT | DELETE |
|----------------|---------|-----------|------|-----|--------|
| `/api/clients` | ✅       | ✅         | ✅    | ✅   | ✅      |
| `/api/items`   | ✅       | ✅         | ✅    | ✅   | ✅      |
| `/api/orders`  | ✅       | ✅         | ✅    | ✅   | ✅      |

## API Documentation (Swagger)

To access to the api documentation with swagger
>Swagger UI: http://localhost:8080/swagger-ui.html
