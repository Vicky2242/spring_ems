# Employee Management System

A learning-oriented full-stack Employee Management System built with **Java 21, Spring Boot, Spring Data JPA, MySQL, REST APIs, and Angular**.

## Features
- Create, view, update, and delete employees
- Maintain departments and roles through REST APIs
- Assign one department and multiple roles to an employee
- Layered Controller → Service → Repository architecture
- DTOs instead of exposing JPA entities directly
- Bean Validation and centralized API errors using `@RestControllerAdvice`
- Angular standalone components, routing, reactive forms, and `HttpClient`
- MySQL configuration and optional Docker Compose database
- Example backend unit test and IntelliJ HTTP Client requests

> This is a solid learning project, not a fully production-hardened system. Authentication, authorization, migrations, audit trails, and deployment hardening are intentionally left as next steps.

## Stack
- Java 21
- Spring Boot 3.4.5
- Spring Web, Validation, Spring Data JPA, Hibernate
- MySQL 8
- Angular 19, TypeScript, RxJS
- Maven, npm, Postman / IntelliJ HTTP Client

## Prerequisites
1. JDK 21
2. IntelliJ IDEA
3. MySQL 8 **or** Docker Desktop
4. Node.js 20.11+ and npm
5. Internet access for first-time dependency installation

Check installations:
```bash
java -version
node --version
npm --version
```

## Folder structure
```text
employee-management-system/
├── backend/       # Spring Boot REST API
├── frontend/      # Angular UI
├── database/      # SQL setup notes
├── docker-compose.yml
└── README.md
```

## 1. Start MySQL

### Option A: local MySQL
Create the database in MySQL Workbench or a MySQL terminal:
```sql
CREATE DATABASE employee_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

The application defaults are host `localhost`, port `3306`, database `employee_management`, username `root`, password `password`. Change these to match your own local setup. The application reads `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` environment variables.

**Windows PowerShell** (run these in the same terminal you use to start the backend):
```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/employee_management?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your_mysql_password"
```

### Option B: Docker Compose
From the project root:
```bash
docker compose up -d mysql
```
This uses example local-only credentials from `docker-compose.yml`. Do not reuse them in production. To stop MySQL:
```bash
docker compose down
```
To also delete the database volume/data:
```bash
docker compose down -v
```

## 2. Run the backend in IntelliJ IDEA
1. Open the `backend` directory (the one containing `pom.xml`) in IntelliJ.
2. Set Project SDK to Java 21.
3. Allow IntelliJ to import Maven dependencies.
4. Open `src/main/java/com/example/ems/EmployeeManagementApplication.java`.
5. Click the green Run icon next to `main`.

Or, from the `backend` directory, if Maven is installed:
```bash
mvn spring-boot:run
```
On Windows PowerShell, the included `mvnw.cmd` is a convenience launcher that uses installed Maven; if you do not have Maven, run using IntelliJ's Maven integration or install Maven. (This project does not bundle the Maven Wrapper JAR.)

Backend URL: `http://localhost:8080`

## 3. Run Angular
Open a second terminal:
```bash
cd frontend
npm install
npm start
```
Open `http://localhost:4200`. Angular proxies `/api` requests to `http://localhost:8080` using `proxy.conf.json`.

## 4. API reference
Base URL: `http://localhost:8080/api`

### Employees
| Method | Endpoint | Description |
|---|---|---|
| GET | `/employees` | List employees |
| GET | `/employees/{id}` | Get employee details |
| POST | `/employees` | Create employee |
| PUT | `/employees/{id}` | Update employee |
| DELETE | `/employees/{id}` | Delete employee |

Example employee request:
```json
{
  "firstName": "Asha",
  "lastName": "Kumar",
  "email": "asha.kumar@example.com",
  "phone": "9876543210",
  "joiningDate": "2025-05-01",
  "departmentId": 1,
  "roleIds": [1]
}
```

### Departments
| Method | Endpoint | Description |
|---|---|---|
| GET | `/departments` | List departments |
| POST | `/departments` | Create department |
| PUT | `/departments/{id}` | Update department |
| DELETE | `/departments/{id}` | Delete department |

Example:
```json
{"name":"Engineering","description":"Product development team"}
```

### Roles
| Method | Endpoint | Description |
|---|---|---|
| GET | `/roles` | List roles |
| POST | `/roles` | Create role |
| PUT | `/roles/{id}` | Update role |
| DELETE | `/roles/{id}` | Delete role
```
Example:
```json
{"name":"Developer","description":"Builds and maintains software"}
```

### Recommended first run order
1. Start MySQL.
2. Start Spring Boot.
3. Create a department using `POST /api/departments`.
4. Create a role using `POST /api/roles`.
5. Create an employee using `POST /api/employees`.
6. Open Angular and confirm the employee appears.

The app intentionally does not seed sample data automatically. Hibernate `ddl-auto=update` creates/updates tables for local learning. For real projects, use Flyway or Liquibase migrations instead.

## Request flow / architecture
```text
Angular component
    ↓
Angular API service (HttpClient)
    ↓
REST Controller  — HTTP endpoints and request validation
    ↓
Service          — business rules and transactions
    ↓
Repository       — Spring Data JPA database access
    ↓
MySQL            — persistent data
```

- **Entity:** maps Java objects to database tables.
- **DTO:** defines the API request/response contract.
- **Mapper:** converts entity objects into response DTOs.
- **Controller:** receives HTTP requests and returns JSON/status codes.
- **Service:** contains business logic and transaction boundaries.
- **Repository:** provides database operations.
- **Global exception handler:** returns consistent error responses.

## Test the backend
From `backend`:
```bash
mvn test
```
The `backend/employee-management.http` file contains sample requests for IntelliJ IDEA. You can also recreate the requests in Postman.

## Troubleshooting
- **Communications link failure:** confirm MySQL is running on port 3306 and the JDBC URL is correct.
- **Access denied:** verify `DB_USERNAME` and `DB_PASSWORD`.
- **Unknown database:** create `employee_management` or start Docker Compose.
- **Port 8080 already in use:** stop the other process or set `SERVER_PORT=8081`.
- **Angular API error:** ensure Spring Boot is running on port 8080 and Angular on 4200.
- **Dependency errors:** verify internet access and use a supported JDK/Node version.
- **Department/role delete conflict:** reassign employees before deleting a referenced department or role.

## Learning path
1. Core Java: classes, interfaces, collections, exceptions, generics, streams.
2. Spring basics: IoC, dependency injection, beans, component scanning.
3. REST: HTTP verbs, status codes, JSON, validation, DTOs.
4. JPA: entity relationships, repositories, transactions, SQL constraints.
5. Angular: components, routing, reactive forms, services, observables, HttpClient.
6. Quality: unit/integration tests, pagination, sorting, search, migrations.
7. Advanced: Spring Security, authentication/authorization, Docker, CI/CD, deployment.

## Next improvements
- Pagination, sorting, and employee search
- Department and role administration pages in Angular
- Spring Security with role-based authorization
- Flyway database migrations
- Integration tests with Testcontainers
- Structured logging and audit fields
- Dockerize API and frontend
- CI pipeline and deployment

## Git setup
```bash
git init
git add .
git commit -m "Initial employee management system"
```
Never commit real passwords or secrets.
