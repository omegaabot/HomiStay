# Homistay Trip Planner Feature Documentation

The **Trip Planner** is a guided travel planner popup integrated directly into the Homistay navigation. It allows users and hosts to select a city, check off the places they want to visit, and receive 3 smart, tailored accommodation recommendations based on cost, distance, and overall value.

---

## 1. Feature Overview & How It Works

* **Accessing the Planner**: Click the **"Trip Planner"** (Map icon) button on the desktop navigation bar. It is also available in the user profile dropdown when logged in.
* **Authentication Gate**: 
  - If a guest or host is **not logged in**, clicking the button triggers a sign-in popup.
  - Once signed in, they can access the planner.
* **Under the Hood**: The planner runs entirely on the frontend. It reads active properties from the global application state and matches them against tourist attraction coordinates using math formulas (like Haversine distance and min-max normalization).

---

## 2. The 3-Step User Flow

### Step 1: Choose a City
Users choose from three beautiful city cards:
* **Bengaluru 🌳** (Garden City of India)
* **Goa 🏖️** (Beach Paradise of India)
* **Mumbai 🌆** (City of Dreams)

A search bar at the top lets users filter cities in real-time. Selecting a city takes them to Step 2.

### Step 2: Select Places to Visit
Users choose which local attractions they plan to visit (e.g., beaches, palaces, temples) using simple checkboxes.
* At least **1 place** must be selected to generate the plans.
* The screen displays how many properties are available in the chosen city.

### Step 3: View Travel Plans
The planner computes the results and displays 3 plan options side-by-side:
1. 💰 **Budget Plan**: Recommends the cheapest property.
2. 📍 **Nearest Plan**: Recommends the property that is closest on average to all selected places.
3. ⭐ **Best Overall**: Recommends the property that strikes the best balance between price and distance.

**Interactive Details on the Cards:**
* **Nights Counter**: Change the stay duration (from 1 to 30 nights) to see the total price update live.
* **Distance Table**: Shows exactly how many kilometers the property is from each selected tourist spot, along with the average distance.
* **Booking link**: Clicking "Book this property" takes the user directly to the booking details page.

---

## 3. Data & Coordinates

### Tourist Attractions (Static Coordinates)

| City | Attraction Name | Icon | Latitude | Longitude | Description |
|---|---|---|---|---|---|
| **Bengaluru** | Lalbagh Botanical Garden | 🌿 | `12.95000` | `77.59000` | Glass house, rare plants, and flower shows. |
| | Cubbon Park | 🌳 | `12.97500` | `77.59333` | Large green park for walking and relaxing. |
| | Bangalore Palace | 🏰 | `12.99870` | `77.59200` | Historic palace inspired by Windsor Castle. |
| | ISKCON Temple | 🛕 | `13.00981` | `77.55109` | One of India's largest visited Krishna temples. |
| | Bannerghatta Biological Park | 🦁 | `12.80083` | `77.57556` | Wildlife safaris, zoo, and butterfly park. |
| **Goa** | Baga Beach | 🏖️ | `15.55889` | `73.75333` | Famous for water sports, nightlife, and shacks. |
| | Calangute Beach | 🌊 | `15.54167` | `73.76194` | Widely known as the "Queen of Beaches." |
| | Basilica of Bom Jesus | ⛪ | `15.50087` | `73.91151` | Historic church housing remains of St. Francis. |
| | Fort Aguada | 🏰 | `15.48800` | `73.76300` | 17th-century fort with beautiful ocean views. |
| | Dudhsagar Falls | 💧 | `15.31277` | `74.31416` | Spectacular tall waterfall in the jungle. |
| **Mumbai** | Gateway of India | 🚪 | `18.92198` | `72.83466` | Iconic stone arch overlooking the harbor. |
| | Marine Drive | 🌊 | `18.94400` | `72.82300` | Famous seaside road known as the Queen's Necklace. |
| | Elephanta Caves | 🗿 | `18.96333` | `72.93139` | Ancient rock-cut temples on Elephanta Island. |
| | Juhu Beach | 🏖️ | `19.10000` | `72.83000` | Popular beach famous for street food and sunset walks. |
| | Siddhivinayak Temple | 🛕 | `19.01692` | `72.83041` | Highly visited, historic temple dedicated to Lord Ganesha. |

