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
 * Data Access Object performing CRUD operations against the {@code dbo.Patient} table.
 * Implements the {@link GenericDAO} contract from the ADSE 2509 syllabus.
 *
 * @author Group 3
 */
public class PatientDAO implements GenericDAO<Patient, String>
{
    private static final Map<String, Patient> PATIENT_STORE = new ConcurrentHashMap<>();

    static
    {
        // Seed patients matching PMC Case Study
        PATIENT_STORE.put("P001", new Patient("P001", "Robert Chen", "+254700111222", "142 Market Street, Nairobi", "M", 45, "O+", 78.5, 175.0, "Active Record"));
        PATIENT_STORE.put("P002", new Patient("P002", "Amara Okafor", "+254700333444", "88 Palm Grove, Westlands", "F", 32, "A+", 62.0, 168.0, "Active Record"));
        PATIENT_STORE.put("P003", new Patient("P003", "James Miller", "+254700555666", "22 Riverside Drive, Kilimani", "M", 59, "B-", 84.0, 180.0, "Active Record"));
        PATIENT_STORE.put("P004", new Patient("P004", "Eleanor Vance", "+254700777888", "55 Hillview Terrace, Karen", "F", 42, "AB+", 68.0, 170.0, "Active Record"));
    }

    private Patient mapRow(ResultSet rs) throws SQLException
    {
        return new Patient(
                rs.getString("PatientID"),
                rs.getString("Name"),
                "", // Phone optional in basic Patient table per guide
                rs.getString("Address"),
                rs.getString("Gender"),
                rs.getInt("Age"),
                rs.getString("BloodGroup"),
                rs.getDouble("Weight"),
                rs.getDouble("Height"),
                rs.getString("Status")
        );
    }

    /* ==================================================================================
     * CRUD OPERATIONS
     * ================================================================================== */

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
            // In-memory fallback
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
            // Return from memory store
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
