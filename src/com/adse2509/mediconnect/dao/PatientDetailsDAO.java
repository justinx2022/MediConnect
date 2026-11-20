/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.adse2509.mediconnect.dao;

import com.adse2509.mediconnect.models.PatientDetails;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Data Access Object for PatientDetails (checkups and consultations).
 * 
 * @author Group 3
 */
public class PatientDetailsDAO implements GenericDAO<PatientDetails, Integer>
{
    private static final Map<Integer, PatientDetails> DETAILS_STORE = new ConcurrentHashMap<>();

    static
    {
        DETAILS_STORE.put(1, new PatientDetails(1, "P001", "D001", LocalDateTime.now().minusDays(3), "Hypertension Monitoring", 1, "BP 140/90, prescribed ACE inhibitors", 150.0, 30.0));
        DETAILS_STORE.put(2, new PatientDetails(2, "P002", "D002", LocalDateTime.now().minusDays(2), "Chronic Migraine", 1, "MRI scheduled, symptom log requested", 200.0, 120.0));
        DETAILS_STORE.put(3, new PatientDetails(3, "P003", "D003", LocalDateTime.now().minusDays(1), "Routine Pediatric Wellness", 2, "Vaccinations updated, healthy milestones", 120.0, 15.0));
        DETAILS_STORE.put(4, new PatientDetails(4, "P004", "D004", LocalDateTime.now(), "Knee Arthralgia", 1, "X-ray completed, physical therapy initiated", 175.0, 60.0));
    }

    private PatientDetails mapRow(ResultSet rs) throws SQLException
    {
        Timestamp ts = rs.getTimestamp("Date");
        LocalDateTime ldt = (ts != null) ? ts.toLocalDateTime() : LocalDateTime.now();
        return new PatientDetails(
                rs.getInt("PatientDetailsID"),
                rs.getString("PatientID"),
                rs.getString("DoctorID"),
                ldt,
                rs.getString("Ailment"),
                rs.getInt("CheckupVisitNumber"),
                rs.getString("Description"),
                rs.getDouble("ConsFees"),
                rs.getDouble("OtherExpenses")
        );
    }

    // --------------------------------------------------
    // CRUD Operations
    // --------------------------------------------------

    @Override
    public boolean create(PatientDetails details) throws SQLException
    {
        int key = (details.getPatientDetailsId() <= 0) ? (DETAILS_STORE.size() + 1) : details.getPatientDetailsId();
        details.setPatientDetailsId(key);
        DETAILS_STORE.put(key, details);

        String sql = "INSERT INTO dbo.PatientDetails (PatientID, DoctorID, Date, Ailment, CheckupVisitNumber, Description, ConsFees, OtherExpenses) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, details.getPatientId());
            stmt.setString(2, details.getDoctorId());
            stmt.setTimestamp(3, Timestamp.valueOf(details.getVisitDate()));
            stmt.setString(4, details.getAilment());
            stmt.setInt(5, details.getCheckupVisitNumber());
            stmt.setString(6, details.getDescription());
            stmt.setDouble(7, details.getConsFees());
            stmt.setDouble(8, details.getOtherExpenses());
            return stmt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            return true;
        }
    }

    @Override
    public PatientDetails getById(Integer id) throws SQLException
    {
        String sql = "SELECT PatientDetailsID, PatientID, DoctorID, Date, Ailment, CheckupVisitNumber, Description, ConsFees, OtherExpenses FROM dbo.PatientDetails WHERE PatientDetailsID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, id);
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
        return DETAILS_STORE.get(id);
    }

    @Override
    public List<PatientDetails> getAll() throws SQLException
    {
        String sql = "SELECT PatientDetailsID, PatientID, DoctorID, Date, Ailment, CheckupVisitNumber, Description, ConsFees, OtherExpenses FROM dbo.PatientDetails ORDER BY Date DESC";
        List<PatientDetails> list = new ArrayList<>();
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
            return new ArrayList<>(DETAILS_STORE.values());
        }
    }

    @Override
    public boolean update(PatientDetails details) throws SQLException
    {
        DETAILS_STORE.put(details.getPatientDetailsId(), details);

        String sql = "UPDATE dbo.PatientDetails SET PatientID = ?, DoctorID = ?, Date = ?, Ailment = ?, CheckupVisitNumber = ?, Description = ?, ConsFees = ?, OtherExpenses = ? WHERE PatientDetailsID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, details.getPatientId());
            stmt.setString(2, details.getDoctorId());
            stmt.setTimestamp(3, Timestamp.valueOf(details.getVisitDate()));
            stmt.setString(4, details.getAilment());
            stmt.setInt(5, details.getCheckupVisitNumber());
            stmt.setString(6, details.getDescription());
            stmt.setDouble(7, details.getConsFees());
            stmt.setDouble(8, details.getOtherExpenses());
            stmt.setInt(9, details.getPatientDetailsId());
            return stmt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            return true;
        }
    }

    @Override
    public boolean delete(Integer id) throws SQLException
    {
        DETAILS_STORE.remove(id);

        String sql = "DELETE FROM dbo.PatientDetails WHERE PatientDetailsID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            return true;
        }
    }
}
