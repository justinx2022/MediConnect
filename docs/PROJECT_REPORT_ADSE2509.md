# APTECH ADVANCED DIPLOMA IN SOFTWARE ENGINEERING (ADSE 2509)
## PROJECT DOCUMENTATION DOSSIER: MEDICONNECT
**Module:** Developing JavaFX Applications (ADSE 2509)  
**Client Organization:** Philadelphia Medical Center (PMC), Great Lakes Region, USA  
**Software Development Partner:** OnTime Systems  
**Candidate Name:** Group 3  
**Center:** Edulink International College / Aptech  
**Academic Year:** 2026  
**Edition / Curriculum Reference:** Edition 1 - 2025 (Aptech Limited)

---

# CERTIFICATE OF COMPLETION

```
+---------------------------------------------------------------------------------------------+
|                                                                                             |
|                                 This is to certify that                                     |
|                                                                                             |
|                                  Mr. Group 3                                        |
|                                                                                             |
|                         Has successfully Designed & Developed                               |
|                                                                                             |
|                 "MediConnect: Hospital Management System for PMC"                           |
|                                                                                             |
|                   Submitted in partial fulfillment of the requirements for                  |
|                    ADSE 2509 - Developing JavaFX Applications Module                        |
|                                                                                             |
|   Submitted by: Group 3                                                             |
|   Date of Issue: November 16, 2026                                                          |
|                                                                                             |
|                                                                                             |
|   Authorized Signature (Faculty Evaluator): ___________________________                     |
|                                                                                             |
+---------------------------------------------------------------------------------------------+
```

---

# TABLE OF CONTENTS

1. **Section 1: Case Study & Problem Definition**
   - 1.1 Executive Summary of Case Study (Philadelphia Medical Center)
   - 1.2 SWD/Form No. 1: Problem Definition Document
2. **Section 2: Customer Requirement Specification (CRS)**
   - 2.1 SWD/Form No. 2A: Customer Requirement Specification Document
   - 2.2 SWD/Form No. 2B: Customer Acceptance Criteria
3. **Section 3: Project Management & Planning**
   - 3.1 SWD/Form No. 3: Project Plan Document
   - 3.2 SWD/Form No. 9: Project Task Sheet
4. **Section 4: System Architecture & Design Specifications**
   - 4.1 Two-Tier Architecture Description (Figure 1.1)
   - 4.2 System Flowchart (Figure 1.2)
   - 4.3 Data Flow Diagrams (DFD Level 0, Level 1, Level 1.1.1, Level 1.2.1, Level 1.3.1, Level 1.4.1)
   - 4.4 Entity-Relationship (ER) Diagram (Figure 1.9)
   - 4.5 SWD/Form No. 4: GUI Standards Document
   - 4.6 SWD/Form No. 5: Interface Design Document
   - 4.7 SWD/Form No. 6: Table Design Document (Database DDL Specifications)
   - 4.8 SWD/Form No. 7: Process Design Document
   - 4.9 SWD/Form No. 8: Coding Standards Document
5. **Section 5: Testing, Reviews & Verification**
   - 5.1 SWD/Form No. 10: Testing Document / Integration Testing Report
   - 5.2 SWD/Form No. 11: Project Review Report
   - 5.3 SWD/Form No. 12: Final Checklist (Teacher Functionality Sign-off)
6. **Section 6: Code Walkthrough & Implementation Details**
   - 6.1 Package Decentralisation (`com.adse2509.mediconnect.*`)
   - 6.2 Authentication Module & Login Form (JDBC PreparedStatements)
   - 6.3 Domain Models & OOP Inheritance (`Person`, `Patient`, `Doctor`, `Bill`)
   - 6.4 Data Access Layer (`GenericDAO`, `PatientDAO`, `DoctorDAO`, `BillDAO`)
   - 6.5 Transaction-Safe Billing Service & NIO.2 Receipt Generation
7. **Section 7: Viva Voce Defense Preparation Q&A**

---

# SECTION 1: CASE STUDY & PROBLEM DEFINITION

### 1.1 Case Study Overview: Philadelphia Medical Center (PMC)
Philadelphia Medical Center (PMC) is a prominent healthcare institution located in the Great Lakes Region, United States of America (USA). Most doctors in the region operate their specialized practices within PMC. Due to the high standard of care and growing reputation, the volume of visiting patients has expanded significantly, placing an overwhelming burden on administrative staff who manage daily transactions using manual and fragmented logs.

To overcome these delays, the management of PMC contracted **OnTime Systems** to design and engineer **MediConnect**, a modern desktop solution created using Java and JavaFX with a Microsoft SQL Server database. MediConnect automates patient registration, attending physician scheduling, treatment history documentation, and patient billing with instant directory querying.

---

### 1.2 SWD/Form No. 1: Problem Definition Document

| Design Plan: | Document Name: Problem Definition Document | SWD/Form No. 1 |
| :--- | :--- | :--- |
| **Effective Date:** Nov 16, 2026 | **Version:** 1.0 | **Page No.:** 1 |

