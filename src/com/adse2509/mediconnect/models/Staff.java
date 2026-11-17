package com.adse2509.mediconnect.models;

import java.io.Serializable;

/**
 * Model class representing an authorized administrative staff member at PMC.
 * Mapped to the {@code dbo.Staff} table in Microsoft SQL Server.
 *
 * @author Group 3
 */
public class Staff implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String staffId;
    private String staffName;
    private String password;

    public Staff()
    {
    }

    public Staff(String staffId, String staffName, String password)
    {
        this.staffId = staffId;
        this.staffName = staffName;
        this.password = password;
    }

    // Getters and Setters
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

    @Override
    public String toString()
    {
        return "Staff{" + "staffId='" + staffId + '\'' + ", staffName='" + staffName + '\'' + '}';
    }
}
