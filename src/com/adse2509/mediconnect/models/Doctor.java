package com.adse2509.mediconnect.models;

/**
 * Model class representing an attending medical specialist at PMC.
 * Inherits common fields (id, name, phone) from the abstract {@link Person} class.
 * Mapped to the {@code dbo.Doctor} table in Microsoft SQL Server.
 *
 * @author Group 3
 */
public class Doctor extends Person
{
    private static final long serialVersionUID = 1L;

    private String specialization;
    private String emailId;
    private String roomNumber;
    private String status;

    public Doctor()
    {
        super();
        this.roomNumber = "Suite 101";
        this.status = "Available";
    }

    public Doctor(String doctorId, String name, String specialization, String phoneNo, String emailId)
    {
        super(doctorId, name, phoneNo);
        this.specialization = specialization;
        this.emailId = emailId;
        this.roomNumber = "Suite 101";
        this.status = "Available";
    }

    public Doctor(String doctorId, String name, String specialization, String phoneNo, String emailId, String roomNumber, String status)
    {
        super(doctorId, name, phoneNo);
        this.specialization = specialization;
        this.emailId = emailId;
        this.roomNumber = roomNumber;
        this.status = status;
    }

    // Getters and Setters
    public String getDoctorId()
    {
        return id;
    }

    public void setDoctorId(String doctorId)
    {
        this.id = doctorId;
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
        return name + " (" + specialization + " - " + id + ")";
    }
}