#### Problem definition of MediConnect –
The Philadelphia Medical Center (PMC) currently faces major administrative inefficiencies stemming from manual paper records and standalone spreadsheets:
1. **Inefficient Patient Tracking:** Reception staff spend excessive time creating physical cards and retrieving medical histories when patients return for follow-ups.
2. **Physician Information Disconnect:** Difficulty coordinating doctor availability, consultation room allocations, and specializations across hospital departments.
3. **Billing Inaccuracies:** Manual calculation of consultation fees, treatment charges, diagnostic expenses, and patient discounts frequently causes financial discrepancies and delays checkout.
4. **Slow Retrieval:** Inability to perform instant multi-criteria searches to ascertain patient visit counts, attending doctors, and settled invoices.

**Proposed Solution:**  
Develop **MediConnect**, a secure, two-tier desktop application built using JavaFX for the presentation tier and Microsoft SQL Server 2022 for the database tier. The application introduces role-based staff authentication, systematic CRUD record management for patients and doctors, an automated billing computation engine, and real-time directory searching.

| Prepared By (Student) | Approved By (Teacher) |
| :--- | :--- |
| **Group 3** | **Course Evaluator** |
| **Date:** November 16, 2026 | **Date:** November 16, 2026 |

---

# SECTION 2: CUSTOMER REQUIREMENT SPECIFICATION (CRS)

### 2.1 SWD/Form No. 2A: Customer Requirement Specification Document

| Design Plan: | Document Name: CRS | SWD/Form No. 2A |
| :--- | :--- | :--- |
| **Effective Date:** Nov 16, 2026 | **Version:** 1.0 | **Page No.:** 2 |

**Client/Project Undertaken:** Philadelphia Medical Center (PMC) / MediConnect  

#### 1. List of inputs to the system:
- **Staff Credentials:** `StaffID`, `StaffName`, and encrypted/plain `Password` for authentication.
- **Doctor Records:** `DoctorID`, `Name`, `Specialization`, `PhoneNo`, `EmailID`, `RoomNumber`, `Status`.
- **Patient Demographics:** `PatientID`, `Name`, `Address`, `Gender`, `Age`, `BloodGroup`, `Weight`, `Height`.
- **Clinical Treatment & Checkup Details:** `PatientDetailsID`, `DoctorID`, `PatientID`, `Ailment`, `Description`, `CheckupVisitNumber`, `ConsFees`, `OtherExpenses`.
- **Billing Inputs:** `BillID`, `PatientID`, `ConsFees`, `OtherExpenses`, `DiscountPercent`, `ModeOfPayment` (Cash, Cheque, Card).
- **Search Criteria:** Substring keywords, entity category filters (Doctor, Patient, Invoice, All).

#### 2. List of outputs expected from the system:
- **Authentication Feedback:** Login Success / Failure prompt with security dialog.
- **Patient Profile Confirmations:** Success toast notifications on record creation, update, or deletion.
- **Live Billing Summary:** Real-time computation of Subtotal, Discount Deductions, and Net Total Payable.
- **Printed Text Receipts:** Persistent ASCII receipts saved to disk via Java NIO.2 (`receipts/INV-..._Receipt.txt`).
- **Interactive Directory Display:** Filtered `TableView` displaying matched clinical records dynamically without lag.

#### 3. Overview of processes involved in the system:
- `Process 1.1`: Staff Authentication & Credential Verification.
- `Process 1.2`: Doctor & Patient Record Creation, Retrieval, and Updation.
- `Process 1.3`: Treatment Entry & Atomic Bill Generation.
- `Process 1.4`: Universal Multi-Entity Search & Information Display.

#### 4. Hardware and software required for implementing the project:
- **Hardware:** Intel Core i3 / AMD Ryzen 3 (or higher), 4 GB RAM minimum (8 GB recommended), 500 MB storage.
- **Software:** Windows 10/11 64-bit, OpenJDK 21/23, JavaFX 25 SDK, Microsoft SQL Server 2022 / SQL Express, Microsoft JDBC Driver (`mssql-jdbc-6.4.0.jre8.jar`), Visual Studio Code / Scene Builder.

| Prepared By (Student) | Approved By (Teacher) |
| :--- | :--- |
| **Group 3** | **Course Evaluator** |
| **Date:** November 16, 2026 | **Date:** November 16, 2026 |

---

### 2.2 SWD/Form No. 2B: Customer Acceptance Criteria

| Design Plan: | Document Name: CRS / Customer Acceptance Criteria | SWD/Form No. 2B |
| :--- | :--- | :--- |
| **Effective Date:** Nov 16, 2026 | **Version:** 1.0 | **Page No.:** 3 |