### Properties (Database Seeds - `data.sql`)

| ID | Title | City | Type | Latitude | Longitude | Price/Night | Cleaning Fee |
|---|---|---|---|---|---|---|---|
| **2** | Modern Apartment in Indiranagar | Bengaluru | APARTMENT | `12.9719` | `77.6412` | \$80.00 | \$0.00 |
| **7** | Executive Suite Loft in Bengaluru | Bengaluru | LOFT | `12.9733` | `77.6112` | \$90.00 | \$0.00 |
| **1** | Luxury Beach Villa in Goa | Goa | VILLA | `15.5164` | `73.7632` | \$250.00 | \$0.00 |
| **4** | Portuguese Heritage Beach House | Goa | VILLA | `15.5011` | `73.7667` | \$180.00 | \$0.00 |
| **20** | Modern Condo near Baga Beach | Goa | APARTMENT | `15.5550` | `73.7580` | \$130.00 | \$0.00 |
| **21** | Secluded Jungle Cabin in South Goa | Goa | CABIN | `15.1023` | `74.1245` | \$110.00 | \$0.00 |
| **5** | Modern High-Rise Apartment | Mumbai | APARTMENT | `18.9431` | `72.8230` | \$150.00 | \$0.00 |
| **22** | Chic Apartment in Bandra | Mumbai | APARTMENT | `19.0544` | `72.8402` | \$95.00 | \$0.00 |
| **23** | Luxury Villa near Juhu Beach | Mumbai | VILLA | `19.1020` | `72.8250` | \$290.00 | \$0.00 |

---

## 4. Calculations & Smart Strategies

### A. Distance Calculation (Haversine Formula)
To find the exact distance between a property and a tourist attraction, we cannot simply draw a straight 2D line on a map (like using the standard school math $a^2 + b^2 = c^2$ formula). Because the Earth is a curved sphere, flat grid formulas become highly inaccurate over longer distances. 

Instead, the planner uses the **Haversine formula** to calculate the **"great-circle distance"**. 

#### In Layman's Terms:
Imagine you have a physical globe of the Earth. If you stretch a piece of string tightly across the globe's surface from a property to an attraction, the string will follow a curved path. The Haversine formula calculates the exact length of that string in kilometers. 

#### How the JavaScript Code Works:
1. **Degrees to Radians**: Latitude and longitude are given in degrees (e.g., `12.97°`). Computers calculate angles using "radians", so the code first converts degrees to radians.
2. **Difference Mapping**: It calculates the difference in latitude and longitude between the two coordinates.
3. **The Trigonometry Curve Trick**: It uses sines and cosines to calculate the spherical angle (curvature) between the two coordinates.
4. **Scale to Earth**: Finally, it multiplies that curvature angle by the Earth's average radius (**6,371 kilometers**) to give you the final straight-line distance in kilometers.

**JavaScript Code:**
```javascript
function haversineKm(lat1, lon1, lat2, lon2) {
  const R = 6371; // Earth's average radius in kilometers
  const toRad = (v) => (v * Math.PI) / 180; // Converts degrees to radians
  
  const dLat = toRad(lat2 - lat1);
  const dLon = toRad(lon2 - lon1);
  
  // Calculate the curved angle between the points
  const a =
    Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
    Math.sin(dLon / 2) * Math.sin(dLon / 2);
    
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  return R * c; // Multiply curved angle by Earth's radius to get kilometers
}
```

---

### B. Travel Plan Logic

#### 1. Budget Plan
* **Goal**: Recommends the cheapest accommodation.
* **Formula**: 
  $$\text{Total Cost} = (\text{Price per Night} \times \text{Nights}) + \text{Cleaning Fee}$$
* The property with the lowest total cost is selected.

