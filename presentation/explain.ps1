# ============================================================
#  explain.ps1  —  Homistay Project Walkthrough
#  Location: homistay2/presentation/explain.ps1
#  Run: powershell -ExecutionPolicy Bypass -File presentation\explain.ps1
# ============================================================

param(
    [switch]$AutoPlay,           # Skip "Press Enter" prompts
    [int]$AutoPlayDelay = 3      # Seconds between sections in AutoPlay mode
)

$ErrorActionPreference = "SilentlyContinue"

# ── Root detection ─────────────────────────────────────────────────────────────
# Script lives in presentation/ subfolder, so go one level up to reach homistay2/
$scriptDir   = if ($PSScriptRoot) { $PSScriptRoot } else { Split-Path -Parent $MyInvocation.MyCommand.Path }
$script:root = Split-Path -Parent $scriptDir          # homistay2/
$backendBase = Join-Path $script:root "homistay\backend\src\main\java\com\homistay"
$frontendBase = Join-Path $script:root "homistay\frontend\src"

# ── Colour helpers ─────────────────────────────────────────────────────────────
function Banner {
    param([string]$Text)
    $width = 60
    $line  = "=" * $width
    Write-Host ""
    Write-Host "  +$line+" -ForegroundColor Cyan
    Write-Host ("  |  {0,-$($width-2)}|" -f $Text) -ForegroundColor Cyan
    Write-Host "  +$line+" -ForegroundColor Cyan
}

function Title  { param([string]$T); Write-Host "`n  $("-"*56)" -ForegroundColor DarkCyan; Write-Host "  >> $T" -ForegroundColor Yellow; Write-Host "  $("-"*56)" -ForegroundColor DarkCyan }
function Info   { param([string]$T); Write-Host "    OK  $T" -ForegroundColor Green }
function Detail { param([string]$T); Write-Host "        $T" -ForegroundColor DarkGray }
function Accent { param([string]$T); Write-Host "    >>  $T" -ForegroundColor Magenta }
function Warn   { param([string]$T); Write-Host "    !!  $T" -ForegroundColor Yellow }
function Code   { param([string]$T); Write-Host "    $T" -ForegroundColor White }
function Pause  {
    if ($AutoPlay) { Start-Sleep -Seconds $AutoPlayDelay }
    else { Write-Host "`n  " -NoNewline; Read-Host "  Press Enter to continue" | Out-Null }
}
function Count  { param([string]$Path); (Get-ChildItem $Path -ErrorAction SilentlyContinue).Count }

# =============================================================================
Clear-Host
Banner "  HOMISTAY  ---  Full-Stack Homestay Booking App"
Write-Host ""
Write-Host "  Welcome! This script walks through every layer of Homistay." -ForegroundColor White
Write-Host "  It is an Airbnb-inspired platform built with:" -ForegroundColor White
Write-Host "     React 19  +  Spring Boot 3.2  +  PostgreSQL" -ForegroundColor Cyan
Write-Host ""
Write-Host "  Root path : $($script:root)" -ForegroundColor DarkGray
Pause

# =============================================================================
# SECTION 1 — WHAT IS IT?
# =============================================================================
Banner "SECTION 1 of 10 --- WHAT IS HOMISTAY?"

Title "Concept"
Info  "An Airbnb-inspired property rental marketplace"
Detail "Guests can search, book, and review homestay properties"
Detail "Hosts can list properties, manage bookings, and track earnings"
Detail "Built as a full-stack project with real-world patterns"

Title "User Roles"
Info  "GUEST        -- Browse, search, book, wishlist, trip plan, review"
Info  "HOST         -- List properties, manage availability, earnings, pricing"
Info  "ADMIN        -- Full platform access"
Info  "SUPPORT TEAM -- Handle support tickets via dedicated dashboard"
Pause

# =============================================================================
# SECTION 2 — TECH STACK
# =============================================================================
Banner "SECTION 2 of 10 --- TECH STACK"

