/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.adse2509.mediconnect.models;

import java.io.Serializable;

/**
 * Model representing a patient in the hospital system.
 * 
 * @author Group 3
 */
public class Patient extends Person implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String address;
    private int age;
    private String gender;
    private String bloodGroup;
    private double weight;
    private double height;
    private String status;

    public Patient()
    {
    }

    public Patient(String patientId, String name, String address, String phone, int age, String gender, String bloodGroup, double weight, double height, String status)
    {
        super(patientId, name, phone);
        this.address = address;
        this.age = age;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.weight = weight;
        this.height = height;
        this.status = status;
    }

    public String getPatientId()
    {
        return getId();
    }

    public void setPatientId(String patientId)
    {
        setId(patientId);
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
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
        return getName() + " (" + getId() + ")";
    }
}
