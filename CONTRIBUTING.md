# Contributing

## Workflow

1. Open a **Feature** or **Bug** issue (use the templates). Feature issues carry a feature ID such as `F06`.
2. Create a branch from `main`:
   - `feature/F06-checkout`
   - `fix/<short-description>`
   - `docs/<short-description>`
   - `chore/<short-description>`
3. Commit in small steps using [Conventional Commits](https://www.conventionalcommits.org/).
4. Open a pull request early (draft is fine) and link the issue with `Closes #<number>`.
5. Merge when CI is green and at least one teammate has approved. Use **Squash and merge**, then delete the branch.

`main` is always buildable. Never push directly to `main`; branch protection enforces this.

## Commit messages

```
<type>(<optional scope>): <summary in imperative mood>
```

Types: `feat`, `fix`, `docs`, `refactor`, `test`, `chore`, `ci`.

Examples:

```
feat(circulation): add checkout endpoint
fix(frontend): show error when login fails
test(catalog): cover duplicate ISBN validation
chore: bump Spring Boot to 4.1.3
```

## Code style

- Formatting basics come from `.editorconfig`; configure your IDE to respect it.
- **Backend (Java):** standard Java conventions, 4-space indent, no wildcard imports. Organise code by feature package (`com.library.<feature>`), with `controller`, `service`, `repository`, `entity` and `dto` inside. Return DTOs from controllers, never entities.
- **Frontend (React):** Prettier and ESLint are configured. Run `npm run format` and `npm run lint` before pushing.
- **Database:** every schema change is a new Flyway migration in `backend/src/main/resources/db/migration`, named `V<n>__<feature>_<what>.sql`. Never edit a migration that has already been applied.

## Before you open a PR

```bash
# backend
cd backend && ./mvnw verify        # PowerShell: .\mvnw.cmd verify

# frontend
cd frontend && npm run lint && npm run format:check && npm run build
```