Title "Backend (Java / Spring)"
Info  "Java 21 -- LTS runtime"
Info  "Spring Boot 3.2.5 -- auto-configuration, embedded Tomcat"
Info  "Spring Security -- JWT filter chain, BCrypt (strength 12)"
Info  "Spring Data JPA + Hibernate 6.4 -- ORM, JPQL queries"
Info  "PostgreSQL -- primary relational database"
Info  "Maven -- build and dependency management"
Info  "Lombok 1.18.38 -- boilerplate reduction"
Info  "jjwt 0.12.6 -- JWT generation and validation"
Info  "Springdoc OpenAPI 2.5.0 -- Swagger UI"

Title "Frontend (React)"
Info  "React 19.2.5 -- latest React"
Info  "Vite 8 -- instant dev server, optimized builds"
Info  "Tailwind CSS 4.2.4 -- utility-first styling"
Info  "wouter 3.3.5 -- lightweight routing"
Info  "TanStack React Query 5 -- server state and cache"
Info  "Axios 1.16 -- HTTP client with interceptors"
Info  "shadcn/ui (53+ Radix primitives) -- accessible UI"
Info  "Leaflet 1.9.4 -- interactive property maps"
Info  "Recharts 2.15.2 -- earnings bar charts"
Info  "Framer Motion 12 -- animations"
Info  "React Hook Form + Zod 4 -- form state + validation"
Pause

# =============================================================================
# SECTION 3 — PROJECT SCALE
# =============================================================================
Banner "SECTION 3 of 10 --- PROJECT SCALE"

Title "Backend File Counts (live from disk)"
$entities    = Count (Join-Path $backendBase "entity\*.java")
$controllers = Count (Join-Path $backendBase "controller\*.java")
$services    = Count (Join-Path $backendBase "service\*.java")
$repos       = Count (Join-Path $backendBase "repository\*.java")
$dtos_req    = Count (Join-Path $backendBase "dto\request\*.java")
$dtos_res    = Count (Join-Path $backendBase "dto\response\*.java")
$enums       = Count (Join-Path $backendBase "enums\*.java")
$security    = Count (Join-Path $backendBase "security\*.java")

Info ("JPA Entities         : {0}" -f $entities)
Info ("REST Controllers     : {0}" -f $controllers)
Info ("Service Classes      : {0}" -f $services)
Info ("Repositories         : {0}" -f $repos)
Info ("Request DTOs         : {0}" -f $dtos_req)
Info ("Response DTOs        : {0}" -f $dtos_res)
Info ("Enumerations         : {0}" -f $enums)
Info ("Security Classes     : {0}" -f $security)

Title "Frontend File Counts (live from disk)"
$guestPages   = Count (Join-Path $frontendBase "pages\*.jsx")
$hostPages    = Count (Join-Path $frontendBase "pages\host\*.jsx")
$components   = Count (Join-Path $frontendBase "components\*.jsx")
$uiPrimitives = Count (Join-Path $frontendBase "components\ui\*.jsx")

Info ("Guest Pages          : {0}" -f $guestPages)
Info ("Host Pages           : {0}" -f $hostPages)
Info ("Custom Components    : {0}" -f $components)
Info ("shadcn/ui Primitives : {0}" -f $uiPrimitives)
Info ("Total Pages          : {0}" -f ($guestPages + $hostPages))

Title "Seed Data in data.sql"
Info "23  properties with images, descriptions, amenities"
Info "64  bookings across multiple guests and properties"
Info "57  reviews with star ratings and comments"
Info "13  property add-ons (breakfast, pickup, guided tours)"
Pause

# =============================================================================
# SECTION 4 — BACKEND ARCHITECTURE
# =============================================================================
Banner "SECTION 4 of 10 --- BACKEND ARCHITECTURE"

