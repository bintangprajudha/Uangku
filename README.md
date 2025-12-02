# Uangku - Personal Finance Management Application

A modern web-based personal finance management application built with Spring Boot and Thymeleaf. Track your income, expenses, and manage your finances with ease.

## 🚀 Features

- **Dashboard** - Real-time overview of your financial status
- **Income & Expense Tracking** - Easy-to-use modal forms for quick transactions
- **Categories** - Pre-loaded income and expense categories
- **Statistics** - View balance, total income, and total expenses
- **Responsive UI** - Modern, mobile-friendly interface with Bootstrap 5
- **H2 Database** - In-memory database for development (easily switchable to MySQL/PostgreSQL)

## 🛠️ Tech Stack

- **Backend**: Java 21, Spring Boot 3.5.7
- **Frontend**: Thymeleaf, Bootstrap 5, Font Awesome
- **Database**: H2 (development), MySQL (production-ready)
- **Build Tool**: Maven
- **Libraries**: Lombok, Spring Data JPA

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

- **Java 21** or higher ([Download](https://www.oracle.com/java/technologies/downloads/))
- **Git** ([Download](https://git-scm.com/downloads))
- (Optional) **Maven** - The project includes Maven Wrapper, so local Maven installation is not required

## 🔧 Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/bintangprajudha/Uangku.git
cd Uangku
```

### 2. Verify Java Installation

```bash
java -version
```

You should see Java 21 or higher.

### 3. Build the Project

**On Linux/macOS:**

```bash
./mvnw clean install
```

**On Windows:**

```cmd
mvnw.cmd clean install
```

This will:

- Download all dependencies
- Compile the project
- Run tests
- Package the application

### 4. Run the Application

**Option A: Using Maven Wrapper (Recommended)**

```bash
./mvnw spring-boot:run
```

**Option B: Using JAR file**

```bash
./mvnw clean package
java -jar target/uangku-0.0.1-SNAPSHOT.jar
```

### 5. Access the Application

Once the application starts successfully, open your browser and navigate to:

```
http://localhost:8080
```

You should see the Uangku dashboard!

### 6. Access H2 Console (Optional - for debugging)

To view the database contents:

1. Go to: `http://localhost:8080/h2-console`
2. Use these credentials:
   - **JDBC URL**: `jdbc:h2:mem:testdb`
   - **Username**: `sa`
   - **Password**: `password`
3. Click **Connect**

## 📁 Project Structure

```
Uangku/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/uangku/
│   │   │       ├── config/          # Configuration classes
│   │   │       ├── controller/      # Web & REST controllers
│   │   │       ├── dto/             # Data Transfer Objects
│   │   │       ├── model/           # JPA entities
│   │   │       ├── repository/      # Data access layer
│   │   │       ├── service/         # Business logic
│   │   │       └── UangkuApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/              # CSS, JS, images
│   │       └── templates/           # Thymeleaf HTML templates
│   └── test/                        # Unit tests
├── pom.xml                          # Maven dependencies
├── mvnw                             # Maven wrapper (Linux/Mac)
├── mvnw.cmd                         # Maven wrapper (Windows)
└── README.md
```

## 🎯 Default Categories

The application comes with pre-loaded categories:

**Income Categories:**

- Salary
- Business
- Investment
- Freelance
- Bonus
- Gift
- Other Income

**Expense Categories:**

- Food & Dining
- Transport
- Utilities
- Entertainment
- Health
- Education
- Shopping
- Rent
- Insurance
- Phone & Internet
- Other Expense

## 🔄 Switching to MySQL (Production)

To use MySQL instead of H2:

### 1. Update `application.properties`:

```properties
spring.application.name=uangku

# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/uangku_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Disable H2 Console
spring.h2.console.enabled=false
```

### 2. Create MySQL Database:

```sql
CREATE DATABASE uangku_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Restart the application

## 🐛 Troubleshooting

### Port 8080 already in use

```bash
# Linux/Mac - Kill process on port 8080
lsof -ti:8080 | xargs kill -9

# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### Maven wrapper permission denied (Linux/Mac)

```bash
chmod +x mvnw
```

### Application fails to start

1. Check Java version: `java -version`
2. Clean build: `./mvnw clean install`
3. Check logs in console for specific errors

## 📝 API Endpoints

### Web Endpoints

- `GET /` - Dashboard
- `POST /income/add` - Add income transaction
- `POST /expense/add` - Add expense transaction
- `GET /transactions` - View all transactions
- `GET /categories` - Manage categories

### REST API Endpoints

- `POST /api/transactions/income` - Add income (JSON)
- `POST /api/transactions/expense` - Add expense (JSON)
- `GET /api/transactions` - Get all transactions (JSON)

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is open source and available under the MIT License.

## 👤 Author

**Bintang Prajudha**

- GitHub: [@bintangprajudha](https://github.com/bintangprajudha)

## 🙏 Acknowledgments

- Spring Boot Team
- Bootstrap Team
- Font Awesome

---

**Happy Budgeting! 💰**

## 📊 Architecture Diagram

┌─────────────────────────────────────────────────────────────────────┐
│ USER INTERFACE │
│ (Browser - dashboard.html) │
└────────────────┬────────────────────────────────┬───────────────────┘
│ │
GET / │ POST /income/add
│ POST /expense/add
▼ ▼
┌─────────────────────────────────────────────────────────────────────┐
│ CONTROLLER LAYER │
│ ┌──────────────────────┐ ┌─────────────────────────┐ │
│ │ DashboardController │ │ TransactionController │ │
│ │ @Controller │ │ @RestController │ │
│ │ - dashboard() │ │ - addIncome() │ │
│ │ - addIncome() │ │ - addExpense() │ │
│ │ - addExpense() │ │ - getAll() │ │
│ └──────────┬───────────┘ └─────────────┬───────────┘ │
└─────────────┼───────────────────────────────────┼───────────────────┘
│ │
│ Calls Services │
▼ ▼
┌─────────────────────────────────────────────────────────────────────┐
│ SERVICE LAYER │
│ ┌─────────────────┐ ┌──────────────────┐ ┌─────────────────┐ │
│ │ Dashboard │ │ TransactionMgr │ │ StatCalculator │ │
│ │ - getTotalInc() │ │ - getAll() │ │ - getTotalInc() │ │
│ │ - getTotalExp() │ │ - getByRange() │ │ - getTotalExp() │ │
│ │ - getBalance() │ │ - getTotalByR() │ │ - getBalance() │ │
│ │ - getMonthly() │ └────────┬─────────┘ └────────┬────────┘ │
│ └────────┬────────┘ │ │ │
│ │ │ │ │
│ ┌────────┴────────┐ ┌────────┴────────┐ ┌────────┴────────┐ │
│ │ IncomeService │ │ ExpenseService │ │ CategoryService │ │
│ │ - addIncome() │ │ - addExpense() │ │ - getAll() │ │
│ │ - getAll() │ │ - getAll() │ │ - getById() │ │
│ │ - getById() │ │ - getById() │ │ - add() │ │
│ │ - delete() │ │ - delete() │ │ - delete() │ │
│ └────────┬────────┘ └────────┬────────┘ └────────┬────────┘ │
└───────────┼─────────────────────┼─────────────────────┼─────────────┘
│ │ │
│ Uses Repository │ │
▼ ▼ ▼
┌─────────────────────────────────────────────────────────────────────┐
│ REPOSITORY LAYER (JPA) │
│ ┌──────────────────┐ ┌───────────────────┐ ┌─────────────────┐ │
│ │ IncomeRepository │ │ ExpenseRepository │ │ CategoryRepo │ │
│ │ extends JPA │ │ extends JPA │ │ extends JPA │ │
│ └────────┬─────────┘ └─────────┬─────────┘ └────────┬────────┘ │
└───────────┼──────────────────────┼──────────────────────┼───────────┘
│ │ │
│ CRUD Operations │ │
▼ ▼ ▼
┌─────────────────────────────────────────────────────────────────────┐
│ DATABASE LAYER │
│ H2 Database (In-Memory) │
│ ┌──────────────┐ ┌───────────────┐ ┌──────────────┐ │
│ │ INCOME Table │ │ EXPENSE Table │ │ CATEGORY Tbl │ │
│ └──────────────┘ └───────────────┘ └──────────────┘ │
└─────────────────────────────────────────────────────────────────────┘