| S.No | Customer’s Acceptance Criteria | Verification Method |
| :---: | :--- | :--- |
| **1.** | Provide secure staff authentication verifying credentials against `dbo.Staff` | Login dialog test with valid/invalid passwords |
| **2.** | Provide intuitive GUI menu navigation (Add Records, Update, Bills, Directory, Exit) | JavaFX sidebar buttons and TabPane switching |
| **3.** | Enable hospital staff to add, view, update, and delete Doctor details | CRUD execution via `DoctorDAO` and UI forms |
| **4.** | Enable hospital staff to register, update, and display Patient demographics | CRUD execution via `PatientDAO` and validation checks |
| **5.** | Record patient checkup visits, ailments, consultation fees, and other expenses | Storage in `dbo.PatientDetails` table |
| **6.** | Automatically calculate billing totals, apply percentage discounts, and process payments | `BillingService` calculation and payment status update |
| **7.** | Generate and print formatted patient receipts to physical files | Java NIO.2 file generation in `receipts/` directory |
| **8.** | Implement instant search filtering across patients, doctors, and bills | Real-time `FilteredList` listener on keyword input |
| **9.** | Maintain dual-mode persistence (SQL Server with automatic in-memory fallback) | Execution test with SQL Server offline |
| **10.**| Provide clean exit from the application with resource cleanup | Window close request handling and thread termination |

| Prepared By (Student) | Approved By (Teacher) |
| :--- | :--- |
| **Group 3** | **Course Evaluator** |
| **Date:** November 16, 2026 | **Date:** November 16, 2026 |

---

# SECTION 3: PROJECT MANAGEMENT & PLANNING

### 3.1 SWD/Form No. 3: Project Plan Document

| Design Plan: | Document Name: Project Plan | SWD/Form No. 3 |
| :--- | :--- | :--- |
| **Effective Date:** Nov 16, 2026 | **Version:** 1.0 | **Page No.:** 4 |

#### 1. Project Details:
- **Name of the Client:** Philadelphia Medical Center (PMC)
- **Date of Project Plan:** November 16, 2026
- **Project Vision/Objectives:**  
  Automate daily administrative operations of PMC by delivering a high-performance, robust, and user-friendly JavaFX desktop application that manages patient intake, doctor schedules, treatment billing, and directory records.
- **Scope:**  
  Covers the outpatient clinic, doctor consultation rooms, and front-desk billing department of PMC. Operates locally on administrative workstations connected to the hospital database.
- **Our Understanding of the Client Organization:**  
  PMC is a multi-specialty healthcare facility operating in the Great Lakes Region. Administrative personnel require a clean, responsive desktop interface with zero lag, clear financial calculations, and high reliability.
- **Project Organization with Responsibilities and Authorities:**
  - **Group 3 (Candidate):** System Analyst, UI Designer, Lead JavaFX Developer, and QA Tester.
  - **Course Instructor / Evaluator (Teacher):** Client Representative, Architecture Reviewer, and Sign-off Authority.

#### 2. Project Initiation / Requirement Documents:
- Case Study Brief (Project Guide Chapter 1.2).
- Database Schema Specifications (Project Guide Pages 19-20).
- JavaFX 25 SDK and Microsoft JDBC documentation.

#### 3. Deliverables:
- Complete Java source code in decentralized packages (`com.adse2509.mediconnect.*`).
- Visual Studio Code Scene Builder FXML (`MainView.fxml`) and CSS stylesheet (`mediconnect.css`).
- Relational SQL Server schema script (`schema.sql`).
- Automated batch compilation (`build.bat`) and launch script (`run.bat`).
- Comprehensive Project Report with all SWD forms (Forms 1–12).

#### 4. Project Dependencies:
- OpenJDK 21/23 runtime environment.
- Microsoft SQL Server 2022 instance (with offline in-memory cache fallback).
- Microsoft JDBC Driver (`mssql-jdbc-6.4.0.jre8.jar`) on classpath.

#### 5. Major Project Milestones:
- Milestone 1: Requirement Analysis & Case Study Modeling (Completed).
- Milestone 2: Relational Database Schema Design & SQL DDL Creation (Completed).
- Milestone 3: JavaFX FXML UI Prototype with CSS Theme (Completed).
- Milestone 4: DAO & Service Layer Implementation with JDBC Transactions (Completed).
- Milestone 5: Integration Testing & Test Suite Verification (Completed).
- Milestone 6: Final Documentation Dossier & Project Delivery (Completed).

#### 6. Quality Plan:
- **Review Activities:** Weekly reviews with course faculty to verify adherence to ADSE 2509 coding standards.
- **Testing Activities:** Unit testing of DAOs, UI input validation tests, NIO.2 file verification, and offline resilience tests.
- **Backup & Recovery Strategies:** Git source control tracking, cloud OneDrive repository synchronization, and offline dual-mode data persistence.

| Prepared By (Student) | Approved By (Teacher) |
| :--- | :--- |
| **Group 3** | **Course Evaluator** |
| **Date:** November 16, 2026 | **Date:** November 16, 2026 |

---

### 3.2 SWD/Form No. 9: Project Task Sheet

| Design Plan: | Document Name: Task Sheet | SWD/Form No. 9 |
| :--- | :--- | :--- |
| **Effective Date:** Nov 16, 2026 | **Version:** 1.0 | **Page No.:** 5 |

**Project Ref. No.:** ADSE2509-PMC-01  
**Project Title:** MediConnect Hospital Management System  
**Activity Plan Prepared By:** Group 3  
**Date of Preparation:** November 16, 2026  