Title "Package Structure"
Code "com.homistay/"
Code "|-- config/       SecurityConfig, WebConfig, OpenApiConfig, DataInitializer"
Code "|-- controller/   14 REST controllers"
Code "|-- dto/"
Code "|   |-- request/  Incoming JSON payloads"
Code "|   +-- response/ Outgoing response objects"
Code "|-- entity/       18 JPA entities mapped to PostgreSQL tables"
Code "|-- enums/        Role | BookingStatus | PaymentStatus | PropertyType"
Code "|-- exception/    GlobalExceptionHandler"
Code "|-- repository/   17 Spring Data JPA repositories"
Code "|-- security/     JwtTokenProvider | JwtAuthenticationFilter"
Code "+-- service/      11 service classes (all business logic)"

Title "Key Services"
Info  "AuthService         -- Register, login, JWT, profile, role upgrade"
Info  "PropertyService     -- CRUD, search, addons, availability blocking"
Info  "BookingService      -- Create (pessimistic lock), cancel+refund, modify, message"
Info  "PricingService      -- Dynamic pricing: seasonal x demand multiplier per-night"
Info  "ReviewService       -- Create (only after completed booking), list"
Info  "WishlistService     -- Add/remove/list wishlisted properties"
Info  "HostDashboardService-- Stat cards, 12-month earnings chart data"
Info  "FaqService          -- Regex-pattern FAQ chatbot (25+ patterns)"
Info  "SupportTicketService-- Ticket lifecycle management"

Title "Security Filter Chain"
Info  "Request --> JwtAuthenticationFilter --> validate token --> set SecurityContext"
Info  "--> Spring Security authorization check --> Controller --> Service --> DB"
Detail "Stateless: SessionCreationPolicy.STATELESS (no server-side sessions)"
Detail "BCrypt strength=12: ~300ms hash time -- brute-force resistant"
Pause

# =============================================================================
# SECTION 5 — KEY ALGORITHMS
# =============================================================================
Banner "SECTION 5 of 10 --- KEY ALGORITHMS"

Title "1. Pessimistic Lock -- Double Booking Prevention"
Info  "BookingService.createBooking() uses @Transactional + SELECT FOR UPDATE"
Code  "overlapping = bookingRepository.findOverlappingBookingsWithLock(...);"
Code  "if (!overlapping.isEmpty()) throw BusinessException('Already booked');"
Detail "Prevents race conditions when two users try to book the same dates"

Title "2. Dynamic Pricing Engine (PricingService.java)"
Info  "Calculates effective rate PER NIGHT across the entire stay:"
Accent "Step 1: Apply hardcoded default seasons (Summer 1.5x, Holidays 2.0x, etc.)"
Accent "Step 2: Override with host-defined seasonal rates (PERCENTAGE or FIXED)"
Accent "Step 3: Apply demand multiplier based on recent booking count"
Code  "demandMultiplier = minMult + (maxMult - minMult) * (bookings / threshold)"
Detail "Returns full per-night breakdown in PricingBreakdownResponse"

Title "3. Trip Planner -- Haversine Scoring (TripPlanner.jsx)"
Info  "3 plan types generated from available properties in selected city:"
Accent "Budget Plan   -- Cheapest total cost (nights x price + cleaning fee)"
Accent "Nearest Plan  -- Smallest average Haversine distance to attractions"
Accent "Best Overall  -- Weighted: 40pct price + 60pct distance (min-max normalized)"

Title "4. Cancellation Refund Policy (BookingService.cancelBooking)"
Info  "Host cancels    --> 100% refund to guest"
Info  "Guest, before   --> 90% refund (10% service fee retained)"
Info  "Guest, during   --> 70% of remaining unused nights refunded"
Detail "Payment: PAID --> REFUNDED or PARTIALLY_REFUNDED"

Title "5. FAQ Chatbot (FaqService.java)"
Info  "25+ compiled Java regex Pattern objects map question --> answer"
Info  "Covers: booking, cancel, modify, login, register, search, wishlist,"
Info  "        payment, refund, hosting, reviews, support, platform info"
Info  "No match --> 'contact support@homistay.com'"
Pause

# =============================================================================
# SECTION 6 — FRONTEND ARCHITECTURE
# =============================================================================
Banner "SECTION 6 of 10 --- FRONTEND ARCHITECTURE"

