# 🏡 Homistay — Module-by-Module Presentation Scripts

> **17 modules · ~200 words each · with technical terminology**  
> Use these scripts when walking through individual modules during a demo or viva.

---

## Module 1 — Authentication & User Management

> *Files: `AuthController.java`, `AuthService.java`, `JwtTokenProvider.java`, `JwtAuthenticationFilter.java`, `Profile.jsx`*

---

The Authentication and User Management module is the security backbone of the entire Homistay platform. It handles user registration, login, session management, and profile updates in a completely **stateless** manner using **JSON Web Tokens (JWT)**.

When a user registers, the system validates the request payload using **Bean Validation** annotations, hashes the password using **BCrypt with a cost factor of 12** — which makes brute-force attacks computationally expensive — and persists the user entity to PostgreSQL via Spring Data JPA.

On login, Spring Security's `AuthenticationManager` verifies credentials against the stored BCrypt hash. If valid, the `JwtTokenProvider` generates two tokens: a short-lived **access token** (15 minutes) and a long-lived **refresh token** (7 days), both signed with an **HMAC-SHA256** secret key. These are returned to the client and stored in `localStorage`.

Every subsequent API request passes through the `JwtAuthenticationFilter` — a Spring `OncePerRequestFilter` — which extracts the Bearer token from the `Authorization` header, validates its signature and expiry, and loads the `UserDetails` into the **SecurityContext**.

The system supports four roles — `GUEST`, `HOST`, `ADMIN`, and `SUPPORT_TEAM` — enforced at the endpoint level via `@EnableMethodSecurity` and `requestMatchers`. A guest can upgrade to a host by calling the `/api/auth/upgrade-to-host` endpoint, which atomically updates the user's role in the database.

Profile updates support **multipart form data**, allowing users to upload an avatar image. The file is saved to a local `uploads/profile/` directory with a UUID-based filename to prevent collisions. Password changes require the current password for re-verification before the new hash is persisted.

---

## Module 2 — Property Listings

> *Files: `PropertyController.java`, `PropertyService.java`, `Property.java`, `PropertyImage.java`, `PropertyAddon.java`, `PropertyDetails.jsx`, `AddProperty.jsx`*

---

The Property Listings module manages the entire lifecycle of a homestay listing — from creation by a host to public display for guests. It is the central data entity of the platform, with 18 fields covering everything from location coordinates to guest-restriction policies.

A `Property` entity includes: title, description, `PropertyType` enum (HOUSE, APARTMENT, VILLA, COTTAGE, STUDIO, CABIN, RESORT), city, country, GPS coordinates (`latitude` / `longitude`), `pricePerNight`, `cleaningFee`, `maxGuests`, `bedrooms`, `bathrooms`, `amenities` (stored as a comma-separated string), `houseRules`, `guestRequirements`, `checkInInstructions`, and three boolean flags — `allowsChildren`, `allowsInfants`, and `allowsPets`.

Associated `PropertyImage` entities maintain an ordered list of image URLs with an `isPrimary` flag and `displayOrder` for the gallery carousel displayed on the frontend.

The `PropertyService.mapToResponse()` method enriches raw entity data before returning it to the client — it fetches the average rating from the `ReviewRepository`, resolves the primary image, splits the amenities CSV into a list, checks for active seasonal rates via `SeasonalRateRepository`, and computes an `effectivePricePerNight` to reflect any current seasonal pricing.

On the frontend, `PropertyDetails.jsx` (54KB) renders a full-page detail view: an image gallery, amenity grid with icon mapping, a live pricing breakdown fetched from `/api/properties/{id}/pricing`, a **Leaflet** interactive map with **Nominatim** geocoding, a "Meet your host" section, guest reviews, similar properties, and a date-availability checker. All data is fetched using **TanStack React Query** for automatic caching and background refetching.

---

## Module 3 — Search & Filtering

> *Files: `PropertyController.java`, `PropertyRepository.java` (JPQL query), `Search.jsx`, `SearchBar.jsx`*

---

The Search and Filtering module allows guests to discover properties through a combination of server-side database queries and client-side filter refinement. It is designed to handle multi-parameter searches efficiently using a single custom **JPQL query** in the `PropertyRepository`.

