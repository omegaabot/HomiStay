# 🏡 Homistay — Full-Stack Homestay Booking Application

> **Airbnb-inspired** homestay booking platform built with **React 19 + Spring Boot 3.2 + PostgreSQL**.  
> Guests browse, search and book homestays. Hosts list properties and manage their business.

---

## 🎤 60-Second Verbal Pitch (100–150 words)

> *Read this aloud when presenting the project to an audience.*

---

> "Homistay is a full-stack property rental platform — think Airbnb — built with **React 19** on the frontend and **Spring Boot 3.2** with **PostgreSQL** on the backend.
>
> Guests can search properties by destination, dates, and guest count, book them with a multi-step checkout, manage their bookings, save wishlists, and even plan a full trip using our built-in **Trip Planner** — which uses the Haversine formula to recommend the best, cheapest, and nearest property based on attractions they want to visit.
>
> Hosts get a full dashboard — add properties with a 5-step wizard, block calendar dates, manage bookings, approve modification requests, and track earnings through monthly charts.
>
> The backend features **JWT authentication**, **pessimistic locking** to prevent double-booking, a **dynamic pricing engine** with seasonal and demand-based multipliers, a **regex-powered FAQ chatbot**, and full **Swagger API documentation**.
>
> The database has 18 tables, seeded with 23 properties, 64 bookings, and 57 reviews — ready to demo out of the box."

---

---

## 📋 Table of Contents