Title "18 Routes (wouter)"
Info  "/                  --> Home.jsx              (Public)"
Info  "/search            --> Search.jsx            (Public)"
Info  "/property/:id      --> PropertyDetails.jsx   (Public, 54KB!)"
Info  "/checkout          --> Checkout.jsx          (Auth, 19KB)"
Info  "/my-bookings       --> MyBookings.jsx        (Guest, 30KB)"
Info  "/profile           --> Profile.jsx           (Auth, 25KB)"
Info  "/wishlist          --> Wishlist.jsx          (Guest)"
Info  "/host/dashboard    --> host/Dashboard.jsx    (HOST role)"
Info  "/host/add-property --> host/AddProperty.jsx  (HOST role, 43KB!)"
Info  "/host/bookings     --> host/Bookings.jsx     (HOST role, 32KB)"
Info  "/host/calendar     --> host/Calendar.jsx     (HOST role, 25KB)"
Info  "/host/earnings     --> host/Earnings.jsx     (HOST role)"
Info  "/host/pricing      --> host/Pricing.jsx      (HOST role, 29KB)"
Info  "/support/dashboard --> SupportDashboard.jsx  (SUPPORT/ADMIN)"

Title "Global State (AppContext.jsx)"
Info  "Provides: user, properties, bookings, currentBooking, wishlist"
Info  "On mount: restores session from localStorage via GET /api/auth/me"
Info  "On mount: loads all properties via GET /api/properties?size=50"
Info  "toggleWishlist(): POST or DELETE /api/wishlists/{id}"
Info  "logout(): clears localStorage, redirects to /"
Info  "Listens for 'auth:logout' event from Axios 401 interceptor"

Title "Notable Custom Components"
Info  "Navbar.jsx (19KB)      -- Role-aware links, auth modal, Trip Planner"
Info  "SearchBar.jsx (13KB)   -- Hero + compact variants, date pickers"
Info  "TripPlanner.jsx (564L) -- 3-step city->places->plan wizard"
Info  "PropertyCard.jsx       -- Listing thumbnail + wishlist heart"
Info  "ChatPopup.jsx          -- Floating chatbot widget"
Pause

# =============================================================================
# SECTION 7 — DATABASE SCHEMA
# =============================================================================
Banner "SECTION 7 of 10 --- DATABASE SCHEMA (18 TABLES)"

Title "Core Tables"
Info  "users          -- email, passwordHash, fullName, phone, role, bio, dob, gender"
Info  "properties     -- host_id, title, type, city, price_per_night, amenities, ..."
Info  "property_images-- property_id, url, is_primary, display_order"
Info  "bookings       -- property_id, guest_id, dates, adults/children/infants/pets, status"
Info  "payments       -- booking_id, amount, status, transaction_id"
Info  "reviews        -- booking_id UNIQUE, rating (1-5), comment"
Info  "availability   -- property_id, date, is_available, reason (BOOKED|HOST_BLOCKED)"

Title "Feature Tables"
Info  "wishlists             -- (user_id, property_id) UNIQUE constraint"
Info  "property_addons       -- name, description, price, is_active"
Info  "booking_addons        -- booking_id, addon_id, quantity, price"
Info  "booking_modifications -- new dates, reason, status, host_response"
Info  "booking_messages      -- booking_id, sender_id, message, created_at"
Info  "seasonal_rates        -- start_date, end_date, adjustment_type, value"
Info  "dynamic_pricing_configs -- enabled, min/max_multiplier, demand_threshold"
Info  "chat_queries          -- question + chatbot answer log"
Info  "support_tickets       -- subject, description, status, resolution"
Info  "experiences           -- local tours/activities by hosts"
Pause

# =============================================================================
# SECTION 8 — API OVERVIEW
# =============================================================================
Banner "SECTION 8 of 10 --- REST API ENDPOINTS"

