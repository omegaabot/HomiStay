# Homistay — Full Project Presentation Script

## Speaker 1: Introduction and Project Overview

Good morning/afternoon everyone. Today we are presenting our project **Homistay**, which is a full-stack homestay booking platform inspired by Airbnb. The main idea behind this project is to create a complete travel and stay experience where guests can search and book properties, and hosts can manage their listings, bookings, availability, and pricing from a single platform. It is built using **React 19** on the frontend, **Spring Boot 3.2** on the backend, and **PostgreSQL** as the database. 

This project is designed to behave like a real product rather than just a basic CRUD application. It includes JWT-based authentication, pessimistic locking to avoid double booking, dynamic pricing, trip planning, chatbot support, wishlist management, reviews, and a host dashboard. The platform supports four roles: **Guest, Host, Admin, and Support Team**. Guests can browse, book, review, and plan trips, while hosts can list properties, manage bookings, block dates, track earnings, and set pricing. 

To make the project demo-ready, we also seeded the database with **23 properties, 64 bookings, 57 reviews, and 13 add-ons**. So the application is not empty; it already contains real data that can be shown immediately during a presentation or live demo. 

**Transition:**
Now my teammate will explain the frontend structure and how the user interacts with the system.

---

## Speaker 2: Frontend Module

The frontend is built in **React** and is structured around multiple pages and reusable components. It includes pages such as Home, Search, Property Details, Checkout, Confirmation, Profile, My Bookings, Wishlist, Host Dashboard, Calendar, Earnings, Pricing, and Support Dashboard. The routing is handled with **wouter**, and global state is managed through **AppContext**, which stores the authenticated user, properties, bookings, wishlist, and the current booking in progress.  

The frontend focuses heavily on user experience. For example, the **SearchBar** allows users to search by destination, dates, and guest count. The **PropertyCard** gives a quick listing preview. The **PropertyDetails** page shows images, maps, pricing breakdown, host details, and reviews. The **Checkout** flow is multi-step, so the user reviews the stay, chooses add-ons, enters guest details, confirms rules, and then completes the booking.  

A major part of the frontend is the **TripPlanner** component, which is a 3-step modal wizard. It is one of the most important custom components in the whole project because it gives intelligent property recommendations based on trip needs. I will explain that in detail later. 

**Transition:**
Next, the security and authentication module will be explained, because every action in the system depends on role and access control.

---

## Speaker 3: Authentication and Security Module

Security is one of the strongest parts of the system. The application uses **JWT authentication**, where login returns an access token and a refresh token. The frontend stores the tokens and sends the access token in the `Authorization: Bearer <token>` header on every request. The backend validates this token using a JWT filter before any protected controller is reached. The application is stateless, meaning it does not rely on server sessions.  

Passwords are hashed using **BCrypt**, and role-based authorization is enforced using Spring Security. That means a guest cannot access host-only endpoints, a support user cannot perform host-only operations, and only the right role can reach the right dashboard or action. The project also uses ownership checks, so even if a user is a host, they can only edit or manage their own properties and bookings. 

This module is important because it protects user data and makes the platform behave like a real booking application rather than a mock demo. It also ensures that login, profile update, booking, support, and host actions are all properly separated by permission. 

**Transition:**
Now the property management module will be explained, which is where hosts create and maintain listings.

---

## Speaker 4: Property Management Module

The property module handles everything related to listings. On the backend, the **PropertyController** supports public viewing and authenticated write operations. Guests can browse and inspect properties, while hosts can create, update, or delete their own listings. The property details also include images, amenities, house rules, guest requirements, check-in instructions, and blocked dates.  

For hosts, the most important part is the **5-step Add Property wizard**. It guides them through selecting property type, entering location, filling basic details, adding amenities and images, and finally setting pricing and house rules. This makes the property entry process organized and much easier to use than a single long form. The wizard is also integrated with auto-geocoding in the UI, which helps convert location data into latitude and longitude. 

