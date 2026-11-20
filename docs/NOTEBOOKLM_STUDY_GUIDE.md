# MediConnect — Comprehensive Architectural & File-by-File Study Guide
**Course:** ADSE 2509 — Advanced Java & JavaFX Development  
**System Name:** MediConnect Hospital Management System  
**Architecture:** Decentralized MVC + DAO Pattern + Service Layer + Modern JavaFX / Scene Builder  
**Ready for Google NotebookLM:** This document is structured as an authoritative reference source for ingestion, summarization, podcast audio generation, and Q&A in [Google NotebookLM](https://notebooklm.google.com/).

---

## 1. Executive Summary & Design Overview

MediConnect is an enterprise-grade clinical desktop application built using **Java 21/23** and **JavaFX**. The application interface reflects a modern corporate aesthetic inspired by Tailwind CSS (Slate 900 / Sky Blue / Emerald / Indigo palettes) and features four core operational modules:
1. **Patient Registration & Records Management** (Add New Records)
2. **Doctor Specialization & Personnel Directory** (Update Details)
3. **Medical Billing & Real-Time Calculation Engine** (Generate Bills & Printable Invoices)
4. **Universal Multi-Entity Search Directory** (Display Information across Patients, Doctors, and Invoices)
5. **Interactive Developer Code Exporter Drawer** (Live FXML layout and pure Java code generation with one-click clipboard copying)

### Core Architectural Patterns:
- **Decentralized Layering:** Strict separation across `models`, `dao`, `services`, `controllers`, `views`, `styles`, and `utils`.
- **Inheritance & Polymorphism:** An abstract `Person` domain model serving as the polymorphic base for `Patient` and `Doctor`.
- **Generic Data Access Object (DAO) Pattern:** A generic contract `GenericDAO<T, ID>` ensuring uniform CRUD operations across SQL Server and memory storage.
- **ACID Transaction Management:** Explicit JDBC transaction boundaries (`setAutoCommit(false)`, `commit()`, `rollback()`) implemented in `BillingService`.
- **Reactive UI Data Binding:** Real-time calculation listeners and `FilteredList<DirectoryItem>` bindings for instantaneous table searching without database roundtrip latency.
- **Modern Java Standards:** Java Text Blocks (`"""`), enhanced switch expressions (`->`), Streams API with lambdas, NIO.2 file handling, and strong type safety.

---

## 2. ADSE 2509 Syllabus Mapping Matrix

| Syllabus Session | Topic Covered | Implementation in MediConnect |
| :--- | :--- | :--- |
| **Session 01: Java Util APIs** | Lists, Sets, Maps, Unmodifiable Collections | `List<Patient>`, `ConcurrentHashMap` in DAOs, `FXCollections.observableArrayList()` in Controllers. |
| **Session 02: Generics** | Generic classes, Generic interfaces, Type parameters | `GenericDAO<T, ID>` defining standard CRUD contract for entities and primary keys. |
| **Session 03: Files & NIO.2** | `Path`, `Files`, `BufferedWriter`, File I/O | `InvoiceReportService.java` saving formatted text receipts into `receipts/` directory. |
| **Session 04: Threads** | Thread lifecycle, Runnable | Asynchronous UI transitions, non-blocking toast animations. |
| **Session 05: Concurrency** | Concurrency safety, synchronization | `ConcurrentHashMap` in memory stores, `AtomicInteger` for thread-safe sequential invoice ID generation. |
| **Session 06: JDBC Architecture** | Connections, `PreparedStatement`, `ResultSet` mapping | `DatabaseConnection.java`, `PatientDAO.java`, `DoctorDAO.java`, `BillDAO.java`, `schema.sql`. |
| **Session 07: Advanced JDBC** | Transaction management (`commit`, `rollback`) | `BillingService.processBillTransaction()` with `setAutoCommit(false)` and error rollbacks. |
| **Session 08: Design Patterns** | DAO Pattern, Service Layer, Factory, Singleton | Centralized `DatabaseConnection`, decoupled DAO and Service architecture. |
| **Session 09: Data Structures** | Tables, Lists, Maps, Key-Value mappings | Dynamic TableView with custom cell factories, quick doctor selection cards. |
| **Session 10: Advanced Features** | Text blocks, enhanced switch, records/DTOs | `InvoiceReportService` text blocks, arrow syntax `switch` in `MainController`, `DirectoryItem` DTO. |

---

## 3. In-Depth File-by-File Technical Breakdown

### 3.1 Domain Models (`com.adse2509.mediconnect.models`)

#### 1. `Person.java`
- **Location:** `src/com/adse2509/mediconnect/models/Person.java`
- **Role:** Abstract base entity representing any human actor in the clinical system.
- **Key Concepts:**
  - `protected String fullName;` and `protected String phone;` — demonstrating encapsulation and protected inheritance.
  - Implements `Serializable` for stream persistence.
  - Parameterised constructor and standard getter/setter encapsulation.
  - Overridden `toString()` method for clean logging.
- **Why It Matters:** Enforces DRY (Don't Repeat Yourself) principles. Both patients and doctors share identification and contact properties.

#### 2. `Patient.java`
- **Location:** `src/com/adse2509/mediconnect/models/Patient.java`
- **Role:** Concrete domain entity representing registered hospital patients.
- **Inherits From:** `Person`
- **Unique Attributes:** `patientId`, `age`, `gender`, `bloodGroup`, `height`, `weight`, `address`, `status`.
- **Key Methods:**
  - Full parameterised constructor passing `fullName` and `phone` up via `super(fullName, phone)`.
  - Default status set to `"Active Record"`.
  - Getters and setters for all clinical metrics.
- **Why It Matters:** Directly maps to `dbo.Patients` in SQL Server and binds to the "Add New Records" UI form.

#### 3. `Doctor.java`
- **Location:** `src/com/adse2509/mediconnect/models/Doctor.java`
- **Role:** Concrete domain entity representing medical staff and specialists.
- **Inherits From:** `Person`
- **Unique Attributes:** `doctorId`, `specialization`, `email`, `status`.
- **Key Methods:**
  - Constructor passing common person fields to `super()`.
  - Getters and setters for specialization and hospital credentials.
- **Why It Matters:** Directly maps to `dbo.Doctors`. Drives both the "Update Details" editor and the "Active Medical Personnel" quick directory sidebar.

#### 4. `Bill.java`
- **Location:** `src/com/adse2509/mediconnect/models/Bill.java`
- **Role:** Financial transaction model representing an itemized patient bill.
- **Attributes:** `billId`, `patientId`, `patientName`, `billDate` (`LocalDate`), `paymentMode`, `consultationFees` (`BigDecimal`), `otherExpenses` (`BigDecimal`), `totalAmount` (`BigDecimal`), `status`.
- **Key Methods:**
  - `recalculateTotal()`: Safe monetary addition using `BigDecimal.add()` to avoid IEEE 754 floating-point inaccuracies.
  - Date initialization defaulting to `LocalDate.now()`.
- **Why It Matters:** Accurate medical billing requires decimal precision; `BigDecimal` guarantees exact currency mathematics for audits and insurance claims.

#### 5. `DirectoryItem.java`
- **Location:** `src/com/adse2509/mediconnect/models/DirectoryItem.java`
- **Role:** Unified Data Transfer Object (DTO) designed for JavaFX `TableView` presentation.
- **Attributes:** `recordId`, `category` (Patient, Doctor, Bill), `primaryInfo`, `detailsMetrics`, `status`.
- **Why It Matters:** Allows heterogeneous domain models (Patients, Doctors, Bills) to be presented uniformly inside a single interactive table without polluting the core domain models.

---

### 3.2 Data Access Layer (`com.adse2509.mediconnect.dao`)

#### 6. `GenericDAO.java`
- **Location:** `src/com/adse2509/mediconnect/dao/GenericDAO.java`
- **Role:** Generic interface establishing the standard CRUD contract (Session 02 Generics).
- **Type Parameters:** `<T, ID>` where `T` is the entity type and `ID` is the primary key type (e.g. `String`).
- **Declared Methods:**
  - `boolean create(T entity) throws SQLException;`
  - `T getById(ID id) throws SQLException;`
  - `List<T> getAll() throws SQLException;`
  - `boolean update(T entity) throws SQLException;`
  - `boolean delete(ID id) throws SQLException;`
- **Why It Matters:** Promotes code reuse, strong compile-time type verification, and architectural consistency across all entity DAOs.

#### 7. `DatabaseConnection.java`
- **Location:** `src/com/adse2509/mediconnect/dao/DatabaseConnection.java`
- **Role:** Central connection factory and driver lifecycle manager.
- **Key Mechanisms:**
  - **Static Initializer Block:** Automatically loads Microsoft SQL Server driver `com.microsoft.sqlserver.jdbc.SQLServerDriver` using reflection `Class.forName()`.
  - **Connection URL:** Configured for `jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=ADSE2509_Hospital;encrypt=false;trustServerCertificate=true;loginTimeout=2;`.
  - **Intelligent Fallback Architecture:** Probes connectivity with a 2-second timeout and caches the result (`cachedServerAvailable`). If SQL Server is running, live JDBC queries are executed; if offline, it falls back to the high-speed in-memory repository seamlessly.
  - **Try-With-Resources Ready:** Provides standard `Connection` objects designed for automatic closure.

#### 8. `PatientDAO.java`
- **Location:** `src/com/adse2509/mediconnect/dao/PatientDAO.java`
- **Role:** Executes SQL statements against `dbo.Patients` and manages patient persistence.
- **Key Mechanisms:**
  - `PreparedStatement` parameterization (`stmt.setString(1, ...)`, `stmt.setInt(3, ...)`), eliminating SQL injection vulnerabilities.
  - `mapRow(ResultSet rs)` private helper for consistent object-relational mapping.
  - Synchronized `ConcurrentHashMap` caching seed patients (`PAT-501`, `PAT-502`, `PAT-503`, `PAT-1095`).

#### 9. `DoctorDAO.java`
- **Location:** `src/com/adse2509/mediconnect/dao/DoctorDAO.java`
- **Role:** Executes SQL operations against `dbo.Doctors`.
- **Key Operations:**
  - `update(Doctor doctor)`: Updates doctor specialization, phone, email, and active status.
  - `getAll()`: Retrieves all doctors ordered alphabetically or by ID.
  - Pre-seeded with specialists: Dr. Sarah Jenkins (Cardiology), Dr. Marcus Vance (Neurology), Dr. Elena Rostova (Pediatrics), Dr. Julian Bashir (Internal Medicine).

#### 10. `BillDAO.java`
- **Location:** `src/com/adse2509/mediconnect/dao/BillDAO.java`
- **Role:** Manages billing persistence and financial aggregation.
- **Key Operations:**
  - SQL `JOIN` query: Links `dbo.Bills` with `dbo.Patients` to fetch denormalized patient names in a single query.
  - `calculateTotalRevenue()`: Uses Java Streams `reduce(BigDecimal.ZERO, BigDecimal::add)` to compute hospital-wide financial totals.

---

### 3.3 Service Layer (`com.adse2509.mediconnect.services`)

#### 11. `BillingService.java`
- **Location:** `src/com/adse2509/mediconnect/services/BillingService.java`
- **Role:** High-level business logic, invoice ID generation, and JDBC transaction coordination.
- **Key Features:**
  - **Thread-Safe ID Sequence:** Uses `AtomicInteger` to issue sequential invoice numbers (`INV-2026-09`, `INV-2026-10`, etc.).
  - **ACID Transaction Management (Session 07):**
    ```java
    connection.setAutoCommit(false); // Begin transaction
    try {
        // Execute insert
        connection.commit(); // Commit on success
    } catch (SQLException ex) {
        connection.rollback(); // Rollback on error
        throw ex;
    } finally {
        connection.setAutoCommit(true);
    }
    ```
  - Calculates combined bills (`consultationFees + otherExpenses`).

#### 12. `DirectoryService.java`
- **Location:** `src/com/adse2509/mediconnect/services/DirectoryService.java`
- **Role:** Aggregates records across Patients, Doctors, and Bills into a unified view.
- **Key Features:**
  - Session 01 & 10 Streams API integration:
    ```java
    return all.stream()
        .filter(item -> item.getRecordId().toLowerCase().contains(term)
                     || item.getPrimaryInfo().toLowerCase().contains(term)
                     || item.getCategory().toLowerCase().contains(term))
        .collect(Collectors.toList());
    ```
  - Enables instant, case-insensitive global searching across all categories simultaneously.

#### 13. `InvoiceReportService.java`
- **Location:** `src/com/adse2509/mediconnect/services/InvoiceReportService.java`
- **Role:** Disk persistence of official printable invoice receipts.
- **Key Features:**
  - **Java Text Blocks (Session 10):** Formats clean ASCII receipt cards with date formatting and itemized breakdowns.
  - **NIO.2 File Handling (Session 03):** Uses `Paths.get()`, `Files.createDirectories()`, and `Files.newBufferedWriter()` to output receipts directly to `receipts/INV-xxxx_Receipt.txt`.

---

### 3.4 User Interface & Presentation Layer

#### 14. `MainController.java`
- **Location:** `src/com/adse2509/mediconnect/controllers/MainController.java`
- **Role:** Master controller binding JavaFX FXML elements to service logic.
- **Key Mechanisms:**
  - Implements `javafx.fxml.Initializable`.
  - **Tab Navigation Engine:** Dynamically shows/hides view panels (`tabPatients`, `tabDoctors`, `tabBilling`, `tabDisplay`) and updates sidebar active CSS classes.
  - **Live Fee Calculator:** Attaches `ChangeListener` to fee text fields so the total bill updates as the user types.
  - **Directory Table Binding:** Configures `PropertyValueFactory` on all table columns and binds an `ObservableList<DirectoryItem>` wrapped in a `FilteredList`.
  - **Toast Animation Engine:** Uses `PauseTransition` and `FadeTransition` for non-blocking notification banners.
  - **Code Exporter Drawer:** Dynamically renders FXML or Java code snippets for the active tab and copies to the OS system clipboard using `Clipboard.getSystemClipboard()`.
  - **Printable Modal Controller:** Controls the invoice receipt popup dialog and triggers the disk print service.

#### 15. `MediConnectApp.java`
- **Location:** `src/com/adse2509/mediconnect/MediConnectApp.java`
- **Role:** Main JavaFX Application launcher.
- **Key Mechanisms:**
  - Inherits from `javafx.application.Application`.
  - Loads `/com/adse2509/mediconnect/views/MainView.fxml` via `FXMLLoader`.
  - Establishes primary Stage dimensions (1140 x 760), min constraints (960 x 640), and stage title.

#### 16. `MainView.fxml`
- **Location:** `src/com/adse2509/mediconnect/views/MainView.fxml`
- **Role:** Declarative Scene Builder-ready user interface layout.
- **Structure:**
  - Top system toolbar with simulated window controls and preview badges.
  - Slate 900 sidebar with branded stethoscope banner and navigation buttons.
  - Central `StackPane` housing all 4 view panels, the floating toast banner, and the modal backdrop.
  - Right-hand slide-out Code Exporter Drawer with syntax tabs and copy action.

#### 17. `mediconnect.css`
- **Location:** `src/com/adse2509/mediconnect/styles/mediconnect.css`
- **Role:** Custom JavaFX stylesheet implementing Tailwind CSS styling.
- **Key Selectors:**
  - `.card-panel`: White rounded cards (`-fx-background-radius: 12px;`) with subtle drop shadows.
  - `.btn-primary`, `.btn-indigo`, `.btn-emerald`: Styled action buttons with distinct hover states.
  - `.table-view`: Clean modern headers, row striping, and sky-blue selection highlighting.
  - `.calc-banner`: Highlighted banner for real-time bill calculations.

---

### 3.5 Database & Test Scripts

#### 18. `schema.sql`
- **Location:** `sql/schema.sql`
- **Role:** Complete Microsoft SQL Server database script.
- **Includes:**
  - `CREATE DATABASE ADSE2509_Hospital;`
  - DDL for `Patients`, `Doctors`, and `Bills` tables with check constraints and foreign keys (`FK_Bills_Patients`).
  - Realistic seed records matching the UI preview.

#### 19. `MediConnectTest.java`
- **Location:** `src/com/adse2509/mediconnect/test/MediConnectTest.java`
- **Role:** Automated unit test suite verifying:
  1. PatientDAO CRUD operations
  2. DoctorDAO update and retrieval
  3. BillingService calculation and transaction rollback logic
  4. Unified directory multi-attribute filtering
  5. NIO.2 text receipt file writing
- **Status:** 5/5 PASSED.

#### 20. `build.bat` & `run.bat`
- **Location:** Project root
- **Role:** One-click compilation and execution scripts that handle spaces in file paths and launch the modular JavaFX runtime.

---

## 4. Key Questions & Viva Voce Defense Points

1. **Why is `BigDecimal` used instead of `double` for billing?**
   - Floating-point representations (`double`) suffer from binary rounding errors (e.g. `0.1 + 0.2 = 0.30000000000000004`). In financial and medical billing systems, `BigDecimal` guarantees arbitrary-precision arithmetic.

2. **How does `BillingService` ensure ACID compliance?**
   - By setting `connection.setAutoCommit(false)`, multiple related SQL statements are grouped into an atomic transaction. If any error occurs, `connection.rollback()` reverts all partial changes, ensuring database consistency.

3. **What is the advantage of the `GenericDAO<T, ID>` interface?**
   - It eliminates repetitive CRUD method signatures, enforces architectural consistency across all entity repositories, and provides compile-time type safety via Java generics.

4. **How does the real-time search table avoid performance lag?**
   - The table binds to a `FilteredList` wrapping an `ObservableList`. When the search field text changes, the filter predicate re-evaluates in memory in under a millisecond, avoiding expensive SQL queries on every keystroke.