The backend exposes `GET /api/properties` which accepts query parameters: `city`, `type` (PropertyType enum), `minPrice`, `maxPrice`, `guests`, `checkIn`, `checkOut`, `sortBy`, `sortDir`, `page`, and `size`. The `PropertyRepository.searchAvailable()` method executes a dynamic JPQL query that uses `IS NULL` guards for optional parameters — so passing no city returns all properties, while passing a city applies a `LOWER(p.city) LIKE :city` filter.

Date-based availability filtering joins against the `Availability` table to exclude properties with blocked dates in the requested range. Results are paginated using Spring Data's `Pageable` abstraction and wrapped in a `PageResponse<PropertyResponse>` DTO — which includes `totalElements`, `totalPages`, `page`, and `size` metadata for frontend pagination controls.

On the frontend, `SearchBar.jsx` provides two variants: a **hero variant** on the home page with a large destination input, a `react-day-picker` calendar for check-in/out date selection, and a guest counter stepper; and a **compact variant** embedded at the top of `Search.jsx`. Once results arrive, `Search.jsx` applies additional **client-side filters** — property type chips, a price range slider, amenity checkboxes, minimum rating selector, and category quick-filters (Beach 🏖, Mountain 🏔, City 🌆, Countryside 🌳). All search state is managed in the URL query string via wouter's `useLocation` hook, making searches bookmarkable and shareable.

---

## Module 4 — Booking Management

> *Files: `BookingController.java`, `BookingService.java`, `Booking.java`, `Checkout.jsx`, `Confirmation.jsx`, `MyBookings.jsx`*

---

The Booking Management module is the most complex and critical feature of Homistay. It orchestrates the complete reservation lifecycle — creation, confirmation, cancellation, and retrieval — while enforcing business rules and data integrity at every step.

The `createBooking()` method in `BookingService` is wrapped in a `@Transactional` block and begins with rigorous **business rule validation**: check-out must be after check-in, check-in cannot be in the past, the guest count must not exceed `property.maxGuests`, and the property's pet/children/infant policies are checked against the request. It also prevents hosts from booking their own property.

The most critical safeguard is the **pessimistic locking** step — the method calls `bookingRepository.findOverlappingBookingsWithLock()`, which executes a `SELECT ... FOR UPDATE` query. This database-level lock blocks concurrent transactions from reading the same rows, completely eliminating the **double-booking race condition** that would otherwise occur when two users try to book the same property dates simultaneously.

After availability is confirmed, the total price is computed via `PricingService.calculateSubtotal()` — which applies seasonal and demand multipliers — and the cleaning fee is added on top. Any selected add-ons are fetched, validated, and persisted as `BookingAddon` entities. The booked dates are then written to the `Availability` table as `reason=BOOKED` records. Finally, a simulated payment is created with status `PAID` and a `TXN-XXXXXXXX` transaction ID.

On the frontend, `Checkout.jsx` (19KB) presents a 5-step flow: property summary → add-ons selection → guest breakdown (adults/children/infants/pets) + special requests → house rules acknowledgment → payment method selection and final confirmation. `MyBookings.jsx` (30KB) displays all bookings with tab-based status filtering and a rich detail modal.

---

## Module 5 — Payment

> *Files: `Payment.java`, `PaymentRepository.java`, `BookingService.java` (payment logic), `PaymentResponse.java`*

---

The Payment module handles the financial transaction layer of the Homistay platform. While the current implementation uses a **simulated payment gateway** rather than a live one like Stripe or Razorpay, it is architecturally designed to be a drop-in replacement — the `Payment` entity, repository, and response DTO are fully separated from the booking logic.

Every confirmed booking generates exactly one `Payment` record via a **OneToOne** JPA mapping. The payment entity stores: `amount` (BigDecimal with 10,2 precision), `status` (a `PaymentStatus` enum — `PENDING`, `PAID`, `REFUNDED`, `PARTIALLY_REFUNDED`), `paymentMethod` (the string value selected during checkout — e.g., Credit Card, UPI, Net Banking), `transactionId` (a UUID-prefixed string like `TXN-A3F92B1C`), and `paidAt` (the timestamp of payment capture).

