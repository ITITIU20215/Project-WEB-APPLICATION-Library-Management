# Project WEB APPLICATION: Library Management

[![CI](https://github.com/ITITIU20215/Project-WEB-APPLICATION-Library-Management/actions/workflows/ci.yml/badge.svg)](https://github.com/ITITIU20215/Project-WEB-APPLICATION-Library-Management/actions/workflows/ci.yml)

A web application for managing a library, built with a Spring Boot REST API, a React single-page app and a MySQL database.

## Tech stack

| Layer    | Technology                                                        |
| -------- | ----------------------------------------------------------------- |
| Backend  | Java 17, Spring Boot 4.1, Spring Data JPA, Spring Security, Maven |
| Database | MySQL 8.4, Flyway migrations                                      |
| API docs | springdoc-openapi (Swagger UI)                                    |
| Frontend | React 19, Vite 8, React Router, Axios                             |
| Tooling  | Docker Compose, ESLint, Prettier, GitHub Actions                  |

## Repository structure

```
.
├── backend/              Spring Boot application (Maven wrapper included)
│   └── src/main/resources/db/migration/   Flyway migrations
├── frontend/             React + Vite application
├── .github/              CI workflow, issue and PR templates
├── docker-compose.yml    MySQL (+ Adminer) for local development
├── .env.example          Local environment template
└── CONTRIBUTING.md       Git workflow and code style
```

## Prerequisites

- [JDK 17](https://adoptium.net/) (Temurin recommended). Maven is not required; use the wrapper.
- [Node.js LTS](https://nodejs.org/) (includes npm)
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) (on Windows Home, enable WSL 2)
- Git

## Quick start

### 1. Environment

```bash
# bash
cp .env.example .env
# PowerShell
copy .env.example .env
```

The defaults work out of the box. If port `3307` is already in use on your machine, change `MYSQL_HOST_PORT` in `.env`; the backend picks up the same value.

### 2. Database

```bash
docker compose up -d
```

This starts MySQL on `localhost:3307` (database `library_db`, user `library` / `library`) and Adminer on http://localhost:8081.

Using a native MySQL install instead? Create the database and user, then set `DB_URL`, `DB_USERNAME` and `DB_PASSWORD` in `.env`. Note that XAMPP ships MariaDB, not MySQL.

### 3. Backend

```bash
cd backend
./mvnw spring-boot:run          # bash
.\mvnw.cmd spring-boot:run      # PowerShell
```

Flyway applies migrations on startup.

### Demo accounts

The migrations seed these accounts, plus 20 sample books with 3 copies each:

| Email                     | Password       | Role                                     |
| ------------------------- | -------------- | ---------------------------------------- |
| `admin@library.local`     | `Admin@123`    | ADMIN (must change password)             |
| `librarian@library.local` | `Password@123` | LIBRARIAN                                |
| `member1@library.local`   | `Password@123` | MEMBER                                   |
| `member2@library.local`   | `Password@123` | MEMBER                                   |
| `member3@library.local`   | `Password@123` | MEMBER (deactivated, for negative tests) |

### 4. Frontend

```bash
cd frontend
npm install
npm run dev
```

The Vite dev server proxies `/api` to the backend, so no extra configuration is needed. To call a backend on another origin, copy `frontend/.env.example` to `frontend/.env` and set `VITE_API_BASE_URL` (origin only, e.g. `http://localhost:8080`).

### URLs

| Service      | URL                                   |
| ------------ | ------------------------------------- |
| Frontend     | http://localhost:5173                 |
| Backend API  | http://localhost:8080/api/v1          |
| Health check | http://localhost:8080/api/v1/health   |
| Swagger UI   | http://localhost:8080/swagger-ui.html |
| Adminer      | http://localhost:8081                 |

## Database schema

The full schema for every feature (F01–F13) already exists. A feature does **not** create its own tables. It adds services, controllers and repository queries on top of the existing entities.

| Migration | Tables                                         | Entities (package `com.library.*`)                   |
| --------- | ---------------------------------------------- | ---------------------------------------------------- |
| V2        | `users` + seeded admin                         | `user.entity.User`, `Role`, `UserStatus`             |
| V3, V4    | `authors`, `publishers`, `categories`, `books`, `book_authors`, `book_categories` + sample books | `catalog.entity.Book`, `Author`, `Publisher`, `Category` |
| V5        | `book_copies`                                  | `inventory.entity.BookCopy`, `CopyStatus`            |
| V6        | search indexes (FULLTEXT on title/subtitle)    | —                                                    |
| V7        | `loans`                                        | `circulation.entity.Loan`, `LoanStatus`              |
| V8        | `reservations`                                 | `reservation.entity.Reservation`, `ReservationStatus` |
| V9        | `fines`, `fine_payments`                       | `fine.entity.Fine`, `FinePayment`, `FineStatus`, `PaymentMethod` |
| V10       | `settings` + default loan rules                | `setting.entity.Setting`, `SettingType`              |
| V11       | `notifications`                                | `notification.entity.Notification`, `NotificationType` |
| V12       | `reviews`, `wishlist_items`                    | `review.entity.Review`, `WishlistItem`               |
| V13       | `audit_logs`                                   | `audit.entity.AuditLog`                              |
| V14       | demo accounts and book copies                  | —                                                    |

Each feature package also has a basic Spring Data repository (`<feature>/repository`).

Conventions:

- `created_at` / `updated_at` are filled automatically by JPA auditing (UTC). Inject `java.time.Clock` instead of calling `LocalDate.now()`.
- Enums are stored as text (`@Enumerated(EnumType.STRING)`).
- Relationships are `LAZY`. Map entities to DTOs inside a `@Transactional` service.
- The database enforces these rules on its own. Catch `DataIntegrityViolationException` and return `409`:
  - unique email, ISBN and barcode
  - at most one `ACTIVE` loan per copy
  - at most one open (`PENDING`/`READY`) hold per member and book
  - at most one fine per loan
  - one review per member and book
- **Schema changes go in a new migration starting at `V15__...`.** Never edit V1–V14 after they are merged.
- `DatabaseSchemaTest` runs all migrations on a real MySQL through Testcontainers and checks that every entity matches the schema. It is skipped when Docker is not running.

## Useful commands

| Task                  | Command                                                 |
| --------------------- | ------------------------------------------------------- |
| Backend tests + build | `./mvnw verify` (PowerShell: `.\mvnw.cmd verify`)       |
| Frontend lint         | `npm run lint`                                          |
| Frontend format       | `npm run format` / `npm run format:check`               |
| Frontend build        | `npm run build`                                         |
| Stop the database     | `docker compose down` (add `-v` to delete all its data) |

## Troubleshooting

- **`/bin/sh^M: bad interpreter`** when running `./mvnw`: the file was checked out with CRLF line endings. Delete `backend/mvnw` and restore it with `git checkout -- backend/mvnw`; `.gitattributes` makes Git write it with LF.
- **Port 3307 already in use:** set another `MYSQL_HOST_PORT` in `.env`, then `docker compose up -d` again.
- **Frontend shows "Backend: unreachable":** start the backend and check http://localhost:8080/api/v1/health.
- **Flyway checksum mismatch:** an applied migration was edited. Revert the edit and add a new migration instead.

## Team setup

1. Add teammates under **Settings → Collaborators**.
2. Under **Settings → Branches → main**, raise **Required approvals** to 1.

See [CONTRIBUTING.md](CONTRIBUTING.md) for the branch, commit and pull request workflow.
