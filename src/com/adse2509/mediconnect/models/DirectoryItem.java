/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.adse2509.mediconnect.models;

import java.io.Serializable;

/**
 * Model representing an aggregated record in the universal directory table.
 * 
 * @author Group 3
 */
public class DirectoryItem implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String category;
    private String recordId;
    private String primaryName;
    private String secondaryInfo;
    private String detail;
    private String status;
    private String recordDate;

    public DirectoryItem()
    {
    }

    public DirectoryItem(String category, String recordId, String primaryName, String secondaryInfo, String detail, String status)
    {
        this(category, recordId, primaryName, secondaryInfo, detail, status, "");
    }

    public DirectoryItem(String category, String recordId, String primaryName, String secondaryInfo, String detail, String status, String recordDate)
    {
        this.category = category;
        this.recordId = recordId;
        this.primaryName = primaryName;
        this.secondaryInfo = secondaryInfo;
        this.detail = detail;
        this.status = status;
        this.recordDate = (recordDate != null) ? recordDate : "";
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getRecordId()
    {
        return recordId;
    }

    public void setRecordId(String recordId)
    {
        this.recordId = recordId;
    }

    public String getPrimaryName()
    {
        return primaryName;
    }

    public void setPrimaryName(String primaryName)
    {
        this.primaryName = primaryName;
    }

    public String getSecondaryInfo()
    {
        return secondaryInfo;
    }

    public void setSecondaryInfo(String secondaryInfo)
    {
        this.secondaryInfo = secondaryInfo;
    }

    public String getDetail()
    {
        return detail;
    }

    public void setDetail(String detail)
    {
        this.detail = detail;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getRecordDate()
    {
        return recordDate;
    }

    public void setRecordDate(String recordDate)
    {
        this.recordDate = recordDate;
    }
}
