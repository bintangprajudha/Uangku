# Uangku - Dokumentasi Sistem Manajemen Keuangan Personal

## Daftar Isi

1. [Pendahuluan](#pendahuluan)
2. [Arsitektur Sistem](#arsitektur-sistem)
3. [Teknologi yang Digunakan](#teknologi-yang-digunakan)
4. [Persyaratan Sistem](#persyaratan-sistem)
5. [Instalasi dan Konfigurasi](#instalasi-dan-konfigurasi)
6. [Struktur Project](#struktur-project)
7. [Model Data](#model-data)
8. [Layer Arsitektur](#layer-arsitektur)
9. [Fitur Utama](#fitur-utama)
10. [Keamanan](#keamanan)
11. [Testing](#testing)
12. [Deployment](#deployment)
13. [Troubleshooting](#troubleshooting)

---

## Pendahuluan

Uangku adalah aplikasi manajemen keuangan personal berbasis web yang memungkinkan pengguna untuk melacak pendapatan dan pengeluaran mereka. Aplikasi ini dibangun menggunakan Spring Boot 3.5.7 dengan Java 21 dan menggunakan arsitektur MVC (Model-View-Controller).

### Tujuan Aplikasi

- Membantu pengguna mengelola keuangan personal secara terorganisir
- Menyediakan visualisasi data keuangan melalui dashboard interaktif
- Mengkategorikan transaksi untuk analisis yang lebih baik
- Menghasilkan laporan keuangan berkala

### Target Pengguna

- Individu yang ingin mengelola keuangan personal
- Freelancer yang perlu melacak pendapatan dan pengeluaran
- Siapa saja yang ingin memiliki kontrol lebih baik terhadap keuangan mereka

### Dokumentasi Tambahan

- **[API Documentation](API_DOCUMENTATION.md)** - Dokumentasi lengkap untuk REST API endpoints
- **[README. md](README.md)** - Quick start guide dan basic information

---

## Arsitektur Sistem

### Gambaran Umum

Aplikasi menggunakan arsitektur berlapis (layered architecture) dengan pemisahan tanggung jawab yang jelas:

```
┌─────────────────────────────────────────────┐
│         Presentation Layer                  │
│    (Controllers, Views, Templates)          │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│         Business Logic Layer                │
│            (Services, DTOs)                 │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│         Data Access Layer                   │
│      (Repositories, Entities)               │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│           Database Layer                    │
│         (MySQL / H2 Database)               │
└─────────────────────────────────────────────┘
```

### Pola Desain yang Digunakan

1. **MVC (Model-View-Controller)**:  Pemisahan logika presentasi, bisnis, dan data
2. **Repository Pattern**: Abstraksi akses data
3. **Service Layer Pattern**: Enkapsulasi logika bisnis
4. **DTO (Data Transfer Object)**: Transfer data antar layer
5. **Dependency Injection**: Pengelolaan dependensi melalui Spring IoC

---

## Teknologi yang Digunakan

### Backend

| Teknologi | Versi | Fungsi |
|-----------|-------|--------|
| Java | 21 | Bahasa pemrograman utama |
| Spring Boot | 3.5.7 | Framework aplikasi |
| Spring Data JPA | 3.5.7 | Persistensi data |
| Spring Web | 3.5.7 | REST API dan MVC |
| Spring Security | - | Enkripsi password |
| Hibernate | - | ORM (Object-Relational Mapping) |
| Lombok | - | Mengurangi boilerplate code |

### Frontend

| Teknologi | Versi | Fungsi |
|-----------|-------|--------|
| Thymeleaf | - | Template engine |
| Bootstrap | 5 | CSS framework |
| Font Awesome | - | Icon library |
| JavaScript | ES6+ | Interaktivitas client-side |

### Database

| Database | Penggunaan |
|----------|------------|
| H2 | Development (in-memory) |
| MySQL | Production |

### Tools & Utilities

| Tool | Fungsi |
|------|--------|
| Maven | Build tool dan dependency management |
| Maven Wrapper | Maven embedded untuk portabilitas |
| Spring Boot DevTools | Hot reload untuk development |
| SonarQube | Code quality analysis |
| Docker Compose | Container orchestration |

---

## Persyaratan Sistem

### Minimum Requirements

- **Java**:  JDK 21 atau lebih tinggi
- **RAM**: 2 GB (minimum), 4 GB (recommended)
- **Storage**: 500 MB untuk aplikasi dan dependencies
- **OS**: Windows 10/11, macOS 10.15+, Linux (Ubuntu 20.04+)

### Database Requirements

#### Development (H2)
- Tidak ada persyaratan tambahan (embedded database)

#### Production (MySQL)
- MySQL Server 8.0 atau lebih tinggi
- Database:  `uangku_db`
- Character Set: UTF8MB4

### Network Requirements

- Port 8080 (aplikasi)
- Port 3306 (MySQL, jika digunakan)
- Port 9000 (SonarQube, optional)

---

## Instalasi dan Konfigurasi

### 1. Clone Repository

```bash
git clone https://github.com/bintangprajudha/Uangku.git
cd Uangku
```

### 2. Verifikasi Java Installation

```bash
java -version
```

Output yang diharapkan:
```
java version "21.x.x"
```

### 3. Konfigurasi Database

#### Option A: Menggunakan H2 (Development)

Edit `src/main/resources/application.properties`:

```properties
spring.application.name=uangku

# H2 Database Configuration
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource. driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource. password=password

# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect. H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

#### Option B: Menggunakan MySQL (Production)

1. Buat database MySQL: 

```sql
CREATE DATABASE uangku_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. Edit `src/main/resources/application.properties`:

```properties
spring.application.name=uangku

# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/uangku_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource. driver-class-name=com. mysql.cj.jdbc.Driver

# JPA Configuration
spring. jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa. properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Disable H2 Console
spring.h2.console.enabled=false
```

### 4. Build Aplikasi

**Linux/macOS:**
```bash
./mvnw clean install
```

**Windows:**
```cmd
mvnw. cmd clean install
```

### 5. Menjalankan Aplikasi

**Option A: Menggunakan Maven Wrapper**
```bash
./mvnw spring-boot:run
```

**Option B: Menggunakan JAR File**
```bash
./mvnw clean package
java -jar target/uangku-0.0.1-SNAPSHOT. jar
```

### 6. Akses Aplikasi

Buka browser dan navigasi ke: 
```
http://localhost:8080
```

---

## Struktur Project

```
Uangku/
├── . mvn/                           # Maven wrapper files
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── uangku/
│   │   │               ├── config/
│   │   │               │   └── WebConfig.java
│   │   │               ├── controller/
│   │   │               │   ├── AuthController.java
│   │   │               │   ├── DashboardController.java
│   │   │               │   ├── HomeController.java
│   │   │               │   └── TransactionController.java
│   │   │               ├── dto/
│   │   │               │   ├── CategoryRequestDTO.java
│   │   │               │   ├── CategoryStatsDTO.java
│   │   │               │   ├── QuickStatsDTO.java
│   │   │               │   ├── UserLoginDTO.java
│   │   │               │   └── UserRegisterDTO.java
│   │   │               ├── model/
│   │   │               │   ├── Category.java
│   │   │               │   ├── Expense.java
│   │   │               │   ├── Income.java
│   │   │               │   ├── Report.java
│   │   │               │   ├── Transaction.java
│   │   │               │   └── User.java
│   │   │               ├── repository/
│   │   │               │   ├── CategoryRepository.java
│   │   │               │   ├── ExpenseRepository.java
│   │   │               │   ├── IncomeRepository.java
│   │   │               │   └── UserRepository.java
│   │   │               ├── service/
│   │   │               │   ├── CategoryService.java
│   │   │               │   ├── DashboardService.java
│   │   │               │   ├── ExpenseService.java
│   │   │               │   ├── IncomeService.java
│   │   │               │   ├── QuickStatsService.java
│   │   │               │   ├── ReportGenerator.java
│   │   │               │   ├── StatisticCalculatorService.java
│   │   │               │   ├── TransactionManagerService.java
│   │   │               │   └── UserService.java
│   │   │               ├── utils/
│   │   │               │   └── AuthInterceptor.java
│   │   │               └── UangkuApplication.java
│   │   └── resources/
│   │       ├── static/               # CSS, JS, images
│   │       ├── templates/            # Thymeleaf templates
│   │       └── application.properties
│   └── test/                         # Unit tests
├── docker-compose.yml                # Docker configuration
├── pom.xml                          # Maven configuration
├── mvnw                             # Maven wrapper (Unix)
├── mvnw.cmd                         # Maven wrapper (Windows)
├── CLASS DIAGRAM.drawio(2).png      # Class diagram
├── README.md                        # Quick start guide
├── DOCUMENTATION.md                 # This file
└── API_DOCUMENTATION.md             # API documentation
```

---

## Model Data

### Entity Relationship Diagram

```
┌─────────────────┐
│      User       │
├─────────────────┤
│ id (PK)         │
│ username        │
│ password        │
└────────┬────────┘
         │ 1
         │
         │ *
┌────────▼────────┐
│   Transaction   │ (Abstract)
├─────────────────┤
│ id (PK)         │
│ amount          │
│ date            │
│ description     │
│ user_id (FK)    │
│ category_id (FK)│
└────┬────┬───────┘
     │    │
  ┌──▼──┐ └──▼──┐
  │Income│ │Expense│
  └─────┘ └──────┘

┌─────────────────┐
│    Category     │
├─────────────────┤
│ id (PK)         │
│ name            │
│ type            │
│ icon            │
│ color           │
└─────────────────┘
```

### Deskripsi Entity

#### User
```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(nullable = false)
    private String password; // Encrypted using BCrypt
}
```

**Atribut:**
- `id`: Primary key, auto-increment
- `username`: Username unik untuk login
- `password`: Password terenkripsi menggunakan BCrypt

#### Transaction (Abstract)
Base class untuk Income dan Expense.

**Atribut:**
- `id`: Primary key
- `amount`: Jumlah transaksi
- `date`: Tanggal transaksi
- `description`: Deskripsi transaksi
- `user`: Relasi ke User
- `category`: Relasi ke Category

#### Income
Merepresentasikan transaksi pendapatan.

#### Expense
Merepresentasikan transaksi pengeluaran.

#### Category
```java
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String type;  // "income" or "expense"
    private String icon;
    private String color;
}
```

**Atribut:**
- `id`: Primary key
- `name`: Nama kategori
- `type`: Tipe kategori (income/expense)
- `icon`: Icon untuk UI
- `color`: Warna untuk visualisasi

### Kategori Default

#### Income Categories
- Salary
- Business
- Investment
- Freelance
- Bonus
- Gift
- Other Income

#### Expense Categories
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

---

## Layer Arsitektur

### 1. Controller Layer

Controller bertanggung jawab untuk menangani HTTP requests dan responses.

#### AuthController
```java
@Controller
@RequestMapping("/auth")
public class AuthController {
    // Endpoints: 
    // GET  /auth/register - Tampilkan form registrasi
    // POST /auth/register - Proses registrasi
    // GET  /auth/login    - Tampilkan form login
    // POST /auth/login    - Proses login
    // GET  /auth/logout   - Logout user
}
```

#### DashboardController
Menangani tampilan dashboard dan operasi transaksi.

#### TransactionController
REST API untuk transaksi (optional, untuk AJAX calls).

**Untuk detail lengkap endpoint, lihat [API Documentation](API_DOCUMENTATION.md)**

### 2. Service Layer

Service layer mengandung logika bisnis aplikasi.

#### UserService
```java
@Service
public class UserService {
    public User register(UserRegisterDTO dto);
    public User login(UserLoginDTO dto);
}
```

**Fungsi:**
- Registrasi user baru
- Validasi login
- Enkripsi password menggunakan BCrypt

#### DashboardService
Menyediakan data untuk dashboard. 

#### TransactionManagerService
Mengelola semua operasi transaksi.

**Fungsi:**
- `getAllTransactionsByUser(User user)`
- `getTransactionsByDateRange(User user, LocalDate start, LocalDate end)`
- `getTotalIncomeByDateRange(... )`
- `getTotalExpenseByDateRange(...)`

#### StatisticCalculatorService
Menghitung statistik keuangan. 

**Fungsi:**
- `getTotalIncome(User user)`
- `getTotalExpense(User user)`
- `getBalance(User user)`
- `getMonthlySummaries(User user)`

#### QuickStatsService
Menyediakan statistik cepat untuk dashboard.

**Fitur:**
- Pengeluaran hari ini
- Rata-rata pengeluaran harian
- Kategori teratas
- Progress bulan
- Savings rate

#### ReportGenerator
Menghasilkan laporan keuangan. 

**Jenis Laporan:**
- Monthly Report
- Category Report
- Custom Date Range Report

### 3. Repository Layer

Repository menggunakan Spring Data JPA untuk akses database.

```java
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

public interface IncomeRepository extends JpaRepository<Income, Long> {
    List<Income> findByUser(User user);
}

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUser(User user);
}

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByType(String type);
}
```

---

## Fitur Utama

### 1. Autentikasi dan Autorisasi

#### Registrasi User
- Validasi username unik
- Enkripsi password menggunakan BCrypt
- Penyimpanan user ke database

#### Login
- Validasi credentials
- Session management
- Authentication interceptor untuk protected routes

#### Logout
- Invalidate session
- Redirect ke login page

### 2. Dashboard

Dashboard menyediakan overview keuangan user: 

**Komponen:**
- Total Balance
- Total Income (periode tertentu)
- Total Expense (periode tertentu)
- Quick Stats: 
  - Today's Expense
  - Daily Average
  - Top Category
  - Month Progress
  - Savings Rate
- Recent Transactions
- Monthly Summary Chart

### 3. Manajemen Transaksi

#### Tambah Income
- Input amount, date, description, category
- Validasi data
- Simpan ke database
- Update statistik

#### Tambah Expense
- Input amount, date, description, category
- Validasi data
- Simpan ke database
- Update statistik

#### View Transactions
- Filter by date range
- Filter by category
- Filter by type (income/expense)
- Sorting options

### 4. Manajemen Kategori

- View all categories
- Add custom category
- Edit category
- Delete category (jika tidak digunakan)

### 5. Reporting

#### Monthly Report
- Total income dan expense per bulan
- Category breakdown
- Trend analysis

#### Category Report
- Total transaksi per kategori
- Persentase dari total
- List transaksi detail

#### Custom Report
- Pilih date range custom
- Export data (future feature)

### 6. Statistics & Analytics

#### Quick Stats
- Today's expense vs daily average
- Top spending category
- Month progress indicator
- Savings rate calculation
- Financial health status

#### Monthly Summaries
- Ringkasan 12 bulan terakhir
- Chart visualization
- Income vs expense comparison

---

## Keamanan

### Password Security

**BCrypt Hashing:**
```java
@Autowired
private PasswordEncoder passwordEncoder;

// Registration
String hashedPassword = passwordEncoder.encode(plainPassword);
user.setPassword(hashedPassword);

// Login
boolean isMatch = passwordEncoder.matches(plainPassword, user.getPassword());
```

**Konfigurasi:**
```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

### Session Management

**Authentication Interceptor:**
```java
@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response. sendRedirect("/auth/login");
            return false;
        }
        return true;
    }
}
```

**Protected Routes:**
- `/dashboard`
- `/`
- `/income/**`
- `/expense/**`
- `/categories/**`

**Public Routes:**
- `/auth/**`
- `/static/**`

### SQL Injection Prevention

Spring Data JPA menggunakan prepared statements secara otomatis, mencegah SQL injection.

### XSS Prevention

Thymeleaf secara otomatis escape output, mencegah XSS attacks. 

---

## Testing

### Unit Testing

Framework:  JUnit 5, Mockito

**Struktur Test:**
```
src/test/java/com/example/uangku/
├── controller/
├── service/
└── repository/
```

**Example Test:**
```java
@SpringBootTest
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @InjectMocks
    private UserService userService;
    
    @Test
    void testRegisterSuccess() {
        // Test implementation
    }
    
    @Test
    void testLoginSuccess() {
        // Test implementation
    }
}
```

### Integration Testing

**Database Testing:**
- Menggunakan H2 in-memory database untuk testing
- `@DataJpaTest` untuk repository tests

### Code Quality

**SonarQube Integration:**

1. Start SonarQube: 
```bash
docker-compose up -d
```

2. Run analysis:
```bash
./mvnw clean verify sonar:sonar \
  -Dsonar. host.url=http://localhost:9000 \
  -Dsonar.login=your_token
```

---

## Deployment

### Production Deployment

#### Prerequisites
- Java 21 JRE
- MySQL 8.0+
- Reverse proxy (Nginx/Apache) - optional
- SSL Certificate - recommended

#### Deployment Steps

1. **Build Production JAR:**
```bash
./mvnw clean package -DskipTests
```

2. **Setup MySQL Database:**
```sql
CREATE DATABASE uangku_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'uangku_user'@'localhost' IDENTIFIED BY 'strong_password';
GRANT ALL PRIVILEGES ON uangku_db. * TO 'uangku_user'@'localhost';
FLUSH PRIVILEGES;
```

3. **Configure application.properties:**
```properties
spring.application.name=uangku

# Production Database
spring.datasource.url=jdbc:mysql://localhost:3306/uangku_db
spring. datasource.username=uangku_user
spring.datasource. password=strong_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Settings
spring.jpa.hibernate. ddl-auto=validate
spring.jpa.show-sql=false

# Logging
logging.level.root=WARN
logging.level.com.example. uangku=INFO

# Server Settings
server.port=8080
server.error.whitelabel. enabled=false
```

4. **Run Application:**
```bash
java -jar target/uangku-0.0.1-SNAPSHOT. jar
```

#### Systemd Service (Linux)

Create `/etc/systemd/system/uangku.service`:

```ini
[Unit]
Description=Uangku Finance Manager
After=syslog.target network.target

[Service]
User=uangku
ExecStart=/usr/bin/java -jar /opt/uangku/uangku-0.0.1-SNAPSHOT.jar
SuccessExitStatus=143
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user. target
```

Enable dan start service:
```bash
sudo systemctl enable uangku
sudo systemctl start uangku
sudo systemctl status uangku
```

#### Nginx Reverse Proxy

```nginx
server {
    listen 80;
    server_name uangku.example.com;
    
    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

### Docker Deployment (Future)

Dockerfile example:
```dockerfile
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/uangku-0.0.1-SNAPSHOT.jar app. jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## Troubleshooting

### Common Issues

#### 1. Port 8080 Already in Use

**Solusi Linux/macOS:**
```bash
lsof -ti: 8080 | xargs kill -9
```

**Solusi Windows:**
```cmd
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

**Atau ubah port di `application.properties`:**
```properties
server.port=8081
```

#### 2. Maven Wrapper Permission Denied

```bash
chmod +x mvnw
```

#### 3. Database Connection Error

**Cek MySQL Service:**
```bash
sudo systemctl status mysql
```

**Verify Database Exists:**
```sql
SHOW DATABASES LIKE 'uangku_db';
```

**Test Connection:**
```bash
mysql -u uangku_user -p -h localhost uangku_db
```

#### 4. Java Version Mismatch

```bash
# Check current version
java -version

# Set JAVA_HOME (Linux/Mac)
export JAVA_HOME=/path/to/java21
export PATH=$JAVA_HOME/bin:$PATH

# Set JAVA_HOME (Windows)
setx JAVA_HOME "C:\Program Files\Java\jdk-21"
```

#### 5. Application Fails to Start

**Check Logs:**
```bash
# In console output, look for: 
- Port binding errors
- Database connection errors
- Bean creation errors

# Enable debug logging in application.properties:
logging.level.org.springframework=DEBUG
```

#### 6. Session Not Persisting

**Possible Causes:**
- Cookie domain mismatch
- HTTPS/HTTP mismatch
- Session timeout too short

**Solution:**
```properties
# application.properties
server.servlet.session.timeout=30m
server.servlet.session.cookie.http-only=true
server.servlet.session.cookie.secure=false  # Set true for HTTPS
```

### Debug Mode

Enable detailed logging:
```properties
logging.level.com.example.uangku=DEBUG
logging.level.org.springframework.web=DEBUG
logging.level.org.hibernate. SQL=DEBUG
logging.level. org.hibernate.type. descriptor.sql.BasicBinder=TRACE
```

### Performance Issues

**Increase JVM Memory:**
```bash
java -Xms512m -Xmx1024m -jar uangku-0.0.1-SNAPSHOT.jar
```

**Enable JPA Query Caching:**
```properties
spring.jpa.properties.hibernate.cache.use_second_level_cache=true
spring.jpa. properties.hibernate.cache.region.factory_class=org.hibernate.cache.ehcache.EhCacheRegionFactory
```

---

## Pengembangan Lebih Lanjut

### Roadmap Fitur

#### Phase 1: Core Features (Completed)
- Authentication system
- Basic transaction management
- Dashboard with statistics
- Category management

#### Phase 2: Enhanced Features (In Progress)
- Export reports (PDF/Excel)
- Email notifications
- Budget planning
- Recurring transactions

#### Phase 3: Advanced Features (Planned)
- Multi-currency support
- Mobile app integration
- AI-powered expense insights
- Collaborative budgets (family/team)

### Contributing

Untuk kontribusi ke project: 

1. Fork repository
2. Create feature branch
3. Commit changes
4. Push to branch
5. Create Pull Request

**Code Style Guidelines:**
- Follow Java naming conventions
- Use meaningful variable names
- Add comments for complex logic
- Write unit tests for new features
- Keep methods short and focused

---

## Lisensi

Project ini menggunakan MIT License.

---

## Kontak dan Dukungan

**Developer:** Bintang Prajudha

**GitHub:** [@bintangprajudha](https://github.com/bintangprajudha)

**Repository:** [https://github.com/bintangprajudha/Uangku](https://github.com/bintangprajudha/Uangku)

---

## Referensi

### Dokumentasi Framework
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Thymeleaf Documentation](https://www.thymeleaf.org/documentation.html)
- [Bootstrap 5](https://getbootstrap.com/docs/5.0/)

### Tutorial dan Resources
- [Baeldung - Spring Boot Guides](https://www.baeldung.com/spring-boot)
- [Spring Academy](https://spring.academy/)
- [Java 21 Documentation](https://docs.oracle.com/en/java/javase/21/)

---

**Version:** 1.0.0  
**Last Updated:** 2026-01-04  
**Status:** Production Ready
