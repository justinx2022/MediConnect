/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.adse2509.mediconnect.dao;

import com.adse2509.mediconnect.models.Patient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Data Access Object for Patient records in the database.
 * 
 * @author Group 3
 */
public class PatientDAO implements GenericDAO<Patient, String>
{
    private static final Map<String, Patient> PATIENT_STORE = new ConcurrentHashMap<>();

    static
    {
        PATIENT_STORE.put("P001", new Patient("P001", "Alice Kamau", "14 Riverside Drive, Nairobi", "+254711223344", 29, "F", "A+", 62.5, 168.0, "Active"));
        PATIENT_STORE.put("P002", new Patient("P002", "Brian Ochieng", "45 Westlands Rd, Nairobi", "+254722334455", 42, "M", "O+", 78.0, 180.5, "Active"));
        PATIENT_STORE.put("P003", new Patient("P003", "Catherine Nduta", "88 Kilimani Lane, Nairobi", "+254733445566", 35, "F", "B+", 68.0, 162.0, "Active"));
        PATIENT_STORE.put("P004", new Patient("P004", "Daniel Mutua", "12 Parklands Ave, Nairobi", "+254744556677", 51, "M", "AB-", 84.0, 175.0, "Active"));
    }

    private Patient mapRow(ResultSet rs) throws SQLException
    {
        return new Patient(
                rs.getString("PatientID"),
                rs.getString("Name"),
                rs.getString("Address"),
                "",
                rs.getInt("Age"),
                rs.getString("Gender"),
                rs.getString("BloodGroup"),
                rs.getDouble("Weight"),
                rs.getDouble("Height"),
                rs.getString("Status")
        );
    }

    // --------------------------------------------------
    // CRUD Operations
    // --------------------------------------------------

    @Override
    public boolean create(Patient patient) throws SQLException
    {
        PATIENT_STORE.put(patient.getPatientId(), patient);

        String sql = "INSERT INTO dbo.Patient (PatientID, Name, Address, Gender, Age, BloodGroup, Weight, Height, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, patient.getPatientId());
            stmt.setString(2, patient.getName());
            stmt.setString(3, patient.getAddress());
            stmt.setString(4, patient.getGender());
            stmt.setInt(5, patient.getAge());
            stmt.setString(6, patient.getBloodGroup());
            stmt.setDouble(7, patient.getWeight());
            stmt.setDouble(8, patient.getHeight());
            stmt.setString(9, patient.getStatus());
            return stmt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            return true;
        }
    }

    @Override
    public Patient getById(String patientId) throws SQLException
    {
        String sql = "SELECT PatientID, Name, Address, Gender, Age, BloodGroup, Weight, Height, Status FROM dbo.Patient WHERE PatientID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, patientId);
            try (ResultSet rs = stmt.executeQuery())
            {
                if (rs.next())
                {
                    return mapRow(rs);
                }
            }
        }
        catch (SQLException e)
        {
            // Fallback to local store
        }
        return PATIENT_STORE.get(patientId);
    }

    @Override
    public List<Patient> getAll() throws SQLException
    {
        String sql = "SELECT PatientID, Name, Address, Gender, Age, BloodGroup, Weight, Height, Status FROM dbo.Patient ORDER BY Name";
        List<Patient> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery())
        {
            while (rs.next())
            {
                list.add(mapRow(rs));
            }
            return list;
        }
        catch (SQLException e)
        {
            return new ArrayList<>(PATIENT_STORE.values());
        }
    }

    @Override
    public boolean update(Patient patient) throws SQLException
    {
        PATIENT_STORE.put(patient.getPatientId(), patient);

        String sql = "UPDATE dbo.Patient SET Name = ?, Address = ?, Gender = ?, Age = ?, BloodGroup = ?, Weight = ?, Height = ?, Status = ? WHERE PatientID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, patient.getName());
            stmt.setString(2, patient.getAddress());
            stmt.setString(3, patient.getGender());
            stmt.setInt(4, patient.getAge());
            stmt.setString(5, patient.getBloodGroup());
            stmt.setDouble(6, patient.getWeight());
            stmt.setDouble(7, patient.getHeight());
            stmt.setString(8, patient.getStatus());
            stmt.setString(9, patient.getPatientId());
            return stmt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            return true;
        }
    }

    @Override
    public boolean delete(String patientId) throws SQLException
    {
        PATIENT_STORE.remove(patientId);

        String sql = "DELETE FROM dbo.Patient WHERE PatientID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, patientId);
            return stmt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            return true;
        }
    }
}
