# Uangku - API Documentation

## Table of Contents

1. [Overview](#overview)
2. [Authentication](#authentication)
3. [Base URL](#base-url)
4. [Response Format](#response-format)
5. [Error Handling](#error-handling)
6. [Authentication Endpoints](#authentication-endpoints)
7. [Dashboard Endpoints](#dashboard-endpoints)
8. [Transaction Endpoints](#transaction-endpoints)
9. [Category Endpoints](#category-endpoints)
10. [Statistics Endpoints](#statistics-endpoints)
11. [Report Endpoints](#report-endpoints)
12. [Request Examples](#request-examples)
13. [Status Codes](#status-codes)

---

## Overview

Uangku menyediakan dua jenis endpoint: 
- **Web Endpoints**: Menggunakan form submission dan redirect (Thymeleaf)
- **REST API Endpoints**: Menggunakan JSON request/response untuk AJAX calls

API Version: `v1`  
Last Updated: `2026-01-04`

---

## Authentication

### Authentication Method

Uangku menggunakan **Session-based Authentication**: 
- Login menghasilkan session cookie
- Session disimpan di server
- Setiap request harus menyertakan session cookie

### Session Cookie

```
Cookie:  JSESSIONID=<session_id>
```

### Authentication Flow

```
1. User POST /auth/login dengan credentials
2. Server validasi dan create session
3. Server return session cookie
4. Client menyimpan cookie
5. Client include cookie di setiap request
6. Server validasi session di AuthInterceptor
```

---

## Base URL

### Development
```
http://localhost:8080
```

## Response Format

### Web Endpoints
Web endpoints menggunakan Thymeleaf templates dan mengembalikan HTML: 
- Success:  Redirect atau render template
- Error: Render template dengan error message

### REST API Endpoints
REST API endpoints mengembalikan JSON: 

**Success Response:**
```json
{
  "success": true,
  "data": { ...  },
  "message": "Operation successful"
}
```

**Error Response:**
```json
{
  "success": false,
  "error": "Error message",
  "timestamp": "2026-01-04T10:30:00"
}
```

---

## Error Handling

### HTTP Status Codes

| Code | Meaning | Description |
|------|---------|-------------|
| 200 | OK | Request successful |
| 201 | Created | Resource created successfully |
| 400 | Bad Request | Invalid request parameters |
| 401 | Unauthorized | Authentication required |
| 403 | Forbidden | Insufficient permissions |
| 404 | Not Found | Resource not found |
| 409 | Conflict | Resource conflict (e.g., duplicate username) |
| 500 | Internal Server Error | Server error |

### Error Response Structure

```json
{
  "success": false,
  "error": "Detailed error message",
  "errorCode": "ERROR_CODE",
  "timestamp":  "2026-01-04T10:30:00",
  "path": "/api/endpoint"
}
```

---

## Authentication Endpoints

### 1. Show Registration Form

**Endpoint:** `GET /auth/register`

**Description:** Menampilkan halaman form registrasi

**Authentication:** Not Required

**Response:** HTML page

**Example:**
```
GET http://localhost:8080/auth/register
```

---

### 2. Register User

**Endpoint:** `POST /auth/register`

**Description:** Mendaftarkan user baru

**Authentication:** Not Required

**Content-Type:** `application/x-www-form-urlencoded`

**Request Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| username | String | Yes | Username (3-50 characters, unique) |
| password | String | Yes | Password (min 6 characters) |
| confirmPassword | String | Yes | Password confirmation |

**Request Example:**
```http
POST /auth/register
Content-Type: application/x-www-form-urlencoded

username=johndoe&password=secret123&confirmPassword=secret123
```

**Success Response:**
```
Status: 302 Redirect
Location: /auth/login
```

**Error Response:**
```
Status: 200 OK
Content:  HTML page with error message
Error Messages: 
  - "Username already exists"
  - "Password must be at least 6 characters"
  - "Passwords do not match"
```

---

### 3. Show Login Form

**Endpoint:** `GET /auth/login`

**Description:** Menampilkan halaman form login

**Authentication:** Not Required

**Response:** HTML page

**Example:**
```
GET http://localhost:8080/auth/login
```

---

### 4. Login User

**Endpoint:** `POST /auth/login`

**Description:** Login user dan membuat session

**Authentication:** Not Required

**Content-Type:** `application/x-www-form-urlencoded`

**Request Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| username | String | Yes | User's username |
| password | String | Yes | User's password |

**Request Example:**
```http
POST /auth/login
Content-Type: application/x-www-form-urlencoded

username=johndoe&password=secret123
```

**Success Response:**
```
Status: 302 Redirect
Location: /
Set-Cookie: JSESSIONID=<session_id>; Path=/; HttpOnly
```

**Error Response:**
```
Status: 200 OK
Content: HTML page with error message
Error:  "Invalid username or password"
```

---

### 5. Logout User

**Endpoint:** `GET /auth/logout`

**Description:** Logout user dan invalidate session

**Authentication:** Required

**Request Example:**
```http
GET /auth/logout
Cookie: JSESSIONID=<session_id>
```

**Success Response:**
```
Status: 302 Redirect
Location: /auth/login
Set-Cookie: JSESSIONID=deleted; Max-Age=0
```

---

## Dashboard Endpoints

### 1. Show Dashboard

**Endpoint:** `GET /` 

**Description:** Menampilkan dashboard dengan ringkasan keuangan

**Authentication:** Required

**Response:** HTML page dengan data: 
- Total balance
- Total income (current month)
- Total expense (current month)
- Quick statistics
- Recent transactions
- Monthly summaries

**Request Example:**
```http
GET /
Cookie: JSESSIONID=<session_id>
```

**Success Response:**
```
Status: 200 OK
Content-Type: text/html
Content: Dashboard HTML page
```

**Data yang Dikirim ke Template:**
```java
Model attributes:
  - user: User object
  - balance: Double
  - totalIncome: Double
  - totalExpense: Double
  - quickStats: QuickStatsDTO
  - recentTransactions:  List<Transaction>
  - monthlySummaries: List<Map<String, Object>>
  - incomeCategories: List<Category>
  - expenseCategories: List<Category>
```

---

## Transaction Endpoints

### 1. Add Income

**Endpoint:** `POST /income/add`

**Description:** Menambahkan transaksi pendapatan

**Authentication:** Required

**Content-Type:** `application/x-www-form-urlencoded`

**Request Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| amount | Double | Yes | Jumlah pendapatan (> 0) |
| date | Date | Yes | Tanggal transaksi (YYYY-MM-DD) |
| description | String | No | Deskripsi transaksi |
| categoryId | Long | Yes | ID kategori pendapatan |

**Request Example:**
```http
POST /income/add
Cookie: JSESSIONID=<session_id>
Content-Type:  application/x-www-form-urlencoded

amount=5000000&date=2026-01-04&description=Monthly Salary&categoryId=1
```

**Success Response:**
```
Status: 302 Redirect
Location: /
```

**Error Response:**
```
Status: 400 Bad Request
Error Messages:
  - "Amount must be greater than 0"
  - "Date is required"
  - "Category not found"
  - "Invalid category type"
```

---

### 2. Add Expense

**Endpoint:** `POST /expense/add`

**Description:** Menambahkan transaksi pengeluaran

**Authentication:** Required

**Content-Type:** `application/x-www-form-urlencoded`

**Request Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| amount | Double | Yes | Jumlah pengeluaran (> 0) |
| date | Date | Yes | Tanggal transaksi (YYYY-MM-DD) |
| description | String | No | Deskripsi transaksi |
| categoryId | Long | Yes | ID kategori pengeluaran |

**Request Example:**
```http
POST /expense/add
Cookie: JSESSIONID=<session_id>
Content-Type: application/x-www-form-urlencoded

amount=150000&date=2026-01-04&description=Grocery Shopping&categoryId=7
```

**Success Response:**
```
Status: 302 Redirect
Location: /
```

**Error Response:**
```
Status: 400 Bad Request
Error Messages:
  - "Amount must be greater than 0"
  - "Date is required"
  - "Category not found"
  - "Invalid category type"
```

---

### 3. View All Transactions

**Endpoint:** `GET /transactions`

**Description:** Menampilkan semua transaksi user

**Authentication:** Required

**Query Parameters (Optional):**

| Parameter | Type | Description |
|-----------|------|-------------|
| startDate | Date | Filter start date (YYYY-MM-DD) |
| endDate | Date | Filter end date (YYYY-MM-DD) |
| type | String | Filter by type:  "income" or "expense" |
| categoryId | Long | Filter by category ID |
| sort | String | Sort by:  "date", "amount" (default:  "date") |
| order | String | Order: "asc" or "desc" (default: "desc") |

**Request Example:**
```http
GET /transactions?startDate=2026-01-01&endDate=2026-01-31&type=expense
Cookie: JSESSIONID=<session_id>
```

**Success Response:**
```
Status: 200 OK
Content-Type: text/html
Content:  Transactions list page
```

---

### 4. Delete Transaction

**Endpoint:** `DELETE /transactions/{id}`

**Description:** Menghapus transaksi

**Authentication:** Required

**Path Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| id | Long | Transaction ID |

**Request Example:**
```http
DELETE /transactions/123
Cookie: JSESSIONID=<session_id>
```

**Success Response:**
```
Status: 302 Redirect
Location: /transactions
```

**Error Response:**
```
Status: 404 Not Found
Error:  "Transaction not found"
```

---

## Category Endpoints

### 1. View All Categories

**Endpoint:** `GET /categories`

**Description:** Menampilkan semua kategori

**Authentication:** Required

**Query Parameters (Optional):**

| Parameter | Type | Description |
|-----------|------|-------------|
| type | String | Filter by type: "income" or "expense" |

**Request Example:**
```http
GET /categories?type=expense
Cookie: JSESSIONID=<session_id>
```

**Success Response:**
```
Status: 200 OK
Content-Type: text/html
Content: Categories page
```

---

### 2. Add Category

**Endpoint:** `POST /categories/add`

**Description:** Menambahkan kategori baru

**Authentication:** Required

**Content-Type:** `application/x-www-form-urlencoded`

**Request Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| name | String | Yes | Nama kategori (3-50 characters) |
| type | String | Yes | Tipe:  "income" or "expense" |
| icon | String | No | Font Awesome icon class |
| color | String | No | Hex color code (e.g., #FF5733) |

**Request Example:**
```http
POST /categories/add
Cookie: JSESSIONID=<session_id>
Content-Type: application/x-www-form-urlencoded

name=Side Hustle&type=income&icon=fa-coins&color=#28a745
```

**Success Response:**
```
Status: 302 Redirect
Location: /categories
```

**Error Response:**
```
Status: 400 Bad Request
Error Messages:
  - "Category name is required"
  - "Category type must be 'income' or 'expense'"
  - "Category name already exists"
```

---

### 3. Delete Category

**Endpoint:** `DELETE /categories/{id}`

**Description:** Menghapus kategori (jika tidak digunakan)

**Authentication:** Required

**Path Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| id | Long | Category ID |

**Request Example:**
```http
DELETE /categories/15
Cookie: JSESSIONID=<session_id>
```

**Success Response:**
```
Status: 302 Redirect
Location: /categories
```

**Error Response:**
```
Status: 409 Conflict
Error: "Cannot delete category that is in use"

Status: 404 Not Found
Error: "Category not found"
```

---

## Statistics Endpoints

### 1. Get Quick Statistics

**Endpoint:** `GET /api/stats/quick`

**Description:** Mendapatkan statistik cepat untuk dashboard

**Authentication:** Required

**Response Type:** JSON

**Request Example:**
```http
GET /api/stats/quick
Cookie: JSESSIONID=<session_id>
Accept: application/json
```

**Success Response:**
```json
{
  "success": true,
  "data":  {
    "todayExpense": 150000.0,
    "dailyAverage": 200000.0,
    "topCategory": "Food & Dining",
    "topCategoryAmount": 1500000.0,
    "monthProgress": 13,
    "currentDay": 4,
    "totalDaysInMonth": 31,
    "savingsRate": 35,
    "totalIncome": 5000000.0,
    "totalExpense":  3250000.0,
    "totalSavings": 1750000.0,
    "isTodayAboveAverage": false,
    "savingsRateStatus": "Good"
  }
}
```

**Response Fields:**

| Field | Type | Description |
|-------|------|-------------|
| todayExpense | Double | Pengeluaran hari ini |
| dailyAverage | Double | Rata-rata pengeluaran harian bulan ini |
| topCategory | String | Kategori dengan pengeluaran tertinggi |
| topCategoryAmount | Double | Total pengeluaran kategori teratas |
| monthProgress | Integer | Persentase progress bulan (0-100) |
| currentDay | Integer | Hari ke-X dalam bulan |
| totalDaysInMonth | Integer | Total hari dalam bulan |
| savingsRate | Integer | Persentase tabungan (0-100) |
| totalIncome | Double | Total pendapatan bulan ini |
| totalExpense | Double | Total pengeluaran bulan ini |
| totalSavings | Double | Total tabungan (income - expense) |
| isTodayAboveAverage | Boolean | Apakah pengeluaran hari ini di atas rata-rata |
| savingsRateStatus | String | Status:  "Excellent", "Good", "Fair", "Poor" |

---

### 2. Get Monthly Summaries

**Endpoint:** `GET /api/stats/monthly`

**Description:** Mendapatkan ringkasan 12 bulan terakhir

**Authentication:** Required

**Response Type:** JSON

**Request Example:**
```http
GET /api/stats/monthly
Cookie: JSESSIONID=<session_id>
Accept: application/json
```

**Success Response:**
```json
{
  "success": true,
  "data": [
    {
      "month":  "FEBRUARY 2025",
      "income": 5000000.0,
      "expense": 3200000.0
    },
    {
      "month": "MARCH 2025",
      "income": 5500000.0,
      "expense": 3800000.0
    },
    // ...  10 more months
  ]
}
```

---

### 3. Get Category Statistics

**Endpoint:** `GET /api/stats/categories`

**Description:** Mendapatkan statistik per kategori

**Authentication:** Required

**Response Type:** JSON

**Query Parameters (Optional):**

| Parameter | Type | Description |
|-----------|------|-------------|
| startDate | Date | Start date (YYYY-MM-DD) |
| endDate | Date | End date (YYYY-MM-DD) |
| type | String | "income" or "expense" |

**Request Example:**
```http
GET /api/stats/categories? type=expense&startDate=2026-01-01&endDate=2026-01-31
Cookie: JSESSIONID=<session_id>
Accept: application/json
```

**Success Response:**
```json
{
  "success": true,
  "data": [
    {
      "categoryId": 7,
      "categoryName": "Food & Dining",
      "totalAmount": 1500000.0,
      "transactionCount": 25,
      "percentage": 46.15
    },
    {
      "categoryId": 8,
      "categoryName": "Transport",
      "totalAmount": 800000.0,
      "transactionCount": 15,
      "percentage": 24.62
    }
    // ... more categories
  ],
  "totalAmount": 3250000.0
}
```

---

## Report Endpoints

### 1. Generate Monthly Report

**Endpoint:** `GET /reports/monthly`

**Description:** Generate laporan bulanan

**Authentication:** Required

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| year | Integer | Yes | Tahun (e.g., 2026) |
| month | Integer | Yes | Bulan (1-12) |

**Request Example:**
```http
GET /reports/monthly?year=2026&month=1
Cookie: JSESSIONID=<session_id>
```

**Success Response:**
```
Status: 200 OK
Content-Type: text/html
Content: Monthly report page
```

**Report Data:**
```json
{
  "title": "Monthly Report - JANUARY 2026",
  "period": "2026-01",
  "totalIncome": 5000000.0,
  "totalExpense": 3250000.0,
  "balance": 1750000.0,
  "transactions": [... ],
  "categoryBreakdown": {
    "Food & Dining": 1500000.0,
    "Transport":  800000.0,
    "Utilities": 500000.0,
    "Entertainment": 450000.0
  },
  "generatedAt": "2026-01-04T10:30:00"
}
```

---

### 2. Generate Category Report

**Endpoint:** `GET /reports/category/{categoryName}`

**Description:** Generate laporan per kategori

**Authentication:** Required

**Path Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| categoryName | String | Nama kategori |

**Request Example:**
```http
GET /reports/category/Food%20%26%20Dining
Cookie: JSESSIONID=<session_id>
```

**Success Response:**
```
Status: 200 OK
Content-Type: text/html
Content:  Category report page
```

---

### 3. Generate Custom Report

**Endpoint:** `GET /reports/custom`

**Description:** Generate laporan dengan date range custom

**Authentication:** Required

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| startDate | Date | Yes | Start date (YYYY-MM-DD) |
| endDate | Date | Yes | End date (YYYY-MM-DD) |

**Request Example:**
```http
GET /reports/custom?startDate=2026-01-01&endDate=2026-01-31
Cookie: JSESSIONID=<session_id>
```

**Success Response:**
```
Status: 200 OK
Content-Type: text/html
Content:  Custom report page
```

---

## REST API Endpoints (JSON)

### 1. Add Income (JSON)

**Endpoint:** `POST /api/transactions/income`

**Description:** Menambahkan pendapatan via JSON API

**Authentication:** Required

**Content-Type:** `application/json`

**Request Body:**
```json
{
  "amount": 5000000.0,
  "date": "2026-01-04",
  "description": "Monthly Salary",
  "categoryId": 1
}
```

**Success Response:**
```json
{
  "success": true,
  "data": {
    "id":  123,
    "amount":  5000000.0,
    "date": "2026-01-04",
    "description": "Monthly Salary",
    "category": {
      "id": 1,
      "name": "Salary",
      "type": "income"
    },
    "createdAt": "2026-01-04T10:30:00"
  },
  "message": "Income added successfully"
}
```

---

### 2. Add Expense (JSON)

**Endpoint:** `POST /api/transactions/expense`

**Description:** Menambahkan pengeluaran via JSON API

**Authentication:** Required

**Content-Type:** `application/json`

**Request Body:**
```json
{
  "amount":  150000.0,
  "date": "2026-01-04",
  "description": "Grocery Shopping",
  "categoryId": 7
}
```

**Success Response:**
```json
{
  "success": true,
  "data": {
    "id": 124,
    "amount": 150000.0,
    "date": "2026-01-04",
    "description": "Grocery Shopping",
    "category":  {
      "id": 7,
      "name": "Food & Dining",
      "type": "expense"
    },
    "createdAt": "2026-01-04T11:00:00"
  },
  "message": "Expense added successfully"
}
```

---

### 3. Get All Transactions (JSON)

**Endpoint:** `GET /api/transactions`

**Description:** Mendapatkan semua transaksi dalam format JSON

**Authentication:** Required

**Response Type:** JSON

**Query Parameters (Optional):**

| Parameter | Type | Description |
|-----------|------|-------------|
| page | Integer | Page number (default: 0) |
| size | Integer | Page size (default: 20) |
| startDate | Date | Filter start date |
| endDate | Date | Filter end date |
| type | String | Filter by type: "income" or "expense" |
| categoryId | Long | Filter by category ID |

**Request Example:**
```http
GET /api/transactions?page=0&size=10&type=expense
Cookie: JSESSIONID=<session_id>
Accept: application/json
```

**Success Response:**
```json
{
  "success": true,
  "data": {
    "transactions": [
      {
        "id": 124,
        "type": "expense",
        "amount": 150000.0,
        "date": "2026-01-04",
        "description": "Grocery Shopping",
        "category": {
          "id": 7,
          "name": "Food & Dining",
          "type": "expense",
          "icon": "fa-utensils",
          "color":  "#dc3545"
        }
      },
      // ... more transactions
    ],
    "pagination": {
      "currentPage": 0,
      "totalPages": 5,
      "totalItems": 100,
      "pageSize": 20
    }
  }
}
```

---

## Request Examples

### cURL Examples

**1. Register User:**
```bash
curl -X POST http://localhost:8080/auth/register \
  -d "username=johndoe&password=secret123&confirmPassword=secret123"
```

**2. Login:**
```bash
curl -X POST http://localhost:8080/auth/login \
  -d "username=johndoe&password=secret123" \
  -c cookies.txt
```

**3. Add Income:**
```bash
curl -X POST http://localhost:8080/income/add \
  -b cookies.txt \
  -d "amount=5000000&date=2026-01-04&description=Salary&categoryId=1"
```

**4. Get Quick Stats (JSON):**
```bash
curl -X GET http://localhost:8080/api/stats/quick \
  -b cookies.txt \
  -H "Accept: application/json"
```

**5. Add Expense (JSON):**
```bash
curl -X POST http://localhost:8080/api/transactions/expense \
  -b cookies.txt \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 150000,
    "date": "2026-01-04",
    "description": "Grocery",
    "categoryId": 7
  }'
```

### JavaScript (Fetch API) Examples

**1. Add Income:**
```javascript
fetch('/api/transactions/income', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  credentials: 'include', // Include session cookie
  body: JSON.stringify({
    amount: 5000000,
    date: '2026-01-04',
    description: 'Monthly Salary',
    categoryId:  1
  })
})
.then(response => response.json())
.then(data => console.log(data))
.catch(error => console. error('Error:', error));
```

**2. Get Quick Stats:**
```javascript
fetch('/api/stats/quick', {
  method: 'GET',
  headers: {
    'Accept': 'application/json'
  },
  credentials: 'include'
})
.then(response => response.json())
.then(data => {
  console.log('Today Expense:', data.data.todayExpense);
  console.log('Savings Rate:', data.data.savingsRate + '%');
})
.catch(error => console.error('Error:', error));
```

### Postman Examples

**1. Setup:**
- Create new collection: "Uangku API"
- Add base URL variable: `{{baseUrl}}` = `http://localhost:8080`

**2. Login Request:**
```
POST {{baseUrl}}/auth/login
Body (x-www-form-urlencoded):
  username: johndoe
  password: secret123

Tests:
  pm.test("Login successful", function() {
    pm.response.to.have.status(302);
  });
```

**3. Add Income Request:**
```
POST {{baseUrl}}/api/transactions/income
Headers:
  Content-Type: application/json
Body (raw JSON):
{
  "amount": 5000000,
  "date": "2026-01-04",
  "description": "Salary",
  "categoryId":  1
}

Tests:
  pm.test("Income added", function() {
    pm.response.to.have.jsonBody("success", true);
  });
```

---

## Status Codes

### Success Codes

| Code | Status | Usage |
|------|--------|-------|
| 200 | OK | Successful GET/PUT/DELETE |
| 201 | Created | Successful POST (resource created) |
| 302 | Redirect | Successful form submission |

### Client Error Codes

| Code | Status | Usage |
|------|--------|-------|
| 400 | Bad Request | Invalid parameters |
| 401 | Unauthorized | Not authenticated |
| 403 | Forbidden | Authenticated but no permission |
| 404 | Not Found | Resource not found |
| 409 | Conflict | Resource conflict (duplicate) |

### Server Error Codes

| Code | Status | Usage |
|------|--------|-------|
| 500 | Internal Server Error | Unexpected server error |
| 503 | Service Unavailable | Service temporarily down |

---

## Rate Limiting

**Current Status:** Not Implemented

**Future Implementation:**
- 100 requests per minute per user
- 1000 requests per hour per user
- Header: `X-RateLimit-Remaining: 95`

---

## API Versioning

**Current Version:** v1 (implicit)

**Future Versioning Strategy:**
```
/api/v1/transactions
/api/v2/transactions
```

---

## Changelog

### Version 1.0.0 (2026-01-04)
- Initial API release
- Authentication endpoints
- Transaction management
- Category management
- Statistics endpoints
- Report generation

---

## Support

**Documentation Issues:**  
Open issue at:  https://github.com/bintangprajudha/Uangku/issues

**API Questions:**  
Contact: [@bintangprajudha](https://github.com/bintangprajudha)

---

**API Version:** 1.0.0  
**Last Updated:** 2026-01-04  
**Status:** Stable