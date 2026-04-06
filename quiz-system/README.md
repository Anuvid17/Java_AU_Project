# 🎓 Online Quiz System

A full-stack, production-ready interactive quiz application built with Java Spring Boot. It features a sleek glassmorphism dark-theme UI, role-based access control, timed quiz sessions, admin dashboards for managing questions, and an automated leaderboard.

---

## 🏗️ Project Structure
The application follows standard Spring Boot MVC architecture.

```text
quiz-system/
├── data/                             # 🗄️ H2 Embedded Database directory
│   └── quizdb.mv.db                  # Your local database file
├── src/main/java/com/quizsystem/
│   ├── config/                       # Configuration, Security Settings, Data Initializers
│   ├── controller/                   # Web Controllers handling routing and views
│   ├── dto/                          # Data Transfer Objects (e.g., UserRegistration)
│   ├── model/                        # JPA Entities (User, Quiz, Question, Result)
│   ├── repository/                   # Spring Data JPA Repository interfaces
│   ├── service/                      # Core Business Logic (Scoring, Logic)
│   └── QuizSystemApplication.java    # Application entry point
├── src/main/resources/
│   ├── static/                       # Static assets
│   │   ├── css/style.css             # Custom glassmorphism stylesheet
│   │   └── js/quiz-timer.js          # Client-side JavaScript logic
│   ├── templates/                    # Server-rendered HTML (Thymeleaf)
│   │   ├── admin/                    # Admin Dashboard and CRUD forms
│   │   ├── auth/                     # Login and Registration pages
│   │   └── quiz/                     # User Quiz interfaces & Result reviews
│   └── application.properties        # Application-wide configurations
├── .mvn/                             # Maven Wrapper configurations
├── mvnw.cmd                          # Maven script for Windows
├── mvnw                              # Maven script for Linux/Mac
└── pom.xml                           # Maven Dependencies and Build instructions
```

---

## 📥 Prerequisites

You do **NOT** need to install a standalone database like MySQL because this app uses a self-contained embedded database (H2). 

You only need:
1. **Java Development Kit (JDK):** Version 21 (or 17) installed.
2. A terminal (Command Prompt, PowerShell, or Git Bash).

*(Note: You do not even need to install Maven. The project uses the Maven Wrapper (`mvnw`), which automatically downloads Maven for you behind the scenes)*

---

## 🚀 How to Run Locally

### Step 1: Open Terminal in the specific folder
Open your terminal window and navigate using the `cd` command so that your active folder is `quiz-system`.

### Step 2: Start the server
Run the provided Maven wrapper to instantly compile and run the Spring Boot server!

**For Windows:**
```bash
./mvnw.cmd spring-boot:run
```

**For Mac / Linux:**
```bash
./mvnw spring-boot:run
```

Wait until you see the message: `Started QuizSystemApplication` and `Database already initialized`.

### Step 3: Access the Site
Once the server is running, open your favorite web browser and navigate to:
👉 **[http://localhost:8080](http://localhost:8080)**

---

## 🔐 Default Credentials

Since the system includes a `DataInitializer`, your database comes fully pre-loaded with **4 Categories, 8 Quizzes, and 96 Questions**, along with two default testing accounts:

| Role | Username | Password |
| :--- | :--- | :--- |
| **Administrator** | `admin` | `admin123` |
| **Student** | `student` | `student123` |

Log in as the `admin` to create new quizzes or questions. Log in as a `student` to try taking quizzes and get placed on the leaderboard!

---

## 🔄 Resetting the Database
If you ever want to start completely fresh (or if things break):
1. Stop the application by pressing `Ctrl + C` in your terminal.
2. Simply delete the `data` folder inside your project directory.
3. Start the application again! The database will be rebuilt instantly from scratch using the default setup records.
