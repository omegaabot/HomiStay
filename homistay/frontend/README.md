# Homistay Frontend

A beautifully designed, full-featured Airbnb-like homestay marketplace built with **React**, **Vite**, and **TailwindCSS**. 

This application provides a seamless booking experience for guests and a comprehensive dashboard for hosts to manage their properties and bookings.

---

## 🌟 Key Features

- **Dual-Role System**: Users can switch between Guest and Host experiences.
- **Dynamic Search & Filtering**: Hero and compact search bars to find properties by destination.
- **Seamless Booking Flow**: Step-by-step checkout process with real-time validation and mock payment integration.
- **Host Dashboard**: Detailed analytics, earnings overview, and a property management interface for hosts.
- **Hybrid Data Architecture**: Intelligently merges local mock data with real backend API data to ensure the app is always populated and interactive, even during initial setup.

---

## 🛠️ Tech Stack

- **Framework**: React 18
- **Build Tool**: Vite
- **Styling**: TailwindCSS & `shadcn/ui` components
- **Routing**: `wouter` (lightweight routing)
- **State Management**: React Context API (`AppContext.jsx`)
- **Icons**: `lucide-react`
- **HTTP Client**: `axios`

---

## 🚀 Getting Started

### Prerequisites
- Node.js (v18+)
- npm or pnpm

### Installation

1. Clone the repository and navigate to the frontend directory:
   ```bash
   cd HMS-Frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm run dev
   ```

4. Open your browser and visit: **http://localhost:5173**

---

## 📂 Project Structure

```
src/
├── api/             # Axios instance and interceptors setup
├── components/      # Reusable UI components (SearchBar, PropertyCard, etc.)
├── context/         # AppContext for global state management
├── pages/           # Page layouts and views (Home, Checkout, Confirmation)
│   └── host/        # Host-specific pages (Dashboard, AddProperty, Bookings)
├── services/        # API service layers and mock data providers
└── index.css        # Global styles and Tailwind configuration
```

---

## 🧠 Core Logic & Architecture

### Hybrid Data Fetching
The application uses a hybrid data model located in `AppContext.jsx`. When the app initializes, it loads beautifully formatted mock properties. As the backend API responds, the app dynamically merges real user-created properties with the mock data. This guarantees that new users always see a populated interface.

### ID Validation (`api.js`)
Since the app handles both mock properties (string IDs like `"prop1"`) and real backend properties (numeric string IDs like `"7"`), the `isRealId()` function parses the ID to determine the source. 
- **Numeric IDs**: Sent directly to the backend `bookingsApi.create()`.
- **Alphanumeric Mock IDs**: Bypasses the backend and creates a simulated local booking so the user can test the checkout flow without errors.

---

## 🧪 Testing Accounts

You can log in instantly using these pre-configured mock accounts to test different roles:

- **Guest Account**: 
  - Email: `emma@example.com` 
  - Password: *(any password)*
- **Host Account**: 
  - Email: `marco@example.com` 
  - Password: *(any password)*

> **Tip:** Use the promo code **WELCOME10** at checkout to see the discount logic in action!
