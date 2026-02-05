# Credit Card Service API

A mini fintech credit card service built with Spring Boot 3.2.1 and Java 17. This application provides RESTful APIs for managing credit card transactions, including authorization and capture operations with comprehensive unit tests.

## 🚀 Features

- **Card Management**: Retrieve credit card summaries including credit limits, available limits, and total captured amounts
- **Transaction Processing**: Authorize and capture credit card transactions with validation
- **In-Memory Database**: H2 database with automatic schema and data initialization
- **RESTful APIs**: Clean and simple REST endpoints for seamless integration
- **Global Exception Handling**: Centralized error handling with appropriate HTTP status codes
- **Comprehensive Unit Tests**: 21 unit tests covering services and controllers (100% passing)
- **Postman Collection**: Pre-configured API collection for easy testing
- **H2 Console**: Built-in database console for development and debugging

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.8.1 or higher
- Git (optional, for cloning)

## 🔧 Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/ginseng1000years/credit-card-service.git
cd credit-card-service
```

### 2. Build the Project
```bash
mvn clean install
```

### 3. Run the Application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080` and automatically:
- Create the database schema
- Initialize test data (one credit card with limit of 10,000.00)

## 📡 API Documentation

### Base URL
```
http://localhost:8080
```

### Card Endpoints

#### Get Card Summary
```http
GET /cards/{cardId}/summary
```

**Description**: Retrieves the summary of a credit card including available limit and total captured amount.

**Path Parameters**:
- `cardId` (Long): The ID of the credit card

**Response** (200 OK):
```json
{
  "cardId": 1,
  "cardNumber": "4532015112830366",
  "creditLimit": 10000.00,
  "availableLimit": 9900.00,
  "totalCapturedAmount": 100.00
}
```

**Error Responses**:
- `404 Not Found`: Card not found with the specified ID
- `500 Internal Server Error`: Server error

---

### Transaction Endpoints

#### Authorize Transaction
```http
POST /transactions/authorize
Content-Type: application/json
```

**Description**: Authorizes a transaction on a credit card. The amount is deducted from the available limit.

**Request Body**:
```json
{
  "cardId": 1,
  "amount": 100.00
}
```

**Response** (201 Created):
```json
{
  "transactionId": 1,
  "cardId": 1,
  "amount": 100.00,
  "type": "AUTHORIZED",
  "createdAt": "2026-02-05T20:10:30.123456"
}
```

**Error Responses**:
- `400 Bad Request`: Insufficient available limit or invalid input
- `404 Not Found`: Card not found

---

#### Capture Transaction
```http
POST /transactions/capture/{transactionId}
```

**Description**: Captures a previously authorized transaction, converting it to CAPTURED state.

**Path Parameters**:
- `transactionId` (Long): The ID of the transaction to capture

**Response** (200 OK):
```json
{
  "transactionId": 1,
  "cardId": 1,
  "amount": 100.00,
  "type": "CAPTURED",
  "createdAt": "2026-02-05T20:10:30.123456"
}
```

**Error Responses**:
- `400 Bad Request`: Only AUTHORIZED transactions can be captured
- `404 Not Found`: Transaction not found

---

## 🧪 Testing

### Run All Tests
```bash
mvn test
```

### Test Results
- **Total Tests**: 21
- **Status**: ✅ All Passing
- **Coverage Areas**:
  - CardService (4 tests)
  - TransactionService (8 tests)
  - CardController (3 tests)
  - TransactionController (6 tests)

### Run Specific Test Class
```bash
mvn test -Dtest=CardServiceTest
mvn test -Dtest=TransactionServiceTest
mvn test -Dtest=CardControllerTest
mvn test -Dtest=TransactionControllerTest
```

---

## 💾 Database

### H2 Console
Access the H2 database console at: `http://localhost:8080/h2-console`

**Connection Details**:
- **JDBC URL**: `jdbc:h2:mem:creditcarddb`
- **Username**: `sa`
- **Password**: (leave empty)

### Database Schema

#### credit_cards Table
```sql
CREATE TABLE credit_cards (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    card_number VARCHAR(16) NOT NULL UNIQUE,
    credit_limit DECIMAL(19, 2) NOT NULL,
    available_limit DECIMAL(19, 2) NOT NULL
);
```

#### card_transactions Table
```sql
CREATE TABLE card_transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    card_id BIGINT NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    type VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (card_id) REFERENCES credit_cards(id)
);
```

### Sample Data
One test credit card is automatically created on startup:
- **Card Number**: 4532015112830366
- **Credit Limit**: 10,000.00
- **Initial Available Limit**: 10,000.00

---

## 📮 Postman Collection

A pre-configured Postman collection is included: `Credit-Card-Service.postman_collection.json`

### Import Steps
1. Open Postman
2. Click **Import** → **File** → Select `Credit-Card-Service.postman_collection.json`
3. Update the `baseUrl` variable if needed (default: `http://localhost:8080`)
4. Start testing!

### Collection Includes
- ✅ Get Card Summary
- ✅ Authorize Transaction (auto-saves transaction ID)
- ✅ Capture Transaction (uses saved transaction ID)
- ✅ Pre-written test assertions for each request

---

## 📁 Project Structure

```
credit-card-service/
├── src/
│   ├── main/
│   │   ├── java/com/example/creditcard/
│   │   │   ├── CreditCardApplication.java          # Main Spring Boot application
│   │   │   ├── config/
│   │   │   │   └── DataInitializer.java           # Automatic data initialization
│   │   │   ├── controller/
│   │   │   │   ├── CardController.java            # Card endpoints
│   │   │   │   └── TransactionController.java     # Transaction endpoints
│   │   │   ├── domain/
│   │   │   │   ├── CreditCard.java               # Credit card entity
│   │   │   │   ├── CardTransaction.java          # Transaction entity
│   │   │   │   └── TransactionType.java          # Transaction state enum
│   │   │   ├── exception/
│   │   │   │   └── GlobalExceptionHandler.java   # Centralized error handling
│   │   │   ├── repository/
│   │   │   │   ├── CardRepository.java           # Credit card data access
│   │   │   │   └── TransactionRepository.java    # Transaction data access
│   │   │   └── service/
│   │   │       ├── CardService.java              # Card business logic
│   │   │       └── TransactionService.java       # Transaction business logic
│   │   └── resources/
│   │       ├── application.yml                    # Application configuration
│   │       ├── schema.sql                         # Database schema (optional)
│   │       └── data.sql                           # Initial data (optional)
│   └── test/
│       └── java/com/example/creditcard/
│           ├── service/
│           │   ├── CardServiceTest.java
│           │   └── TransactionServiceTest.java
│           └── controller/
│               ├── CardControllerTest.java
│               └── TransactionControllerTest.java
├── pom.xml                                         # Maven configuration
├── Credit-Card-Service.postman_collection.json    # Postman collection
└── README.md                                       # This file
│       └── data.sql
└── test/
    └── java/
        └── com/example/creditcard/
            └── CreditCardApplicationTests.java
```

## Configuration

Application configuration can be found in `src/main/resources/application.yml`. Default settings include:

- Server port: 8080
- H2 database configuration
- JPA settings

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Run tests
5. Submit a pull request

## License

This project is licensed under the MIT License.
