# Spring Boot + JWT + React Item Manager

Spring Boot exposes a JWT-secured REST API backed by MySQL (`spring_securitydb`).  
The bundled React SPA lives in `/frontend`; during development Vite proxies `/api` traffic to Spring Boot (`http://localhost:8080`).

## Backend features

- `POST /api/auth/register` – persist a new account (stored in MySQL via JPA).
- `POST /api/auth/login` – validates credentials against the stored hash and returns a JWT.
- `GET/POST /api/items` – list and create rows for the authenticated user (`items.owner_id`).
- `PUT /api/items/{id}` – update a row you own (otherwise **404**).
- `DELETE /api/items/{id}` – delete a row you own (**204 No Content**); otherwise **404**.
- SPA-friendly forwarding for `/login`, `/register`, and `/dashboard` once the frontend bundle sits in `src/main/resources/static/`.

## Tech stack

| Layer    | Choices                             |
|:---------|:-------------------------------------|
| API      | Java 17 • Spring Boot 3 • Maven      |
| Security | Spring Security + Stateless JWT       |
| Data     | Spring Data JPA • MySQL 8 Connector  |
| UI       | React 18 • Vite 5 • React Router DOM 6 |

## Database configuration

`src/main/resources/application.properties` contains the JDBC defaults plus overrides:

| Property / env var | Meaning |
|--------------------|---------|
| `MYSQL_URL`        | Overrides JDBC URL (optional). |
| `MYSQL_USERNAME`   | DB user (`root` locally). |
| `MYSQL_PASSWORD`   | Plain password injected from env/profile. |

For workstation convenience you can drop credentials into **`src/main/resources/application-local.properties` (already gitignored)** and run Boot with `-Dspring-boot.run.profiles=local`.

| Secret / claim | Notes |
|:---------------|:------|
| `JWT_SECRET` | HS256 signing key — **at least 32 bytes**; the app fails fast on startup if shorter. |
| `JWT_ISSUER` | Optional `iss` claim (default `spring-security-jwt-crud`); must match when validating tokens. |

Access tokens are **stateless JWTs** (`Authorization: Bearer …`) with `sub` (username), `iss`, `uid` (database user id), `iat`, and `exp`. Login/register responses include `token`, `tokenType` (`Bearer`), and `expiresIn` (seconds).

### Schema migration tips

Adding `owner_id NOT NULL` to `items` can fail if stale rows existed before this feature. Easiest remediation:

```sql
TRUNCATE TABLE items;
```

Run that after backing up anything important.

## Frontend workflow

Install Node.js 18+ (ships with `npm`).

### Development UI (recommended)

Use two terminals:

```bash
# Terminal A – API
mvn spring-boot:run -Dspring-boot.run.profiles=local

# Terminal B – Vite UI (proxies /api → :8080)
cd frontend
npm install
npm run dev
```

Open **`http://localhost:5173`**. Registers + logins call the REST endpoints and hydrate `sessionStorage` with returned JWT tokens.

### Production-ish bundle served from Boot

Build the SPA into Spring’s static bucket (Vite is configured with `build.outDir: ../src/main/resources/static`):

```bash
cd frontend
npm install
npm run build
```

Then restart Spring Boot. Visit **`http://localhost:8080`** — client-side routes reuse the forwarding controller.

Until you run `npm run build`, the placeholder `src/main/resources/static/index.html` explains why only the API responds on `:8080`.

### Cleaning stale hashed assets

`vite.config.js` sets `emptyOutDir: false` so the placeholder survives between builds. If you ever need a squeaky-clean static directory, manually delete hashed files under `src/main/resources/static/assets/` before rebuilding.

## Example REST calls after login

Acquire a bearer token via login, then reuse it:

```bash
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"demo","password":"supersecret"}' \
  | jq -r '.token')

curl -H "Authorization: Bearer ${TOKEN}" \
     http://localhost:8080/api/items
```

Update item `1` (name required; description optional or empty string):

```bash
curl -X PUT http://localhost:8080/api/items/1 \
  -H "Authorization: Bearer ${TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{"name":"Updated name","description":"New notes"}'
```

Delete item `1`:

```bash
curl -X DELETE http://localhost:8080/api/items/1 \
  -H "Authorization: Bearer ${TOKEN}" \
  -w "\nHTTP %{http_code}\n"
```

## Git setup shortcuts

Initialize git if needed:

```bash
git init
git add .
git commit -m "Initial Spring Boot security create/view app"
```

Configure your remote:

```bash
git remote add origin <your-repository-url>
git branch -M main
git push -u origin main
```
