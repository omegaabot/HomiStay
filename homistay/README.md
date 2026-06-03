# 🏡 Homistay — Your Next Perfect Getaway

Welcome to **Homistay**! A modern, full-stack vacation rental platform that lets guests find and book unique homes, villas, and cabins, and helps hosts list their properties and track their earnings. 

Plus, it comes with a super-handy **Trip Planner** that does the hard math of travel planning for you!

---

## 🌟 Key Features

* **Stunning Home Page**: Discover handpicked featured homes and browse properties by categories (Villas, Beachfront, Cabins, Mountains, etc.).
* **Advanced Search & Filter**: Filter by price, rooms, ratings, property types, and specific amenities (like Pool, WiFi, or Ocean View).
* **Bespoke Trip Planner**: 
  - Pick a city (**Bengaluru**, **Goa**, or **Mumbai**).
  - Select your favorite sights to explore.
  - Instantly get 3 custom travel plans (**Budget**, **Nearest**, and **Best Overall**) containing exact distance breakdowns and live cost calculations.
* **Host Dashboard**: List your homes, set custom pricing and cleaning fees, add check-in instructions, manage bookings, and view your monthly earnings!

---

## 🛠️ The Tech Stack

* **Frontend**: React (Vite, Vanilla CSS styling, Tailwind-inspired Radix components, Wouter routing)
* **Backend**: Spring Boot 3 (Java 17, JPA/Hibernate, Spring Security + JWT)
* **Database**: PostgreSQL

---

## 🚀 How to Run the Project

Follow these quick steps to get Homistay running on your local machine:

### 1. Prerequisites
Make sure you have the following installed:
* [Node.js](https://nodejs.org/) (v18 or newer)
* [Java JDK 17](https://www.oracle.com/java/technologies/downloads/) (or newer)
* [Maven](https://maven.apache.org/) (usually comes with your IDE, or can be installed standalone)
* [PostgreSQL](https://www.postgresql.org/) (running locally on port `5432`)

---

### 2. Database Setup
1. Open your PostgreSQL console (or pgAdmin) and create a new database:
   ```sql
   CREATE DATABASE homistay_db;
   ```
2. The backend is configured to connect using:
   - **Database URL**: `jdbc:postgresql://localhost:5432/homistay_db`
   - **Username**: `postgres`
   - **Password**: `Post@1234`
   *(If your local PostgreSQL credentials differ, you can update them in `backend/src/main/resources/application.properties`)*

---

### 3. Start the Backend Server
1. Navigate to the backend directory:
   ```bash
   cd backend
   ```
2. Run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```
   *The server will start on port **`8090`**. On first run, it will automatically create the tables and seed them with pre-loaded users, properties, reviews, and bookings via `data.sql`!*

---

### 4. Start the Frontend Dev Server
1. Navigate to the frontend directory:
   ```bash
   cd ../frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Run the Vite development server:
   ```bash
   npm run dev
   ```
   *The frontend dev server will launch on **`http://localhost:5173/`**!*

---

## ✈️ Test-Drive the Trip Planner!

1. Open `http://localhost:5173/` in your browser.
2. Sign in by clicking the menu icon in the top-right corner. You can use the pre-loaded credentials:
   - **Email**: `user1@example.com`
   - **Password**: `password123`
3. Click **"Trip Planner"** in the Navbar.
4. Select a city (like **Goa** or **Mumbai**), check off the attractions you want to visit, and hit **"Generate Plans"** to see the smart recommended properties in action!

Happy Travels! 🏖️🏰🌆
