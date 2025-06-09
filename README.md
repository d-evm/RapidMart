# 🛒 RapidMart – Quick-Commerce Backend System (Under development)

RapidMart is a Spring Boot-based backend system that mimics the operational flow of modern quick-commerce services like Zepto, Blinkit, and Instamart. The focus is on logistics, zone-based store allocation, and real-time delivery simulation — not just carts and listings.

---

## 🚀 Features

✅ Zone Management  
✅ Store Management  
✅ PostgreSQL Integration  
✅ RESTful APIs tested via Postman  
✅ Modular Codebase with DTO-Service-Controller layers  
✅ Secure (configurable with Spring Security)

---

## 🧱 Tech Stack

| Layer       | Technology            |
|-------------|------------------------|
| Backend     | Spring Boot (Java 17+) |
| Database    | PostgreSQL             |
| ORM         | Spring Data JPA        |
| Build Tool  | Maven                  |
| Extras      | Lombok, DevTools       |
| Security    | Spring Security (dev mode: disabled) |

---

## 📌 How to Run

```bash
# Clone the repo
git clone https://github.com/YOUR_USERNAME/rapidmart.git
cd rapidmart

# Setup PostgreSQL database
CREATE DATABASE rapidmart;

# Run the Spring Boot app
./mvnw spring-boot:run
```

---

## 🤝 Contributing
Pull requests are welcome! For major changes, please open an issue first.