This module is also responsible for showing blocked dates and availability states. That is useful because hosts can clearly manage when a property is available, host-blocked, or already booked. This module forms the backbone of the listing side of the platform. 

**Transition:**
Next, the booking module will be explained, because once a user selects a property, the booking lifecycle starts here.

---

## Speaker 5: Booking Module

The booking module is one of the most important modules in the project. Guests can create bookings, view their bookings, cancel bookings, send and receive messages, request modifications, and leave reviews after a completed stay. The backend exposes endpoints for booking creation, booking details, cancellation, messages, and modification history. 

A very important feature here is **pessimistic locking** during booking creation. This means when two users try to book the same property for overlapping dates at the same time, the system locks the relevant records and prevents double booking. That makes the booking logic more reliable and production-like. 

The booking module also handles cancellation and refund logic. According to the project rules, if the host cancels, the guest gets a 100% refund. If the guest cancels before check-in, they receive a 90% refund, and if they cancel during the stay, unused nights are refunded according to the policy. This makes the platform more realistic and adds business logic to the booking lifecycle. 

**Transition:**
Now my teammate will explain the host management and pricing side, which is where the property business is controlled.

---

## Speaker 6: Host Dashboard, Calendar, and Pricing Module

The host module gives hosts a full control panel. It includes the host dashboard, earnings page, bookings page, calendar page, pricing page, and property management tools. Hosts can view total earnings, monthly earnings charts, booking counts, and average ratings. They can also filter bookings by property or status and respond to modifications or special requests. 

The **calendar module** is especially useful because it allows hosts to visually block or unblock dates. Booked dates are shown differently from host-blocked dates, which makes availability management very clear. Hosts can also use the calendar to monitor upcoming bookings. 

The **pricing module** is another strong feature. It supports seasonal rates and demand-based pricing. That means the system can adjust price based on date ranges, seasons, and recent booking activity. This makes pricing flexible and more realistic than a fixed-rate system. The backend exposes endpoints to create, update, delete, and preview pricing rules.  

**Transition:**
Now the next speaker will explain the most unique feature in this project — the Trip Planner.

---

## Speaker 7: Trip Planner Module

The **Trip Planner** is one of the most unique and impressive features of the project. It is a guided planner popup inside the navigation bar. If the user is not logged in, it first asks them to sign in. Once logged in, they can open the planner and start planning a trip. The planner works entirely on the frontend by reading active properties from global app state and matching them with tourist attractions using mathematical formulas. 

The flow has three steps. First, the user selects a city like Bengaluru, Goa, or Mumbai. Second, they select the places they want to visit. Third, the system generates three recommendations: **Budget Plan**, **Nearest Plan**, and **Best Overall Plan**. Each card also shows the total price for the selected number of nights and the distance from the property to each chosen attraction. 

The distance is calculated using the **Haversine formula**, which gives the shortest distance between two points on the surface of the Earth. This is important because Earth is spherical, so a flat 2D distance formula would not be accurate. In simple terms, the formula converts latitude and longitude to radians, uses trigonometric functions, and then multiplies the result by Earth’s radius to get the real distance in kilometers. 

For recommendation logic, the **Budget Plan** chooses the cheapest option using total cost, the **Nearest Plan** chooses the property with the smallest average distance to the selected attractions, and the **Best Overall Plan** uses a weighted score where price contributes 40% and distance contributes 60%. Since price and distance are different units, the system first normalizes them to a 0–1 scale before scoring. That makes the comparison fair and mathematically meaningful. 

For example, in Mumbai, one property may be cheaper but farther away, while another may be more expensive but much closer to selected attractions. In that case, the best overall property is the one with the lower weighted score, because it balances both cost and convenience. 

**Transition:**
Now the final speaker will explain chatbot support, database design, and the conclusion of the project.

---

## Speaker 8: Chatbot, Support Module, Database, and Conclusion