1. [Project Overview](#-project-overview)
2. [Tech Stack](#-tech-stack)
3. [Project Structure](#-project-structure)
4. [Backend Architecture](#-backend-architecture)
5. [Frontend Architecture](#-frontend-architecture)
6. [Database Schema](#-database-schema)
7. [API Endpoints](#-api-endpoints)
8. [Features — Deep Dive](#-features--deep-dive)
9. [Authentication & Security](#-authentication--security)
10. [Dynamic Pricing Engine](#-dynamic-pricing-engine)
11. [Trip Planner Module](#-trip-planner-module)
12. [AI Chatbot (FAQ Engine)](#-ai-chatbot-faq-engine)
13. [Running the Project](#-running-the-project)
14. [Test Credentials](#-test-credentials)
15. [Project Stats](#-project-stats)
16. [Security Audit Summary](#-security-audit-summary)
17. [PowerShell Presentation Script](#-powershell-presentation-script)

---

## 🌟 Project Overview

**Homistay** is a full-featured property rental marketplace inspired by Airbnb. It connects:

| Role | What they do |
|---|---|
| **Guest** | Browse properties, book stays, leave reviews, save wishlists, plan trips |
| **Host** | List properties, manage bookings, block calendar dates, track earnings, set dynamic pricing |
| **Admin** | Full platform access |
| **Support Team** | Handle support tickets via a dedicated dashboard |

### What makes it complete?

- ✅ **Real JWT auth** (access + refresh tokens, role-based endpoints)
- ✅ **Pessimistic locking** to prevent double-booking race conditions
- ✅ **Dynamic pricing engine** — seasonal multipliers + demand-based adjustment
- ✅ **Trip Planner** — Haversine-distance optimized property recommendations
- ✅ **AI Chatbot** — Regex-pattern FAQ engine for 25+ common support queries
- ✅ **Full booking lifecycle** — create, cancel, modify, message, review
- ✅ **Host Calendar** — visual availability management with bulk date blocking
- ✅ **Add-ons system** — extra services (breakfast, airport pickup, etc.)
- ✅ **Seed data** — 23 properties, 64 bookings, 57 reviews, 13 addons ready to demo

---

## 🛠 Tech Stack

### Backend

| Technology | Version | Role |
|---|---|---|
| Java | 21 | Runtime |
| Spring Boot | 3.2.5 | Application framework |
| Spring Security | (bundled) | Auth, JWT filter chain |
| Spring Data JPA | (bundled) | ORM / repository layer |
| Hibernate | 6.4.x | JPA implementation |
| PostgreSQL | any | Primary database |
| Maven | 3.9+ | Build tool |
| Lombok | 1.18.38 | Boilerplate reduction |
| jjwt | 0.12.6 | JWT generation & validation |
| Springdoc OpenAPI | 2.5.0 | Swagger UI at `/swagger-ui/index.html` |
| BCrypt | strength 12 | Password hashing |

### Frontend

| Technology | Version | Role |
|---|---|---|
| React | 19.2.5 | UI framework |
| Vite | 8.0.10 | Build tool & dev server |
| Tailwind CSS | 4.2.4 | Utility-first styling |
| wouter | 3.3.5 | Client-side routing |
| TanStack React Query | 5 | Server state & caching |
| Axios | 1.16 | HTTP client |
| Leaflet | 1.9.4 | Interactive maps |
| Recharts | 2.15.2 | Charts (earnings bar chart) |
| Framer Motion | 12 | Animations |
| React Hook Form | 7.55 | Form state management |
| Zod | 4 | Schema validation |
| shadcn/ui (Radix) | latest | 60+ accessible UI primitives |
| Sonner | 2 | Toast notifications |
| date-fns | 3 | Date utilities |
| react-day-picker | 9 | Date range calendar |
| react-icons | 5.4 | Icon library |

---

## 📁 Project Structure

```
homistay2/
├── PROJECT_PRESENTATION.md   ← This file
├── implementation_plan.md
├── progress.txt              ← Full change log
├── trip_planner.md           ← Trip Planner design doc
├── walkthrough.md
│
└── homistay/
    ├── SECURITY_AUDIT.md     ← Full security & upgrade assessment
    │
    ├── backend/              ← Spring Boot REST API
    │   ├── pom.xml
    │   ├── uploads/          ← User-uploaded profile images
    │   └── src/main/
    │       ├── java/com/homistay/
    │       │   ├── HomistayApplication.java   ← Entry point
    │       │   ├── config/                    ← Security, CORS, OpenAPI, DataInit
    │       │   │   ├── SecurityConfig.java
    │       │   │   ├── WebConfig.java
    │       │   │   ├── OpenApiConfig.java
    │       │   │   └── DataInitializer.java
    │       │   ├── controller/               ← 14 REST controllers
    │       │   ├── dto/
    │       │   │   ├── request/              ← Incoming payloads
    │       │   │   └── response/             ← Outgoing payloads
    │       │   ├── entity/                   ← 18 JPA entities
    │       │   ├── enums/                    ← BookingStatus, PaymentStatus, PropertyType, Role
    │       │   ├── exception/                ← GlobalExceptionHandler
    │       │   ├── repository/               ← 17 Spring Data repositories
    │       │   ├── security/                 ← JWT filter, token provider
    │       │   │   ├── JwtTokenProvider.java
    │       │   │   ├── JwtAuthenticationFilter.java
    │       │   │   └── CustomUserDetailsService.java
    │       │   ├── service/                  ← 11 service classes
    │       │   └── util/
    │       └── resources/
    │           ├── application.properties    ← DB, JWT, pagination config
    │           ├── schema.sql                ← DDL (run on startup)
    │           └── data.sql                  ← Seed data (23 props, 64 bookings)
    │
    └── frontend/             ← React SPA (Vite)
        ├── index.html
        ├── vite.config.js
        ├── package.json
        └── src/
            ├── App.jsx       ← Root router
            ├── main.jsx      ← ReactDOM.render
            ├── index.css     ← Global Tailwind styles
            ├── api/          ← Axios instance (axiosInstance.js)
            ├── components/
            │   ├── Navbar.jsx          ← Role-aware navbar + auth modal
            │   ├── SearchBar.jsx       ← Hero + compact search variants
            │   ├── PropertyCard.jsx    ← Listing card
            │   ├── TripPlanner.jsx     ← 3-step trip wizard (564 lines)
            │   ├── ChatPopup.jsx       ← Support chatbot popup
            │   ├── StarRating.jsx      ← Interactive star picker
            │   ├── Whirly.jsx          ← Loading character
            │   └── ui/                 ← 60+ shadcn/ui primitives
            ├── context/
            │   └── AppContext.jsx      ← Global state (user, properties, wishlist)
            ├── hooks/                  ← Custom React hooks
            ├── lib/
            │   └── utils.js            ← cn() helper
            ├── pages/
            │   ├── Home.jsx            ← Landing page
            │   ├── Search.jsx          ← Search results + filters
            │   ├── PropertyDetails.jsx ← Full property detail view (54KB!)
            │   ├── Checkout.jsx        ← Booking checkout (19KB)
            │   ├── Confirmation.jsx    ← Booking success page
            │   ├── MyBookings.jsx      ← Guest booking management (30KB)
            │   ├── Profile.jsx         ← User profile editor (25KB)
            │   ├── Wishlist.jsx        ← Saved properties
            │   ├── SupportDashboard.jsx
            │   └── host/
            │       ├── Dashboard.jsx   ← Host stats overview
            │       ├── Properties.jsx  ← Property list
            │       ├── AddProperty.jsx ← 5-step wizard (43KB!)
            │       ├── Bookings.jsx    ← Booking management (32KB)
            │       ├── Calendar.jsx    ← Availability calendar (25KB)
            │       ├── Earnings.jsx    ← Revenue charts
            │       └── Pricing.jsx     ← Dynamic pricing config (29KB)
            └── services/
                └── api.js              ← All API calls + normalizers
```

---

## 🖥 Backend Architecture

### Entities (18 JPA Tables)

| Entity | Table | Key Fields | Relationships |
|---|---|---|---|
| `User` | `users` | email, passwordHash, fullName, phone, role, bio, dob, gender, avatarUrl | One host → many Properties; One guest → many Bookings |
| `Property` | `properties` | title, type, city, country, pricePerNight, maxGuests, bedrooms, bathrooms, amenities, cleaningFee, allowsChildren/Infants/Pets, houseRules, guestRequirements, checkInInstructions | Many Images, Bookings, Reviews, Availabilities |
| `PropertyImage` | `property_images` | url, isPrimary, displayOrder | ManyToOne Property |
| `Booking` | `bookings` | checkIn, checkOut, guestsCount, adults, children, infants, pets, totalPrice, status, specialRequests, specialRequestStatus, hostNotes | → Property, Guest, Payment, Review |
| `Payment` | `payments` | amount, status, paymentMethod, transactionId, paidAt | OneToOne Booking |
| `Review` | `reviews` | rating, comment | → Booking, Property, Guest |
| `Availability` | `availability` | date, isAvailable, reason (BOOKED/HOST_BLOCKED) | → Property |
| `Wishlist` | `wishlists` | (user_id, property_id) unique | → User, Property |
| `PropertyAddon` | `property_addons` | name, description, price, isActive | ManyToOne Property |
| `BookingAddon` | `booking_addons` | quantity, price | → Booking, PropertyAddon |
| `BookingModification` | `booking_modifications` | newCheckIn, newCheckOut, newGuests, reason, status, hostResponse, resolvedAt | → Booking, requestedBy User |
| `BookingMessage` | `booking_messages` | message, createdAt | → Booking, sender User |
| `Experience` | `experiences` | title, description, price, duration | → host Property |
| `BookingExperience` | `booking_experiences` | (booking_id, experience_id) | → Booking, Experience |
| `SeasonalRate` | `seasonal_rates` | name, startDate, endDate, adjustmentType (PERCENTAGE/FIXED), adjustmentValue | ManyToOne Property |
| `DynamicPricingConfig` | `dynamic_pricing_configs` | enabled, minPriceMultiplier, maxPriceMultiplier, demandThreshold, lookbackMonths | OneToOne Property |
| `ChatQuery` | `chat_queries` | question, answer, createdAt | — |
| `SupportTicket` | `support_tickets` | subject, description, status, resolution | → User |

### Enumerations

```java
enum Role            { GUEST, HOST, ADMIN, SUPPORT_TEAM }
enum BookingStatus   { PENDING, CONFIRMED, CANCELLED, COMPLETED }
enum PaymentStatus   { PENDING, PAID, REFUNDED, PARTIALLY_REFUNDED }
enum PropertyType    { HOUSE, APARTMENT, VILLA, COTTAGE, STUDIO, CABIN, RESORT }
```

### Controllers (14 REST controllers)

| Controller | Base Path | Access | Key Endpoints |
|---|---|---|---|
| `AuthController` | `/api/auth` | Public / Auth | login, register, me, refresh, profile, change-password, upgrade-to-host |
| `PropertyController` | `/api/properties` | GET: Public; Write: Auth | search, get-by-id, create, update, delete, blocked-dates, pricing-preview |
| `BookingController` | `/api/bookings` | Auth (Guest) | create, my-bookings, get-by-id, cancel, send-message, get-messages, request-modification |
| `HostController` | `/api/host` | HOST / ADMIN | dashboard, earnings, properties, bookings, availability, addons, notes, modification-respond |
| `WishlistController` | `/api/wishlists` | Auth (Guest) | get, add, remove |
| `ModificationController` | `/api/bookings/{id}/modification` | Auth (Guest) | submit, list |
| `ReviewController` | `/api/reviews` | Auth | create, list by property |
| `ExperienceController` | `/api/experiences` | GET: Public; Write: HOST | CRUD |
| `PricingController` | `/api/properties/{id}/pricing`, `/api/host/pricing/**` | Mixed | breakdown, seasonal-rates CRUD, dynamic-config |
| `ChatbotController` | `/api/chatbot` | Public | ask question → FAQ response |
| `SupportDashboardController` | `/api/support/dashboard` | SUPPORT / ADMIN | stats overview |
| `SupportQueryController` | `/api/support/queries` | SUPPORT / ADMIN | list, update |
| `SupportTicketController` | `/api/support/tickets` | SUPPORT / ADMIN | list, resolve |
| `TicketController` | `/api/tickets` | Auth (Guest) | create, list, get |

### Services (11 service classes)

| Service | Responsibilities |
|---|---|
| `AuthService` | Register, login, token generation, profile update, avatar upload, role upgrade |
| `PropertyService` | Property CRUD, image management, search with filters, addon CRUD, availability toggle |
| `BookingService` | Create booking (pessimistic lock), cancel with refund calculation, notes, special request status, modifications, messaging |
| `PricingService` | Dynamic pricing breakdown per night, seasonal multipliers, demand-based adjustment |
| `ReviewService` | Create review (only after completed booking), retrieve by property |
| `WishlistService` | Add/remove/list wishlisted properties |
| `HostDashboardService` | Total earnings, active listings, booking counts, 12-month earnings chart |
| `ExperienceService` | Local experience CRUD |
| `ChatQueryService` | Store chatbot query + answer |
| `SupportTicketService` | Ticket creation, status management |
| `FaqService` | Regex-pattern FAQ matching for chatbot (25+ question patterns) |

### Security Layer

```
Request → JwtAuthenticationFilter → Spring Security → Controller → Service → Repository → DB
```

- **JWT**: `JwtTokenProvider` issues access tokens (15 min) + refresh tokens (7 days)
- **Filter**: `JwtAuthenticationFilter` extracts `Authorization: Bearer <token>`, validates, loads `UserDetails`
- **Stateless**: No HTTP sessions (`SessionCreationPolicy.STATELESS`)
- **BCrypt**: Password strength = 12 rounds
- **Role Guards**: `@EnableMethodSecurity` + endpoint-level `requestMatchers`

---

## 🎨 Frontend Architecture

### Routing (wouter — 18 routes)

| Route | Page Component | Access |
|---|---|---|
| `/` | `Home.jsx` | Public |
| `/search` | `Search.jsx` | Public |
| `/property/:id` | `PropertyDetails.jsx` | Public |
| `/checkout` | `Checkout.jsx` | Auth required |
| `/confirmation` | `Confirmation.jsx` | Auth required |
| `/profile` | `Profile.jsx` | Auth required |
| `/my-bookings` | `MyBookings.jsx` | Guest |
| `/wishlist` | `Wishlist.jsx` | Guest |
| `/host/dashboard` | `host/Dashboard.jsx` | HOST role |
| `/host/properties` | `host/Properties.jsx` | HOST role |
| `/host/add-property` | `host/AddProperty.jsx` | HOST role |
| `/host/edit-property/:id` | `host/AddProperty.jsx` | HOST role (reused) |
| `/host/bookings` | `host/Bookings.jsx` | HOST role |
| `/host/calendar` | `host/Calendar.jsx` | HOST role |
| `/host/earnings` | `host/Earnings.jsx` | HOST role |
| `/host/pricing` | `host/Pricing.jsx` | HOST role |
| `/support/dashboard` | `SupportDashboard.jsx` | SUPPORT / ADMIN |
| `*` | `not-found.jsx` | Fallback |

### Global State (AppContext)

```js
// AppContext.jsx — provided at App root
{
  user,              // null or { id, email, fullName, role, phone, bio, avatarUrl }
  setUser,
  properties,        // Array of normalized property objects (loaded on mount)
  setProperties,
  bookings,          // Guest bookings array
  setBookings,
  currentBooking,    // In-progress booking object (passed to Checkout)
  setCurrentBooking,
  isAuthenticated,   // !! user
  authLoading,       // true while restoring session from localStorage
  login,             // (userData) => setUser(userData)
  logout,            // Clears localStorage, redirects to /
  wishlist,          // Array of property ID strings the user has saved
  toggleWishlist,    // async (propertyId) => add/remove from backend + local state
}
```

Session restoration: On mount, reads `localStorage.accessToken`, calls `/api/auth/me` to restore `user`. Listens for `auth:logout` events from the Axios 401 interceptor.

### Custom Components

| Component | Purpose | Complexity |
|---|---|---|
| `Navbar.jsx` | Top nav bar with logo, role-aware links, auth modal (login/register/upgrade-to-host), search pill, Trip Planner trigger | High (19KB) |
| `SearchBar.jsx` | Hero variant (home page) + compact variant (search page), destination, date range, guest picker | Medium (13KB) |
| `PropertyCard.jsx` | Listing thumbnail with image, price, rating, wishlist heart | Low |
| `TripPlanner.jsx` | 3-step modal wizard: City → Places → Plans (Haversine + normalization scoring) | Very High (564 lines) |
| `ChatPopup.jsx` | Floating chatbot button + dialog, calls `/api/chatbot`, displays formatted answers | Medium |
| `StarRating.jsx` | Clickable star picker (1–5) | Low |
| `Whirly.jsx` | Animated mascot/spinner character | Low |

### API Layer (`services/api.js`)

- `authApi` — login, register, me, refresh, updateProfile, changePassword, upgradeToHost
- `propertiesApi` — search, getById, create, update, delete, getBlockedDates
- `bookingsApi` — create, getMyBookings, getById, cancel, sendMessage, getMessages
- `wishlistApi` — get, add, remove
- `reviewsApi` — create, getByProperty
- `pricingApi` — getBreakdown, getSeasonalRates, createSeasonalRate, updateSeasonalRate, deleteSeasonalRate, getConfig, saveConfig
- `experiencesApi` — CRUD
- `chatbotApi` — ask
- `supportApi` — getDashboard, getTickets, resolveTicket
- `hostApi` — getDashboard, getMonthlyEarnings, getProperties, getBookings, toggleAvailability, getAddons, createAddon, deleteAddon, updateNotes, updateSpecialRequestStatus, respondToModification

**Normalizers** (`normalizeProperty`, `normalizeUser`) transform backend snake_case → frontend camelCase and map nested structures.

---

## 🗄 Database Schema

### Core Tables

```sql
-- Users with flexible roles
users (
  id BIGSERIAL PRIMARY KEY,
  email VARCHAR UNIQUE NOT NULL,
  password_hash VARCHAR NOT NULL,
  full_name VARCHAR NOT NULL,
  phone VARCHAR,
  bio TEXT,
  dob DATE,
  gender VARCHAR,
  avatar_url VARCHAR,
  role VARCHAR NOT NULL DEFAULT 'GUEST',  -- GUEST | HOST | ADMIN | SUPPORT_TEAM
  is_active BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP
)

-- Properties (listings)
properties (
  id BIGSERIAL PRIMARY KEY,
  host_id BIGINT → users,
  title VARCHAR NOT NULL,
  description TEXT,
  type VARCHAR NOT NULL,                   -- HOUSE | APARTMENT | VILLA | ...
  city VARCHAR NOT NULL,
  country VARCHAR NOT NULL,
  address VARCHAR,
  latitude DOUBLE,
  longitude DOUBLE,
  price_per_night NUMERIC(10,2) NOT NULL,
  cleaning_fee NUMERIC(10,2) DEFAULT 0,
  max_guests INT DEFAULT 1,
  bedrooms INT DEFAULT 1,
  bathrooms INT DEFAULT 1,
  amenities TEXT,                          -- CSV string e.g. "WiFi,Pool,Kitchen"
  house_rules TEXT,
  guest_requirements TEXT,
  check_in_instructions TEXT,              -- Shown only to confirmed guests
  allows_children BOOLEAN DEFAULT TRUE,
  allows_infants BOOLEAN DEFAULT TRUE,
  allows_pets BOOLEAN DEFAULT FALSE,
  is_active BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP
)

-- Bookings with full guest breakdown
bookings (
  id BIGSERIAL PRIMARY KEY,
  property_id BIGINT → properties,
  guest_id BIGINT → users,
  check_in DATE NOT NULL,
  check_out DATE NOT NULL,
  guests_count INT DEFAULT 1,
  adults INT DEFAULT 1,
  children INT DEFAULT 0,
  infants INT DEFAULT 0,
  pets INT DEFAULT 0,
  total_price NUMERIC(10,2) NOT NULL,
  status VARCHAR DEFAULT 'PENDING',        -- PENDING | CONFIRMED | CANCELLED | COMPLETED
  special_requests TEXT,
  special_request_status VARCHAR DEFAULT 'PENDING',  -- PENDING | ACCEPTED | DECLINED | NOTED
  host_notes TEXT,
  created_at TIMESTAMP
)

-- Payments (simulated — no gateway)
payments (
  id BIGSERIAL PRIMARY KEY,
  booking_id BIGINT → bookings,
  amount NUMERIC(10,2),
  status VARCHAR,                          -- PAID | REFUNDED | PARTIALLY_REFUNDED
  payment_method VARCHAR,
  transaction_id VARCHAR,                  -- TXN-XXXXXXXX
  paid_at TIMESTAMP
)

-- Reviews (one per booking)
reviews (
  id BIGSERIAL PRIMARY KEY,
  booking_id BIGINT → bookings UNIQUE,
  property_id BIGINT → properties,
  guest_id BIGINT → users,
  rating INT NOT NULL,                     -- 1–5
  comment TEXT,
  created_at TIMESTAMP
)
```

### Feature Tables

```sql
wishlists         (user_id, property_id, created_at — UNIQUE(user_id, property_id))
property_images   (id, property_id, url, is_primary, display_order)
availability      (id, property_id, date, is_available, reason)  -- reason: BOOKED | HOST_BLOCKED
property_addons   (id, property_id, name, description, price, is_active)
booking_addons    (id, booking_id, addon_id, quantity, price)
booking_modifications (id, booking_id, requested_by, new_check_in, new_check_out, new_guests, reason, status, host_response, created_at, resolved_at)
booking_messages  (id, booking_id, sender_id, message, created_at)
experiences       (id, property_id, host_id, title, description, price, duration, category, city, is_active)
booking_experiences (booking_id, experience_id)
seasonal_rates    (id, property_id, name, start_date, end_date, adjustment_type, adjustment_value, is_active)
dynamic_pricing_configs (id, property_id, enabled, min_price_multiplier, max_price_multiplier, demand_threshold, lookback_months, updated_at)
chat_queries      (id, user_id, question, answer, created_at)
support_tickets   (id, user_id, subject, description, status, resolution, created_at)
```

---

## 🔌 API Endpoints

### Auth (`/api/auth`)

| Method | Path | Body / Params | Description |
|---|---|---|---|
| POST | `/api/auth/register` | `{ email, password, fullName, phone, role }` | Register new user |
| POST | `/api/auth/login` | `{ email, password }` | Login → `{ accessToken, refreshToken, user }` |
| GET | `/api/auth/me` | — | Get current authenticated user |
| POST | `/api/auth/refresh` | `{ refreshToken }` | Issue new token pair |
| PUT | `/api/auth/profile` | multipart (fields + optional avatar file) | Update user profile |
| PUT | `/api/auth/change-password` | `{ currentPassword, newPassword }` | Change password |
| POST | `/api/auth/upgrade-to-host` | — | Upgrade GUEST → HOST role |

### Properties (`/api/properties`)

| Method | Path | Auth | Description |
|---|---|---|---|
| GET | `/api/properties` | Public | Search with params: `city, type, minPrice, maxPrice, guests, checkIn, checkOut, sortBy, sortDir, page, size` |
| GET | `/api/properties/{id}` | Public | Property details incl. addons, images, host info, seasonal price |
| POST | `/api/properties` | HOST | Create property listing |
| PUT | `/api/properties/{id}` | HOST (owner) | Update property |
| DELETE | `/api/properties/{id}` | HOST (owner) | Hard delete property |
| GET | `/api/properties/{id}/blocked-dates` | Public | Returns array of `LocalDate` |
| GET | `/api/properties/{id}/pricing` | Public | Pricing breakdown with per-night detail |

### Bookings (`/api/bookings`)

| Method | Path | Auth | Description |
|---|---|---|---|
| POST | `/api/bookings` | Auth | Create booking (pessimistic lock) |
| GET | `/api/bookings/my` | Auth | Guest's own bookings (paginated) |
| GET | `/api/bookings/{id}` | Auth | Get single booking (guest or host) |
| PATCH | `/api/bookings/{id}/cancel` | Auth | Cancel booking → refund calculation |
| POST | `/api/bookings/{id}/messages` | Auth | Send message to host/guest |
| GET | `/api/bookings/{id}/messages` | Auth | Get conversation thread |
| POST | `/api/bookings/{id}/modification` | Auth (Guest) | Request date/guest modification |
| GET | `/api/bookings/{id}/modifications` | Auth | View modification history |

### Host (`/api/host` — requires HOST or ADMIN)

| Method | Path | Description |
|---|---|---|
| GET | `/api/host/dashboard` | Stats: total earnings, active listings, avg rating, confirmed bookings, recent bookings |
| GET | `/api/host/earnings/monthly` | 12-month earnings breakdown |
| GET | `/api/host/properties` | Host's properties (paginated) |
| GET | `/api/host/bookings` | Bookings on host's properties (filter by status, paginated) |
| POST | `/api/host/properties/{id}/availability` | Block/unblock dates `{ startDate, endDate, block: true/false }` |
| GET | `/api/host/properties/{id}/addons` | List addons |
| POST | `/api/host/properties/{id}/addons` | Create addon |
| DELETE | `/api/host/properties/{id}/addons/{addonId}` | Delete addon |
| PATCH | `/api/host/bookings/{id}/notes` | Update host notes on booking |
| PATCH | `/api/host/bookings/{id}/special-request-status` | Set special request status |
| GET | `/api/host/bookings/{id}/modifications` | View modification requests |
| PATCH | `/api/host/bookings/{bookingId}/modifications/{modId}` | Approve / deny modification |

### Pricing (`/api/host/pricing`)

| Method | Path | Description |
|---|---|---|
| POST | `/api/host/pricing/seasons` | Create seasonal rate(s) for one or more properties |
| PUT | `/api/host/pricing/seasons/{id}` | Update seasonal rate |
| DELETE | `/api/host/pricing/seasons/{id}` | Delete seasonal rate |
| GET | `/api/host/pricing/seasons?propertyId=X` | List all seasonal rates for property |
| PUT | `/api/host/pricing/config` | Save dynamic pricing config |
| GET | `/api/host/pricing/config/{propertyId}` | Get dynamic pricing config |

### Other

| Method | Path | Auth | Description |
|---|---|---|---|
| GET/POST | `/api/wishlists/{propertyId}` | Auth (Guest) | Add/Remove wishlist |
| GET | `/api/wishlists` | Auth | Get wishlist |
| POST | `/api/reviews` | Auth | Create review |
| GET | `/api/reviews/property/{id}` | Public | Get reviews for property |
| GET/POST | `/api/experiences` | Public/HOST | Experience listing |
| POST | `/api/chatbot/ask` | Public | Chatbot FAQ query |
| GET/PATCH | `/api/support/**` | SUPPORT/ADMIN | Support dashboard + tickets |
| GET/POST/PATCH | `/api/tickets` | Auth | User support tickets |

---

## ✨ Features — Deep Dive

### 🏠 Guest Features

#### Search & Filter
- **SearchBar** component with two variants: hero (home page banner) and compact (top of search page)
- Destination text input + check-in/check-out date pickers (react-day-picker) + guest count stepper
- Server-side search with params: `city`, `type`, `minPrice`, `maxPrice`, `guests`, date availability
- Client-side filter chips: Property type, Price range (slider), Amenities, Rating
- Category quick-filter: 🏖 Beach, 🏔 Mountain, 🌆 City, 🌳 Countryside

#### Property Details (54KB page)
- Multi-image gallery carousel
- Full amenity grid with icon mapping
- **Live pricing breakdown**: fetches `/api/properties/{id}/pricing?checkIn=...&checkOut=...` → shows per-night rate, seasonal multiplier name, demand multiplier, cleaning fee, total
- **Leaflet map**: Auto-geocodes city+country via Nominatim (OSM) API with 800ms debounce
- "Meet your host" section: avatar, name, join date, bio
- Guest reviews with star ratings + pagination
- Similar properties section
- Block check button for date availability

#### Booking Flow (5 steps inside Checkout page)
1. Review property details
2. Select add-ons (extras per property with quantities)
3. Enter guest breakdown (adults, children, infants, pets) + special requests
4. Acknowledge guest requirements / house rules
5. Payment method selection + final price confirmation

After checkout: creates booking via `POST /api/bookings`, creates payment (simulated `TXN-XXXXXXXX`), navigates to `/confirmation`

#### My Bookings (30KB)
- Tab filters: All / Upcoming / Completed / Cancelled
- Booking card shows: property image, dates, guests, status badge, price
- **Booking Detail Modal** (opens on click):
  - Guest breakdown (adults/children/infants/pets)
  - Check-in instructions (from host)
  - Special request status with color badge
  - Host notes display
  - Add-ons summary
  - Refund amount (on cancelled bookings)
  - **Messaging thread** (send/receive messages with host)
  - **Modification Request** form (new dates + reason → `POST /api/bookings/{id}/modification`)
  - **Review Form** (for completed bookings, only shown if no review exists)
  - Cancel Booking button (shows refund calculation preview)

#### Wishlist
- Heart toggle on property cards and detail page
- Toggle calls `toggleWishlist()` from AppContext → `POST /api/wishlists/{id}` or `DELETE /api/wishlists/{id}`
- Wishlist page shows full property cards with remove option

#### Profile (25KB)
- Edit full name, phone, bio, date of birth (≥16 years old validation), gender
- Avatar upload (multipart form to `/api/auth/profile`)
- Change password: requires current password, new password (8+ chars, must have letter + number + symbol)
- "Become a Host" button → calls `/api/auth/upgrade-to-host` → role changes to HOST

---

### 👑 Host Features

#### Dashboard (`/host/dashboard`)
- Stat cards: Total Earnings, Active Listings, Confirmed Bookings, Average Rating
- Recent bookings table (last 5, clickable)
- Data from `GET /api/host/dashboard`

#### Add Property — 5-Step Wizard (43KB)
| Step | Fields |
|---|---|
| 1. Type | Property type (HOUSE/APARTMENT/VILLA…) + category (Beach/Mountain/City/Countryside) |
| 2. Location | Country, city, address → **Auto-geocode** via Nominatim with 800ms debounce, sets lat/lng |
| 3. Basics | Bedrooms, bathrooms, max guests |
| 4. Details | Amenities (21 presets + custom), guest requirements, image URLs (up to 5) |
| 5. Pricing | Price per night, cleaning fee, house rules, check-in instructions, `allowsChildren/Infants/Pets` |

Form state managed with React Hook Form + Zod validation. Calls `POST /api/properties` on finish.  
Edit mode loads existing property data and calls `PUT /api/properties/{id}`.

#### Host Bookings (32KB)
- Property filter dropdown (filter by property)
- Status filter: All / Confirmed / Pending / Cancelled / Completed
- Booking row → click → **Full Booking Modal**:
  - Guest info (name, email, phone)
  - Dates + guest breakdown
  - Special request with status changer (`PENDING → ACCEPTED/DECLINED/NOTED`)
  - Host Notes text editor (auto-save on blur)
  - Add-ons summary
  - **Modification Requests**: list + approve/deny buttons with optional response message
  - **Messaging** thread (send/receive)

#### Calendar (25KB)
- Left sidebar: property selector
- Right panel: month calendar grid
  - Booked dates highlighted (red, non-clickable)
  - Host-blocked dates highlighted (gray)
  - Available dates (green dot)
- **Date blocking**: click single date or drag a range → modal to confirm block/unblock
- Upcoming bookings list below calendar

#### Earnings
- 4 stat cards: Gross, Service Fee (10%), Net, Avg per Booking
- **Recharts BarChart**: 12 months of monthly gross/net earnings
- Monthly breakdown table

#### Pricing (`/host/pricing`) — 29KB
- **Seasonal Rates**: Create named seasonal periods with PERCENTAGE or FIXED price adjustments. Applies to multiple properties at once.
- **Dynamic Pricing Config**: Enable/disable demand-based pricing. Set `minMultiplier`, `maxMultiplier`, `demandThreshold`, `lookbackMonths`.
- Live pricing preview: enter check-in/out dates → see per-night breakdown

---

### 🛟 Support Features

- **SupportDashboard** (`/support/dashboard`): ticket stats, open/resolved counts
- **ChatPopup**: floating button on all pages, submits to `/api/chatbot/ask`, displays regex-matched FAQ answer or fallback message

---

## 🔑 Authentication & Security

### JWT Flow

```
1. POST /api/auth/login → { accessToken (15min), refreshToken (7 days) }
2. Frontend stores both in localStorage (known security trade-off — see Security Audit)
3. Axios adds: Authorization: Bearer <accessToken> to every request
4. On 401: Axios interceptor fires auth:logout event → AppContext clears user
5. Refresh: POST /api/auth/refresh { refreshToken } → new token pair
```

### Endpoint Access Matrix

| Route Pattern | Required |
|---|---|
| `/api/auth/login`, `/register`, `/refresh` | Public |
| `GET /api/properties/**` | Public |
| `GET /api/experiences/**` | Public |
| `GET /api/reviews/**` | Public |
| `/api/chatbot/**` | Public |
| `/uploads/**` | Public (static file serving) |
| `/api/host/**` | `ROLE_HOST` or `ROLE_ADMIN` |
| `/api/support/**` | `ROLE_SUPPORT_TEAM` or `ROLE_ADMIN` |
| Everything else | Any authenticated user |

### Ownership Guards

Every host operation verifies the authenticated user owns the target resource:

```java
// Example from PropertyService
if (!p.getHost().getEmail().equals(hostEmail)) {
    throw new UnauthorizedException("You do not own this property");
}
```

Similar checks exist in `BookingService`, `PricingController`, `HostController`.

---

## 💰 Dynamic Pricing Engine

**File**: `PricingService.java`

The pricing engine computes an effective nightly rate for each date in the requested stay:

### Step 1: Default Built-in Seasons (hardcoded)

| Season | Dates | Multiplier |
|---|---|---|
| Summer Peak | June 1 – Aug 31 | ×1.50 |
| Winter Holidays | Dec 15 – Jan 5 | ×2.00 |
| Spring Break | Mar 15 – Apr 15 | ×1.30 |
| Autumn/Fall | Sep 15 – Nov 15 | ×1.20 |

### Step 2: Host-Defined Seasonal Rates (override defaults)

Hosts can create named seasons with PERCENTAGE or FIXED adjustments:
- `PERCENTAGE`: `effectivePrice = basePrice × (1 + adjustment/100)`
- `FIXED`: `effectivePrice = basePrice + adjustment`

### Step 3: Demand-Based Multiplier

```java
// Counts confirmed bookings in last N months
long bookingCount = bookingRepository.countConfirmedBookingsSince(propertyId, since);

if (bookingCount >= demandThreshold) {
    double ratio = Math.min(1.0, bookingCount / (demandThreshold * 2.0));
    multiplier = minMultiplier + (maxMultiplier - minMultiplier) × ratio;
}
```

Default config: lookback=3 months, threshold=5 bookings, min=1.0×, max=1.5×

### Pricing Breakdown Response

```json
{
  "basePricePerNight": 100.00,
  "nights": 5,
  "baseTotal": 500.00,
  "seasonalMultiplier": 1.50,
  "demandMultiplier": 1.20,
  "effectivePricePerNight": 181.00,
  "subtotal": 905.00,
  "cleaningFee": 50.00,
  "addOnsTotal": 0,
  "total": 955.00,
  "seasonName": "Summer Peak",
  "nightlyBreakdown": [ { "date": "2025-07-01", "basePrice": 100, "seasonalMultiplier": 1.5, "demandMultiplier": 1.2, "effectivePrice": 180.0 }, ... ]
}
```

---

## ✈️ Trip Planner Module

**File**: `TripPlanner.jsx` (564 lines)  
**Design doc**: `trip_planner.md`

A 3-step modal wizard accessible from the Navbar:

### Step 1 — Choose City
- Cards for Bengaluru 🌳, Goa 🏖️, Mumbai 🌆
- Searchable by name or tagline
- Each city has 5 curated attractions with lat/lng coordinates

### Step 2 — Select Places
- Checkbox list of the city's attractions
- Shows count of available properties in that city
- Nights selector (1–30)

### Step 3 — Generate Plans
- Fetches properties in the selected city from AppContext
- For each property, calculates Haversine distances to all selected attractions
- Generates 3 plan types:

| Plan | Algorithm |
|---|---|
| 💰 Budget | Cheapest total cost (nights × price + cleaning fee) |
| 📍 Nearest | Smallest average Haversine distance to selected attractions |
| ⭐ Best Overall | Weighted composite: 40% price score + 60% distance score (min-max normalized) |

- Each plan card shows: property image, rating, city, per-night price, cleaning fee, total cost for N nights, and distance to each selected attraction
- "Book this property" button navigates to `/property/{id}`

---

## 🤖 AI Chatbot (FAQ Engine)

**File**: `FaqService.java`  
**Endpoint**: `POST /api/chatbot/ask` — `{ question: "..." }` → `{ answer: "..." }`

The FAQ engine uses **25+ compiled regex patterns** to match questions and return step-by-step answers. Categories covered:

| Category | Example Questions |
|---|---|
| Booking | "How do I book a property?" |
| Cancel | "How do I cancel my reservation?" |
| Modify | "Can I change my booking dates?" |
| View Bookings | "Where can I see my bookings?" |
| Login/Register | "How do I log in?" / "How do I sign up?" |
| Password | "I forgot my password" |
| Profile | "How do I update my profile?" |
| Search | "How do I find a property?" |
| Wishlist | "How do I save a property?" |
| Payment | "What payment methods do you accept?" |
| Refunds | "How do I get a refund?" |
| Hosting | "How do I list my property?" |
| Reviews | "How do I write a review?" |
| Support | "How do I contact support?" |
| Platform Info | "What is Homistay?" |
| Pricing | "What are the fees?" |

If no pattern matches → returns: *"I'm not sure how to answer that. Please contact support@homistay.com"*

---

## 🔐 Cancellation & Refund Policy (in code)

**From `BookingService.cancelBooking()`:**

```
Host cancels → 100% refund to guest, no penalty

Guest cancels before check-in → 90% refund (10% service fee retained)
Guest cancels during stay → 70% of unused nights refunded
```

Payment status updated to: `PAID → REFUNDED` or `PARTIALLY_REFUNDED`

---

## 🚀 Running the Project

### Prerequisites

- **JDK 21+** (ensure `JAVA_HOME` is set)
- **Node.js 20+**
- **PostgreSQL** running on `localhost:5432`
- Create database: `CREATE DATABASE test;`

### Backend

```powershell
cd homistay\backend
# Set JAVA_HOME if needed:
# $env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
mvn spring-boot:run
```

- Runs on: **http://localhost:8088**
- Swagger UI: **http://localhost:8088/swagger-ui/index.html**
- On first run: `schema.sql` creates all tables, `data.sql` seeds 23 properties + 64 bookings

### Frontend

```powershell
cd homistay\frontend
npm install    # or pnpm install
npm run dev
```

- Runs on: **http://localhost:5173**
- Proxies `/api/**` and `/uploads/**` to `http://localhost:8088`

---

## 🔑 Test Credentials (Seed Data)

> All seed passwords are BCrypt-hashed. Register a new account via the UI for easiest testing, or check `data.sql` for exact hashed values.

| Role | Email | Typical Password (from data.sql) |
|---|---|---|
| Guest | `user1@example.com` | See `data.sql` bcrypt hash |
| Guest | `guest2@example.com` | See `data.sql` bcrypt hash |
| Host | `host1@example.com` | See `data.sql` bcrypt hash |
| Support | `support@homistay.com` | `password123` (DataInitializer) |

**Recommended**: Register a fresh account, then call `POST /api/auth/upgrade-to-host` to get host access.

---

## 📊 Project Stats

| Metric | Count |
|---|---|
| Backend entities (JPA tables) | 18 |
| REST controllers | 14 |
| Service classes | 11 |
| Spring Data repositories | 17 |
| Enumerations | 4 |
| Frontend pages | 17 (10 guest + 7 host/support) |
| Custom UI components | 7 |
| shadcn/ui primitives | 53+ |
| Frontend routes | 18 |
| Seed properties | 23 |
| Seed bookings | 64 |
| Seed reviews | 57 |
| Seed add-ons | 13 |
| FAQ chatbot patterns | 25+ |
| Trip Planner cities | 3 (Bengaluru, Goa, Mumbai) |
| Lines of code (estimate) | ~8,000+ |

---

## 🛡 Security Audit Summary

> Full report: `homistay/SECURITY_AUDIT.md`

### ✅ Good Practices Implemented
- BCrypt password hashing (strength 12)
- Pessimistic lock on booking creation (prevents double-booking race condition)
- Ownership verification on all host operations
- Stateless JWT auth
- Role-based authorization (`GUEST`, `HOST`, `ADMIN`, `SUPPORT_TEAM`)
- Global exception handler with structured error responses

### ⚠️ Known Issues (not production-ready)
- DB credentials and JWT secret hardcoded in `application.properties` (use env vars in prod)
- CORS allows all origins (`*`) — restrict to frontend origin in production
- No rate limiting on auth endpoints (brute-force risk)
- JWT tokens stored in `localStorage` (XSS risk — prefer HttpOnly cookies)
- Refresh token not rotated on use
- `spring.sql.init.mode=always` re-runs seed data on every restart
- No Content-Security-Policy headers

---

## 🎬 PowerShell Presentation Script

> **Save as `explain.ps1` in the `homistay2/` directory and run:**  
> `powershell -ExecutionPolicy Bypass -File explain.ps1`

```powershell
# ============================================================
#  explain.ps1  —  Homistay Project Walkthrough
#  Run from: homistay2/ directory
#  Command: powershell -ExecutionPolicy Bypass -File explain.ps1
# ============================================================

param(
    [switch]$AutoPlay,           # Skip "Press Enter" prompts
    [int]$AutoPlayDelay = 3      # Seconds between sections in AutoPlay mode
)

$ErrorActionPreference = "SilentlyContinue"

# ── Root detection ─────────────────────────────────────────────────────────────
$script:root = $PSScriptRoot
if (-not $script:root) { $script:root = Split-Path -Parent $MyInvocation.MyCommand.Path }
$backendBase = Join-Path $script:root "homistay\backend\src\main\java\com\homistay"
$frontendBase = Join-Path $script:root "homistay\frontend\src"

# ── Colour helpers ─────────────────────────────────────────────────────────────
function Banner {
    param([string]$Text)
    $width = 60
    $line  = "═" * $width
    Write-Host ""
    Write-Host "  ╔$line╗" -ForegroundColor Cyan
    Write-Host ("  ║  {0,-$($width-2)}║" -f $Text) -ForegroundColor Cyan
    Write-Host "  ╚$line╝" -ForegroundColor Cyan
}

function Title  { param([string]$T); Write-Host "`n  $("─"*56)" -ForegroundColor DarkCyan; Write-Host "  ◆ $T" -ForegroundColor Yellow; Write-Host "  $("─"*56)" -ForegroundColor DarkCyan }
function Info   { param([string]$T); Write-Host "    ✔  $T" -ForegroundColor Green }
function Detail { param([string]$T); Write-Host "       $T" -ForegroundColor DarkGray }
function Accent { param([string]$T); Write-Host "    ▸  $T" -ForegroundColor Magenta }
function Warn   { param([string]$T); Write-Host "    ⚠  $T" -ForegroundColor Yellow }
function Code   { param([string]$T); Write-Host "    $T" -ForegroundColor White }
function Pause  {
    if ($AutoPlay) { Start-Sleep -Seconds $AutoPlayDelay }
    else { Write-Host "`n  " -NoNewline; Read-Host "  ↩  Press Enter to continue" | Out-Null }
}
function Count  { param([string]$Path); (Get-ChildItem $Path -ErrorAction SilentlyContinue).Count }

# ═══════════════════════════════════════════════════════════════
Clear-Host
Banner "🏡  HOMISTAY  —  Project Walkthrough"
Write-Host ""
Write-Host "  Welcome! This script walks through every layer of the" -ForegroundColor White
Write-Host "  Homistay full-stack homestay booking application." -ForegroundColor White
Write-Host ""
Write-Host "  Root path : $($script:root)" -ForegroundColor DarkGray
Pause

# ═══════════════════════════════════════════════════════════════
# SECTION 1 — WHAT IS IT?
# ═══════════════════════════════════════════════════════════════
Banner "SECTION 1 of 10 — WHAT IS HOMISTAY?"

Title "Concept"
Info  "An Airbnb-inspired property rental marketplace"
Detail "Guests can search, book, and review homestay properties"
Detail "Hosts can list properties, manage bookings, and track earnings"
Detail "Built from scratch as a full-stack academic/portfolio project"

Title "User Roles"
Info  "👤  GUEST        → Browse, search, book, wishlist, trip plan, review"
Info  "👑  HOST         → List properties, manage availability, earnings, pricing"
Info  "⚙️   ADMIN        → Full platform access"
Info  "🛟  SUPPORT TEAM → Handle support tickets via dedicated dashboard"
Pause

# ═══════════════════════════════════════════════════════════════
# SECTION 2 — TECH STACK
# ═══════════════════════════════════════════════════════════════
Banner "SECTION 2 of 10 — TECH STACK"

Title "Backend (Java / Spring)"
Info  "Java 21 — LTS runtime with records, text blocks, pattern matching"
Info  "Spring Boot 3.2.5 — auto-configuration, embedded Tomcat"
Info  "Spring Security — JWT filter chain, BCrypt (strength 12)"
Info  "Spring Data JPA + Hibernate 6.4 — ORM, JPQL queries"
Info  "PostgreSQL — primary relational database"
Info  "Maven — build & dependency management"
Info  "Lombok 1.18.38 — @Getter @Setter @Builder boilerplate reduction"
Info  "jjwt 0.12.6 — JSON Web Token generation & validation"
Info  "Springdoc OpenAPI 2.5.0 — Swagger UI auto-generation"

Title "Frontend (React)"
Info  "React 19.2.5 — latest React with concurrent features"
Info  "Vite 8 — instant dev server, optimized builds"
Info  "Tailwind CSS 4.2.4 — utility-first styling"
Info  "wouter 3.3.5 — lightweight client-side routing"
Info  "TanStack React Query 5 — server state & cache"
Info  "Axios 1.16 — HTTP client with interceptors"
Info  "shadcn/ui (53+ Radix primitives) — accessible UI components"
Info  "Leaflet 1.9.4 — interactive property maps"
Info  "Recharts 2.15.2 — earnings bar charts"
Info  "Framer Motion 12 — animations"
Info  "React Hook Form + Zod 4 — form state + schema validation"
Info  "date-fns 3 + react-day-picker 9 — date handling"
Pause

# ═══════════════════════════════════════════════════════════════
# SECTION 3 — LIVE FILE COUNTS
# ═══════════════════════════════════════════════════════════════
Banner "SECTION 3 of 10 — PROJECT SCALE"

Title "Backend File Counts (live)"
$entities    = Count (Join-Path $backendBase "entity\*.java")
$controllers = Count (Join-Path $backendBase "controller\*.java")
$services    = Count (Join-Path $backendBase "service\*.java")
$repos       = Count (Join-Path $backendBase "repository\*.java")
$dtos_req    = Count (Join-Path $backendBase "dto\request\*.java")
$dtos_res    = Count (Join-Path $backendBase "dto\response\*.java")
$enums       = Count (Join-Path $backendBase "enums\*.java")
$security    = Count (Join-Path $backendBase "security\*.java")

Info ("JPA Entities        : {0}" -f $entities)
Info ("REST Controllers    : {0}" -f $controllers)
Info ("Service Classes     : {0}" -f $services)
Info ("Repositories        : {0}" -f $repos)
Info ("Request DTOs        : {0}" -f $dtos_req)
Info ("Response DTOs       : {0}" -f $dtos_res)
Info ("Enumerations        : {0}" -f $enums)
Info ("Security Classes    : {0}" -f $security)

Title "Frontend File Counts (live)"
$guestPages  = Count (Join-Path $frontendBase "pages\*.jsx")
$hostPages   = Count (Join-Path $frontendBase "pages\host\*.jsx")
$components  = Count (Join-Path $frontendBase "components\*.jsx")
$uiPrimitives= Count (Join-Path $frontendBase "components\ui\*.jsx")

Info ("Guest Pages         : {0}" -f $guestPages)
Info ("Host Pages          : {0}" -f $hostPages)
Info ("Custom Components   : {0}" -f $components)
Info ("shadcn/ui Primitives: {0}" -f $uiPrimitives)
Info ("Total Frontend Pages: {0}" -f ($guestPages + $hostPages))

Title "Seed Data (in data.sql)"
Info "23  properties with images, descriptions, amenities"
Info "64  bookings across multiple guests and properties"
Info "57  reviews with star ratings and comments"
Info "13  property add-ons (breakfast, pickup, guided tours)"
Pause

# ═══════════════════════════════════════════════════════════════
# SECTION 4 — BACKEND ARCHITECTURE
# ═══════════════════════════════════════════════════════════════
Banner "SECTION 4 of 10 — BACKEND ARCHITECTURE"

Title "Package Structure"
Code "com.homistay/"
Code "├── config/       SecurityConfig, WebConfig, OpenApiConfig, DataInitializer"
Code "├── controller/   14 REST controllers (AuthController, BookingController, ...)"
Code "├── dto/"
Code "│   ├── request/  Incoming JSON payloads (BookingRequest, PropertyRequest, ...)"
Code "│   └── response/ Outgoing responses (BookingResponse, PricingBreakdownResponse, ...)"
Code "├── entity/       18 JPA entities mapped to PostgreSQL tables"
Code "├── enums/        Role | BookingStatus | PaymentStatus | PropertyType"
Code "├── exception/    GlobalExceptionHandler (structured error responses)"
Code "├── repository/   17 Spring Data JPA repository interfaces"
Code "├── security/     JwtTokenProvider | JwtAuthenticationFilter | CustomUserDetailsService"
Code "└── service/      11 service classes (all business logic lives here)"

Title "Key Services"
Info  "AuthService     — Register, login, JWT generation, profile, role upgrade"
Info  "PropertyService — CRUD, image mgmt, search, addon CRUD, availability blocking"
Info  "BookingService  — Create (pessimistic lock), cancel+refund, modify, message, notes"
Info  "PricingService  — Dynamic pricing: seasonal × demand multiplier per-night"
Info  "ReviewService   — Create review (only for completed booking), list"
Info  "WishlistService — Add/remove/list wishlisted properties"
Info  "HostDashboardService — Stat cards, 12-month earnings chart data"
Info  "FaqService      — Regex-pattern FAQ chatbot (25+ patterns)"
Info  "SupportTicketService — Support ticket lifecycle management"

Title "Security Filter Chain"
Info  "HTTPS Request → JwtAuthenticationFilter → validate token → set SecurityContext"
Info  "→ Spring Security authorization check → Controller → Service → Repository → DB"
Detail "Stateless: SessionCreationPolicy.STATELESS (no server-side sessions)"
Detail "BCrypt strength=12: ~300ms hash time — resistant to brute force"
Pause

# ═══════════════════════════════════════════════════════════════
# SECTION 5 — KEY ALGORITHMS
# ═══════════════════════════════════════════════════════════════
Banner "SECTION 5 of 10 — KEY ALGORITHMS & FEATURES"

Title "1. Pessimistic Lock — Double Booking Prevention"
Info  "BookingService.createBooking() uses @Transactional + SELECT FOR UPDATE"
Code  "List<Booking> overlapping = bookingRepository"
Code  "    .findOverlappingBookingsWithLock(propertyId, checkIn, checkOut);"
Code  "if (!overlapping.isEmpty()) throw BusinessException('Already booked');"
Detail "Prevents race conditions when two users try to book the same dates simultaneously"

Title "2. Dynamic Pricing Engine"
Info  "PricingService calculates effective rate per night across the stay:"
Accent "Step 1: Apply hardcoded default seasons (Summer 1.5×, Holidays 2.0×, etc.)"
Accent "Step 2: Override with host-defined seasonal rates (PERCENTAGE or FIXED)"
Accent "Step 3: Apply demand multiplier based on recent booking count"
Code  "demandMultiplier = minMultiplier + (maxMultiplier - minMultiplier) × ratio"
Detail "Returns full per-night breakdown in PricingBreakdownResponse"

Title "3. Trip Planner — Haversine Scoring"
Info  "TripPlanner.jsx (Step 3) generates 3 optimized plans:"
Accent "Budget Plan   — Cheapest total cost across the stay"
Accent "Nearest Plan  — Smallest average Haversine distance to selected attractions"
Accent "Best Overall  — Weighted: 40% price score + 60% distance (min-max normalized)"
Code  "haversineKm(lat1, lon1, lat2, lon2)  ← Spherical Earth formula"

Title "4. Cancellation Refund Policy"
Info  "Host cancels     → 100% refund to guest"
Info  "Guest, before    → 90% refund (10% service fee retained)"
Info  "Guest, during    → 70% of remaining unused nights refunded"
Detail "Payment status: PAID → REFUNDED or PARTIALLY_REFUNDED"

Title "5. FAQ Chatbot"
Info  "FaqService uses 25+ compiled Java regex Pattern objects"
Code  "Pattern.compile('(?i).*\\b(book|reserve)\\b.*\\b(how|can)\\b.*')"
Info  "If no pattern matches → 'contact support@homistay.com'"
Detail "Covers: booking, cancel, modify, login, register, password, search, wishlist,"
Detail "        payment, refund, hosting, reviews, support contact, platform info"
Pause

# ═══════════════════════════════════════════════════════════════
# SECTION 6 — FRONTEND ARCHITECTURE
# ═══════════════════════════════════════════════════════════════
Banner "SECTION 6 of 10 — FRONTEND ARCHITECTURE"

Title "Routing (wouter — 18 routes)"
Info  "/                   → Home.jsx              (Public)"
Info  "/search             → Search.jsx            (Public)"
Info  "/property/:id       → PropertyDetails.jsx   (Public, 54KB)"
Info  "/checkout           → Checkout.jsx          (Auth, 19KB)"
Info  "/my-bookings        → MyBookings.jsx        (Guest, 30KB)"
Info  "/profile            → Profile.jsx           (Auth, 25KB)"
Info  "/wishlist           → Wishlist.jsx          (Guest)"
Info  "/host/dashboard     → host/Dashboard.jsx    (HOST role)"
Info  "/host/add-property  → host/AddProperty.jsx  (HOST role, 43KB)"
Info  "/host/bookings      → host/Bookings.jsx     (HOST role, 32KB)"
Info  "/host/calendar      → host/Calendar.jsx     (HOST role, 25KB)"
Info  "/host/earnings      → host/Earnings.jsx     (HOST role)"
Info  "/host/pricing       → host/Pricing.jsx      (HOST role, 29KB)"
Info  "/support/dashboard  → SupportDashboard.jsx  (SUPPORT/ADMIN)"

Title "Global State (AppContext)"
Info  "Provides: user, properties, bookings, currentBooking, wishlist"
Info  "On mount: restores session from localStorage via GET /api/auth/me"
Info  "On mount: loads all properties via GET /api/properties?size=50"
Info  "wishlist: array of property ID strings, synced with backend"
Info  "toggleWishlist(): POST or DELETE /api/wishlists/{id}"
Info  "logout(): clears localStorage, redirects to /"

Title "Notable Custom Components"
Info  "Navbar.jsx (19KB)       — Role-aware links, auth modal, Trip Planner trigger"
Info  "SearchBar.jsx (13KB)    — Hero + compact variants, date pickers, guest counter"
Info  "TripPlanner.jsx (564L)  — 3-step city→places→plan wizard"
Info  "PropertyCard.jsx        — Listing thumbnail + wishlist heart"
Info  "ChatPopup.jsx           — Floating chatbot widget"
Pause

# ═══════════════════════════════════════════════════════════════
# SECTION 7 — DATABASE SCHEMA
# ═══════════════════════════════════════════════════════════════
Banner "SECTION 7 of 10 — DATABASE SCHEMA (18 TABLES)"

Title "Core Tables"
Info  "users             — email, passwordHash, fullName, phone, role, bio, dob, gender, avatarUrl"
Info  "properties        — host_id, title, type, city, price_per_night, max_guests, amenities, ..."
Info  "property_images   — property_id, url, is_primary, display_order"
Info  "bookings          — property_id, guest_id, check_in, check_out, adults/children/infants/pets, total_price, status"
Info  "payments          — booking_id, amount, status (PAID|REFUNDED|PARTIALLY_REFUNDED), transaction_id"
Info  "reviews           — booking_id UNIQUE, property_id, guest_id, rating (1-5), comment"
Info  "availability      — property_id, date, is_available, reason (BOOKED|HOST_BLOCKED)"

Title "Feature / Extension Tables"
Info  "wishlists              — (user_id, property_id) UNIQUE"
Info  "property_addons        — property_id, name, description, price, is_active"
Info  "booking_addons         — booking_id, addon_id, quantity, price"
Info  "booking_modifications  — booking_id, new_check_in, new_check_out, status, host_response"
Info  "booking_messages       — booking_id, sender_id, message, created_at"
Info  "experiences            — host-offered local tours/activities"
Info  "booking_experiences    — M2M join: booking ↔ experience"
Info  "seasonal_rates         — property_id, start_date, end_date, adjustment_type, adjustment_value"
Info  "dynamic_pricing_configs — property_id, enabled, min/max_multiplier, demand_threshold"
Info  "chat_queries           — user questions + chatbot answers log"
Info  "support_tickets        — user_id, subject, description, status, resolution"
Pause

# ═══════════════════════════════════════════════════════════════
# SECTION 8 — API ENDPOINTS
# ═══════════════════════════════════════════════════════════════
Banner "SECTION 8 of 10 — REST API ENDPOINTS"

Title "Auth  (/api/auth)"
Info  "POST /api/auth/register              — Register new user"
Info  "POST /api/auth/login                 — Login → { accessToken, refreshToken, user }"
Info  "GET  /api/auth/me                    — Current user profile"
Info  "POST /api/auth/refresh               — Refresh token pair"
Info  "PUT  /api/auth/profile               — Update profile (multipart with avatar)"
Info  "PUT  /api/auth/change-password       — Change password"
Info  "POST /api/auth/upgrade-to-host       — Upgrade GUEST → HOST role"

Title "Properties  (/api/properties — GET: public, write: HOST)"
Info  "GET  /api/properties                 — Search (city, type, price, guests, dates)"
Info  "GET  /api/properties/{id}            — Property details + addons + seasonal price"
Info  "POST /api/properties                 — Create listing"
Info  "PUT  /api/properties/{id}            — Update listing"
Info  "DELETE /api/properties/{id}          — Delete listing (hard delete)"
Info  "GET  /api/properties/{id}/blocked-dates — Unavailable dates array"
Info  "GET  /api/properties/{id}/pricing    — Pricing breakdown for date range"

Title "Bookings  (/api/bookings — authenticated)"
Info  "POST   /api/bookings                 — Create booking (pessimistic lock)"
Info  "GET    /api/bookings/my              — Guest's bookings (paginated)"
Info  "GET    /api/bookings/{id}            — Single booking detail"
Info  "PATCH  /api/bookings/{id}/cancel     — Cancel + refund calculation"
Info  "POST   /api/bookings/{id}/messages   — Send message"
Info  "GET    /api/bookings/{id}/messages   — Get message thread"
Info  "POST   /api/bookings/{id}/modification  — Request date/guest change"
Info  "GET    /api/bookings/{id}/modifications — Modification history"

Title "Host  (/api/host — HOST or ADMIN only)"
Info  "GET  /api/host/dashboard             — Stat cards + recent bookings"
Info  "GET  /api/host/earnings/monthly      — 12-month earnings data"
Info  "GET  /api/host/properties            — Host's property listings"
Info  "GET  /api/host/bookings              — Bookings (filterable by status)"
Info  "POST /api/host/properties/{id}/availability — Block / unblock dates"
Info  "CRUD /api/host/properties/{id}/addons       — Manage extra services"
Info  "PATCH /api/host/bookings/{id}/notes         — Update host notes"
Info  "PATCH /api/host/bookings/{id}/special-request-status — Accept/decline request"
Info  "PATCH /api/host/bookings/{bid}/modifications/{mid}   — Approve/deny modification"

Title "Pricing  (/api/host/pricing)"
Info  "POST /api/host/pricing/seasons       — Create seasonal rate"
Info  "PUT  /api/host/pricing/seasons/{id}  — Update seasonal rate"
Info  "DELETE /api/host/pricing/seasons/{id} — Delete seasonal rate"
Info  "PUT  /api/host/pricing/config        — Save dynamic pricing config"
Info  "GET  /api/host/pricing/config/{id}   — Get dynamic pricing config"

Title "Other Endpoints"
Info  "POST /api/chatbot/ask                — FAQ chatbot query"
Info  "GET/POST /api/wishlists/**           — Wishlist management"
Info  "GET/POST /api/reviews               — Reviews"
Info  "GET/POST /api/experiences           — Local experiences"
Info  "GET/PATCH /api/support/**           — Support dashboard + tickets"
Info  "GET/POST /api/tickets              — User ticket management"
Pause

# ═══════════════════════════════════════════════════════════════
# SECTION 9 — HOW TO RUN
# ═══════════════════════════════════════════════════════════════
Banner "SECTION 9 of 10 — RUNNING THE PROJECT"

Title "Prerequisites"
Info  "JDK 21+ installed (verify: java -version)"
Info  "Node.js 20+ installed (verify: node -v)"
Info  "PostgreSQL running on localhost:5432"
Info  "Database 'test' must exist: CREATE DATABASE test;"

Title "Start Backend"
Code  "cd $($script:root)\homistay\backend"
Code  "mvn spring-boot:run"
Code  ""
Code  "  → API:     http://localhost:8088"
Code  "  → Swagger: http://localhost:8088/swagger-ui/index.html"
Code  ""
Code  "  On first run: schema.sql creates all 18 tables"
Code  "                data.sql seeds 23 properties + 64 bookings"

Title "Start Frontend"
Code  "cd $($script:root)\homistay\frontend"
Code  "npm install   # or pnpm install"
Code  "npm run dev"
Code  ""
Code  "  → App: http://localhost:5173"

Title "Live Status Check"
$be88 = Get-NetTCPConnection -LocalPort 8088 -ErrorAction SilentlyContinue
$fe53 = Get-NetTCPConnection -LocalPort 5173 -ErrorAction SilentlyContinue
if ($be88) { Info "✅ Backend  is RUNNING on port 8088" }
else       { Warn "⚠  Backend  is NOT running — start with 'mvn spring-boot:run'" }
if ($fe53) { Info "✅ Frontend is RUNNING on port 5173" }
else       { Warn "⚠  Frontend is NOT running — start with 'npm run dev'" }

Title "Quick Test Credentials"
Info  "Register a new account via the UI (easiest)"
Info  "Then POST /api/auth/upgrade-to-host to become a Host"
Info  "Support user: support@homistay.com / password123"
Pause

# ═══════════════════════════════════════════════════════════════
# SECTION 10 — SUMMARY
# ═══════════════════════════════════════════════════════════════
Banner "SECTION 10 of 10 — SUMMARY & ARCHITECTURE DIAGRAM"

Title "Architecture at a Glance"
Write-Host ""
Write-Host "  ┌─────────────────────────────────────────────────────────┐" -ForegroundColor Cyan
Write-Host "  │              BROWSER (React 19 + Vite)                  │" -ForegroundColor Cyan
Write-Host "  │  HomePage  SearchPage  PropertyDetails  Checkout  ...   │" -ForegroundColor White
Write-Host "  │  HostDashboard  Calendar  Earnings  Pricing  ...        │" -ForegroundColor White
Write-Host "  │              ↑ AppContext (global state)                │" -ForegroundColor DarkGray
Write-Host "  │  TripPlanner    ChatPopup    Navbar    SearchBar         │" -ForegroundColor White
Write-Host "  └──────────────────────┬──────────────────────────────────┘" -ForegroundColor Cyan
Write-Host "                         │  Axios HTTP (JWT Bearer token)" -ForegroundColor DarkGray
Write-Host "                         ↓" -ForegroundColor DarkGray
Write-Host "  ┌─────────────────────────────────────────────────────────┐" -ForegroundColor Yellow
Write-Host "  │          SPRING BOOT 3.2 (port 8088)                    │" -ForegroundColor Yellow
Write-Host "  │  JwtAuthFilter → Controllers → Services → Repositories  │" -ForegroundColor White
Write-Host "  │  AuthService  BookingService  PricingService  FaqService │" -ForegroundColor White
Write-Host "  │  PropertyService  HostDashboardService  WishlistService  │" -ForegroundColor White
Write-Host "  └──────────────────────┬──────────────────────────────────┘" -ForegroundColor Yellow
Write-Host "                         │  Spring Data JPA (Hibernate)" -ForegroundColor DarkGray
Write-Host "                         ↓" -ForegroundColor DarkGray
Write-Host "  ┌─────────────────────────────────────────────────────────┐" -ForegroundColor Magenta
Write-Host "  │        PostgreSQL (localhost:5432 / db: test)           │" -ForegroundColor Magenta
Write-Host "  │  18 tables  ·  23 seed properties  ·  64 bookings       │" -ForegroundColor White
Write-Host "  └─────────────────────────────────────────────────────────┘" -ForegroundColor Magenta
Write-Host ""

Title "What Makes This Stand Out"
Info  "Pessimistic lock on booking — production-grade concurrency"
Info  "Multi-layer dynamic pricing — seasonal + demand-based per night"
Info  "Full booking lifecycle — create, cancel+refund, modify, message, review"
Info  "Host Calendar — visual availability grid with bulk date blocking"
Info  "Trip Planner — Haversine algorithm + min-max normalized scoring"
Info  "FAQ Chatbot — 25+ compiled regex patterns, no external AI needed"
Info  "5-step property creation wizard with Nominatim auto-geocoding"
Info  "Add-ons system — bookable extras per property"
Info  "Rich seed data — demo-ready out of the box"
Info  "Swagger UI — full API docs at /swagger-ui/index.html"

Title "Documentation Files"
Info  "PROJECT_PRESENTATION.md — This full writeup"
Info  "SECURITY_AUDIT.md       — Security findings + upgrade path"
Info  "trip_planner.md         — Trip Planner design and algorithm"
Info  "progress.txt            — Full development change log"
Info  "implementation_plan.md  — Original feature planning"
Info  "walkthrough.md          — Development walkthrough notes"

Write-Host ""
Write-Host "  ╔════════════════════════════════════════════════════════╗" -ForegroundColor Green
Write-Host "  ║   Walkthrough Complete!  Thanks for your attention.   ║" -ForegroundColor Green
Write-Host "  ║                                                        ║" -ForegroundColor Green
Write-Host "  ║   Swagger : http://localhost:8088/swagger-ui/index.html║" -ForegroundColor Green
Write-Host "  ║   App     : http://localhost:5173                      ║" -ForegroundColor Green
Write-Host "  ╚════════════════════════════════════════════════════════╝" -ForegroundColor Green
Write-Host ""
```

---

## 📝 How to Run the Script

```powershell
# Option 1: Interactive (press Enter between each section)
powershell -ExecutionPolicy Bypass -File explain.ps1

# Option 2: Auto-play (auto-advances every 3 seconds)
powershell -ExecutionPolicy Bypass -File explain.ps1 -AutoPlay

# Option 3: Auto-play with 5 second delay
powershell -ExecutionPolicy Bypass -File explain.ps1 -AutoPlay -AutoPlayDelay 5
```

> **Tip**: Run after starting both backend and frontend — the script will detect live ports and show ✅ or ⚠️ for each service.

---

*Generated: June 2026 | Homistay v1.0.0*
