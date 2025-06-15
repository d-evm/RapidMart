# 🛒 RapidMart – Quick-Commerce Backend System (Under development)

RapidMart is a Spring Boot-based backend system that mimics the operational flow of modern quick-commerce services like Zepto, Blinkit, and Instamart. The focus is on logistics, zone-based store allocation, and real-time delivery simulation — not just carts and listings.

---

## 🚀 Features

- ✅ JWT-based Signup & Login
- ✅ Product listing by user's zone (auto-resolved from pincode)
- ✅ Store selection based on stock & proximity
- ✅ ETA calculation: processing + delivery + buffer
- ✅ Live order tracking via WebSocket
- ✅ Realistic delivery simulation (10km/h, buffers)
- ✅ Inventory deduction on order
- ✅ Scheduler updates order status every 30s

---

## 📦 Tech Stack

| Layer        | Tech                         |
|--------------|------------------------------|
| Backend      | Java, Spring Boot            |
| Security     | Spring Security, JWT         |
| Real-time    | WebSockets (STOMP + SockJS)  |
| Database     | PostgreSQL                   |
| Testing      | Postman, HTML Client         |

---

## 🔐 Authentication

Use the `/auth/signup` and `/auth/login` endpoints to get a JWT token.

Then pass it in the `Authorization` header like:
```
Authorization: Bearer <your-jwt-token>
```

---

## 📡 WebSocket Live Order Tracking

You can test **live order tracking** in real time using this demo page:

👉 [🖥 Live WebSocket Demo](https://d-evm.github.io/rapidmart-html-client/)

### How to Use:

1. Place an order via `/orders` (see Postman below)
2. Open the demo link
3. Enter:
   - `Order ID` (from response)
   - `JWT Token` (from login)
4. You will receive live updates like:
   ```
   Status: Out for Delivery | 6 min left
   ```

---

## 🔁 Order Flow

1. User logs in (JWT token)
2. User places order (POST `/orders`)
3. System:
   - Selects nearest store with full inventory
   - Deducts stock
   - Calculates ETA
4. Background scheduler updates order status every 30s
5. User receives WebSocket push updates

---

## 🧪 Postman Collection

You can import this collection to test signup, login, order placement, etc.

📥 [Download RapidMart Postman Collection](./client-test/RapidMart_Postman_Collection.json)

---

## 🗂 Project Structure

```
src/main/java/com/rapidmart/
├── controllers        # REST Controllers
├── dtos               # Request/Response DTOs
├── models             # JPA Entities
├── repositories       # Spring Data JPA Repositories
├── schedulers         # WebSocket update scheduler
├── security           # JWT, Filters, Configs
├── services           # Business logic
├── config             # WebSocket & app configs
└── RapidMartApplication.java
```

---

## 🛠 Running the App

```bash
# Clone backend
git clone https://github.com/yourusername/rapidmart-backend.git
cd rapidmart-backend

# Configure PostgreSQL (update application.properties)

# Run the app
./mvnw spring-boot:run
```

App runs at: `http://localhost:8084`

---

## 🌐 GitHub Pages Demo

The real-time HTML WebSocket client is deployed live:

👉 https://d-evm.github.io/rapidmart-html-client/

If you want to run it locally:
```bash
cd client-test
open rapidmart_order_tracker.html
```

