# OpenWealth (Portfolio + Performance Reports)

OpenWealth is a portfolio reporting web application built using a modern,
polyglot, service-oriented architecture. The system includes an Ember.js web
client, a Spring Boot backend for portfolio and holdings data, a Python service
for performance analytics (TWR/IRR), and optional compiled or distributed
processing components for high-performance financial workloads.

---

## Features (MVP)

- Upload positions, cashflows, and pricing data (CSV)
- View consolidated household or account-level holdings
- Run performance calculations:
  - **TWR** — Time-Weighted Return
  - **IRR / XIRR** — Money-Weighted Return
- Export results (CSV; optional PDF)
- Installable **PWA** frontend for quick, app-like access

---

## Technology Overview

### Frontend
- **Ember.js (Octane)** — SPA / PWA client
- Tailwind CSS — styling
- ember-data — JSON:API communication
- ember-simple-auth — authentication (OIDC/JWT)
- ember-service-worker — offline caching / PWA

### Backend (Java)
- **Java 21 + Spring Boot**
  - API Gateway
  - Portfolio Service (holdings, uploads)
  - Reporting Service (TWR/IRR orchestration)
- Spring Web, Spring Security (JWT), Spring Data JPA
- Flyway database migrations

### Analytics Engine (Python)
- **FastAPI**
- NumPy, Pandas (PyArrow optional)
- Endpoints:
  - `/compute/twr`
  - `/compute/irr`

### Optional Compiled Services
(Useful where Go/Rust/Scala experience is desired)
- **Go** — high-performance rollups, Arrow batch scanning
- **Rust** — low-latency analytics or memory-safe batch computations
- **Scala** — Spark-based distributed analytics jobs

### Storage & Data Layer
- **PostgreSQL** — relational portfolio/positions database
- **MinIO or S3** — object storage for exports, Parquet datasets
- **Parquet + Apache Arrow** — columnar data representation for analytics workloads

### Distributed Processing (Optional)
To demonstrate big-data frameworks commonly used in quantitative systems:
- **Apache Spark** — batch analytics over Parquet (Scala or Python)
- **Apache Flink** — real-time pricing/event streams
- **Apache Storm** — streaming computations or alerts
- **Apache Arrow Flight** — high-throughput columnar transport

---

## System Architecture (Layered Design)

OpenWealth follows a clean, layered architecture that separates the user
interface, application logic, domain rules, and infrastructure concerns.  
Additional optional components (Go, Rust, Spark, Flink, Arrow) plug into the
Infrastructure and Analytics layers without adding architectural complexity.

---

### **1. Presentation Layer (Client)**
**Purpose:** User-facing interface for interactions and report viewing.

- Ember.js (Octane)
- Tailwind CSS
- ember-data (JSON:API)
- ember-simple-auth (OIDC/JWT)
- PWA features via ember-service-worker

---

### **2. Application Layer (Service APIs)**
**Purpose:** Coordinates requests, enforces application-level flows, and exposes API endpoints.

Implemented in **Java 21 + Spring Boot**:

- Portfolio API  
  - upload positions, cashflows, prices  
  - fetch holdings (as-of date)  
- Reporting API  
  - orchestrates TWR/IRR computations  
  - handles exports (CSV/PDF optional)

This layer contains:
- Controllers  
- Application services  
- DTOs  
- API authentication/authorization  
- Basic request/response validation  

---

### **3. Domain Layer (Business Logic)**  
**Purpose:** Represents core financial logic and rules.  
Pure, reusable logic that is independent of frameworks.

Contains:
- Portfolio aggregation rules  
- Cashflow alignment logic  
- Position/price reconciliation  
- Report inputs/outputs  
- Domain models (Household, Account, Position, Price, Cashflow, Report)

**Performance Analytics**  
- TWR (Time-Weighted Return)  
- IRR/XIRR (Money-Weighted Return)

Python is used here for numerical heavy-lifting:
- FastAPI
- NumPy, Pandas  
- PyArrow for columnar operations

The Java Application Layer calls Python analytics where needed.

---

### **4. Infrastructure Layer**
**Purpose:** External systems and technical concerns supporting the application.

Includes:

#### **Data Storage**
- PostgreSQL (relational)
- MinIO / S3 (object storage for exports and Parquet datasets)

#### **Optional High-Performance or Compiled Components**
(plug in without changing design)
- Go microservice — fast rollups, Arrow scanning
- Rust microservice — low-latency or memory-safe heavy compute

#### **Distributed Processing (Optional)**
- Spark (batch, Scala or Python)
- Flink (streaming)
- Storm (event-based streams)
- Arrow / Parquet (columnar storage optimizations)
- Arrow Flight (high-throughput data transfer)

These components enhance performance or scalability but do **not** complicate the core architecture.

---

## Simplified High-Level Diagram

```mermaid
flowchart TB
  UI[EmberJS Web Client - PWA]
  API[Spring Boot Portfolio and Reporting APIs - Java]
  PY[FastAPI Analytics Service - Python]
  GO[Go or Rust Performance Service - Optional]
  DSP[Spark Flink Storm Distributed Processing - Optional]
  DB[PostgreSQL Relational Database]
  OBJ[MinIO or Amazon S3 Object Storage]
  COL[Apache Arrow and Parquet Columnar Data]

  %% Client to API
  UI --> API

  %% API <-> Python Analytics
  API --> PY
  PY --> API

  %% API <-> Optional Go or Rust Microservice
  API -. optional .-> GO
  GO -. optional .-> API

  %% API <-> Database
  API --> DB
  DB --> API

  %% Python <-> Object Storage
  PY --> OBJ
  OBJ --> PY

  %% Distributed systems <-> Object Storage
  DSP -. optional .-> OBJ
  OBJ -. optional .-> DSP

  %% Columnar formats relation
  COL --- OBJ
  COL --- PY
  COL -. optional .- GO
  COL -. optional .- DSP
