# EventSphere

A unified event planning platform: budgeting, guest & RSVP management, ticketing,
vendor marketplace, live announcements, and analytics — in one place.

This is a **full project scaffold**: every module from the proposal is wired up
end-to-end (entity → repository → service → REST controller → React page) so you
have a working base to build on, not just placeholder files.

## Stack

- **Backend:** Java 17, Spring Boot 3, Spring Security + JWT, Spring Data JPA, MySQL
- **Frontend:** React 18 + Vite, React Router, Tailwind CSS, Axios
- **Database:** MySQL 8 (schema in `database/schema.sql`, also auto-created by Hibernate)

## Project structure

```
eventsphere/
├── backend/            Spring Boot REST API
│   └── src/main/java/com/eventsphere/
│       ├── entity/         User, Event, Guest, Ticket, BudgetItem, Vendor, Notification
│       ├── repository/     Spring Data JPA repositories
│       ├── service/         business logic (+ impl/ package)
│       ├── controller/     REST endpoints per module
│       ├── security/       JWT auth filter + util
│       ├── config/         Spring Security + CORS config
│       ├── dto/            request/response payloads
│       └── exception/      global error handling
├── frontend/            React + Vite app
│   └── src/
│       ├── pages/           Login, Register, Dashboard, Events, EventDetail, Vendors
│       ├── components/      Sidebar, Layout, PulseRail (event lifecycle indicator), ProtectedRoute
│       ├── context/          AuthContext (JWT session)
│       └── api/              axios client
└── database/
    └── schema.sql        reference SQL schema (Hibernate also auto-creates this)
```

## Modules implemented

| Module | Backend | Frontend |
|---|---|---|
| Authentication | JWT register/login, role-based (`ADMIN`, `ORGANIZER`, `VENDOR`, `ATTENDEE`) | Login / Register pages |
| Event Management | CRUD + lifecycle status (`PLANNING → PUBLISHED → ONGOING → COMPLETED`) | Events list, create form, event detail |
| Guest & RSVP | add guest, update RSVP, check-in | Guests tab |
| Ticketing | ticket tiers, purchase, QR-ready check-in codes | Tickets tab |
| Budget Manager | line items + **smart budget estimator** (heuristic, see note below) | Budget tab |
| Vendor Marketplace | CRUD + **smart recommendations** (scored by category/city/budget/rating) | Vendors page |
| Notifications | persisted announcements per event | Notifications tab |
| Reports / Analytics | RSVP & ticket summary, **sustainability score** | Reports tab |

**Note on "AI" features:** the budget estimator and vendor recommendations are
implemented as transparent, rule-based heuristics (clearly commented in
`BudgetServiceImpl` and `VendorServiceImpl`), not trained ML/LLM models. They give
a reasonable first-pass answer today and are structured so you can swap in a real
model or an LLM call later without changing the controller/DTO contracts.

## Getting started

### 1. Database
Install MySQL locally (or run it in Docker) and either let Hibernate auto-create
the schema on first run, or run `database/schema.sql` yourself:

```bash
mysql -u root -p < database/schema.sql
```

### 2. Backend

```bash
cd backend
# set DB credentials if different from defaults (root/root)
export DB_USERNAME=root
export DB_PASSWORD=your_password
mvn spring-boot:run
```

The API starts on `http://localhost:8080`.

### 3. Frontend

```bash
cd frontend
npm install
npm run dev
```

The app starts on `http://localhost:5173` and proxies `/api` calls to the backend
(configured in `vite.config.js`).

### 4. Try it out

1. Go to `http://localhost:5173/register`, create an account with role **Organizer**.
2. Create an event from the Dashboard/Events page.
3. Open the event and add guests, ticket tiers, budget items, and send an announcement.
4. Check the **Reports** tab for the live summary and sustainability score.
5. Visit **Vendors** to list a vendor or try smart recommendations.

## What's stubbed vs. production-ready

This scaffold is meant to be a strong, working starting point — not a finished
product. Before shipping, you'll want to:

- Add pagination and input validation on list endpoints
- Replace the notification "send" with a real delivery channel (email/SMS/push/WebSocket)
- Generate actual scannable QR images for `ticketCode` (currently a UUID string)
- Add role-based endpoint authorization (e.g. only `ORGANIZER`/`ADMIN` can create events)
- Add automated tests (JUnit for backend, Vitest/RTL for frontend)
- Move secrets (`JWT_SECRET`, DB password) out of `application.yml` defaults and into
  a proper secrets manager for production

## Future scope (from the original proposal)

AI chatbot, native mobile app, calendar sync, payment gateway integration,
predictive attendance modeling, multilingual support.
