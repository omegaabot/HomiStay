# Homistay Backend API

A robust REST API for the Homistay platform, built with **Spring Boot**, **PostgreSQL**, and secured via **JWT Authentication**.

---

## 🌟 Key Features

- **Secure Authentication**: Stateless JWT-based authentication with Access and Refresh tokens.
- **Concurrency Control**: Pessimistic locking implemented on the database level to entirely prevent double-booking.
- **Role-Based Access Control**: Strict endpoint protection distinguishing between `GUEST` and `HOST` permissions.
- **Data Integrity**: Enforced via PostgreSQL relationships, Spring Boot `@Valid` annotations, and strict Enum mappings.

---

## 🛠️ Tech Stack

- **Java**: 21
- **Framework**: Spring Boot 3.x
- **Database**: PostgreSQL 15+
- **ORM**: Hibernate / Spring Data JPA
- **Security**: Spring Security + JWT
- **API Documentation**: Swagger / OpenAPI 3

---

## 🚀 Getting Started

### Prerequisites
- Java 21+
- Maven 3.9+
- PostgreSQL 15+

### Step 1 — Set up PostgreSQL

Create the required user and database in your PostgreSQL instance. Run the following commands in your `psql` shell:

```sql
CREATE USER homistay_user WITH PASSWORD 'homistay_pass123';
CREATE DATABASE homistay_db OWNER homistay_user;
GRANT ALL PRIVILEGES ON DATABASE homistay_db TO homistay_user;
\q
```

### Step 2 — Initialize the Database Schema

Run the provided SQL schema script to construct tables and relationships:

```bash
psql -h localhost -U homistay_user -d homistay_db -f src/main/resources/schema.sql
```
*(Enter password: `homistay_pass123` when prompted).*

> **Note on Constraints:** The `schema.sql` deliberately omits strict database-level `CHECK` constraints on Enum strings (like property types) to prevent conflicts with Hibernate's internal enum serialization format. Data integrity is strictly enforced at the application level via Java Enums.

### Step 3 — Application Configuration

Open `src/main/resources/application.properties`. Defaults are pre-configured to match the database setup above. 

**Security Note**: For production, you must change the `app.jwt.secret` to a highly secure, long string. You can generate one via terminal:
```bash
openssl rand -hex 64
```

### Step 4 — Run the Application

Compile and run the Spring Boot application using the Maven wrapper:

```bash
# Mac/Linux
./mvnw clean package -DskipTests
./mvnw spring-boot:run

# Windows
mvnw.cmd clean package -DskipTests
mvnw.cmd spring-boot:run
```

The API will start on: **http://localhost:8080**

---

## 🧠 Core Architecture & Logic

### Double-Booking Prevention (Concurrency)
To prevent race conditions where two users attempt to book the same property on the same dates, `BookingService.java` relies on pessimistic locking (`@Lock(PESSIMISTIC_WRITE)`).
When User A requests dates for Property X, the database locks those rows. If User B requests the same dates simultaneously, they must wait until User A's transaction completes or rolls back. This provides absolute safety against double-bookings.

### JWT Flow
- **Access Tokens**: Short-lived (default 15 mins), used in the `Authorization: Bearer <token>` header.
- **Refresh Tokens**: Long-lived (default 7 days), used to securely request new Access Tokens without requiring re-login.

### Resource Ownership
Endpoints meticulously verify ownership. For example, a host can only update/delete properties mapped to their own `host_id`, and a guest can only cancel bookings associated with their `guest_id`.

---

## 📖 API Documentation & Testing

Swagger UI is baked into the application for easy endpoint testing and exploration.

**Access Swagger UI:**
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

**How to Test via Swagger:**
1. Navigate to the Swagger UI in your browser.
2. Hit **POST /api/auth/register** to create an account (Use role `GUEST` or `HOST`).
3. Hit **POST /api/auth/login** with your credentials to receive an `accessToken`.
4. Click the **Authorize** padlock button at the top of the page and paste your token.
5. You now have full access to test protected endpoints based on your assigned role!