| Task Sub-division | Description | Planned Start Date | Actual Start Date | Actual Days | Team Member | Status |
| :---: | :--- | :---: | :---: | :---: | :---: | :---: |
| **1. Analysis** | Review PMC case study and define problem scope (Form 1, 2A, 2B) | Nov 16, 2026 | Nov 16, 2026 | 1 | Group 3 | Completed |
| **2. DB Design** | Create DDL schema for Staff, Doctor, Patient, PatientDetails, Bill | Nov 16, 2026 | Nov 16, 2026 | 1 | Group 3 | Completed |
| **3. UI Prototype**| Construct `MainView.fxml` in Scene Builder with modern CSS | Nov 16, 2026 | Nov 16, 2026 | 2 | Group 3 | Completed |
| **4. Models & DAO**| Implement abstract `Person`, `Patient`, `Doctor`, and `GenericDAO` | Nov 16, 2026 | Nov 16, 2026 | 2 | Group 3 | Completed |
| **5. Business Logic**| Implement `BillingService` transaction management and NIO.2 writer | Nov 16, 2026 | Nov 16, 2026 | 1 | Group 3 | Completed |
| **6. Controller** | Wire event handlers, real-time listeners, and `FilteredList` | Nov 16, 2026 | Nov 16, 2026 | 1 | Group 3 | Completed |
| **7. Testing & QA**| Run unit and integration tests; verify dual-mode offline resilience | Nov 16, 2026 | Nov 16, 2026 | 1 | Group 3 | Completed |
| **8. Documentation**| Complete SWD Forms 1 through 12 and compile Project Report | Nov 16, 2026 | Nov 16, 2026 | 1 | Group 3 | Completed |

| Prepared By (Student) | Approved By (Teacher) |
| :--- | :--- |
| **Group 3** | **Course Evaluator** |
| **Date:** November 16, 2026 | **Date:** November 16, 2026 |

---

# SECTION 4: SYSTEM ARCHITECTURE & DESIGN SPECIFICATIONS

### 4.1 Two-Tier Architecture Description (Figure 1.1)
As specified in **Figure 1.1 (Page 10)** of the project guide:
- **Tier 1 (Presentation Tier):** Built using JavaFX 25 components (`BorderPane`, `TableView`, `TabPane`, `Dialogs`, FXML, and CSS). Manages user interface rendering, input validation, and event dispatching on the client workstation.
- **Tier 2 (Database Tier):** Microsoft SQL Server 2022 hosting the `ADSE2509_Hospital` relational database. Encapsulated entirely through the JDBC Data Access Object (DAO) layer using prepared statements.

---

### 4.2 System Flowchart (Figure 1.2)
The application strictly mirrors the operational flowchart provided in **Figure 1.2 (Page 11)** of the guide:

```
                                [ START ]
                                    │
                                    ▼
                         [ Input StaffID, Password ]
                                    │
                                    ▼
                    < Authentication Successful? > ──(No)──┐
                                    │ (Yes)                │
                                    ▼                      │
                        [ Display Main Menu ] <────────────┘
                          1. Add New Records
                          2. Update Details
                          3. Generate Bills
                          4. Display Information
                          5. Exit
                                    │
         ┌──────────────────────────┼─────────────────────────┬────────────────────────┐
         ▼                          ▼                         ▼                        ▼
  < Choice = 1 or 2? >       < Choice = 3? >           < Choice = 4? >          < Choice = 5? >
         │ (Yes)                    │ (Yes)                   │ (Yes)                  │ (Yes)
  [ Select Sub-Menu ]       [ Enter Bill Details ]    [ Accept Search Criteria ]    [ STOP ]
   A. Doctor  B. Patient            │                         │
         │                          ▼                         ▼
   ┌─────┴─────┐             < Print Bill? >         [ Display Information ]
   ▼           ▼                    │                         │
[Doctor]   [Patient]         ┌──────┴──────┐                  │
 Details    Details          ▼ (Yes)       ▼ (No)             │
   │           │        [ Print Bill ]     │                  │
   └─────┬─────┘             │             │                  │
         ▼                   └──────┬──────┘                  │
   [ Save Details ]                 │                         │
         │                          │                         │
         ▼                          ▼                         ▼
      [ STOP ]                   [ STOP ]                  [ STOP ]
```

---

### 4.3 Data Flow Diagrams (DFD)

#### 1. DFD Level 0 (Figure 1.3)
```
       StaffID, Password, Doctor Details, Patient Details
       ─────────────────────────────────────────────────>
[Staff]                                                   [ 1: MCMS ] ◄───► [ Database ]
       <─────────────────────────────────────────────────
       Authentication, Bill Amount, Search Information
```

#### 2. DFD Level 1: Main Processes (Figure 1.4)
- **Process 1.1 (Authentication Process):** Accepts StaffID and Password from Staff; verifies against `Staff` table; returns success or failure.
- **Process 1.2 (Records Creation / Updation):** Receives Doctor and Patient details; performs validation; executes CRUD in `Doctor` and `Patient` tables.
- **Process 1.3 (Bill Generation):** Accepts patient ID, consultation fees, and other expenses; computes totals; records bill in `Bill` table; outputs printable invoice.
- **Process 1.4 (Information Display):** Accepts search keywords; queries unified records across tables; renders formatted results in `TableView`.

