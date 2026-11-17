package com.adse2509.mediconnect.services;

import com.adse2509.mediconnect.dao.BillDAO;
import com.adse2509.mediconnect.dao.DoctorDAO;
import com.adse2509.mediconnect.dao.PatientDAO;
import com.adse2509.mediconnect.dao.PatientDetailsDAO;
import com.adse2509.mediconnect.models.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class responsible for aggregating records across Doctors, Patients, Checkups,
 * and Invoices for the "Display Information" screen (Process 1.4).
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

    /* ==================================================================================
     * AGGREGATION & SEARCH METHODS
     * ================================================================================== */

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
                    d.getStatus()
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
                    p.getStatus()
            ));
        }

        // 3. Clinical Checkups (PatientDetails)
        for (PatientDetails pd : patientDetailsDAO.getAll())
        {
            unifiedList.add(new DirectoryItem(
                    "Checkup",
                    pd.getPatientDetailsId(),
                    "Patient: " + pd.getPatientId() + " (Dr: " + pd.getDoctorId() + ")",
                    "Ailment: " + pd.getAilment() + " (Visit #" + pd.getCheckupVisitNumber() + ")",
                    pd.getDescription() + " | Cons: $" + pd.getConsFees() + ", Other: $" + pd.getOtherExpenses(),
                    "Recorded"
            ));
        }

        // 4. Bills
        for (Bill b : billDAO.getAll())
        {
            unifiedList.add(new DirectoryItem(
                    "Invoice",
                    "INV-" + b.getBillId(),
                    "Patient: " + b.getPatientId(),
                    "Amount: $" + String.format("%.2f", b.getBillAmount()) + " via " + b.getModeOfPayment(),
                    "Date: " + b.getBillDate().toString().replace('T', ' ').substring(0, 16),
                    b.getPaymentStatus()
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
}
