# URL_SHORTENER_SYSTEM

Monorepo layout (LLD §2.1):

```
URL_SHORTENER_SYSTEM/
├── docs/
│   ├── HLD.md
│   └── LLD.md
├── backend/
│   ├── pom.xml
│   ├── Dockerfile
│   ├── docker-compose.yml
│   └── src/
│       ├── main/java/com/urlshortener/
│       └── test/java/com/urlshortener/
├── frontend/
│   ├── package.json
│   ├── vite.config.ts
│   └── src/                    # see LLD §11.1 for subfolders
│       ├── routes/
│       ├── pages/
│       ├── components/
│       │   ├── url/
│       │   ├── analytics/
│       │   ├── layout/
│       │   └── ui/
│       ├── hooks/
│       ├── context/
│       ├── api/
│       ├── types/
│       └── utils/
├── .github/workflows/
│   ├── backend-ci.yml
│   └── frontend-ci.yml
└── README.md
```