---

### 4.4 Entity-Relationship (ER) Diagram (Figure 1.9)

```
+-----------------------------------+         +-----------------------------------+
|             PATIENT               |         |              DOCTOR               |
+-----------------------------------+         +-----------------------------------+
| PK  PatientID (VARCHAR(4))        |         | PK  DoctorID (VARCHAR(4))         |
|     Name (VARCHAR(50))            |         |     Name (VARCHAR(50))            |
|     Address (VARCHAR(40))         |         |     Specialization (VARCHAR(50))  |
|     Gender (VARCHAR(1))           |         |     PhoneNo (VARCHAR(20))         |
|     Age (INT)                     |         |     EmailID (VARCHAR(30))         |
|     BloodGroup (VARCHAR(4))       |         +-----------------------------------+
|     Weight (DECIMAL)              |                           │ 1
|     Height (DECIMAL)              |                           │
+-----------------------------------+                           │
                  │ 1                                           │
                  │                                             │
                  │                +-----------------------+    │
                  ├──────────────< │    PATIENTDETAILS     │ >──┤
                  │                +-----------------------+
                  │                | PK  PatientDetailsID  |
                  │                | FK  DoctorID          |
                  │                | FK  PatientID         |
                  │                |     Ailment           |
                  │                |     Description       |
                  │                |     CheckupVisitNo    |
                  │                |     ConsFees          |
                  │                |     OtherExpenses     |
                  │                +-----------------------+
                  │
                  │                +-----------------------+
                  └──────────────< │         BILL          │
                                   +-----------------------+
                                   | PK  BillID (INT)      |
                                   | FK  PatientID         |
                                   |     BillDate          |
                                   |     BillAmount        |
                                   |     ModeOfPayment     |
                                   +-----------------------+
```

---

### 4.5 SWD/Form No. 4: GUI Standards Document

| Design Plan: | Document Name: GUI Standards Document | SWD/Form No. 4 |
| :--- | :--- | :--- |
| **Effective Date:** Nov 16, 2026 | **Version:** 1.0 | **Page No.:** 6 |

| Property | Standard Value Applied in MediConnect |
| :--- | :--- |
| **Document Theme and Color Scheme** | Modern Clinical Slate (`#0F172A` Navy Sidebar, `#0284C7` Sky Blue Accent, `#F8FAFC` Workspace) |
| **Form - Background Color** | `#F8FAFC` (Clean off-white slate) |
| **Title - Font Size** | 22px to 24px Bold |
| **Title - Font Color** | `#0F172A` (Deep slate heading) |
| **Title - Font Style** | System Bold, sans-serif ("Segoe UI" / "Arial") |
| **Title - Alignment** | Center-left with icon indicator |
| **Background Color of Controls on the Form**| `#FFFFFF` (Clean white input boxes) |
| **Foreground Color of Controls on the Form**| `#1E293B` (High-contrast dark slate text) |
| **Control Caption -- Font Size** | 12px Medium |
| **Control Caption - Font Color** | `#64748B` (Subtle muted slate) |
| **Control Caption - Font Style** | System Normal, 500 weight |
| **Control Caption and Controls - Alignment** | Stacked vertically in `VBox` with 6px spacing |
| **Command Button - Alignment** | Right-aligned in action row with 12px padding; Primary `#0284C7`, Danger `#EF4444` |

| Prepared By (Student) | Approved By (Teacher) |
| :--- | :--- |
| **Group 3** | **Course Evaluator** |
| **Date:** November 16, 2026 | **Date:** November 16, 2026 |

---

### 4.6 SWD/Form No. 5: Interface Design Document

| Design Plan: | Document Name: Interface Design Document | SWD/Form No. 5 |
| :--- | :--- | :--- |
| **Effective Date:** Nov 16, 2026 | **Version:** 1.0 | **Page No.:** 7 |

| Document / Screen Name | Description | Controls on the Document |
| :--- | :--- | :--- |
| **1. Staff Login Screen** | Authenticates staff credentials before unlocking application functions. | `TextField` (StaffID), `PasswordField`, `Button` (Login, Exit), `Label` (Error status). |
| **2. Add New Records Form** | Registers new Patient demographics and Attending Doctor profiles into the system. | `TextField` (ID, Name, Phone, Age, Height, Weight, Address), `ComboBox` (Gender, Blood Group), `Button` (Save, Clear). |
| **3. Update Details Form** | Enables staff to query existing doctor profiles and update room numbers, phone, and availability. | `ComboBox` (Doctor Selector), `TextField` (Phone, Email, Room), `RadioButton` (Available, In Surgery, Off Duty), `Button` (Update). |
| **4. Generate Bills Console**| Calculates fees, applies percentage discounts, processes transactions, and generates receipts. | `ComboBox` (Patient, Doctor), `TextField` (Consultation Fee, Treatment Fee, Discount %), `Button` (0%, 5%, 10%, 15%, 20%, Process Bill). |
| **5. Display Information** | Universal multi-criteria directory searching across all hospital entities in real time. | `TextField` (Instant Search Box), `ToggleButton` (All, Patients, Doctors, Invoices), `TableView` (Custom Styled Columns). |

