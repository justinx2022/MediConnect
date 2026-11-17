package com.adse2509.mediconnect.models;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Model class representing a financial bill generated for a patient at PMC.
 * Mapped to the {@code dbo.Bill} table in Microsoft SQL Server.
 *
 * @author Group 3
 */
public class Bill implements Serializable
{
    private static final long serialVersionUID = 1L;

    private int billId;
    private String patientId;
    private LocalDateTime billDate;
    private double billAmount;
    private String modeOfPayment;
    private String paymentStatus;

    // Transient attributes for UI presentation and reporting
    private String patientName;
    private String doctorName;
    private double consFees;
    private double otherExpenses;
    private double discountPercent;

    public Bill()
    {
        this.billDate = LocalDateTime.now();
        this.modeOfPayment = "Cash";
        this.paymentStatus = "Paid";
    }

    public Bill(int billId, String patientId, LocalDateTime billDate, double billAmount, String modeOfPayment)
    {
        this.billId = billId;
        this.patientId = patientId;
        this.billDate = billDate;
        this.billAmount = billAmount;
        this.modeOfPayment = modeOfPayment;
        this.paymentStatus = "Paid";
    }

    // Getters and Setters
    public int getBillId()
    {
        return billId;
    }

    public void setBillId(int billId)
    {
        this.billId = billId;
    }

    public String getPatientId()
    {
        return patientId;
    }

    public void setPatientId(String patientId)
    {
        this.patientId = patientId;
    }

    public LocalDateTime getBillDate()
    {
        return billDate;
    }

    public void setBillDate(LocalDateTime billDate)
    {
        this.billDate = billDate;
    }

    public double getBillAmount()
    {
        return billAmount;
    }

    public void setBillAmount(double billAmount)
    {
        this.billAmount = billAmount;
    }

    public String getModeOfPayment()
    {
        return modeOfPayment;
    }

    public void setModeOfPayment(String modeOfPayment)
    {
        this.modeOfPayment = modeOfPayment;
    }

    public String getPaymentStatus()
    {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus)
    {
        this.paymentStatus = paymentStatus;
    }

    public String getPatientName()
    {
        return patientName != null ? patientName : patientId;
    }

    public void setPatientName(String patientName)
    {
        this.patientName = patientName;
    }

    public String getDoctorName()
    {
        return doctorName != null ? doctorName : "Attending Physician";
    }

    public void setDoctorName(String doctorName)
    {
        this.doctorName = doctorName;
    }

    public double getConsFees()
    {
        return consFees;
    }

    public void setConsFees(double consFees)
    {
        this.consFees = consFees;
    }

    public double getOtherExpenses()
    {
        return otherExpenses;
    }

    public void setOtherExpenses(double otherExpenses)
    {
        this.otherExpenses = otherExpenses;
    }

    public double getDiscountPercent()
    {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent)
    {
        this.discountPercent = discountPercent;
    }

    @Override
    public String toString()
    {
        return "Bill{" + "billId=" + billId + ", patientId='" + patientId + '\'' + ", amount=" + billAmount + '}';
    }
}
