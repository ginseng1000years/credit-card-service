# Credit Card Service

A mini fintech credit card service built with Spring Boot. This application provides RESTful APIs for managing credit card transactions, including authorization and capture operations.

## Features

- **Card Management**: Retrieve credit card summaries including credit limits, available limits, and total captured amounts.
- **Transaction Processing**: Authorize and capture credit card transactions.
- **In-Memory Database**: Uses H2 database for data persistence during runtime.
- **RESTful APIs**: Clean and simple REST endpoints for integration.

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Installation

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd credit-card-service
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

## Running the Application

To start the service, run the following command:

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080` by default.

## API Endpoints

### Card Endpoints

- **GET /cards/{cardId}/summary**
  - Retrieves the summary of a credit card including ID, card number, credit limit, available limit, and total captured amount.

### Transaction Endpoints

- **POST /transactions/authorize**
  - Authorizes a transaction for a given card and amount.
  - Request Body:
    ```json
    {
      "cardId": 1,
      "amount": 100.00
    }
    ```
  - Response: Transaction details including ID, card ID, amount, type, and creation timestamp.

- **POST /transactions/capture/{transactionId}**
  - Captures a previously authorized transaction.
  - Response: Updated transaction details.

## Database

The application uses an H2 in-memory database. Initial data can be found in `src/main/resources/data.sql`.

To access the H2 console (for development purposes), visit `http://localhost:8080/h2-console` after starting the application.

## Testing

Run the tests using:

```bash
mvn test
```

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/example/creditcard/
│   │       ├── CreditCardApplication.java
│   │       ├── controller/
│   │       │   ├── CardController.java
│   │       │   └── TransactionController.java
│   │       ├── domain/
│   │       │   ├── CardTransaction.java
│   │       │   ├── CreditCard.java
│   │       │   └── TransactionType.java
│   │       ├── repository/
│   │       │   ├── CardRepository.java
│   │       │   └── TransactionRepository.java
│   │       └── service/
│   │           ├── CardService.java
│   │           └── TransactionService.java
│   └── resources/
│       ├── application.yml
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