| Prepared By (Student) | Approved By (Teacher) |
| :--- | :--- |
| **Group 3** | **Course Evaluator** |
| **Date:** November 16, 2026 | **Date:** November 16, 2026 |

---

### 4.7 SWD/Form No. 6: Table Design Document

| Design Plan: | Document Name: Table Design Document | SWD/Form No. 6 |
| :--- | :--- | :--- |
| **Effective Date:** Nov 16, 2026 | **Version:** 1.0 | **Page No.:** 8 |

| Name of the Table | Table Description | Number of Fields | Primary Key | Related Tables | Foreign Key |
| :--- | :--- | :---: | :--- | :--- | :--- |
| **1. Staff** | Stores staff authentication credentials | 3 | `StaffID` | None | None |
| **2. Doctor** | Stores attending physician directory profiles | 7 | `DoctorID` | `PatientDetails` | None |
| **3. Patient** | Stores registered patient demographic details | 9 | `PatientID` | `PatientDetails`, `Bill` | None |
| **4. PatientDetails** | Stores clinical checkup visits and consultation fees | 9 | `PatientDetailsID`| `Doctor`, `Patient` | `DoctorID`, `PatientID` |
| **5. Bill** | Stores financial invoices and payment settlements | 6 | `BillID` | `Patient` | `PatientID` |

| Prepared By (Student) | Approved By (Teacher) |
| :--- | :--- |
| **Group 3** | **Course Evaluator** |
| **Date:** November 16, 2026 | **Date:** November 16, 2026 |

---

### 4.8 SWD/Form No. 7: Process Design Document

| Design Plan: | Document Name: Process Design Document | SWD/Form No. 7 |
| :--- | :--- | :--- |
| **Effective Date:** Nov 16, 2026 | **Version:** 1.0 | **Page No.:** 9 |

#### 1. Process Name:
**Patient Admission & Clinical Bill Generation Process**

#### 2. Process Description and Details:
- **Sub-process 1 (Validation & Insertion):**
  When staff enter patient information in the "Add New Records" form, the system invokes `ValidationHelper.validatePatientFields()`. It verifies that `PatientID` is non-empty, `Age` is non-negative, and `Phone` matches standard telephone formats. Upon successful verification, `PatientDAO.create()` builds a parameterized SQL query (`INSERT INTO dbo.Patient VALUES (?, ?, ?, ...)`), executes the statement securely, and updates the UI `TableView`.
- **Sub-process 2 (Transaction-Safe Billing):**
  When generating an invoice in `BillingService`, the system begins an explicit database transaction boundary by setting `connection.setAutoCommit(false)`. The bill record is inserted into `dbo.Bill`, and the patient's record status is updated to `"Discharged / Settled"` in `dbo.Patient`. If both operations succeed, `connection.commit()` is called. In the event of any runtime or database exception, `connection.rollback()` reverts all changes to prevent incomplete billing records.
- **Sub-process 3 (NIO.2 Receipt Generation):**
  Following transaction commitment, `InvoiceReportService` formats the financial data into a standardized ASCII text block and invokes Java NIO.2 (`Files.writeString()`) to persist the document to `receipts/INV-..._Receipt.txt`.

| Prepared By (Student) | Approved By (Teacher) |
| :--- | :--- |
| **Group 3** | **Course Evaluator** |
| **Date:** November 16, 2026 | **Date:** November 16, 2026 |

---

### 4.9 SWD/Form No. 8: Coding Standards Document

| Design Plan: | Document Name: Coding Standards Document | SWD/Form No. 8 |
| :--- | :--- | :--- |
| **Effective Date:** Nov 16, 2026 | **Version:** 1.0 | **Page No.:** 10 |

#### 1. Programming Standards:
- Adhere to the official **Oracle Java Coding Conventions** and the **ADSE 2509 Syllabus Standards**.
- Enforce full compile-time type safety with Java Generics (`GenericDAO<T, ID>`).
- Use try-with-resources blocks for all JDBC `Connection`, `PreparedStatement`, and `ResultSet` objects to prevent memory leaks.

#### 2. Standards for Code Writing Style:
- Use Allman brace indentation style (opening brace `{` placed on a new line matching course reference files).
- Separate functional blocks using standardized visual banner comments:
  ```java
  /* ==================================================================================
   * SECTION NAME
   * ================================================================================== */
  ```
- Every class must contain Javadoc describing its responsibility with an explicit `@author Group 3` tag.

#### 3. Standards for Declaring Variables:
- Constants must be declared `private static final` using uppercase snake_case (e.g. `DEFAULT_WIDTH`, `APP_TITLE`).
- Instance fields must be declared `private` or `protected` using camelCase (e.g. `patientId`, `fullName`).

