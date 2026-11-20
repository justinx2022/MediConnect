/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.adse2509.mediconnect.services;

import com.adse2509.mediconnect.dao.BillDAO;
import com.adse2509.mediconnect.dao.DoctorDAO;
import com.adse2509.mediconnect.dao.PatientDAO;
import com.adse2509.mediconnect.dao.PatientDetailsDAO;
import com.adse2509.mediconnect.models.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service to aggregate directory records across doctors, patients, checkups, and bills.
 * Provides search and statistical query operations.
 * 
 * @author Group 3
 */
public class DirectoryService
{
    private final DoctorDAO doctorDAO;
    private final PatientDAO patientDAO;
    private final PatientDetailsDAO patientDetailsDAO;
    private final BillDAO billDAO;

    public DirectoryService()
    {
        this.doctorDAO = new DoctorDAO();
        this.patientDAO = new PatientDAO();
        this.patientDetailsDAO = new PatientDetailsDAO();
        this.billDAO = new BillDAO();
    }

    public DirectoryService(DoctorDAO doctorDAO, PatientDAO patientDAO, PatientDetailsDAO patientDetailsDAO, BillDAO billDAO)
    {
        this.doctorDAO = doctorDAO;
        this.patientDAO = patientDAO;
        this.patientDetailsDAO = patientDetailsDAO;
        this.billDAO = billDAO;
    }

    // --------------------------------------------------
    // Aggregation and Search
    // --------------------------------------------------

    public List<DirectoryItem> getAllDirectoryRecords() throws SQLException
    {
        List<DirectoryItem> unifiedList = new ArrayList<>();

        // 1. Doctors
        for (Doctor d : doctorDAO.getAll())
        {
            unifiedList.add(new DirectoryItem(
                    "Doctor",
                    d.getDoctorId(),
                    d.getName(),
                    d.getSpecialization() + " (" + d.getRoomNumber() + ")",
                    "Phone: " + d.getPhone() + " | Email: " + d.getEmailId(),
                    d.getStatus(),
                    ""
            ));
        }

        // 2. Patients
        for (Patient p : patientDAO.getAll())
        {
            unifiedList.add(new DirectoryItem(
                    "Patient",
                    p.getPatientId(),
                    p.getName(),
                    "Age: " + p.getAge() + " (" + p.getGender() + ") | Blood: " + p.getBloodGroup(),
                    "Address: " + p.getAddress(),
                    p.getStatus(),
                    ""
            ));
        }

        // 3. Checkups
        for (PatientDetails pd : patientDetailsDAO.getAll())
        {
            String dStr = pd.getVisitDate() != null ? pd.getVisitDate().toLocalDate().toString() : "2026-11-20";
            unifiedList.add(new DirectoryItem(
                    "Checkup",
                    String.valueOf(pd.getPatientDetailsId()),
                    "Patient: " + pd.getPatientId() + " (Dr: " + pd.getDoctorId() + ")",
                    "Ailment: " + pd.getAilment() + " (Visit #" + pd.getCheckupVisitNumber() + ")",
                    "Date: " + dStr + " | " + pd.getDescription() + " | Cons: $" + pd.getConsFees() + ", Other: $" + pd.getOtherExpenses(),
                    "Recorded",
                    dStr
            ));
        }

        // 4. Bills
        for (Bill b : billDAO.getAll())
        {
            String dStr = b.getBillDate() != null ? b.getBillDate().toLocalDate().toString() : "2026-11-20";
            unifiedList.add(new DirectoryItem(
                    "Invoice",
                    "INV-" + b.getBillId(),
                    "Patient: " + b.getPatientId(),
                    "Amount: $" + String.format("%.2f", b.getBillAmount()) + " via " + b.getModeOfPayment(),
                    "Date: " + dStr,
                    b.getPaymentStatus(),
                    dStr
            ));
        }

        return unifiedList;
    }

    public List<DirectoryItem> search(String query) throws SQLException
    {
        List<DirectoryItem> all = getAllDirectoryRecords();
        if (query == null || query.trim().isEmpty())
        {
            return all;
        }

        String lower = query.toLowerCase().trim();
        List<DirectoryItem> filtered = new ArrayList<>();
        for (DirectoryItem item : all)
        {
            if (item.getPrimaryName().toLowerCase().contains(lower)
                    || item.getRecordId().toLowerCase().contains(lower)
                    || item.getCategory().toLowerCase().contains(lower)
                    || item.getSecondaryInfo().toLowerCase().contains(lower)
                    || item.getDetail().toLowerCase().contains(lower))
            {
                filtered.add(item);
            }
        }
        return filtered;
    }

    // --------------------------------------------------
    // Hospital Statistics and Date Queries
    // --------------------------------------------------

    public int getDoctorCount() throws SQLException
    {
        return doctorDAO.getAll().size();
    }

    public int getPatientCount() throws SQLException
    {
        return patientDAO.getAll().size();
    }

    public int getCheckupCount() throws SQLException
    {
        return patientDetailsDAO.getAll().size();
    }

    public List<DirectoryItem> getVisitsByDate(LocalDate date) throws SQLException
    {
        List<DirectoryItem> results = new ArrayList<>();
        if (date == null) return results;

        String dateStr = date.toString();
        for (DirectoryItem item : getAllDirectoryRecords())
        {
            if (("Checkup".equalsIgnoreCase(item.getCategory()) || "Invoice".equalsIgnoreCase(item.getCategory()))
                    && dateStr.equals(item.getRecordDate()))
            {
                results.add(item);
            }
        }
        return results;
    }
}
