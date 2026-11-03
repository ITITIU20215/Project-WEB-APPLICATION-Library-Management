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