Title "Auth (/api/auth)"
Info  "POST /api/auth/register              -- Register new user"
Info  "POST /api/auth/login                 -- Login (returns accessToken + refreshToken)"
Info  "GET  /api/auth/me                    -- Current user profile"
Info  "POST /api/auth/refresh               -- Refresh token pair"
Info  "PUT  /api/auth/profile               -- Update profile (multipart + avatar)"
Info  "PUT  /api/auth/change-password       -- Change password"
Info  "POST /api/auth/upgrade-to-host       -- Upgrade GUEST to HOST role"

Title "Properties (/api/properties)"
Info  "GET    /api/properties               -- Search (city, type, price, guests, dates)"
Info  "GET    /api/properties/{id}          -- Full property detail + addons + price"
Info  "POST   /api/properties               -- Create listing (HOST)"
Info  "PUT    /api/properties/{id}          -- Update listing (HOST owner)"
Info  "DELETE /api/properties/{id}          -- Delete listing (HOST owner)"
Info  "GET    /api/properties/{id}/pricing  -- Pricing breakdown for date range"

Title "Bookings (/api/bookings)"
Info  "POST   /api/bookings                 -- Create booking (pessimistic lock)"
Info  "GET    /api/bookings/my              -- Guest's own bookings"
Info  "PATCH  /api/bookings/{id}/cancel     -- Cancel + refund calculation"
Info  "POST   /api/bookings/{id}/messages   -- Send message"
Info  "POST   /api/bookings/{id}/modification -- Request date/guest change"

Title "Host (/api/host  -- HOST or ADMIN)"
Info  "GET  /api/host/dashboard             -- Stat cards + recent bookings"
Info  "GET  /api/host/earnings/monthly      -- 12-month earnings data"
Info  "POST /api/host/properties/{id}/availability -- Block/unblock dates"
Info  "CRUD /api/host/properties/{id}/addons       -- Extra services management"
Info  "PATCH /api/host/bookings/{id}/notes         -- Host notes"
Info  "PATCH /api/host/bookings/{id}/special-request-status -- Accept/decline"
Info  "PATCH /api/host/bookings/{bid}/modifications/{mid}   -- Approve/deny mod"

Title "Other"
Info  "POST /api/chatbot/ask                -- FAQ chatbot"
Info  "GET/POST /api/wishlists/**           -- Wishlist CRUD"
Info  "POST /api/reviews                   -- Create review"
Info  "CRUD /api/host/pricing/**           -- Seasonal + dynamic pricing"
Pause

# =============================================================================
# SECTION 9 — HOW TO RUN
# =============================================================================
Banner "SECTION 9 of 10 --- RUNNING THE PROJECT"

Title "Prerequisites"
Info  "JDK 21+       (verify: java -version)"
Info  "Node.js 20+   (verify: node -v)"
Info  "PostgreSQL on localhost:5432"
Info  "Database 'test' must exist: CREATE DATABASE test;"

Title "Start Backend"
Code  "cd $($script:root)\homistay\backend"
Code  "mvn spring-boot:run"
Code  ""
Code  "  API     --> http://localhost:8088"
Code  "  Swagger --> http://localhost:8088/swagger-ui/index.html"
Code  ""
Code  "  First run: schema.sql creates tables, data.sql seeds 23 properties"

Title "Start Frontend"
Code  "cd $($script:root)\homistay\frontend"
Code  "npm install"
Code  "npm run dev"
Code  ""
Code  "  App --> http://localhost:5173"

Title "Live Port Status"
$be88 = Get-NetTCPConnection -LocalPort 8088 -ErrorAction SilentlyContinue
$fe53 = Get-NetTCPConnection -LocalPort 5173 -ErrorAction SilentlyContinue
if ($be88) { Info "Backend  is RUNNING on port 8088" }
else       { Warn "Backend  is NOT running -- start with 'mvn spring-boot:run'" }
if ($fe53) { Info "Frontend is RUNNING on port 5173" }
else       { Warn "Frontend is NOT running -- start with 'npm run dev'" }

