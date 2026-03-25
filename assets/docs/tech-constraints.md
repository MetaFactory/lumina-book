# Technical Constraints

## Stack

- Backend: Java (Spring Boot, JHipster)
- Frontend: Angular + TypeScript
- Database: PostgreSQL

## Architecture Rules

- API contract is source of truth (OpenAPI)
- Do not hand-edit generated API clients
- Use DTO pattern for API responses
- All dates stored in UTC

## AI Integration

- AI parsing service must be isolated
- Fallback to manual scheduling if AI fails

## Performance

- API response time < 500ms (excluding AI calls)
- AI calls async where possible

## Security

- OAuth2 for authentication
- Tokens must not be stored in plain text

## Monorepo Rules

- Shared types generated from OpenAPI
- Backend and frontend must stay contract-aligned
