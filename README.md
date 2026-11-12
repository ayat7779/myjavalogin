# MyJavaLogin - Java Swing Login Application with FlatLaf & MySQL

A modern, responsive, and secure Java Desktop Login Application built with **Java Swing**, **FlatLaf UI Framework**, and **MySQL Database**.

## ✨ Key Features

- 🎨 **Modern FlatLaf UI**: Clean, rounded card layout with FlatIntelliJLaf styling, placeholder text, and clear buttons.
- 🔒 **Secure Authentication**: Passwords are securely hashed with **SHA-256** before being stored in MySQL.
- 🔑 **Flexible Sign In**: Users can log in using either their **Username** or **Email**.
- 📋 **Admin Approval Workflow**: Newly registered users require approval from an Administrator before they can log in.
- 👑 **Admin Management Dashboard**: Built-in Admin Panel allowing Administrators to approve or reject pending user registrations in real-time.
- ⚡ **Auto Database & Table Setup**: Automatically creates the `mylogin_db` database and `users` table on startup if missing.

---

## 🛠️ Technology Stack

- **Language**: Java 17+
- **GUI Framework**: Java Swing + [FlatLaf 3.4.1](https://github.com/FormDev/FlatLaf)
- **Database**: MySQL Server
- **JDBC Driver**: MySQL Connector/J 8.3.0
- **Build Tool**: Maven / Standard Java Compiler

---

## 🚀 Getting Started

### 1. Prerequisites
- Java Development Kit (JDK) 17 or higher.
- MySQL Server (XAMPP, Laragon, or standalone MySQL).

### 2. Database Configuration
By default, the application connects to:
- **URL**: `jdbc:mysql://localhost:3306/mylogin_db`
- **User**: `root`
- **Password**: `123!` (Can be updated in `src/com/mylogin/config/DatabaseConnection.java`)

> Note: The app automatically creates the `mylogin_db` database and `users` table on startup.

### 3. Run Application

#### **Using Maven / IDE (IntelliJ IDEA / Android Studio)**
1. Open the project in IntelliJ IDEA or Android Studio.
2. Run `src/com/mylogin/Main.java`.

#### **Using Terminal / Command Line**
```powershell
# Compile project with dependencies
javac -cp "lib/*" -d bin src/com/mylogin/config/*.java src/com/mylogin/model/*.java src/com/mylogin/service/*.java src/com/mylogin/util/*.java src/com/mylogin/ui/*.java src/com/mylogin/*.java

# Run application
java -cp "bin;lib/*" com.mylogin.Main
```

---

## 👤 Default Accounts

| Role | Username | Password | Status |
| :--- | :--- | :--- | :--- |
| **Admin** | `admin` | `admin123` | `APPROVED` |

---

## 📜 License
MIT License
