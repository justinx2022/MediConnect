package com.adse2509.mediconnect.models;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Model class representing clinical diagnosis, doctor consultation, and visit expenses.
 * Mapped to the {@code dbo.PatientDetails} table in Microsoft SQL Server.
 *
 * @author Group 3
 */
public class PatientDetails implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String patientDetailsId;
    private String doctorId;
    private String patientId;
    private String ailment;
    private String description;
    private int checkupVisitNumber;
    private int consFees;
    private int otherExpenses;
    private LocalDateTime visitDate;

    public PatientDetails()
    {
        this.checkupVisitNumber = 1;
        this.consFees = 150;
        this.otherExpenses = 0;
        this.visitDate = LocalDateTime.now();
    }

    public PatientDetails(String patientDetailsId, String doctorId, String patientId, String ailment,
                          String description, int checkupVisitNumber, int consFees, int otherExpenses)
    {
        this.patientDetailsId = patientDetailsId;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.ailment = ailment;
        this.description = description;
        this.checkupVisitNumber = checkupVisitNumber;
        this.consFees = consFees;
        this.otherExpenses = otherExpenses;
        this.visitDate = LocalDateTime.now();
    }

    // Getters and Setters
    public String getPatientDetailsId()
    {
        return patientDetailsId;
    }

    public void setPatientDetailsId(String patientDetailsId)
    {
        this.patientDetailsId = patientDetailsId;
    }

    public String getDoctorId()
    {
        return doctorId;
    }

    public void setDoctorId(String doctorId)
    {
        this.doctorId = doctorId;
    }

    public String getPatientId()
    {
        return patientId;
    }

    public void setPatientId(String patientId)
    {
        this.patientId = patientId;
    }

    public String getAilment()
    {
        return ailment;
    }

    public void setAilment(String ailment)
    {
        this.ailment = ailment;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public int getCheckupVisitNumber()
    {
        return checkupVisitNumber;
    }

    public void setCheckupVisitNumber(int checkupVisitNumber)
    {
        this.checkupVisitNumber = checkupVisitNumber;
    }

    public int getConsFees()
    {
        return consFees;
    }

    public void setConsFees(int consFees)
    {
        this.consFees = consFees;
    }

    public int getOtherExpenses()
    {
        return otherExpenses;
    }

    public void setOtherExpenses(int otherExpenses)
    {
        this.otherExpenses = otherExpenses;
    }

    public LocalDateTime getVisitDate()
    {
        return visitDate;
    }

    public void setVisitDate(LocalDateTime visitDate)
    {
        this.visitDate = visitDate;
    }

    public int getTotalCost()
    {
        return consFees + otherExpenses;
    }

    @Override
    public String toString()
    {
        return "PatientDetails{" + "id='" + patientDetailsId + '\'' + ", ailment='" + ailment + '\'' + '}';
    }
}