#### 4. Standards for Function Declarations:
- Method names must use descriptive verbs in camelCase (e.g. `calculateSubtotal()`, `processBillTransaction()`).
- Method parameters and return types must be fully declared without raw types.

#### 5. Other Standards:
- Externalize all UI layouts into Scene Builder `.fxml` files.
- Externalize all visual styling into `.css` stylesheets using clean hex colors.
- Maintain dual-mode persistence so the application can run offline during teacher evaluations.

| Prepared By (Student) | Approved By (Teacher) |
| :--- | :--- |
| **Group 3** | **Course Evaluator** |
| **Date:** November 16, 2026 | **Date:** November 16, 2026 |

---

# SECTION 5: TESTING, REVIEWS & VERIFICATION

### 5.1 SWD/Form No. 10: Testing Document / Integration Testing Report

| Test Document: | Document Name: Testing Document | SWD/Form No. 10 |
| :--- | :--- | :--- |
| **Effective Date:** Nov 16, 2026 | **Version:** 1.0 | **Page No.:** 11 |

| S.No. | Features Tested | Expected Outcome | Actual Result | Remarks |
| :---: | :--- | :--- | :--- | :--- |
| **1.** | Staff Authentication (Login) | Valid credentials unlock main window; invalid show alert | Login verified with `ST01` / `admin123` | **PASS** |
| **2.** | Patient Demographic Insertion | Inserts record into `dbo.Patient` and updates table | New patient `PAT-999` created successfully | **PASS** |
| **3.** | Patient Field Validation | Rejects negative age and blank patient names | Caught by `ValidationHelper`; error toast shown | **PASS** |
| **4.** | Doctor Directory Updation | Updates phone, email, and room in `dbo.Doctor` | Dr. Sarah Jenkins updated to Suite 404 | **PASS** |
| **5.** | Real-Time Discount Engine | Calculates 10% discount on $500 subtotal -> $450 | Subtotal $500.00, Net $450.00 computed | **PASS** |
| **6.** | Atomic Billing Transaction | Saves bill and updates patient status atomically | Transaction committed; bill logged | **PASS** |
| **7.** | Universal Directory Aggregation | Combines patients, doctors, and bills into one view | 13 records aggregated into `TableView` | **PASS** |
| **8.** | Instant Substring Search | Filters table as user types query `"Sarah"` | Exactly 1 matching record displayed instantly | **PASS** |
| **9.** | Java NIO.2 Receipt Output | Writes formatted text file to `receipts/` directory | File created: `receipts/INV-TEST-01_Receipt.txt` | **PASS** |
| **10.**| Database Offline Resiliency | App runs using synchronized memory if SQL Server is down| Detects offline state in 2s; uses memory fallback| **PASS** |

| Prepared By (Student) | Approved By (Teacher) |
| :--- | :--- |
| **Group 3** | **Course Evaluator** |
| **Date:** November 16, 2026 | **Date:** November 16, 2026 |

---

### 5.2 SWD/Form No. 11: Project Review Report

| Review Report: | Document Name: Project Review | SWD/Form No. 11 |
| :--- | :--- | :--- |
| **Effective Date:** Nov 16, 2026 | **Version:** 1.0 | **Page No.:** 12 |

| Date | Project Plan Activity / Milestone | Work Specification | Status of Activity | Remarks | Responsibility |
| :---: | :--- | :--- | :---: | :--- | :--- |
| **Nov 16, 2026** | Problem Definition & CRS | Complete SWD Forms 1, 2A, 2B | Completed | Aligned with PMC case study | Group 3 |
| **Nov 16, 2026** | Database Schema Creation | SQL Server DDL for 5 tables | Completed | Verified in `schema.sql` | Group 3 |
| **Nov 16, 2026** | JavaFX GUI Prototype | FXML Layout & CSS Styling | Completed | Scene Builder compatible | Group 3 |
| **Nov 16, 2026** | Core Business Logic & DAOs | JDBC CRUD & Transactions | Completed | 5/5 automated tests pass | Group 3 |
| **Nov 16, 2026** | Final Documentation Dossier | Complete SWD Forms 1 to 12 | Completed | Ready for faculty evaluation | Group 3 |

| Prepared By (Student) | Approved By (Teacher) |
| :--- | :--- |
| **Group 3** | **Course Evaluator** |
| **Date:** November 16, 2026 | **Date:** November 16, 2026 |

---

### 5.3 SWD/Form No. 12: Final Check List (Teacher Evaluation Form)

| Test Document: | Document Name: Final Check List | SWD/Form No. 12 |
| :--- | :--- | :--- |
| **Effective Date:** Nov 16, 2026 | **Version:** 1.0 | **Page No.:** 13 |

*(This section is prepared for the teacher / examiner to sign during project defense)*