On booking creation, the payment is immediately set to `PAID` status and a UUID-based transaction ID is generated, simulating an instant payment capture. This design mirrors how real payment gateways work — the booking service calls the payment service after booking is confirmed and receives a transaction reference back.

Cancellations update the payment status according to the refund policy: host-initiated cancellations produce a `REFUNDED` status with a 100% refund amount; guest cancellations before check-in produce `REFUNDED` with a 90% refund; and guest cancellations mid-stay produce `PARTIALLY_REFUNDED` with a 70% refund on remaining unused nights, calculated dynamically using `ChronoUnit.DAYS.between()`.

The `PaymentResponse` DTO is nested inside `BookingResponse`, so the client always receives payment details alongside booking details in a single API call — avoiding N+1 query patterns.

---

## Module 6 — Reviews

> *Files: `ReviewController.java`, `ReviewService.java`, `Review.java`, `ReviewRepository.java`*

---

The Reviews module enables guests to provide post-stay feedback on properties, forming the reputation and trust layer of the Homistay marketplace. The system enforces a **one review per booking** constraint both at the database level (a `UNIQUE` constraint on `booking_id` in the `reviews` table) and in the service layer, ensuring review integrity.

The `Review` entity has a **OneToOne** relationship with `Booking` and **ManyToOne** relationships with both `Property` and the guest `User`. It stores a `rating` (integer, 1–5) and a `comment` text field, along with a `createdAt` timestamp auto-populated by Hibernate's `@CreationTimestamp`.

The `ReviewService.create()` method validates that the booking belongs to the authenticated guest and that its status is `COMPLETED` before allowing a review — preventing guests from reviewing properties they haven't actually stayed at. It also checks that no review already exists for the booking, throwing a `BusinessException` if one is found.

Property average ratings are computed on-demand using a `@Query` in `ReviewRepository` — `SELECT AVG(r.rating) FROM Review r WHERE r.property.id = :propertyId` — which runs as a single aggregation query. This computed value is embedded in every `PropertyResponse` via `PropertyService.mapToResponse()`, so every property listing and search result always shows an up-to-date average rating.

On the frontend, the review form appears inside `MyBookings.jsx` within the booking detail modal, but only if the booking status is `COMPLETED` and no review has been submitted yet. It uses a `StarRating` component for the interactive 1–5 star picker and submits via `POST /api/reviews`.

---

## Module 7 — Wishlist

> *Files: `WishlistController.java`, `WishlistService.java`, `Wishlist.java`, `WishlistRepository.java`, `Wishlist.jsx`, `AppContext.jsx`*

---

The Wishlist module allows authenticated guests to save and manage their favourite property listings — a core engagement feature modelled closely after Airbnb's heart-toggle mechanism. It is implemented as a lightweight junction entity with a database-enforced uniqueness constraint.

The `Wishlist` JPA entity contains a composite key of `(user_id, property_id)` with a `UNIQUE` constraint at the database level, preventing duplicate saves. A `createdAt` timestamp records when the property was saved. The entity has `ManyToOne` relationships to both `User` and `Property`.

The `WishlistRepository` provides a `findByUserId()` method for retrieving all wishlisted properties, an `existsByUserIdAndPropertyId()` method for duplicate checking, and a `deleteByUserIdAndPropertyId()` method for removal — all generated automatically by Spring Data's method-name query derivation.

The `WishlistService` enforces that only users with the `GUEST` role can add to wishlists — hosts and admins are excluded from this feature. It also prevents adding a property the user doesn't own or that doesn't exist, with proper `ResourceNotFoundException` handling.

On the frontend, the wishlist state is maintained in `AppContext` as an array of property ID strings (`String[]`), loaded from the backend when the user logs in. The `toggleWishlist()` function from AppContext is called by the heart icon on every `PropertyCard` and on the `PropertyDetails` page — it optimistically updates the local state first for instant UI feedback, then syncs with the backend via `POST` or `DELETE /api/wishlists/{propertyId}`. `Wishlist.jsx` renders the full saved properties page using the same `PropertyCard` component as the search results.

---

## Module 8 — Host Dashboard

> *Files: `HostController.java`, `HostDashboardService.java`, `HostDashboardResponse.java`, `host/Dashboard.jsx`*

