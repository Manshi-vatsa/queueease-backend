# QueueEase Backend

## 📌 Overview
QueueEase is a backend system that allows users to join queues digitally and check their queue status in real-time.

## 🚀 Features
- Join Queue API
- Get Queue Status API
- Prevent duplicate bookings
- Auto-increment queue number logic

## 🛠 Tech Stack
- Java
- Spring Boot
- MySQL
- REST APIs

## 📡 APIs

### 1. Join Queue
POST /joinQueue  
Params: userId, centerId  

### 2. Get Queue Status
GET /queueStatus/{userId}

## ⚙️ How to Run
1. Clone the repo  
2. Open in IDE  
3. Configure DB in `application.properties`  
4. Run Spring Boot app  

## 👩‍💻 Author
Manshi Vatsa