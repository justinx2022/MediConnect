package com.adse2509.mediconnect.models;

import java.io.Serializable;

/**
 * Unified Data Transfer Object (DTO) for populating the Display Information screen
 * and TableView matching Process 1.4 of the Aptech Project Guide.
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

    public DirectoryItem()
    {
    }

    public DirectoryItem(String category, String recordId, String primaryName, String secondaryInfo, String detail, String status)
    {
        this.category = category;
        this.recordId = recordId;
        this.primaryName = primaryName;
        this.secondaryInfo = secondaryInfo;
        this.detail = detail;
        this.status = status;
    }

    // Getters and Setters
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

    @Override
    public String toString()
    {
        return "[" + category + "] " + primaryName + " (" + recordId + ")";
    }
}