---

The Host Dashboard module provides hosts with a real-time operational overview of their rental business — consolidating key performance indicators, recent bookings, and quick-access navigation into a single unified view.

The `HostDashboardService.getDashboard()` method aggregates data across multiple repositories in a single service call. It fetches all of the host's properties via `PropertyRepository.findByHostId()`, then for each property computes: total confirmed revenue by summing `payment.amount` across all `CONFIRMED` or `COMPLETED` bookings, active listing count (`isActive = true`), average property rating from the `ReviewRepository`, and a list of the 5 most recent bookings across all their properties ordered by `createdAt DESC`.

The response is serialized as a `HostDashboardResponse` DTO containing: `totalEarnings` (BigDecimal), `activeListings` (int), `confirmedBookings` (int), `averageRating` (Double), and `recentBookings` (List<BookingResponse>). This DTO structure ensures the frontend needs only **one API call** to render the entire dashboard — eliminating unnecessary round trips.

On the frontend, `host/Dashboard.jsx` renders four stat cards with icons: Total Earnings (💰), Active Listings (🏠), Confirmed Bookings (📅), and Average Rating (⭐). Below the stat cards is a "Recent Bookings" table showing the last 5 bookings with property name, guest name, dates, and status badge. Clicking any row navigates to the full bookings management page with the specific booking pre-selected.

The dashboard also serves as the landing page after a host logs in, making it the primary entry point for the host-side of the application.

---

## Module 9 — Host Calendar & Availability

> *Files: `HostController.java` (toggleAvailability), `PropertyService.java` (blocking logic), `Availability.java`, `AvailabilityRepository.java`, `host/Calendar.jsx`*

---

The Host Calendar and Availability module gives hosts granular control over which dates their properties are bookable. It is built on top of a dedicated `Availability` table that stores date-level availability records with a `reason` field distinguishing between two types of blocked dates: `BOOKED` (created automatically by the booking engine) and `HOST_BLOCKED` (created manually by the host).

The `PropertyService.toggleAvailability()` method accepts a `AvailabilityBlockRequest` with `startDate`, `endDate`, and a `block` boolean flag. When blocking dates, it first calls `bookingRepository.hasOverlappingBookings()` to verify there are no existing confirmed bookings in the range — preventing hosts from accidentally blocking dates that are already reserved. It then iterates day-by-day through the range using a `LocalDate` loop, upserting `Availability` records with `isAvailable=false` and `reason=HOST_BLOCKED`. Unblocking deletes only `HOST_BLOCKED` records, leaving `BOOKED` records untouched.

On the frontend, `host/Calendar.jsx` (25KB) is the most visually complex host page. The left sidebar shows a scrollable property selector list. The main panel renders a **custom month calendar grid** — each cell is colour-coded: green dot for available, red for booked (from `BOOKED` availability records), and grey for host-blocked. Clicking a single cell or dragging across multiple cells opens a confirmation modal to block or unblock the selected range. The page also features an "Upcoming Bookings" list below the calendar, showing all future confirmed bookings for the selected property with check-in/out dates and guest count.

---

## Module 10 — Host Earnings

> *Files: `HostController.java` (monthlyEarnings), `HostDashboardService.java`, `MonthlyEarningsResponse.java`, `host/Earnings.jsx`*

---

The Host Earnings module delivers a financial analytics view to hosts, breaking down their revenue across the past 12 months. It combines backend aggregation logic with a **Recharts** bar chart visualization on the frontend, giving hosts a clear picture of their income trends and seasonal performance.

The `HostDashboardService.getMonthlyEarnings()` method queries all `CONFIRMED` or `COMPLETED` bookings for the host's properties created within the past 12 months. It groups them by year-month using Java's `YearMonth` API and aggregates total payment amounts per month. The resulting list of `MonthlyEarningsResponse` objects includes `month` (formatted as "Jan 2025"), `grossRevenue` (BigDecimal), `serviceFee` (10% of gross), and `netRevenue` (90% of gross). Months with no bookings are still included in the response with zero values, ensuring the chart always shows a complete 12-month picture rather than gaps.

