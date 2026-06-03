# Trip Planner — Walkthrough

## Summary
Added a **Trip Planner** popup feature to the Homistay app. Users can plan trips by selecting a city, choosing famous places to visit, and receiving 3 smart property recommendations based on price, proximity, and overall value.

## Files Changed

### [NEW] [TripPlanner.jsx](file:///c:/Users/aditya1/Desktop/2_DO/project/par_project/homistay2/homistay/frontend/src/components/TripPlanner.jsx)
New 320-line React component with a 3-step dialog flow:

| Step | Description |
|------|-------------|
| **1. Choose City** | 3 gradient city cards (Bengaluru 🌳, Goa 🏖️, Mumbai 🌆) with search filter |
| **2. Select Places** | Checkbox list of 5 famous places per city with descriptions |
| **3. Your Plans** | 3 plan cards: Budget 💰, Nearest 📍, Best Overall ⭐ |

**Key features:**
- **Haversine formula** for accurate distance calculations between property and attraction coordinates
- **3 plan strategies**: cheapest property, closest average distance, weighted score (40% price + 60% distance)
- **Mathematical Plan Scoring**: Recommends properties using mathematically correct objective criteria: Budget (absolute cheapest), Nearest (absolute closest average distance), and Best Overall (absolute best combined score using 40% price + 60% distance weights), ensuring optimal recommendations.
- Each plan card shows property image, rating, price breakdown, distance to every selected place, and a "Book this property" CTA
- Adjustable night count (1–30) with live cost recalculation
- Handles edge cases: no properties in city, single property (all plans same), no coordinates

---

### [MODIFY] [Navbar.jsx](file:///c:/Users/aditya1/Desktop/2_DO/project/par_project/homistay2/homistay/frontend/src/components/Navbar.jsx)
- Added **"Trip Planner"** button with `Map` icon (desktop nav bar)
- **Auth-gated**: if not signed in → opens sign-in dialog; if signed in → opens trip planner
- Added **dropdown menu item** for mobile/logged-in users
- Renders `<TripPlanner>` dialog at the bottom of the header

## Properties Used Per City

| City | Properties | Price Range |
|------|-----------|-------------|
| Bengaluru | Modern Apartment (#2), Executive Suite Loft (#7) | $80–$90/night |
| Goa | Luxury Beach Villa (#1), Portuguese Heritage House (#4), Modern Condo (#20), Secluded Jungle Cabin (#21) | $110–$250/night |
| Mumbai | Modern High-Rise (#5), Chic Apartment (#22), Luxury Villa (#23) | $95–$290/night |

## Verification
- ✅ `npm run build` passes — no errors
- The chunk size warning is pre-existing and unrelated to this change
