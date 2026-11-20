/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
 * Service to handle billing calculations and invoice transactions.
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

    // --------------------------------------------------
    // Billing Calculations
    // --------------------------------------------------

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

    // --------------------------------------------------
    // Transaction Processing
    // --------------------------------------------------

    public boolean processBillTransaction(Bill bill) throws SQLException
    {
        if (bill == null || bill.getPatientId() == null || bill.getPatientId().trim().isEmpty())
        {
            throw new IllegalArgumentException("Invalid bill: Patient ID is required.");
        }

        if (bill.getBillDate() == null)
        {
            bill.setBillDate(LocalDateTime.now());
        }

        if (DatabaseConnection.isServerAvailable())
        {
            try (Connection conn = DatabaseConnection.getConnection())
            {
                conn.setAutoCommit(false);
                try
                {
                    String billSql = "INSERT INTO dbo.Bill (PatientID, Date, TotalAmount, ModeOfPayment) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement billStmt = conn.prepareStatement(billSql))
                    {
                        billStmt.setString(1, bill.getPatientId());
                        billStmt.setTimestamp(2, java.sql.Timestamp.valueOf(bill.getBillDate()));
                        billStmt.setDouble(3, bill.getBillAmount());
                        billStmt.setString(4, bill.getModeOfPayment());
                        billStmt.executeUpdate();
                    }

                    String patientSql = "UPDATE dbo.Patient SET Status = 'Settled' WHERE PatientID = ?";
                    try (PreparedStatement patStmt = conn.prepareStatement(patientSql))
                    {
                        patStmt.setString(1, bill.getPatientId());
                        patStmt.executeUpdate();
                    }

                    conn.commit();
                    return true;
                }
                catch (SQLException ex)
                {
                    conn.rollback();
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
            billDAO.create(bill);
            Patient patient = patientDAO.getById(bill.getPatientId());
            if (patient != null)
            {
                patient.setStatus("Settled");
                patientDAO.update(patient);
            }
            return true;
        }
    }
}