On the frontend, `host/Earnings.jsx` begins with a row of four stat cards: **Total Gross Revenue**, **Total Service Fee**, **Total Net Revenue**, and **Average per Booking** — all computed client-side from the API response array. The main visual is a **Recharts `BarChart`** with two overlapping bar series: one for gross revenue (in a primary colour) and one for net revenue (in a secondary colour), with a tooltip showing exact values on hover. Below the chart is a detailed month-by-month breakdown table with columns for Month, Gross, Service Fee, and Net — formatted to two decimal places using `toLocaleString`.

---

## Module 11 — Dynamic Pricing

> *Files: `PricingController.java`, `PricingService.java`, `SeasonalRate.java`, `DynamicPricingConfig.java`, `host/Pricing.jsx`*

---

The Dynamic Pricing module is one of the most technically sophisticated features of Homistay. It computes a variable effective nightly rate based on two layered mechanisms — seasonal pricing and demand-based adjustment — applied independently per date across the requested stay period.

The `PricingService.calculatePrice()` method iterates day-by-day from `checkIn` to `checkOut` (exclusive) using a `LocalDate` loop. For each date, it first checks the **hardcoded default seasons**: Summer Peak (June–August, ×1.50), Winter Holidays (Dec 15–Jan 5, ×2.00), Spring Break (Mar 15–Apr 15, ×1.30), and Autumn/Fall (Sep 15–Nov 15, ×1.20). Then it checks for **host-defined `SeasonalRate` records** in the database — if a matching active season is found, it overrides the default. Adjustments can be either `PERCENTAGE` (e.g., +30% applied as `basePrice × 1.30`) or `FIXED` (e.g., +₹500 flat per night).

The third layer is the **demand-based multiplier**, calculated by `calculateDemandMultiplier()`. It counts confirmed bookings for the property in the past N months (configurable via `DynamicPricingConfig`). If the count exceeds the `demandThreshold`, the multiplier scales linearly between `minPriceMultiplier` and `maxPriceMultiplier` using a `ratio = min(1.0, bookingCount / (threshold × 2))` formula. The final nightly price is: `basePrice × seasonalMultiplier × demandMultiplier + fixedAmount`.

The complete `PricingBreakdownResponse` includes per-night detail for every night in the stay, making the pricing fully transparent to guests. Hosts configure their dynamic pricing rules via `host/Pricing.jsx` (29KB) — a rich page with seasonal rate CRUD forms and a toggle-enabled dynamic pricing configurator with a live price preview.

---

## Module 12 — Property Add-ons

> *Files: `HostController.java` (addon CRUD), `PropertyService.java`, `PropertyAddon.java`, `BookingAddon.java`, `BookingAddonRepository.java`*

---

The Property Add-ons module enables hosts to define optional extra services that guests can purchase alongside their accommodation booking. This feature is inspired by the ancillary revenue model used by modern booking platforms — where hosts can upsell services like breakfast, airport pickup, city tours, or equipment rentals.

A `PropertyAddon` entity has a **ManyToOne** relationship to `Property` and stores: `name`, `description`, `price` (BigDecimal), and an `isActive` boolean flag. Hosts manage their add-ons through a dedicated CRUD API under `/api/host/properties/{propertyId}/addons`. Ownership is strictly enforced — the `getPropertyAndVerifyOwner()` helper in `PropertyService` is called before every mutation, throwing `UnauthorizedException` if the requesting host doesn't own the property.

When a guest selects add-ons during checkout, the `BookingRequest` DTO carries a list of `{ addonId, quantity }` pairs. In `BookingService.createBooking()`, each selected addon is fetched from `PropertyAddonRepository`, validated for existence and `isActive=true` status, and persisted as a `BookingAddon` record with the calculated `price = addon.price × quantity`. The total addon cost is added to the booking's `totalPrice` after the initial subtotal is computed.

On the frontend, add-ons appear in `Checkout.jsx` as Step 2 of the booking flow — a card grid where each add-on shows its name, description, price per unit, and a quantity stepper. The running total updates in real-time as guests adjust quantities. The selected add-ons are also summarized in the `MyBookings.jsx` booking detail modal and in the host's booking management view.

---

## Module 13 — Booking Modifications

