/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.adse2509.mediconnect.models;

import java.io.Serializable;

/**
 * Base class for persons in the hospital system.
 * 
 * @author Group 3
 */
public abstract class Person implements Serializable
{
    private static final long serialVersionUID = 1L;

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
        return name + " (" + id + ")";
    }
}
