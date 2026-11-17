-- ==================================================================================
-- MEDICONNECT - DATABASE CREATION SCRIPT
-- Case Study: Philadelphia Medical Center (PMC), Great Lakes Region, USA
-- Developed By: OnTime Systems / Group 3 (ADSE 2509)
-- Database Engine: Microsoft SQL Server 2022
-- Target Database: ADSE2509_Hospital
-- Reference: Aptech Project Guide - Developing JavaFX Applications (Pages 19-20)
-- ==================================================================================

IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'ADSE2509_Hospital')
BEGIN
    CREATE DATABASE ADSE2509_Hospital;
END
GO

USE ADSE2509_Hospital;
GO

-- Drop tables in reverse foreign key dependency order
IF OBJECT_ID('dbo.Bill', 'U') IS NOT NULL DROP TABLE dbo.Bill;
IF OBJECT_ID('dbo.PatientDetails', 'U') IS NOT NULL DROP TABLE dbo.PatientDetails;
IF OBJECT_ID('dbo.Patient', 'U') IS NOT NULL DROP TABLE dbo.Patient;
IF OBJECT_ID('dbo.Doctor', 'U') IS NOT NULL DROP TABLE dbo.Doctor;
IF OBJECT_ID('dbo.Staff', 'U') IS NOT NULL DROP TABLE dbo.Staff;
GO

/* ==================================================================================
 * TABLE 1: Staff (Authentication & Staff Member Validation)
 * Guide Specification: Page 19
 * ================================================================================== */
CREATE TABLE dbo.Staff (
    StaffID     VARCHAR(4)   NOT NULL,
    StaffName   VARCHAR(50)  NOT NULL,
    Password    VARCHAR(10)  NOT NULL,
    CONSTRAINT PK_Staff PRIMARY KEY (StaffID)
);
GO

/* ==================================================================================
 * TABLE 2: Doctor (Attending Medical Specialists)
 * Guide Specification: Page 19
 * ================================================================================== */
CREATE TABLE dbo.Doctor (
    DoctorID       VARCHAR(4)   NOT NULL,
    Name           VARCHAR(50)  NOT NULL,
    Specialization VARCHAR(50)  NOT NULL,
    PhoneNo        VARCHAR(20)  NOT NULL,
    EmailID        VARCHAR(30)  NULL,
    RoomNumber     VARCHAR(20)  DEFAULT 'Suite 101',
    Status         VARCHAR(30)  DEFAULT 'Available',
    CONSTRAINT PK_Doctor PRIMARY KEY (DoctorID)
);
GO

/* ==================================================================================
 * TABLE 3: Patient (Hospital Patient Demographics)
 * Guide Specification: Page 19
 * ================================================================================== */
CREATE TABLE dbo.Patient (
    PatientID   VARCHAR(4)     NOT NULL,
    Name        VARCHAR(50)    NOT NULL,
    Address     VARCHAR(40)    NOT NULL,
    Gender      VARCHAR(1)     NOT NULL CHECK (Gender IN ('M', 'F', 'O')),
    Age         INT            NOT NULL CHECK (Age >= 0),
    BloodGroup  VARCHAR(4)     NOT NULL,
    Weight      DECIMAL(5, 2)  NOT NULL,
    Height      DECIMAL(5, 2)  NOT NULL,
    Status      VARCHAR(30)    DEFAULT 'Active Record',
    CONSTRAINT PK_Patient PRIMARY KEY (PatientID)
);
GO

/* ==================================================================================
 * TABLE 4: PatientDetails (Visits, Ailments, Checkups & Treatments)
 * Guide Specification: Page 20
 * ================================================================================== */
CREATE TABLE dbo.PatientDetails (
    PatientDetailsID    VARCHAR(4)   NOT NULL,
    DoctorID            VARCHAR(4)   NOT NULL,
    PatientID           VARCHAR(4)   NOT NULL,
    Ailment             VARCHAR(50)  NOT NULL,
    Description         VARCHAR(150) NOT NULL,
    CheckupVisitNumber  INT          NOT NULL DEFAULT 1,
    ConsFees            INT          NOT NULL DEFAULT 150,
    OtherExpenses       INT          NOT NULL DEFAULT 0,
    VisitDate           DATETIME     DEFAULT GETDATE(),
    CONSTRAINT PK_PatientDetails PRIMARY KEY (PatientDetailsID),
    CONSTRAINT FK_PatientDetails_Doctor FOREIGN KEY (DoctorID) REFERENCES dbo.Doctor(DoctorID),
    CONSTRAINT FK_PatientDetails_Patient FOREIGN KEY (PatientID) REFERENCES dbo.Patient(PatientID)
);
GO

