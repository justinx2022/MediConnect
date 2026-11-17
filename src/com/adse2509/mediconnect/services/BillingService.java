package com.adse2509.mediconnect.services;

import com.adse2509.mediconnect.dao.BillDAO;
import com.adse2509.mediconnect.dao.DatabaseConnection;
import com.adse2509.mediconnect.dao.PatientDAO;
import com.adse2509.mediconnect.models.Bill;
import com.adse2509.mediconnect.models.Patient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Service class handling patient billing computations and transaction boundaries.
 * Demonstrates ACID transaction processing from ADSE 2509 Session 07 (Advanced JDBC).
 *
 * @author Group 3
 */
public class BillingService
{
    private final BillDAO billDAO;
    private final PatientDAO patientDAO;

    public BillingService()
    {
        this.billDAO = new BillDAO();
        this.patientDAO = new PatientDAO();
    }

    public BillingService(BillDAO billDAO, PatientDAO patientDAO)
    {
        this.billDAO = billDAO;
        this.patientDAO = patientDAO;
    }

    /* ==================================================================================
     * FINANCIAL CALCULATIONS
     * ================================================================================== */

    public double calculateSubtotal(double consFees, double otherExpenses)
    {
        return Math.max(0.0, consFees + otherExpenses);
    }

    public double calculateDiscountAmount(double subtotal, double discountPercent)
    {
        if (discountPercent <= 0) return 0.0;
        return subtotal * (Math.min(100.0, discountPercent) / 100.0);
    }

    public double calculateTotalPayable(double consFees, double otherExpenses, double discountPercent)
    {
        double subtotal = calculateSubtotal(consFees, otherExpenses);
        double discount = calculateDiscountAmount(subtotal, discountPercent);
        return Math.max(0.0, subtotal - discount);
    }

    /* ==================================================================================
     * TRANSACTION PROCESSING (ACID Boundaries)
     * ================================================================================== */

    public boolean processBillTransaction(Bill bill) throws SQLException
    {
        if (bill == null || bill.getPatientId() == null || bill.getPatientId().trim().isEmpty())
        {
            throw new IllegalArgumentException("Invalid bill: Patient ID is required.");
        }

        // Set billing timestamp if missing
        if (bill.getBillDate() == null)
        {
            bill.setBillDate(LocalDateTime.now());
        }

        // Check if database is online for explicit JDBC transaction
        if (DatabaseConnection.isServerAvailable())
        {
            try (Connection conn = DatabaseConnection.getConnection())
            {
                // 1. Begin atomic transaction
                conn.setAutoCommit(false);

                try
                {
                    // Step A: Insert bill record
                    String billSql = "INSERT INTO dbo.Bill (PatientID, BillDate, BillAmount, ModeOfPayment, PaymentStatus) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement billStmt = conn.prepareStatement(billSql))
                    {
                        billStmt.setString(1, bill.getPatientId());
                        billStmt.setTimestamp(2, java.sql.Timestamp.valueOf(bill.getBillDate()));
                        billStmt.setDouble(3, bill.getBillAmount());
                        billStmt.setString(4, bill.getModeOfPayment());
                        billStmt.setString(5, bill.getPaymentStatus());
                        billStmt.executeUpdate();
                    }

                    // Step B: Update patient status to settled
                    String patientSql = "UPDATE dbo.Patient SET Status = 'Settled / Discharged' WHERE PatientID = ?";
                    try (PreparedStatement patStmt = conn.prepareStatement(patientSql))
                    {
                        patStmt.setString(1, bill.getPatientId());
                        patStmt.executeUpdate();
                    }

                    // 2. Commit transaction if all steps succeed
                    conn.commit();
                    System.out.println("[BillingService] Transaction committed successfully for Patient: " + bill.getPatientId());
                    return true;
                }
                catch (SQLException ex)
                {
                    // 3. Rollback changes on any database error
                    conn.rollback();
                    System.err.println("[BillingService] Transaction rolled back due to error: " + ex.getMessage());
                    throw ex;
                }
                finally
                {
                    conn.setAutoCommit(true);
                }
            }
        }
        else
        {
            // Offline fallback execution
            billDAO.create(bill);
            Patient patient = patientDAO.getById(bill.getPatientId());
            if (patient != null)
            {
                patient.setStatus("Settled / Discharged");
                patientDAO.update(patient);
            }
            return true;
        }
    }
}
