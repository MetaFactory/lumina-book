# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

LuminaBook is an AI-first scheduling platform — a monorepo with a JHipster/Spring Boot backend (`/api`) and an Angular frontend (`/web`).

## Development Commands

### Prerequisites (run once per session)
```bash
# From /api directory
npm run docker:db:up        # Start PostgreSQL
npm run docker:keycloak:up  # Start Keycloak (OAuth2/OIDC)
```

### Backend (`/api`)
```bash
npm run backend:start        # Start Spring Boot dev server (port 8080)
npm run backend:debug        # Start with remote debug port
npm run backend:unit:test    # Run all unit tests
npm run backend:nohttp:test  # Run Checkstyle validation
npm run java:jar:dev         # Build JAR for dev profile
npm run java:jar:prod        # Build JAR for prod profile
```

To run a single test class:
```bash
./mvnw test -pl . -Dtest=MyServiceTest
```

### Frontend (`/web`)
```bash
npm run dev    # Start Angular dev server (port 4200, proxies /api to :8080)
npm run build  # Production build
npm run test   # Run Karma/Jasmine unit tests
npm run lint   # ESLint validation
```

## Architecture

### Backend (Java 21, Spring Boot 4, JHipster 9 monolith)

Layered package structure under `com.metafactory.luminabook`:
- `domain/` — JPA entities: `Event`, `Booking`, `BookingPage`, `CalendarIntegration`, plus JHipster's `User`
- `repository/` — Spring Data JPA interfaces (one per entity)
- `service/` — Business logic; `dto/` for transfer objects, `mapper/` for MapStruct entity↔DTO mapping
- `web/rest/` — REST controllers named `*Resource`; `errors/` for exception mapping
- `security/` — OAuth2/OIDC config; role constants (`ROLE_ADMIN`, `ROLE_USER`)
- `config/` — Spring `@Configuration` classes (security, cache, database, web)

All REST endpoints live under `/api/*`. Authentication is delegated to Keycloak via Spring Security OAuth2. Liquibase manages schema migrations in `src/main/resources/config/liquibase/`.

### Frontend (Angular 20, TypeScript 5.9, Redux Toolkit)

```
web/src/
├── app/
│   ├── dynamic-entities.ts   # Registry that maps entity names → form schemas
│   ├── nav-items.ts          # Sidebar navigation config
│   └── app.routing-module.ts # Routes (many generated dynamically from schemas)
├── schema/                   # Form schema definitions (normalizeDataTableFormSchema, etc.)
├── services/
│   ├── store.service.ts      # Redux facade (StoreServiceBase)
│   ├── rootReducer.ts        # Redux slices for every entity
│   ├── auth.service.ts       # OAuth2 token management
│   └── auth.interceptor.ts   # Injects Bearer token on outgoing requests
├── pages/                    # Feature pages (account, signin)
├── config/index.ts           # Runtime config (API base URL)
└── assets/
    ├── i18n/                 # Translation JSON files
    └── mocked/               # Mock API fixtures for frontend-only dev
```

**Key pattern — Schema-Driven UI**: Entity forms and data tables are defined as JSON schemas in `src/schema/` and registered in `dynamic-entities.ts`. The framework generates Angular routes, forms, and table columns from these schemas automatically. When adding a new entity UI, follow the same schema-definition pattern.

**State management**: Each entity has a Redux slice in `rootReducer.ts`. Components interact with state via `StoreService` rather than directly dispatching actions.

### Domain Model (from `api/lumina-book.jdl`)

| Entity | Key fields |
|--------|-----------|
| Event | title, startTime, endTime, status (EventStatus enum), location |
| BookingPage | slug (unique public URL), duration, isActive |
| Booking | guestName, guestEmail, status (BookingStatus enum), links to BookingPage |
| CalendarIntegration | provider (Google/Outlook), accessToken, refreshToken, links to User |

### Profiles & Config

- **dev**: Uses Keycloak at `localhost:9080`, enables DevTools hot-reload, H2 console at `/h2-console`
- **prod**: Expects env vars for DB and Keycloak; optimized JPA settings
- Secrets template: `api/src/main/resources/config/application-secret-samples.yml`

## Adding New Entities

1. Define entity in `api/lumina-book.jdl` and run JHipster to scaffold: domain → repository → service → REST resource
2. Add Liquibase changelog in `src/main/resources/config/liquibase/changelog/`
3. Create schema file in `web/src/schema/`, export it in `dynamic-entities.ts`, add nav entry in `nav-items.ts`, add Redux slice in `rootReducer.ts`
4. Reference `assets/docs/adding-dynamic-forms.md` and `assets/docs/entity-crud-apis.md` for detailed guidance

## API Documentation

Swagger UI is available at `http://localhost:8080/swagger-ui/` when the backend is running. The OpenAPI spec is at `/openapi.yaml`.