#### 2. Nearest Plan
* **Goal**: Recommends the property closest to the sightseeing spots.
* **Formula**: For each property, the planner calculates the distance to every checked attraction, takes the **average** of those distances, and selects the property with the lowest average distance.

#### 3. Best Overall (Weighted Scoring)
* **Goal**: Finds the best compromise between price and proximity.
* **The "Apples-to-Oranges" Problem**: How do you add \$100 to 5 km? They are completely different units. 
* **The Solution (Normalization)**: The algorithm maps both price and distance to a simple scale from `0.0` (best/lowest value in that city) to `1.0` (worst/highest value in that city):

$$\text{Normalized Value} = \frac{\text{Current Value} - \text{Minimum Value in City}}{\text{Maximum Value in City} - \text{Minimum Value in City}}$$

* **Scoring**: A weighted formula combines these values, giving **40% weight to price** and **60% weight to distance**:

$$\text{Final Score} = (0.4 \times \text{Normalized Price}) + (0.6 \times \text{Normalized Distance})$$

The property with the **lowest score** wins the **Best Overall** recommendation.

---

### Step-by-Step Example (Mumbai)
Imagine you select 3 nights in Mumbai and check off two places to visit. The system evaluates the available properties:

1. **Chic Apartment in Bandra**:
   - Total Cost: **\$285** (The cheapest in Mumbai $\rightarrow$ Normalized Price = `0.0`)
   - Average Distance: **14.3 km** (The furthest in Mumbai $\rightarrow$ Normalized Distance = `1.0`)
   - **Combined Score**: $(0.4 \times 0.0) + (0.6 \times 1.0) = \mathbf{0.60}$

2. **Modern High-Rise Apartment**:
   - Total Cost: **\$450** (More expensive $\rightarrow$ Normalized Price = `1.0`)
   - Average Distance: **7.1 km** (The closest in Mumbai $\rightarrow$ Normalized Distance = `0.0`)
   - **Combined Score**: $(0.4 \times 1.0) + (0.6 \times 0.0) = \mathbf{0.40}$

**Result**: Because **0.40 is lower than 0.60**, the **Modern High-Rise** wins the **Best Overall** recommendation card. It is slightly more expensive, but it saves you 7.2 km of travel on average!

---

## 5. Coding & Edge Case Handling

1. **Only 1 Property Available** (e.g. if a city only has one listing):
   - All 3 cards will recommend the same property.
   - The UI automatically shows a helpful message: *"All plans recommend the same property — it's the best (and only) option in [City Name]!"*
2. **No Properties in the City**:
   - The "Generate Plans" button will disable, and a message will advise the user that new listings are coming soon.
3. **Resetting State**:
   - When the user closes the popup, the state (selected places, nights count, search filters) automatically clears after 250ms so that the next click starts fresh.
4. **Missing Coordinates**:
   - If any property has missing coordinate data in the database, it is automatically ignored in calculations to prevent calculation crashes.

===============================================================================================================================================

# ✈️ **Trip Planner — Complete Final Script**

---

## 🎤 **1. Introduction**

Now I’ll explain one of the most important and unique features of this project — the **Trip Planner**.

Unlike traditional booking platforms that only show listings, this feature helps users **plan their entire trip intelligently** by recommending the best property based on:

* Cost
* Distance
* Overall convenience

---

## 🧭 **2. User Flow (Step-by-Step)**

The Trip Planner works in a simple 3-step process:

---

### 🖱️ Step 1: Choose City

Users select a city:

* Bengaluru
* Goa
* Mumbai

Each city has predefined **tourist attractions with coordinates**.

---

### 🖱️ Step 2: Select Places

Users select places they want to visit:

* Beaches
* Temples
* Landmarks

👉 At least one place must be selected.

The system also shows **available properties count** in that city.

---

### 🖱️ Step 3: Generate Plans

The system processes all properties and generates **3 plans**:

* 💰 Budget Plan
* 📍 Nearest Plan
* ⭐ Best Overall Plan

Each plan shows:

* Property details
* Price breakdown
* Distance to each place
* Total cost

Users can also change the number of nights and see **real-time updates**.

