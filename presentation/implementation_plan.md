# UI Improvements & Feature Additions

5 changes requested across frontend and backend. Items are ordered by dependency.

---

## SS1: Fix Dropdown Going Behind Elements

The search suggestion dropdown in [SearchBar.jsx](file:///c:/Users/abdur.ahmed/Desktop/homistay/homistay/frontend/src/components/SearchBar.jsx) has `z-50` but the hero section form's parent container doesn't have a stacking context that places it above sibling sections (categories row, featured homes).

### Proposed Changes

#### [MODIFY] [SearchBar.jsx](file:///c:/Users/abdur.ahmed/Desktop/homistay/homistay/frontend/src/components/SearchBar.jsx)
- Add `z-[60]` or a high stacking context to the suggestion dropdown container
- Ensure the parent `<form>` wrapper has `relative z-[60]` so the dropdown paints above the hero's sibling sections (categories, featured homes)

**One-liner fix** — fast and safe.

---

## SS2: Airbnb-Style Search Bar

Currently the hero search bar has only a **Where** field + Search button. Airbnb's bar has 4 pill-sections: **Where | Check in | Check out | Who** with dividers, and each section expands into an inline panel on click.

### Proposed Changes

#### [MODIFY] [SearchBar.jsx](file:///c:/Users/abdur.ahmed/Desktop/homistay/homistay/frontend/src/components/SearchBar.jsx)

**Hero variant redesign:**
- Replace the single-input form with a **segmented pill bar** (rounded capsule):
  - **Where** — destination text input with autocomplete dropdown
  - **Check in** — date picker popover (single date)  
  - **Check out** — date picker popover (single date)
  - **Who** — guest counter with +/- buttons in a popover
  - **Search button** — circular red/primary icon on the right
- Vertical dividers between segments (thin `|` borders)
- Active segment gets a subtle elevated background
- On mobile: stack vertically with rounded card style
- **Compact variant stays unchanged** (just the destination input + search icon)

**Styling approach:**
- Use existing Radix `Popover` for date pickers and guest counter
- Use existing `Calendar` component for date selection
- Smooth micro-animations on focus transitions
- All existing URL parameter behavior preserved (`destination`, `checkin`, `checkout`, `guests`)

---

## SS3: Map Feature Suggestions (No Implementation)

> [!NOTE]
> Per your request, I'll only provide suggestions here — no code changes.

### Option A: Split-View Map (Like Airbnb Search)
- **Layout**: Search results page becomes a two-column layout — property cards on the left (~60%), interactive map on the right (~40%)
- **Tech**: [Leaflet.js](https://leafletjs.com/) (free, open-source) or Google Maps JavaScript API (requires API key + billing)
- **Pins**: Each property is a price-label marker on the map (like Airbnb's `₹9,883` bubbles)
- **Interaction**: Hover on a card → highlight the pin. Click a pin → show a mini property card popup
- **Effort**: Medium-high. Needs lat/lng coordinates for each property (already in the DB schema but currently `null`). Would need to populate real coordinates in `data.sql`.

### Option B: Static Map on Property Detail Page Only
- **Simpler**: Just add a Google Maps embed `<iframe>` or Leaflet map on the PropertyDetails page showing the property's location
- **Effort**: Low. Can use the address string with Google Maps embed or geocode the address.

### Recommendation
Start with **Option B** (property detail map — SS5 below) since it's simple. Later add **Option A** as a separate feature with Leaflet + real lat/lng data.

> [!IMPORTANT]
> For Leaflet (free), no API key needed. For Google Maps, you'll need a Google Cloud API key with Maps JavaScript API enabled. Which do you prefer?

---

## SS4: Amenities Filter in Search

Currently amenities are stored as a comma-separated string in the `properties.amenities` column (e.g. `"WiFi,Kitchen,Pool"`). The frontend Search page already has an amenities filter in the sidebar, but it only filters client-side against the loaded page of results. We need **backend-level filtering** so the API returns only matching properties.

### Proposed Changes

#### [MODIFY] [PropertySearchRequest.java](file:///c:/Users/abdur.ahmed/Desktop/homistay/homistay/backend/src/main/java/com/homistay/dto/request/PropertySearchRequest.java)
- Add `private List<String> amenities;` field to accept amenity filter params (e.g. `?amenities=WiFi&amenities=Pool`)

#### [MODIFY] [PropertyRepository.java](file:///c:/Users/abdur.ahmed/Desktop/homistay/homistay/backend/src/main/java/com/homistay/repository/PropertyRepository.java)
- Can't easily do multi-amenity `LIKE` in a single JPQL query for a comma-separated column
- **Approach**: Keep the existing JPQL query as-is, and do amenity filtering in the service layer after the query (since amenities are comma-separated strings, not a join table). This is pragmatic and avoids schema changes.

#### [MODIFY] [PropertyService.java](file:///c:/Users/abdur.ahmed/Desktop/homistay/homistay/backend/src/main/java/com/homistay/service/PropertyService.java)
- In `search()`, after getting the page from the repository, filter results by amenities if provided:
  ```java
  if (req.getAmenities() != null && !req.getAmenities().isEmpty()) {
      content = content.stream()
          .filter(p -> req.getAmenities().stream()
              .allMatch(a -> p.getAmenities() != null 
                  && p.getAmenities().toLowerCase().contains(a.toLowerCase())))
          .collect(Collectors.toList());
  }
  ```

#### [MODIFY] [SearchBar.jsx](file:///c:/Users/abdur.ahmed/Desktop/homistay/homistay/frontend/src/components/SearchBar.jsx)
- Pass selected amenities as URL params when searching: `&amenities=WiFi&amenities=Pool`

#### [MODIFY] [Search.jsx](file:///c:/Users/abdur.ahmed/Desktop/homistay/homistay/frontend/src/pages/Search.jsx)
- Read `amenities` params from the URL
- Pass them to the backend API call (currently properties are loaded globally in AppContext, but the amenities filter already exists client-side in the sidebar, so this may stay client-side filtered — no backend change needed if all properties fit in memory)

> [!IMPORTANT]
> Since we fetch all properties into `AppContext` and the Search page filters client-side, the **amenities filter already works on the frontend** (`Search.jsx` lines 60-62). The main improvement is ensuring the amenities list in the sidebar matches what's actually in the database. Should I also add backend-level filtering for when the dataset grows, or is client-side sufficient for now?

---

## SS5: Map on Property Detail Page

### Proposed Changes

#### [MODIFY] [PropertyDetails.jsx](file:///c:/Users/abdur.ahmed/Desktop/homistay/homistay/frontend/src/pages/PropertyDetails.jsx)
- Add a **"Where you'll be"** section after amenities and before reviews
- Embed an interactive map showing the property's location
- **Approach**: Use a Google Maps `<iframe>` embed with the property's address (zero dependencies, no API key for simple embeds):
  ```jsx
  <iframe
    src={`https://www.google.com/maps/embed/v1/place?key=YOUR_KEY&q=${encodeURIComponent(address)}`}
    className="w-full h-[400px] rounded-2xl"
    allowFullScreen
    loading="lazy"
  />
  ```
- Alternatively, use **Leaflet** (free, no API key) with `react-leaflet` package — but this needs lat/lng coordinates populated in the database

#### [MODIFY] [data.sql](file:///c:/Users/abdur.ahmed/Desktop/homistay/homistay/backend/src/main/resources/data.sql)
- Add real `latitude` and `longitude` values for all 19 properties so map pins are accurate

> [!IMPORTANT]
> **Google Maps embed** requires an API key (even for the simple `<iframe>` embed API). The free alternative is **Leaflet + OpenStreetMap tiles** which needs no key at all. Which approach do you prefer?
> - **Option A**: Google Maps iframe embed (needs API key, ~5 min setup)
> - **Option B**: Leaflet + OpenStreetMap (free, no key, needs `react-leaflet` npm package)

---

## Open Questions

1. **Map library preference**: Google Maps (needs API key) or Leaflet/OpenStreetMap (free, no key)?
2. **Amenities filtering**: Keep it client-side only (current behavior works), or also add backend API support?
3. **Search bar dates**: Should the compact navbar search bar also get the Airbnb-style segments, or keep it as the simple destination-only input?

---

## Verification Plan

### Manual Verification
- Dropdown z-index: type in search on home page → dropdown should float above categories/featured sections
- Airbnb search bar: verify all 4 segments work (Where, Check in, Check out, Who)
- Amenities filter: select WiFi + Pool → only properties with both should show
- Property map: open a property detail page → "Where you'll be" section shows map with correct location

### Automated
- Build check: `npm run build` passes without errors