/* ==================================================================================
 * TABLE 5: Bill (Financial Invoicing & Payment Settlements)
 * Guide Specification: Page 20
 * ================================================================================== */
CREATE TABLE dbo.Bill (
    BillID          INT            IDENTITY(1001, 1),
    PatientID       VARCHAR(4)     NOT NULL,
    BillDate        DATETIME       DEFAULT GETDATE(),
    BillAmount      DECIMAL(10, 2) NOT NULL,
    ModeOfPayment   VARCHAR(6)     NOT NULL CHECK (ModeOfPayment IN ('Cash', 'Cheque', 'Card', 'Other')),
    PaymentStatus   VARCHAR(30)    DEFAULT 'Paid',
    CONSTRAINT PK_Bill PRIMARY KEY (BillID),
    CONSTRAINT FK_Bill_Patient FOREIGN KEY (PatientID) REFERENCES dbo.Patient(PatientID)
);
GO

/* ==================================================================================
 * PMC SAMPLE SEED DATA
 * ================================================================================== */

-- Seed Staff
INSERT INTO dbo.Staff (StaffID, StaffName, Password) VALUES
('ST01', 'Group 3', 'admin123'),
('ST02', 'Alice Kimani', 'pmc2026'),
('ST03', 'Front Desk', 'desk123');

-- Seed Doctors
INSERT INTO dbo.Doctor (DoctorID, Name, Specialization, PhoneNo, EmailID, RoomNumber, Status) VALUES
('D001', 'Dr. Sarah Jenkins', 'Cardiology', '+254700111222', 'sarah.j@pmc.org', 'Suite 404', 'Available'),
('D002', 'Dr. Marcus Vance', 'Neurology', '+254700333444', 'marcus.v@pmc.org', 'Suite 208', 'In Surgery'),
('D003', 'Dr. Elena Rostova', 'Pediatrics', '+254700555666', 'elena.r@pmc.org', 'Suite 102', 'Available'),
('D004', 'Dr. David Kim', 'Orthopedics', '+254700777888', 'david.k@pmc.org', 'Suite 315', 'Available');

-- Seed Patients
INSERT INTO dbo.Patient (PatientID, Name, Address, Gender, Age, BloodGroup, Weight, Height, Status) VALUES
('P001', 'Robert Chen', '142 Market Street, Nairobi', 'M', 45, 'O+', 78.50, 175.00, 'Active Record'),
('P002', 'Amara Okafor', '88 Palm Grove, Westlands', 'F', 32, 'A+', 62.00, 168.00, 'Active Record'),
('P003', 'James Miller', '22 Riverside Drive, Kilimani', 'M', 59, 'B-', 84.00, 180.00, 'Active Record'),
('P004', 'Eleanor Vance', '55 Hillview Terrace, Karen', 'F', 42, 'AB+', 68.00, 170.00, 'Active Record');

-- Seed PatientDetails (Checkup Visits)
INSERT INTO dbo.PatientDetails (PatientDetailsID, DoctorID, PatientID, Ailment, Description, CheckupVisitNumber, ConsFees, OtherExpenses) VALUES
('PD01', 'D001', 'P001', 'Hypertension', 'Regular cardiovascular checkup and ECG', 1, 150, 50),
('PD02', 'D002', 'P002', 'Chronic Migraine', 'Neurological assessment and brain scan review', 2, 200, 120),
('PD03', 'D003', 'P003', 'Pediatric Asthma', 'Pulmonary function consultation and inhaler prescription', 1, 120, 30);

-- Seed Bills
INSERT INTO dbo.Bill (PatientID, BillDate, BillAmount, ModeOfPayment, PaymentStatus) VALUES
('P001', GETDATE(), 200.00, 'Cash', 'Paid'),
('P002', GETDATE(), 320.00, 'Card', 'Paid'),
('P003', GETDATE(), 150.00, 'Cash', 'Paid');
GO
