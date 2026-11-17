# MediConnect: Hospital Management System
**Case Study:** Philadelphia Medical Center (PMC), Great Lakes Region, USA  
**Course:** ADSE 2509 — Developing JavaFX Applications (Advanced Java & JavaFX)  
**Software Development Partner:** OnTime Systems  
**Developer:** Group 3  
**Institution:** Edulink International College / Aptech  
**Edition:** Edition 1 - 2025 (Aptech Limited)

---

## 1. Project Overview
MediConnect is a desktop clinical application developed for Philadelphia Medical Center (PMC). It automates patient registration, doctor schedules, clinical checkup details, treatment billing computations, and instant multi-criteria record queries.

The project strictly follows the **Aptech Project Guide - Developing JavaFX Applications (ADSE 2509)**:
- **Two-Tier Architecture:** JavaFX Presentation Tier (Tier 1) + SQL Server Database Tier (Tier 2).
- **Authentication:** Role-based Staff verification against `dbo.Staff` (matching Flowchart Figure 1.2 and guide reference code).
- **Core Screens:**
  1. **Add New Records:** Patient Demographics and Attending Doctor profiles.
  2. **Update Details:** Consultation room, contact, and doctor availability status.
  3. **Generate Bills:** Real-time billing calculator, discount presets, payment settlement, and printable receipts.
  4. **Display Information:** Unified table search across patients, doctors, checkups, and invoices.
  5. **Exit:** Clean termination and resource cleanup.

---

## 2. Default Login Credentials
When launching the application, the Staff Login Dialog is presented:
- **Staff ID:** `ST01`
- **Password:** `admin123`
*(Alternative staff accounts seeded: `ST02` / `pmc2026`, `ST03` / `desk123`)*

---

## 3. Quick Start (Build & Run)

### Prerequisites
- Windows 10/11 (64-bit)
- OpenJDK 21 or 23 installed
- JavaFX 25 runtime jars (included in `lib/`)

### To Compile:
Open a command prompt or PowerShell in this directory and run:
```cmd
build.bat
```

### To Run the Application:
Double-click or run from command prompt:
```cmd
run.bat
```

### To Run the Headless Automated Test Suite:
```cmd
java -cp "bin;lib/*" com.adse2509.mediconnect.test.MediConnectTest
```
*(Runs 7 automated tests covering staff authentication, doctor CRUD, patient CRUD, checkup visits, billing transactions, directory search, and Java NIO.2 file writing).*

---

## 4. Database Setup (Microsoft SQL Server)
If you have Microsoft SQL Server installed:
1. Open SQL Server Management Studio (SSMS).
2. Open and execute `sql/schema.sql`.
3. The script creates the `ADSE2509_Hospital` database with all 5 tables (`Staff`, `Doctor`, `Patient`, `PatientDetails`, `Bill`) and populates seed data.

> **Note on Offline Resilience:** If Microsoft SQL Server is not running on your machine, MediConnect automatically detects this in 2 seconds and operates in an in-memory fallback repository. Evaluators can test all screens and features without requiring SQL Server.

---

## 5. Documentation Dossier
The complete 12-form Aptech documentation book (SWD/Form No. 1 through Form No. 12) is available in:
- `PROJECT_REPORT_ADSE2509.md` (in this directory)
- `Downloads/MediConnect ADSE2509 Project.md`
