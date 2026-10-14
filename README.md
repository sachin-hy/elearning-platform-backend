# E-Learning Platform Backend

A backend for an online learning platform (something like a small Udemy) built with **Spring Boot**. Instructors can create courses with sections and videos, students can browse, buy and study them, and every course has its own **real-time chat room** where students can ask doubts.

I made this as one of my main projects while studying MCA. I wanted to learn how a real application handles logins, payments, caching and live chat, not just simple CRUD.

> This repository has only the **backend**. The frontend is built with React and Tailwind CSS.

---

## Features

**Users and login**
- Sign up with an **OTP sent to email** (the OTP expires after 10 minutes)
- Login with **JWT** (token is valid for 12 hours)
- Forgot password and reset password flow, plus change password
- Three roles: `STUDENT`, `INSTRUCTOR` and `ADMIN`
- Update profile and profile picture

**Courses**
- Instructors can create courses with a thumbnail, categories, sections and sub-sections (videos)
- Courses are organised like this: **Course → Section → SubSection (video)**
- Browse courses by category with pagination
- Students and instructors each get their own list of courses
- Ratings and reviews from students
- Cart for courses

**Payments**
- Payment with **Razorpay**
- The payment signature is verified on the server (HMAC-SHA256) before the student is enrolled
- A student cannot buy the same course twice
- If the verify request is sent again for an already successful order, it does not enroll the student a second time
- Confirmation email after enrolment

**Real-time chat**
- Every course has its own chat room using **WebSocket (STOMP + SockJS)**
- Messages go through **Redis Pub/Sub**, so chat still works if more than one server instance is running
- Chat history is saved in the database and can be loaded later

**Other things**
- **Redis caching** for categories and course lists, with cache clearing when a course, section or sub-section changes
- Images and videos are uploaded to **Cloudinary**
- File type checks (images: JPEG/PNG, videos: MP4/WMV)
- Global exception handler that returns clean error messages
- Dockerfile and Docker Compose included

---

## Tech Stack

| Area | Technology |
|------|-----------|
| Language | Java 21 |
| Framework | Spring Boot 3.5.6 |
| Security | Spring Security, JWT (jjwt) |
| Database | PostgreSQL with Spring Data JPA / Hibernate |
| Cache and messaging | Redis (Spring Cache + Pub/Sub) |
| Real-time | WebSocket, STOMP, SockJS |
| Payments | Razorpay |
| File storage | Cloudinary |
| Email | Spring Mail (SMTP) |
| Build tool | Maven |
| Deployment | Docker, Docker Compose, Render |

---

## How It Works

**Buying a course**
```
Student                    Backend                    Razorpay
   |                          |                           |
   |-- POST /create-order --->|-- create order --------->|
   |<-- order details --------|<-------------------------|
   |                          |                           |
   |-- pays on Razorpay checkout ----------------------->|
   |<-- payment id + signature ---------------------------|
   |                          |                           |
   |-- POST /verify-payment ->|                           |
   |                          |-- verify HMAC signature   |
   |                          |-- mark order SUCCESS      |
   |                          |-- enroll student + email  |
   |<-- success --------------|                           |
```

**Chat**
```
Student/Instructor -> /app/chatroom/{roomId} -> Server -> Redis channel
                                                           |
                      every server instance listens <------+
                                                           |
                      /topic/... -> all users in that course room
```

---

## Project Structure

```
src/main/java/com/example/project
├── configuration/   # Security, WebSocket, Redis, Cloudinary, Razorpay config
├── controller/      # REST and WebSocket controllers
├── service/         # Business logic (+ Interface/ for service interfaces)
├── repository/      # Spring Data JPA repositories
├── entity/          # Users, Courses, Section, SubSection, Orders, Cart, ChatRoom, ...
├── dto/             # Request and response objects
├── enums/           # AccountType, OrderStatus
├── filter/          # JwtFilter
├── messaging/       # Redis chat publisher and subscriber
├── exception/       # Custom exceptions + global handler
└── utilis/          # JwtUtil, ImageUploader
```

---

## API Endpoints

These are the main ones. Most endpoints need `Authorization: Bearer <token>`.

