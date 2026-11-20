/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.adse2509.mediconnect.models;

import java.io.Serializable;

/**
 * Model representing a hospital staff member for login authentication.
 * 
 * @author Group 3
 */
public class Staff implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String staffId;
    private String staffName;
    private String password;
    private String phone;
    private String role;

    public Staff()
    {
    }

    public Staff(String staffId, String staffName, String password, String phone, String role)
    {
        this.staffId = staffId;
        this.staffName = staffName;
        this.password = password;
        this.phone = phone;
        this.role = role;
    }

    public String getStaffId()
    {
        return staffId;
    }

    public void setStaffId(String staffId)
    {
        this.staffId = staffId;
    }

    public String getStaffName()
    {
        return staffName;
    }

    public void setStaffName(String staffName)
    {
        this.staffName = staffName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    @Override
    public String toString()
    {
        return staffName + " (" + staffId + ")";
    }
}
