/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.adse2509.mediconnect.models;

import java.io.Serializable;

/**
 * Model representing a doctor in the hospital system.
 * 
 * @author Group 3
 */
public class Doctor extends Person implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String specialization;
    private String emailId;
    private String roomNumber;
    private String status;

    public Doctor()
    {
    }

    public Doctor(String doctorId, String name, String specialization, String phone, String emailId, String roomNumber, String status)
    {
        super(doctorId, name, phone);
        this.specialization = specialization;
        this.emailId = emailId;
        this.roomNumber = roomNumber;
        this.status = status;
    }

    public String getDoctorId()
    {
        return getId();
    }

    public void setDoctorId(String doctorId)
    {
        setId(doctorId);
    }

    public String getSpecialization()
    {
        return specialization;
    }

    public void setSpecialization(String specialization)
    {
        this.specialization = specialization;
    }

    public String getEmailId()
    {
        return emailId;
    }

    public void setEmailId(String emailId)
    {
        this.emailId = emailId;
    }

    public String getRoomNumber()
    {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber)
    {
        this.roomNumber = roomNumber;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return getName() + " - " + specialization + " (" + getId() + ")";
    }
}
