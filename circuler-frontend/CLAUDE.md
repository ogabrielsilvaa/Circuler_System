# Circuler Frontend

Mobile app for the Circuler platform — a book donation and exchange system (Senac school project, circular economy).

## Stack

- **React Native** 0.81.5 + **Expo** SDK 54
- **Expo Router** 6 (file-based routing)
- **TypeScript** 5.9
- **React** 19

## Folder Architecture

Expo Router requires `app/` to contain **routes only**. Because of this, the actual screen implementation lives in `src/routes/`, and files inside `app/` only import and re-export the corresponding screen.

```
circuler-frontend/
├── app/                             # ROUTES ONLY (Expo Router — no components here)
│   ├── _layout.tsx                  # Root layout + authentication logic
│   ├── +not-found.tsx
│   ├── (auth)/
│   │   ├── _layout.tsx
│   │   ├── login.tsx                # → imports from src/routes/_auth/login/Login.tsx
│   │   └── register.tsx
│   └── (app)/
│       ├── _layout.tsx              # Tab navigator
│       ├── index.tsx                # → imports from src/routes/_app/home/Home.tsx
│       ├── search.tsx
│       ├── reservations.tsx
│       ├── profile.tsx
│       └── books/
│           └── [id].tsx
│
├── src/
│   ├── routes/                      # Screen implementation (component co-location)
│   │   ├── -components/             # Components shared across all routes
│   │   │
│   │   ├── _auth/                   # Unauthenticated screens
│   │   │   ├── -components/         # Components shared across auth screens
│   │   │   ├── login/
│   │   │   │   ├── -components/     # Components exclusive to the login screen
│   │   │   │   └── Login.tsx        # Login screen
│   │   │   └── register/
│   │   │       ├── -components/
│   │   │       └── Register.tsx
│   │   │
│   │   └── _app/                    # Authenticated screens
│   │       ├── -components/         # Components shared across app screens
│   │       ├── home/
│   │       │   ├── -components/
│   │       │   └── Home.tsx
│   │       ├── search/
│   │       │   ├── -components/
│   │       │   └── Search.tsx
│   │       ├── reservations/
│   │       │   ├── -components/
│   │       │   └── Reservations.tsx
│   │       ├── profile/
│   │       │   ├── -components/
│   │       │   └── Profile.tsx
│   │       └── book-detail/
│   │           ├── -components/
│   │           └── BookDetail.tsx
│   │
│   ├── components/                  # Global reusable components for any screen
│   │   ├── Button.tsx
│   │   ├── Input.tsx
│   │   ├── Card.tsx
│   │   └── Badge.tsx
│   │
│   ├── services/                    # HTTP calls to the Spring Boot API
│   │   ├── api.ts                   # Base axios client + JWT interceptor
│   │   ├── auth.service.ts
│   │   ├── books.service.ts
│   │   ├── users.service.ts
│   │   ├── reservations.service.ts
│   │   └── collection-points.service.ts
│   │
│   ├── hooks/                       # Custom hooks (React Query per domain)
│   │   ├── useAuth.ts
│   │   ├── useBooks.ts
│   │   └── useReservations.ts
│   │
│   ├── stores/                      # Global state (Zustand)
│   │   └── auth.store.ts            # JWT token and logged-in user
│   │
│   ├── types/                       # TypeScript interfaces mirroring the backend
│   │   ├── user.types.ts
│   │   ├── book.types.ts
│   │   ├── reservation.types.ts
│   │   └── api.types.ts
│   │
│   ├── constants/
│   │   ├── colors.ts
│   │   └── config.ts                # API BASE_URL, etc.
│   │
│   └── utils/
│       ├── formatters.ts            # Format dates, statuses, enums
│       └── validators.ts
│
├── assets/
│   ├── fonts/
│   └── images/
│
├── app.json
├── package.json
├── tsconfig.json
└── .env                             # EXPO_PUBLIC_API_URL=http://...
```

## Component Co-location Rule

The scope hierarchy follows this logic — a component should live at the level closest to where it is used:

```
src/components/                          → used on any screen in the app
src/routes/-components/                  → used across more than one route group (auth or app)
src/routes/_auth/-components/            → used across more than one auth screen
src/routes/_auth/login/-components/      → used only on the login screen
```

If a component from a screen-level `-components/` folder starts being used in another screen, it **moves up one level** in the hierarchy.

## Route File Convention

Files inside `app/` are thin — they only import and re-export the actual screen:

```typescript
// app/(auth)/login.tsx
import Login from '@/routes/_auth/login/Login'
export default Login
```

## Imports

Always use relative paths. Never use path aliases (e.g., `@/`).

```typescript
// app/(auth)/login.tsx
import Login from '../../src/routes/_auth/login/Login'

// src/hooks/useBooks.ts
import { getBooks } from '../services/books.service'
```

This avoids module resolution issues when running or building the project.

## Backend Connection

- Backend: Java 21 + Spring Boot, running at `http://localhost:8080`
- Environment variable: `EXPO_PUBLIC_API_URL` in `.env`
- Authentication: JWT via Bearer token
- Error messages are returned in Portuguese by the backend

## Code Conventions

- No unnecessary comments — variable and function names must be self-explanatory
- Components in PascalCase, hooks in camelCase with `use` prefix
- Service files use the `.service.ts` suffix
- Types in `.types.ts` files, never mixed with logic
