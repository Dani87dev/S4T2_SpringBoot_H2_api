# S4.02 — Spring Boot REST API with H2

## Project Goal & Overview

The goal of this project is to build a **REST API** using Spring Boot to manage the stock of a fruit shop. The API implements a full CRUD (Create, Read, Update, Delete) over a `Fruit` entity, persisted in an **H2 in-memory database**.

This project covers:

- Creating a REST API with Spring Boot following the MVC pattern
- Persisting data with Spring Data JPA and H2
- Using DTOs with Bean Validation (`@NotBlank`, `@Positive`)
- Centralized exception handling with `GlobalExceptionHandler`
- Unit testing with Mockito and integration testing with MockMvc
- Multi-stage Dockerfile for production builds

---

## 📌 Model

The `Fruit` entity has the following properties:

| Field          | Type   | Description                        |
|----------------|--------|------------------------------------|
| `id`           | `Long` | Unique identifier, auto-generated  |
| `name`         | `String` | Name of the fruit                |
| `weightInKilos`| `int`  | Weight in kilograms                |

---

## 📌 Endpoints

| Method   | URL            | Description              | Success Code |
|----------|----------------|--------------------------|--------------|
| `POST`   | `/fruits`      | Create a new fruit       | `201`        |
| `GET`    | `/fruits`      | Get all fruits           | `200`        |
| `GET`    | `/fruits/{id}` | Get a fruit by id        | `200`        |
| `PUT`    | `/fruits/{id}` | Update a fruit by id     | `200`        |
| `DELETE` | `/fruits/{id}` | Delete a fruit by id     | `204`        |

### POST /fruits — Example Request

```json
{
  "name": "Apple",
  "weightInKilos": 5
}
```

### POST /fruits — Example Response (201 Created)

```json
{
  "id": 1,
  "name": "Apple",
  "weightInKilos": 5
}
```

### GET /fruits/{id} — Error Case (404 Not Found)

```json
{
  "error": "Fruit not found with id: 99"
}
```

### POST /fruits — Validation Error (400 Bad Request)

```json
{
  "name": "Name cannot be blank",
  "weightInKilos": "Weight must be a positive number"
}
```

---

## 📌 Class Structure

| Class                    | Package        | Role                                          |
|--------------------------|----------------|-----------------------------------------------|
| `Fruit`                  | `model`        | JPA entity mapped to `fruits` table           |
| `FruitDTO`               | `dto`          | Data Transfer Object with validation          |
| `FruitRepository`        | `repository`   | JPA repository interface                      |
| `FruitService`           | `services`     | Interface defining business operations        |
| `FruitServiceImpl`       | `services`     | Service implementation with DTO conversion    |
| `FruitController`        | `controllers`  | REST controller with CRUD endpoints           |
| `FruitNotFoundException` | `exception`    | Custom exception returning 404                |
| `GlobalExceptionHandler` | `exception`    | Centralized error handling for all controllers|

---

## 🧪 Tests

### Unit Tests — `FruitServiceTest`

Uses `@ExtendWith(MockitoExtension.class)` with `@Mock` and `@InjectMocks` to test the service layer in isolation.

| Test                                    | Description                                  |
|-----------------------------------------|----------------------------------------------|
| `createFruit_shouldReturnCreatedFruit`  | Verifies a fruit is created and returned     |
| `getFruitById_shouldReturnFruit`        | Verifies retrieval by existing id            |
| `getFruitById_shouldThrowWhenNotFound`  | Verifies 404 exception for missing id        |
| `getAllFruits_shouldReturnList`          | Verifies all fruits are returned             |
| `updateFruit_shouldReturnUpdatedFruit`  | Verifies a fruit is updated correctly        |
| `deleteFruit_shouldDeleteWhenExists`    | Verifies deletion of existing fruit          |
| `deleteFruit_shouldThrowWhenNotFound`   | Verifies 404 exception on delete missing id  |

### Integration Tests — `FruitControllerTest`

Uses `@WebMvcTest` with `MockMvc` and `@MockitoBean` to test REST endpoints.

| Test                                          | Description                                |
|-----------------------------------------------|--------------------------------------------|
| `createFruit_shouldReturn201`                 | POST returns 201 with valid data           |
| `createFruit_shouldReturn400WhenNameBlank`    | POST returns 400 when name is empty        |
| `createFruit_shouldReturn400WhenWeightInvalid`| POST returns 400 when weight is negative   |
| `getFruitById_shouldReturn200`                | GET by id returns 200 with fruit data      |
| `getFruitById_shouldReturn404`                | GET by id returns 404 when not found       |
| `getAllFruits_shouldReturn200WithList`         | GET all returns 200 with fruit list        |
| `getAllFruits_shouldReturn200WithEmptyList`    | GET all returns 200 with empty array       |
| `updateFruit_shouldReturn200`                 | PUT returns 200 with updated data          |
| `updateFruit_shouldReturn404`                 | PUT returns 404 when id not found          |
| `deleteFruit_shouldReturn204`                 | DELETE returns 204 on success              |
| `deleteFruit_shouldReturn404`                 | DELETE returns 404 when id not found       |

---

## 🐳 Docker

The project includes a multi-stage `Dockerfile`:

- **Stage 1 (Build):** Compiles the application using Maven and generates the `.jar` file.
- **Stage 2 (Run):** Copies only the `.jar` into a lightweight JRE Alpine image.

### Build and run:

```bash
docker build -t fruit-api-h2 .
docker run -p 8080:8080 fruit-api-h2
```

---

## 🚀 Getting Started

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   ```

2. **Navigate to the project folder:**
   ```bash
   cd fruit-api-h2
   ```

3. **Run the application:**
   ```bash
   ./mvnw spring-boot:run
   ```
   The server will start on port **8080**.

4. **Run the tests:**
   ```bash
   ./mvnw test
   ```

5. **Access the H2 Console:**

   Go to `http://localhost:8080/h2-console` and use:
   - JDBC URL: `jdbc:h2:mem:fruitdb`
   - Username: `sa`
   - Password: *(empty)*

---

## 🛠️ Tech Stack

- Java 21
- Spring Boot 4.0.5
- Spring Data JPA
- H2 Database
- Bean Validation
- Lombok
- JUnit 5
- Mockito
- MockMvc
- Docker