> *Files: `ModificationController.java`, `HostController.java` (respond endpoint), `BookingService.java` (requestModification, respondToModification), `BookingModification.java`*

---

The Booking Modifications module provides a structured workflow for guests to request date or guest-count changes on confirmed bookings, and for hosts to review and respond to those requests. This two-sided approval process mirrors how enterprise change-request workflows function, applied to the homestay context.

A guest can submit a modification request via `POST /api/bookings/{id}/modification`, providing a `ModificationRequest` DTO containing `newCheckIn`, `newCheckOut`, `newGuests`, and an optional `reason`. The `BookingService.requestModification()` method validates that the booking belongs to the requesting guest and that its status is `CONFIRMED` — modifications on cancelled or completed bookings are rejected with a `BusinessException`. The request is then persisted as a `BookingModification` entity with `status=PENDING`.

The host sees pending modifications in their `host/Bookings.jsx` modal, where each modification shows the requested date changes, guest count change, and the guest's reason. The host can respond via `PATCH /api/host/bookings/{bookingId}/modifications/{modId}` with `{ status: "APPROVED" | "DENIED", hostResponse: "..." }`.

If **approved**, `BookingService.respondToModification()` applies the changes atomically within a `@Transactional` block: it updates `booking.checkIn`, `booking.checkOut`, and `booking.guestsCount` if provided in the modification, then recalculates and updates `booking.totalPrice` via `PricingService.calculateSubtotal()` for the new date range. If **denied**, the booking remains unchanged and only the modification record is updated with the denial reason.

The full modification history (approved, denied, and pending) is accessible to both the guest and host, providing a transparent audit trail.

---

## Module 14 — Guest–Host Messaging

> *Files: `BookingController.java` (sendMessage, getMessages), `BookingService.java`, `BookingMessage.java`, `BookingMessageRepository.java`*

---

The Guest–Host Messaging module provides a communication channel between guests and hosts that is scoped to a specific booking. This in-booking messaging pattern is a common pattern in rental platforms — it keeps all communication contextual, auditable, and tied to a single reservation rather than being a free-form inbox.

The `BookingMessage` entity stores: `booking` (ManyToOne FK to bookings), `sender` (ManyToOne FK to users), `message` (TEXT), and `createdAt` (LocalDateTime). Messages are persisted to a `booking_messages` table, with `BookingMessageRepository.findByBookingIdOrderByCreatedAtAsc()` retrieving the full conversation thread in chronological order.

Access control is enforced in `BookingService.sendMessage()` and `getMessages()` — both methods verify that the requesting user is either the booking's guest or the property's host. Any attempt by a third party to read or send messages is rejected with an `UnauthorizedException`.

The `POST /api/bookings/{id}/messages` endpoint accepts `{ message: "..." }` in the request body and returns a `MessageResponse` DTO containing `id`, `senderId`, `senderName`, `message`, and `createdAt`. The `GET /api/bookings/{id}/messages` endpoint returns the complete ordered message list.

On the frontend, the messaging thread is rendered inside the booking detail modal in `MyBookings.jsx` (for guests) and inside the booking management modal in `host/Bookings.jsx` (for hosts). Messages are displayed in a chat-bubble style UI — right-aligned for the current user, left-aligned for the other party — with sender name and timestamp. A text input at the bottom allows sending new messages, with the response appended to the thread immediately without a full page reload.

---

## Module 15 — Trip Planner

> *Files: `TripPlanner.jsx` (564 lines), `trip_planner.md` (design doc), uses `AppContext.properties`*

---

The Trip Planner module is a unique, frontend-only intelligent planning tool that helps guests discover the most suitable Homistay property for their travel itinerary. It is accessible via the Navbar and opens as a full-screen modal dialog built with the shadcn/ui `Dialog` primitive.

The planner operates in **three steps**. Step 1 presents a searchable city selector with destination cards for **Bengaluru** 🌳, **Goa** 🏖️, and **Mumbai** 🌆, each pre-loaded with 5 curated attractions including their GPS coordinates. Step 2 shows a checkbox list of the selected city's attractions — guests select which places they plan to visit, along with a nights slider (1–30). Step 3 generates three optimized property plans.

