# Free-tier deployment guide ($0)

Deploy the URL shortener for **portfolio / demo** at zero cost.

| Layer | Service | Cost |
|-------|---------|------|
| Database | MongoDB Atlas M0 | $0 forever |
| Cache + rate limits | Upstash Redis | $0 (500K cmds/mo) |
| Backend API | Render Free Web Service | $0 (sleeps when idle) |
| Frontend | Vercel Hobby | $0 |
| CI | GitHub Actions (public repo) | $0 |
| Local dev | Docker Compose | $0 |

> **Trade-off:** Render free tier sleeps after ~15 minutes of inactivity. The first request after waking up may take **30–60 seconds**.

---

# Order of operations

Complete these steps in order:

```text
1. MongoDB Atlas     → MONGODB_URI
2. Upstash Redis     → REDIS_HOST, REDIS_PORT, REDIS_PASSWORD
3. Render Backend    → https://your-api.onrender.com
4. Vercel Frontend   → https://your-app.vercel.app
5. Update Render CORS + APP_BASE_URL with the Vercel URL
```

---

# Step 1 — MongoDB Atlas (Free M0)

1. Create an account at https://www.mongodb.com/cloud/atlas
2. Create a **Free M0 Cluster**.
3. Go to **Database Access**.
    - Create a database user.
    - Save the username and password.
4. Go to **Network Access**.
    - Add IP Address.
    - Select **Allow Access from Anywhere (0.0.0.0/0)**.
5. Click **Connect → Drivers**.
6. Copy the connection string.

Example:

```text
mongodb+srv://username:password@cluster0.xxxxx.mongodb.net/url_shortener?retryWrites=true&w=majority
```

Save this as:

```text
MONGODB_URI
```

---

# Step 2 — Upstash Redis

1. Go to https://upstash.com
2. Create a Redis database.
3. Open the **Redis** tab (not REST).
4. Copy:

- Endpoint → `REDIS_HOST`
- Port → `REDIS_PORT`
- Password → `REDIS_PASSWORD`

Also set:

```text
REDIS_SSL=true
```

---

# Step 3 — Deploy Backend on Render

## Option A — Using render.yaml

1. Push your project to GitHub.
2. Open https://dashboard.render.com
3. Select:

```
New → Blueprint
```

4. Connect your GitHub repository.
5. Render automatically reads `render.yaml`.
6. Enter the required environment variables.

Required variables:

```text
MONGODB_URI
REDIS_HOST
REDIS_PORT
REDIS_PASSWORD
REDIS_SSL=true
JWT_SECRET=<random 32+ character string>
IP_HASH_SALT=<random string>
APP_BASE_URL=https://YOUR-SERVICE.onrender.com
CORS_ALLOWED_ORIGINS=http://localhost:5173
```

---

## Option B — Manual Deployment

Create a new **Web Service**.

Settings:

```text
Runtime: Docker
Dockerfile Path: backend/Dockerfile
Docker Context: backend
Plan: Free
Health Check Path: /actuator/health
```

Environment variables:

| Variable | Value |
|----------|-------|
| MONGODB_URI | Atlas URI |
| REDIS_HOST | Upstash host |
| REDIS_PORT | 6379 |
| REDIS_PASSWORD | Upstash password |
| REDIS_SSL | true |
| JWT_SECRET | Random 32+ character string |
| IP_HASH_SALT | Random string |
| APP_BASE_URL | https://your-service.onrender.com |
| CORS_ALLOWED_ORIGINS | http://localhost:5173,https://your-app.vercel.app |

Verify deployment:

```bash
curl https://YOUR-SERVICE.onrender.com/actuator/health
```

Expected response:

```json
{"status":"UP"}
```

---

# Step 4 — Deploy Frontend on Vercel

1. Push your project to GitHub.
2. Go to https://vercel.com
3. Click **Add New Project**.
4. Import the repository.
5. Configure:

```text
Root Directory: frontend
Framework: Vite
```

Environment Variable:

```text
VITE_API_URL=https://YOUR-SERVICE.onrender.com
```

Deploy the project.

Example frontend URL:

```text
https://url-shortener.vercel.app
```

---

# Step 5 — Configure CORS

Open your Render service.

Update:

```text
CORS_ALLOWED_ORIGINS=http://localhost:5173,https://YOUR-APP.vercel.app
```

Confirm:

```text
APP_BASE_URL=https://YOUR-SERVICE.onrender.com
```

Render will redeploy automatically.

Test:

1. Open the Vercel app.
2. Login.
3. Create a short URL.
4. Open the generated short link.
5. Verify analytics load correctly.

---

# Step 6 — GitHub Actions

CI Workflows:

```text
.github/workflows/backend-ci.yml
```

Runs:

```text
mvn test
mvn package
```

Frontend:

```text
.github/workflows/frontend-ci.yml
```

Runs:

```text
npm install
npm run build
```

---

# Local Docker

```bash
copy .env.example .env
docker compose up --build

cd frontend
npm run dev
```

---

# Troubleshooting

| Problem | Solution |
|----------|----------|
| CORS error | Add the exact Vercel URL to `CORS_ALLOWED_ORIGINS` |
| Backend slow on first request | Render Free instance is waking up |
| MongoDB connection failed | Check Atlas IP whitelist and credentials |
| Redis connection failed | Ensure `REDIS_SSL=true` and use Redis endpoint (not REST) |
| Short URLs point to localhost | Set `APP_BASE_URL` to the Render URL |
| Vercel shows 404 on refresh | Ensure `vercel.json` is committed and redeploy |

---

# Suggested Git Branches

```text
feature/phase5-github-actions
feature/phase5-deploy-backend
feature/phase5-deploy-frontend
```

Or use a single branch:

```text
feature/phase5-production-free
```