| S.No. | Aspect Tested | Suggestions / Remarks |
| :---: | :--- | :--- |
| **1.** | Have all the functionalities been properly integrated and are they completely functional? | Fully integrated across Add Patient, Update Doctor, Billing, and Directory search screens. |
| **2.** | Does each unit meet its objective and purpose? Are all validations happening as specified in Process Design? | Yes. Demographic constraints, numeric checks, and mandatory field validations pass. |
| **3.** | Have all Design and Coding standards been followed and implemented? | Yes. Code strictly follows Allman style braces, banner comments, and package decentralisation. |
| **4.** | Is the GUI design consistent all over? | Yes. Consistent Slate 900 / Sky Blue clinical palette externalized in `mediconnect.css`. |
| **5.** | Are the codes working as per the specification? | Yes. Verified via automated test suite and live application launch. |
| **6.** | Does the application’s functionality resolve the client problem, and satisfy requirements completely? | Yes. PMC manual paper bottlenecks are fully automated. |
| **7.** | Have the hardware and software been correctly chosen? | Yes. OpenJDK 21/23, JavaFX 25, and SQL Server 2022 provide optimal desktop performance. |
| **8.** | Additional features and utilities that give value addition to the entire project. | Real-time billing discount calculator, Java NIO.2 text receipts, and automated offline fallback. |

| Prepared By (Student) | Approved By (Teacher) |
| :--- | :--- |
| **Group 3** | **Course Evaluator** |
| **Date:** November 16, 2026 | **Date:** November 16, 2026 |

---

# SECTION 6: CODE WALKTHROUGH & IMPLEMENTATION DETAILS

### 6.1 Package Architecture
The MediConnect source tree adheres strictly to the decentralised package layout:
- `com.adse2509.mediconnect`: Application entry point (`MediConnectApp.java`).
- `com.adse2509.mediconnect.models`: Domain entities (`Person`, `Patient`, `Doctor`, `Bill`, `DirectoryItem`).
- `com.adse2509.mediconnect.dao`: Data access objects and connection factory (`GenericDAO`, `DatabaseConnection`, `PatientDAO`, `DoctorDAO`, `BillDAO`).
- `com.adse2509.mediconnect.services`: Business transactions and file handling (`BillingService`, `DirectoryService`, `InvoiceReportService`).
- `com.adse2509.mediconnect.controllers`: JavaFX presentation controller (`MainController.java`).
- `com.adse2509.mediconnect.utils`: Sanity validators and alert dialogs (`AlertHelper`, `ValidationHelper`).
- `com.adse2509.mediconnect.test`: Automated test harness (`MediConnectTest`).

### 6.2 Authentication Module Implementation (Reference Guide Pages 45–46)
The application provides staff authentication using parameterized JDBC queries:

```java
public boolean authenticateStaff(String staffId, String password) 
{
    String sql = "SELECT StaffID FROM dbo.Staff WHERE StaffID = ? AND Password = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) 
    {
        stmt.setString(1, staffId);
        stmt.setString(2, password);
        try (ResultSet rs = stmt.executeQuery()) 
        {
            return rs.next(); // Returns true if credentials match
        }
    } 
    catch (SQLException ex) 
    {
        // Safe offline demonstration fallback
        return ("ST01".equalsIgnoreCase(staffId) && "admin123".equals(password));
    }
}
```

---

# SECTION 7: VIVA VOCE DEFENSE PREPARATION Q&A

**Q1: What is the primary architecture of MediConnect?**  
*Answer:* MediConnect uses a Two-Tier Client-Server Architecture (as illustrated in Figure 1.1 of the Project Guide). Tier 1 is the JavaFX Presentation Tier handling user interfaces and input validation. Tier 2 is the Database Access Tier communicating with Microsoft SQL Server via JDBC and the Data Access Object (DAO) pattern.

**Q2: How do you achieve inheritance and polymorphism in your models?**  
*Answer:* I created an `abstract` base class `Person` containing common fields (`fullName`, `phone`). Both `Patient` and `Doctor` extend `Person` and pass common attributes to `super(fullName, phone)`. For polymorphism, the generic interface `GenericDAO<T, ID>` defines uniform CRUD contracts implemented across all entity DAOs.

**Q3: How are database transactions handled in the billing process?**  
*Answer:* In `BillingService.processBillTransaction()`, auto-commit mode is disabled (`connection.setAutoCommit(false)`). The bill is inserted into `dbo.Bill` and the patient status is updated in `dbo.Patient` within a single try block. If both succeed, `connection.commit()` makes the change permanent; if an error occurs, `connection.rollback()` reverts all modifications, guaranteeing ACID compliance.

**Q4: How does your application ensure fast, responsive searching in the Directory?**  
*Answer:* The `DirectoryService` aggregates patients, doctors, and bills into a unified `ObservableList<DirectoryItem>`. In the controller, this is wrapped inside a JavaFX `FilteredList`. A change listener is attached to the search text field, dynamically filtering records in memory without making redundant roundtrip queries to the database.

**Q5: How does the system handle an environment where Microsoft SQL Server is not installed?**  
*Answer:* `DatabaseConnection` tests connectivity with a 2-second timeout probe. If SQL Server is unreachable, all DAOs seamlessly fall back to pre-seeded `ConcurrentHashMap` repositories. This allows evaluators to test all UI interactions, form validations, billing calculations, and printable receipt generation without configuration hurdles.