The platform also includes a **chatbot module** that answers common FAQ-type questions. It uses compiled regex patterns rather than an external AI model, so it can respond quickly to common queries like booking, cancellation, password help, login, wishlist, and support questions. If the system cannot match a query, it returns a fallback message and points the user toward support.  

There is also a **support dashboard** and ticket system for support-team users and admins. This allows the platform to handle user issues in a more organized way. Combined with the chatbot, this makes the support flow more complete and practical. 

From a database perspective, the project has **18 tables** covering users, properties, property images, bookings, payments, reviews, availability, wishlists, add-ons, booking messages, booking modifications, experiences, seasonal rates, dynamic pricing configs, chatbot queries, and support tickets. That shows the data model is designed to support the complete business flow of the application. 

To conclude, Homistay is not just a booking application. It combines property search, booking lifecycle, host operations, dynamic pricing, intelligent trip planning, chatbot support, and secure authentication in one system. It was built to simulate a real-world product and to demonstrate both frontend and backend engineering in a clean, modular structure. 

# Module-Wise Team Split for 8 People

**1. Person 1 — Introduction and overall flow**
Explain what Homistay is, the problem it solves, the user roles, and the overall architecture. Keep this part short and confident so the audience understands the big picture first. 

**2. Person 2 — Frontend and user experience**
Explain the React pages, routing, AppContext, search flow, property details page, and checkout UI. Focus on how the user moves through the app. 

**3. Person 3 — Authentication and security**
Explain login, JWT tokens, role-based access, BCrypt password hashing, and ownership checks. Mention that the system is stateless and secure by design.  

**4. Person 4 — Property module**
Explain how hosts add and manage properties, how the 5-step property wizard works, and how property details, images, amenities, and availability are stored.  

**5. Person 5 — Booking module**
Explain booking creation, booking lifecycle, messaging, modification requests, cancellation, and refund policy. Also mention pessimistic locking to prevent double booking.   

**6. Person 6 — Host dashboard, calendar, and pricing**
Explain the host dashboard, earnings charts, availability calendar, seasonal rates, and dynamic pricing config. This person should present the business-management side of the platform.  

**7. Person 7 — Trip Planner**
Explain the 3-step flow, Haversine formula, normalization, weighted scoring, and the Mumbai example. This should be the most polished technical section because it is the strongest unique feature.   

**8. Person 8 — Chatbot, support, database, and conclusion**
Explain the chatbot, support dashboard, database tables, seed data, and close the presentation with a strong summary.   

# Smooth speaking order for the team

A good flow would be:

**Intro → Frontend → Auth/Security → Property Management → Booking → Host & Pricing → Trip Planner → Support/Database/Conclusion**

=========================================================================================================

---

# 🎤 **Homistay — Final Team Presentation Script (8 Speakers)**

---

# 👤 **Speaker 1 — Introduction (Start Strong)**

### 🗣️ Say:

Good morning everyone,

Today we are presenting our project **Homistay**, which is a full-stack homestay booking platform inspired by Airbnb.

The goal of this project is to build a complete system where:

* Guests can search and book properties
* Hosts can manage listings and pricing
* And the platform handles the full booking lifecycle

We built this using:

* React for frontend
* Spring Boot for backend
* PostgreSQL for database

This is not just a basic CRUD project — it includes features like:

* Secure authentication
* Booking system
* Host dashboard
* Dynamic pricing
* And an intelligent Trip Planner

---

### 🔄 Transition:

Now I’ll hand it over to my teammate to explain the frontend and user experience.

---

# 👤 **Speaker 2 — Frontend & User Flow**

### 🗣️ Say:

On the frontend side, we focused on creating a smooth and intuitive user experience.

The application includes multiple pages like:

* Home page
* Search results
* Property details
* Checkout
* Bookings and profile

Users can search properties based on location, dates, and number of guests.

Each property page shows:

* Images
* Amenities
* Map location
* Pricing breakdown

The booking process is designed as a **multi-step flow**, making it easy for users to complete bookings step by step.

We also used global state management to store:

* User data
* Properties
* Bookings
* Wishlist

---

### 🔄 Transition:

Now let’s move to security, which ensures all these features are safe and role-based.

---

# 👤 **Speaker 3 — Authentication & Security**

### 🗣️ Say:

Security is handled using **JWT authentication**.

When a user logs in:

* They receive an access token
* This token is sent with every request

The backend validates the token before allowing access.

We also implemented:

* BCrypt password hashing
* Role-based access control

This ensures:

* Guests access guest features
* Hosts access host dashboard
* Support users access support panel

We also added ownership checks, so users can only manage their own data.

---

### 🔄 Transition:

Now I’ll pass it to explain how properties are managed in the system.

---

# 👤 **Speaker 4 — Property Module**

### 🗣️ Say:

The property module handles everything related to listings.

Hosts can:

* Add new properties
* Update details
* Delete properties

We created a **5-step property creation wizard**, which includes:

1. Property type
2. Location
3. Basic details
4. Amenities and images
5. Pricing and rules

Each property stores:

* Images
* Amenities
* House rules
* Location coordinates

We also support availability tracking, where hosts can block specific dates.

---

### 🔄 Transition:

Now once a property is selected, the booking module comes into play.

---

# 👤 **Speaker 5 — Booking Module**

### 🗣️ Say:

The booking module handles the complete booking lifecycle.

Users can:

* Create bookings
* View bookings
* Cancel bookings
* Send messages
* Request modifications

One important feature is **pessimistic locking**, which prevents two users from booking the same property at the same time.

We also implemented a refund system:

* Full refund if host cancels
* Partial refund if guest cancels

This makes the system more realistic and reliable.

---

### 🔄 Transition:

Now let’s see how hosts manage their business on the platform.

---

# 👤 **Speaker 6 — Host Dashboard & Pricing**

### 🗣️ Say:

The host module provides a complete dashboard.

Hosts can:

* View earnings
* Manage bookings
* Track performance

The **calendar feature** allows hosts to:

* Block dates
* View booked dates
* Manage availability visually

We also implemented a **dynamic pricing engine**, where prices change based on:

* Season
* Demand

This makes pricing more flexible and realistic.

---

### 🔄 Transition:

Now I’ll hand it over to explain the most unique feature of our project — the Trip Planner.

---

# 👤 **Speaker 7 — Trip Planner (IMPORTANT PART)**

### 🗣️ Say:

The Trip Planner is the most unique feature of our project.

It helps users plan trips intelligently instead of just booking properties.

---

### 🧭 Flow:

It works in 3 steps:

1. Select a city
2. Select places to visit
3. Get 3 recommendations

---

### 📍 Distance Calculation:

To calculate distances, we use the **Haversine formula**.

This formula calculates the shortest distance between two points on Earth’s surface.

Why this matters:

* Earth is spherical
* Simple formulas are inaccurate
* Haversine gives real-world distance

---

### ⚙️ Plans:

The system generates:

* Budget Plan → cheapest
* Nearest Plan → closest
* Best Overall → balanced

---

### 🎯 Example:

For example, in Mumbai:

One property may be cheaper but farther,
while another is more expensive but closer.

We use a weighted formula:

* 40% price
* 60% distance

So the system recommends the best balance.

---

### 💡 Impact:

This feature:

* Reduces decision effort
* Improves user experience
* Adds intelligence to the platform

---

### 🔄 Transition:

Finally, we’ll look at support features and conclude the project.

---

# 👤 **Speaker 8 — Chatbot, Database & Conclusion**

### 🗣️ Say:

We also implemented a chatbot module that answers common user questions using pattern matching.

If a query is not recognized, the system redirects the user to support.

We also built a support ticket system for handling user issues.

---

### 🗄️ Database:

The system includes **18 tables**, covering:

* Users
* Properties
* Bookings
* Payments
* Reviews
* Support tickets

This ensures the system supports a full real-world workflow.

---

### 🎯 Conclusion:

To conclude,

Homistay is a complete full-stack platform that combines:

* Booking system
* Host management
* Secure authentication
* Intelligent trip planning

It demonstrates both:

* Strong technical implementation
* Real-world product thinking

---

### 🗣️ Final Line:

Thank you — we are happy to answer any questions.

---


=========================================================================================================


# 🧠 **1. Why did you build this project?**

### ✅ Answer:

> I wanted to go beyond a basic booking system and build something closer to a real-world product.
> Most platforms only list properties, but I wanted to improve decision-making, so I added features like trip planning, dynamic pricing, and full booking lifecycle management.

---

# ⚙️ **2. What makes your project different?**

### ✅ Answer:

> The key differentiator is the **Trip Planner**, which uses real data and algorithms to recommend properties based on cost and distance.
> Also, features like **dynamic pricing** and **pessimistic locking** make it more realistic compared to typical student projects.

---

# 🏗️ **3. Why did you choose this tech stack?**

### ✅ Answer:

> I chose React for a fast and interactive UI, Spring Boot for structured backend development, and PostgreSQL for relational data handling.
> This stack is widely used in industry and supports scalability and clean architecture.

---

# 🔐 **4. How did you handle authentication and security?**

### ✅ Answer:

> I implemented JWT-based authentication with access and refresh tokens.
> All requests are validated using a JWT filter, and role-based access control ensures only authorized users can access specific features.
> Passwords are securely stored using BCrypt hashing.

---

# 📊 **5. How does your Trip Planner actually work?**

### ✅ Answer (VERY IMPORTANT):

> The Trip Planner takes user input like city and places to visit, then evaluates all properties in that city.
> It calculates:
>
> * Total cost
> * Distance to each attraction

> Distance is calculated using the **Haversine formula**, which gives real-world distance on Earth’s surface.

> Then it generates:
>
> * Budget plan → cheapest
> * Nearest plan → closest
> * Best overall → weighted score (40% price, 60% distance)

> This ensures users get practical and optimized recommendations.

---

# 📐 **6. Why did you use Haversine formula?**

### ✅ Answer:

> Because Earth is spherical, simple distance formulas are inaccurate.
> Haversine calculates the shortest path between two coordinates on Earth, giving realistic distance values.

---

# ⚖️ **7. How do you combine price and distance?**

### ✅ Answer:

> Since price and distance are different units, I normalize both values to a 0–1 scale.
> Then I apply a weighted formula:
>
> * 40% price
> * 60% distance

> This ensures a fair comparison and better recommendation.

---

# 🔒 **8. How do you prevent double booking?**

### ✅ Answer:

> I used **pessimistic locking** in the booking service.
> When a booking request is made, the system locks the relevant records and checks for overlapping bookings before confirming.
> This ensures no two users can book the same property at the same time.

---

# 💰 **9. How does your pricing engine work?**

### ✅ Answer:

> The pricing engine combines:
>
> * Seasonal pricing (like holidays or peak seasons)
> * Demand-based pricing (based on booking trends)

> This allows dynamic adjustment instead of fixed pricing.

---

# 🧪 **10. What edge cases did you handle?**

### ✅ Answer:

> Some key edge cases:
>
> * No properties in selected city
> * Only one property available
> * Missing coordinates
> * Invalid date ranges
> * Concurrent booking attempts

> These ensure the system is stable and reliable.

---

# ⚡ **11. Why is Trip Planner on frontend and not backend?**

### ✅ Answer (IMPRESSIVE):

> Since all required data is already available on the frontend, I implemented it there to reduce API calls and improve performance.
> This makes the recommendations instant and improves user experience.

---

# 📈 **12. How can this project be improved?**

### ✅ Answer:

> Future improvements include:
>
> * Integrating real map APIs like Google Maps
> * Adding AI-based recommendations
> * Implementing real payment gateway
> * Enhancing security using HttpOnly cookies

---

# 🧱 **13. What challenges did you face?**