---

## ⚙️ **3. Plan Logic**

---

### 💰 Budget Plan

Selects the **cheapest property**:

```
Total Cost = (Price per Night × Nights) + Cleaning Fee
```

---

### 📍 Nearest Plan

* Calculates distance from property → all selected places
* Computes **average distance**
* Chooses minimum

---

### ⭐ Best Overall Plan

Balances cost and distance:

* 40% → Price
* 60% → Distance

---

## 📍 **4. Distance Calculation — Haversine Formula**

### 🧠 What is it?

The **Haversine formula** is used to calculate the **shortest distance between two points on Earth’s surface**.

Since Earth is spherical, we cannot use simple formulas like:

```
distance = √(x² + y²)
```

Instead, we calculate the **great-circle distance**.

---

### 🌍 Conceptual Understanding

Imagine:

* A globe
* A string stretched between two points

👉 The string follows a curved path

Haversine calculates the length of that curved path.

---

### 🧮 Formula

```
a = sin²(Δlat / 2) + cos(lat1) × cos(lat2) × sin²(Δlon / 2)

c = 2 × atan2(√a, √(1−a))

distance = R × c
```

Where:

* R = 6371 km (Earth’s radius)

---

### ⚙️ Steps

1. Convert degrees → radians
2. Calculate Δlat and Δlon
3. Apply sin and cos
4. Multiply by Earth radius

👉 Output = distance in kilometers

---

### 💡 Why Important?

* Accurate real-world distance
* Better recommendations
* Improves user experience

---

## 🎯 **5. Example (Mumbai Case)**

User selects:

* City: Mumbai
* Places:

  * Gateway of India
  * Marine Drive
* Nights: 3

---

### 🏠 Property A — Chic Apartment

* Cost: $285
* Distance: 14.3 km

Scores:

* Price = 0.0
* Distance = 1.0

```
Final Score = (0.4 × 0.0) + (0.6 × 1.0) = 0.60
```

---

### 🏠 Property B — Modern High-Rise

* Cost: $450
* Distance: 7.1 km

Scores:

* Price = 1.0
* Distance = 0.0

```
Final Score = (0.4 × 1.0) + (0.6 × 0.0) = 0.40
```

---

### ✅ Result

* Budget → Property A
* Nearest → Property B
* Best Overall → **Property B**

👉 Even though it's expensive, it reduces travel time.

---

## 💡 **6. Why This Feature Matters**

* Reduces decision-making effort
* Balances cost and convenience
* Provides intelligent recommendations
* Enhances user experience

---

## 🧪 **7. Edge Case Handling**

* No properties → planner disabled
* One property → same recommendation
* Missing coordinates → ignored
* No places selected → blocked

---

# ⚙️ **8. Deep Technical Explanation (Internal Working)**

---

## 🧩 Data Sources

### Static:

* Tourist places with lat/long

### Dynamic:

* Properties from application state

---

## 🔄 Processing Flow

### Step 1: Filter Properties

* By selected city
* Ignore invalid data

---

### Step 2: Distance Calculation

```
for each property:
    for each selected place:
        calculate haversine distance
    calculate average distance
```

---

### Step 3: Cost Calculation

```
total_cost = (price × nights) + cleaning_fee
```

---

### Step 4: Normalization

```
normalized = (value - min) / (max - min)
```

---

### Step 5: Scoring

```
final_score = (0.4 × price) + (0.6 × distance)
```

---

### Step 6: Select Plans

* Budget → min(cost)
* Nearest → min(distance)
* Best → min(score)

---

## ⚡ Performance

* Runs fully on frontend
* No API calls needed
* Instant response

---

## 🧠 Design Decisions

* Distance weighted more → better UX
* Normalization → fair comparison
* Haversine → accuracy
* Frontend logic → speed

---

## 🚀 Future Improvements

* Real-time traffic integration
* Map APIs
* AI recommendations
* Personalization

---

# ✅ Final Impact

This feature transforms the platform from:
➡️ Just booking
➡️ To intelligent trip planning
