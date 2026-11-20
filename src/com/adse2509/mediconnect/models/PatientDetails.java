/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.adse2509.mediconnect.models;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Model representing a patient visit or checkup record.
 * 
 * @author Group 3
 */
public class PatientDetails implements Serializable
{
    private static final long serialVersionUID = 1L;

    private int patientDetailsId;
    private String patientId;
    private String doctorId;
    private LocalDateTime visitDate;
    private String ailment;
    private int checkupVisitNumber;
    private String description;
    private double consFees;
    private double otherExpenses;

    public PatientDetails()
    {
    }

    public PatientDetails(int patientDetailsId, String patientId, String doctorId, LocalDateTime visitDate, String ailment, int checkupVisitNumber, String description, double consFees, double otherExpenses)
    {
        this.patientDetailsId = patientDetailsId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.visitDate = visitDate;
        this.ailment = ailment;
        this.checkupVisitNumber = checkupVisitNumber;
        this.description = description;
        this.consFees = consFees;
        this.otherExpenses = otherExpenses;
    }

    public int getPatientDetailsId()
    {
        return patientDetailsId;
    }

    public void setPatientDetailsId(int patientDetailsId)
    {
        this.patientDetailsId = patientDetailsId;
    }

    public String getPatientId()
    {
        return patientId;
    }

    public void setPatientId(String patientId)
    {
        this.patientId = patientId;
    }

    public String getDoctorId()
    {
        return doctorId;
    }

    public void setDoctorId(String doctorId)
    {
        this.doctorId = doctorId;
    }

    public LocalDateTime getVisitDate()
    {
        return visitDate;
    }

    public void setVisitDate(LocalDateTime visitDate)
    {
        this.visitDate = visitDate;
    }

    public String getAilment()
    {
        return ailment;
    }

    public void setAilment(String ailment)
    {
        this.ailment = ailment;
    }

    public int getCheckupVisitNumber()
    {
        return checkupVisitNumber;
    }

    public void setCheckupVisitNumber(int checkupVisitNumber)
    {
        this.checkupVisitNumber = checkupVisitNumber;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
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
}
