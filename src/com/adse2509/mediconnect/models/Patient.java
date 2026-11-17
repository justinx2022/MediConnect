package com.adse2509.mediconnect.models;

/**
 * Model class representing a registered patient at PMC.
 * Inherits common fields (id, name, phone) from the abstract {@link Person} class.
 * Mapped to the {@code dbo.Patient} table in Microsoft SQL Server.
 *
 * @author Group 3
 */
public class Patient extends Person
{
    private static final long serialVersionUID = 1L;

    private String address;
    private String gender;
    private int age;
    private String bloodGroup;
    private double weight;
    private double height;
    private String status;

    public Patient()
    {
        super();
        this.status = "Active Record";
    }

    public Patient(String patientId, String name, String address, String gender, int age, String bloodGroup, double weight, double height)
    {
        super(patientId, name, "");
        this.address = address;
        this.gender = gender;
        this.age = age;
        this.bloodGroup = bloodGroup;
        this.weight = weight;
        this.height = height;
        this.status = "Active Record";
    }

    public Patient(String patientId, String name, String phone, String address, String gender, int age, String bloodGroup, double weight, double height, String status)
    {
        super(patientId, name, phone);
        this.address = address;
        this.gender = gender;
        this.age = age;
        this.bloodGroup = bloodGroup;
        this.weight = weight;
        this.height = height;
        this.status = status;
    }

    // Getters and Setters
    public String getPatientId()
    {
        return id;
    }

    public void setPatientId(String patientId)
    {
        this.id = patientId;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public String getBloodGroup()
    {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup)
    {
        this.bloodGroup = bloodGroup;
    }

    public double getWeight()
    {
        return weight;
    }

    public void setWeight(double weight)
    {
        this.weight = weight;
    }

    public double getHeight()
    {
        return height;
    }

    public void setHeight(double height)
    {
        this.height = height;
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
        return name + " (" + id + " - Age " + age + ")";
    }
}