Title "Quick Test Credentials"
Info  "Register a new account via the UI (easiest)"
Info  "Then POST /api/auth/upgrade-to-host to become a Host"
Info  "Support user: support@homistay.com / password123"
Pause

# =============================================================================
# SECTION 10 — SUMMARY
# =============================================================================
Banner "SECTION 10 of 10 --- ARCHITECTURE SUMMARY"

Title "System Architecture"
Write-Host ""
Write-Host "  +----------------------------------------------------------+" -ForegroundColor Cyan
Write-Host "  |           BROWSER  (React 19 + Vite)                    |" -ForegroundColor Cyan
Write-Host "  |  Home  Search  PropertyDetails  Checkout  MyBookings    |" -ForegroundColor White
Write-Host "  |  HostDashboard  Calendar  Earnings  Pricing  Support    |" -ForegroundColor White
Write-Host "  |         ^ AppContext (user, properties, wishlist)        |" -ForegroundColor DarkGray
Write-Host "  +---------------------------+------------------------------+" -ForegroundColor Cyan
Write-Host "                              | Axios HTTP (JWT Bearer)" -ForegroundColor DarkGray
Write-Host "                              v" -ForegroundColor DarkGray
Write-Host "  +----------------------------------------------------------+" -ForegroundColor Yellow
Write-Host "  |        SPRING BOOT 3.2  (port 8088)                     |" -ForegroundColor Yellow
Write-Host "  |  JwtAuthFilter -> Controllers -> Services -> Repos       |" -ForegroundColor White
Write-Host "  |  Auth  Property  Booking  Pricing  Review  Wishlist     |" -ForegroundColor White
Write-Host "  |  HostDashboard  FAQ/Chatbot  Support  Experience        |" -ForegroundColor White
Write-Host "  +---------------------------+------------------------------+" -ForegroundColor Yellow
Write-Host "                              | Spring Data JPA (Hibernate)" -ForegroundColor DarkGray
Write-Host "                              v" -ForegroundColor DarkGray
Write-Host "  +----------------------------------------------------------+" -ForegroundColor Magenta
Write-Host "  |     PostgreSQL  (localhost:5432 / database: test)        |" -ForegroundColor Magenta
Write-Host "  |  18 tables  *  23 seed properties  *  64 bookings        |" -ForegroundColor White
Write-Host "  +----------------------------------------------------------+" -ForegroundColor Magenta
Write-Host ""

Title "Key Differentiators"
Info  "Pessimistic lock on booking -- real concurrency safety"
Info  "Multi-layer dynamic pricing -- seasonal + demand-based per night"
Info  "Full booking lifecycle -- create, cancel+refund, modify, message, review"
Info  "Host Calendar -- visual grid with single + bulk date blocking"
Info  "Trip Planner -- Haversine + min-max normalized 3-plan wizard"
Info  "FAQ Chatbot -- 25+ compiled regex, no external AI dependency"
Info  "5-step add property wizard with Nominatim auto-geocoding"
Info  "Add-ons system -- bookable extras per property"
Info  "Rich seed data -- demo-ready out of the box"
Info  "Swagger UI -- full API docs at /swagger-ui/index.html"

Title "Documentation"
Info  "PROJECT_PRESENTATION.md -- Full writeup with all details"
Info  "SECURITY_AUDIT.md       -- Security findings + upgrade path"
Info  "trip_planner.md         -- Trip Planner design doc"
Info  "progress.txt            -- Full development change log"

Write-Host ""
Write-Host "  +========================================================+" -ForegroundColor Green
Write-Host "  |   Walkthrough Complete! Thank you for your attention.  |" -ForegroundColor Green
Write-Host "  |                                                        |" -ForegroundColor Green
Write-Host "  |   Swagger : http://localhost:8088/swagger-ui/index.html|" -ForegroundColor Green
Write-Host "  |   App     : http://localhost:5173                      |" -ForegroundColor Green
Write-Host "  +========================================================+" -ForegroundColor Green
Write-Host ""