The core of the Trip Planner is the **Haversine distance formula** — a spherical geometry algorithm that computes the great-circle distance (in kilometers) between two GPS coordinate pairs, accounting for Earth's curvature. For each available property in the selected city, the algorithm calculates the Haversine distance to every selected attraction.

Three plans are derived from this scoring: the **Budget Plan** sorts properties by total cost (`nights × pricePerNight + cleaningFee`) and picks the cheapest. The **Nearest Plan** picks the property with the smallest average Haversine distance across all selected attractions. The **Best Overall Plan** uses a **min-max normalized composite score** — 40% weight on price and 60% weight on distance — to find the optimal balance. Each plan card displays the property image, rating, individual distances to each attraction, and a direct "Book this property" CTA.

---

## Module 16 — AI Chatbot / FAQ Engine

> *Files: `ChatbotController.java`, `ChatQueryService.java`, `FaqService.java`, `ChatPopup.jsx`*

---

The AI Chatbot module provides guests with an instant, self-service support interface to answer common questions about the platform — without requiring human intervention or integration with a paid LLM API. The engine is built using a **compiled regular expression pattern matcher** implemented entirely in Java.

The `FaqService` class is a Spring `@Service` that maintains an ordered `LinkedHashMap<Pattern, String>` — a mapping from compiled `java.util.regex.Pattern` objects to their corresponding answer strings. The map is populated in the constructor with **25+ question patterns** covering categories such as: booking a property, cancellations, modifications, login and registration, password reset, profile updates, search and filtering, wishlist management, payment methods, refunds, becoming a host, writing reviews, contacting support, platform information, and fee structures.

Each pattern is compiled with `Pattern.compile("(?i).*\\b(keyword1|keyword2)\\b.*")` — using **case-insensitive matching** (`(?i)`) and **word boundary anchors** (`\b`) to improve match precision and reduce false positives. The `findAnswer()` method streams the entry set and returns the answer from the first matching pattern using `Optional<String>`.

When a question is submitted, the `ChatQueryService` calls `FaqService.findAnswer()` and, regardless of whether a match is found, persists the query and answer to the `chat_queries` table for future analytics. Unmatched queries return a graceful fallback: *"I'm not sure how to answer that. Please contact support@homistay.com."*

On the frontend, `ChatPopup.jsx` renders a floating circular button (bottom-right corner) that expands into a chat dialog. Messages are displayed in a scrollable thread with the chatbot's responses formatted as markdown-like multi-line text.

---

## Module 17 — Support Tickets

> *Files: `SupportDashboardController.java`, `SupportTicketController.java`, `SupportQueryController.java`, `TicketController.java`, `SupportTicketService.java`, `SupportTicket.java`, `SupportDashboard.jsx`*

---

The Support Tickets module provides a two-sided issue tracking system — guests can submit support tickets for unresolved problems, and the support team can view, manage, and resolve them through a dedicated dashboard. This module is access-controlled at the endpoint level using Spring Security's role-based authorization.

The `SupportTicket` entity stores: `subject`, `description`, `status` (open, in-progress, resolved, closed), `resolution` text, a `ManyToOne` reference to the reporting `User`, and a `createdAt` timestamp. The `SupportTicketService` handles the full lifecycle: ticket creation by an authenticated user, retrieval by ticket ID or by user, status updates by the support team, and resolution with a note.

The support-facing endpoints under `/api/support/**` are guarded with `.hasAnyRole("SUPPORT_TEAM", "ADMIN")` in `SecurityConfig` — only users with these roles can access the support dashboard or modify ticket statuses. User-facing ticket endpoints under `/api/tickets` are available to any authenticated user.

The `SupportDashboard.jsx` frontend page (accessible at `/support/dashboard`) provides the support team with a quick-glance overview: total tickets, open count, in-progress count, and resolved count presented as stat cards. A sortable, paginated table lists all tickets with columns for user email, subject, status badge (colour-coded), and a resolve button that opens a modal for entering a resolution note.

The module also integrates with the **FAQ Chatbot** — before a user submits a ticket, they are encouraged to try the chatbot widget. This reduces support volume by routing common questions to automated answers, reserving human support effort for genuine unresolved issues.

---

*End of Module Scripts — Homistay v1.0.0 · 17 Modules · June 2026*