**Auth (public)**

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/sendotp` | Send OTP to email |
| POST | `/auth/signup` | Create account with OTP |
| POST | `/auth/login` | Login and get JWT |
| POST | `/auth/forgot-password` | Send reset link |
| PATCH | `/auth/reset-password` | Reset password |
| PATCH | `/change-password` | Change password (logged in) |

**Courses and categories**

| Method | Endpoint | Who | Description |
|--------|----------|-----|-------------|
| GET | `/categories` | Public | List categories |
| POST | `/categories` | Admin | Create category |
| GET | `/categories/{type}/courses` | Public | Courses of a category (paged) |
| POST | `/course/create-course` | Instructor | Create a course |
| GET | `/course/courses-instructor` | Instructor | My created courses |
| GET | `/course/courses-student` | Student | My enrolled courses |
| GET | `/course/d/{courseid}` | Public | Course details |
| DELETE | `/course/delete-course` | Instructor | Delete a course |
| POST | `/create-section` | Instructor | Add a section |
| DELETE | `/delete-section` | Instructor | Delete a section |
| POST / PUT / DELETE | `/subsection/...` | Instructor | Create, update or delete a video |

**Cart, payment, reviews, profile**

| Method | Endpoint | Who | Description |
|--------|----------|-----|-------------|
| GET / POST | `/cart` | Logged in | View or add to cart |
| GET | `/cart-size` | Logged in | Number of items in cart |
| POST | `/create-order` | Student | Create a Razorpay order |
| POST | `/verify-payment` | Student | Verify payment and enrol |
| POST | `/rating-and-review/create-rating` | Student | Rate a course |
| PUT | `/user/update-profile` | Logged in | Update profile |

**Chat**

| Type | Endpoint | Description |
|------|----------|-------------|
| WebSocket | `/chat` | STOMP/SockJS connection point |
| Send | `/app/chatroom/{roomId}` | Send a message |
| Subscribe | `/topic/...` | Receive messages of a room |
| POST | `/history/chatroom` | Get or create the chat room of a course |
| GET | `/history/{roomId}/message` | Old messages of a room |

---

## Getting Started

### What you need
- Java 21 and Maven (or use `mvnw`)
- PostgreSQL
- Redis (Docker Compose can start it for you)
- A Cloudinary account (free plan is fine)
- A Razorpay account (test mode is fine)
- A Gmail (or other SMTP) account for sending OTP emails. For Gmail you need an **App Password**.

### 1. Clone the project
```bash
git clone <your-repo-url>
cd elearning-platform-backend/project
```

### 2. Add your configuration
Open `src/main/resources/application.properties` and fill in your own values. These are the settings the app needs:

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/your_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

# Email (OTP)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email
spring.mail.password=your_app_password

# Cloudinary
cloudinary.cloud_name=your_cloud_name
cloudinary.api_key=your_api_key
cloudinary.api_secret=your_api_secret

# Razorpay
razorpay.key_id=your_key_id
razorpay.key_secret=your_key_secret

# JWT
jwt.secret.key=a_long_random_secret_key

# Redis
spring.data.redis.url=redis://localhost:6379
spring.cache.type=redis
```

> Do not push your real passwords and keys to GitHub. Use environment variables for them (for example `SPRING_DATASOURCE_PASSWORD`) or keep a local copy of the file that is not committed.

### 3. Start Redis
```bash
docker run -d -p 6379:6379 redis:7-alpine
```

### 4. Run the app
```bash
./mvnw spring-boot:run
```
The backend starts at `http://localhost:8080`.

### Run with Docker
```bash
docker build -t elearning-backend .
docker compose up -d
```
The compose file starts Redis and the Spring Boot app. You still need to give the app the database, Cloudinary, Razorpay and mail settings through environment variables.

---

## Things I Learned

- How to secure an API with JWT and role-based access (student, instructor, admin)
- How online payments work, and why the payment has to be verified on the server and not trusted from the frontend
- Making a payment API safe when the same request comes twice
- Real-time chat with WebSocket and STOMP, and why Redis Pub/Sub is needed when running more than one server
- Using Redis as a cache and clearing it when data changes
- Uploading images and videos to a cloud service instead of storing them on my own server
- Writing a Dockerfile and using Docker Compose

## Future Improvements

- Authentication for the WebSocket chat connection (right now chat endpoints are open)
- Course progress tracking and certificates
- Unit and integration tests
- Swagger / OpenAPI documentation
- Moving all secrets out of the config file
- Adding an API for coupons and refunds

---

