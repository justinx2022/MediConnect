package com.adse2509.mediconnect.models;

import java.io.Serializable;

/**
 * Abstract base class representing a person within the Philadelphia Medical Center (PMC).
 * Demonstrates the Object-Oriented Programming (OOP) concept of Inheritance as taught
 * in the ADSE 2509 syllabus.
 *
 * @author Group 3
 */
public abstract class Person implements Serializable
{
    private static final long serialVersionUID = 1L;

    // Protected fields shared by all human entities in the PMC hospital system
    protected String id;
    protected String name;
    protected String phone;

    public Person()
    {
    }

    public Person(String id, String name, String phone)
    {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    // Getters and Setters
    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    @Override
    public String toString()
    {
        return "Person{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", phone='" + phone + '\'' + '}';
    }
}