### ✅ Answer:

> Some challenges were:
>
> * Implementing accurate distance calculation
> * Designing the pricing engine
> * Handling concurrent bookings
> * Structuring the backend for scalability

> These helped me understand real-world system design.

---

# 🧩 **14. How scalable is your system?**

### ✅ Answer:

> The backend is modular with separate services, controllers, and repositories.
> It can be scaled horizontally, and components like pricing, booking, and authentication are loosely coupled.

---

# 🎯 **15. What did YOU specifically work on?**

⚠️ (They will definitely ask this)

### ✅ Answer (customize this):

> I mainly worked on:
>
> * Trip Planner logic and implementation
> * Frontend UI improvements
> * Booking flow and validations
> * Integration between frontend and backend

---

# 🚨 **Bonus Trick Question**

## ❓ “If 1 million users use this, what will break?”

### ✅ Answer:

> Potential bottlenecks:
>
> * Database queries (needs indexing & optimization)
> * No caching layer currently
> * JWT stored in localStorage (security concern)

> Improvements:
>
> * Add Redis caching
> * Use load balancing
> * Move to microservices if needed

---

# 🎯 Final Tip

If you don’t know an answer, say:

> “That’s a good point, I haven’t implemented that yet, but one possible solution would be…”

👉 This shows **thinking ability**, not weakness.

---





=========================================================================================================

---

# 🔍 **Are there alternatives to the Haversine formula?**

### ✅ Yes — there are a few alternatives:

---

## **1. Euclidean Distance (Simple Distance)**

### 📌 Formula:

```
distance = √(x² + y²)
```

### ❌ Why NOT used:

* Assumes Earth is **flat**
* Works only for **very small distances**
* Becomes **inaccurate over cities / large areas**

---

## **2. Manhattan Distance**

### 📌 Formula:

```
distance = |x1 - x2| + |y1 - y2|
```

### ❌ Why NOT used:

* Used in grid systems (like city blocks)
* Not suitable for **geographical coordinates**

---

## **3. Vincenty Formula (More Accurate)**

### ✅ Pros:

* More accurate than Haversine
* Considers Earth as an **ellipsoid** (not perfect sphere)

### ❌ Why NOT used:

* More complex to implement
* Slower (more computations)
* Overkill for this use case

---

## **4. Google Maps API / Real Travel Distance**

### ✅ Pros:

* Gives **actual road distance**
* Considers traffic, routes, etc.

### ❌ Why NOT used:

* Requires API key (paid)
* Needs backend integration
* Slower (network calls)

---

# 🎯 **Why We Chose Haversine Formula**

### ✅ Best Answer (Say This in Interview)

> We chose the Haversine formula because it provides a good balance between **accuracy and performance**.

---

### 💡 Detailed Explanation:

1. ✅ **Accurate enough**

   * Accounts for Earth’s curvature
   * Much better than simple formulas

2. ⚡ **Fast computation**

   * Uses basic trigonometry
   * Runs instantly on frontend

3. 🧩 **No external dependency**

   * No API required
   * Works offline

4. 🎯 **Perfect for our use case**

   * We only need approximate distance
   * Not exact driving routes

---

# 🧠 **Smart Comparison (Say This to Impress)**

> Euclidean distance is too simple and inaccurate,
> Vincenty is more accurate but computationally expensive,
> and APIs like Google Maps are powerful but require external integration.
>
> So Haversine was the best middle-ground — **accurate, fast, and simple to implement**.

---

# 🚀 **Bonus (If Manager Asks “What Would You Do in Production?”)**

### ✅ Answer:

> In a production system, I would integrate something like **Google Maps API** to calculate real travel distance and time, including traffic conditions.
> But for this project, Haversine was the best choice for performance and simplicity.

---

# ✅ Final One-Line Answer

If you need a quick version:

> “Haversine gives accurate real-world distance on Earth with good performance, unlike simple formulas, and without the complexity of advanced models or external APIs.”

---
