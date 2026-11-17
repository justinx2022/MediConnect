package com.adse2509.mediconnect.dao;

import com.adse2509.mediconnect.models.Doctor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Data Access Object performing CRUD operations against the {@code dbo.Doctor} table.
 * Implements the {@link GenericDAO} contract from the ADSE 2509 syllabus.
 *
 * @author Group 3
 */
public class DoctorDAO implements GenericDAO<Doctor, String>
{
    private static final Map<String, Doctor> DOCTOR_STORE = new ConcurrentHashMap<>();

    static
    {
        // Seed doctors matching PMC Case Study
        DOCTOR_STORE.put("D001", new Doctor("D001", "Dr. Sarah Jenkins", "Cardiology", "+254700111222", "sarah.j@pmc.org", "Suite 404", "Available"));
        DOCTOR_STORE.put("D002", new Doctor("D002", "Dr. Marcus Vance", "Neurology", "+254700333444", "marcus.v@pmc.org", "Suite 208", "In Surgery"));
        DOCTOR_STORE.put("D003", new Doctor("D003", "Dr. Elena Rostova", "Pediatrics", "+254700555666", "elena.r@pmc.org", "Suite 102", "Available"));
        DOCTOR_STORE.put("D004", new Doctor("D004", "Dr. David Kim", "Orthopedics", "+254700777888", "david.k@pmc.org", "Suite 315", "Available"));
    }

    private Doctor mapRow(ResultSet rs) throws SQLException
    {
        return new Doctor(
                rs.getString("DoctorID"),
                rs.getString("Name"),
                rs.getString("Specialization"),
                rs.getString("PhoneNo"),
                rs.getString("EmailID"),
                rs.getString("RoomNumber"),
                rs.getString("Status")
        );
    }

    /* ==================================================================================
     * CRUD OPERATIONS
     * ================================================================================== */

    @Override
    public boolean create(Doctor doctor) throws SQLException
    {
        DOCTOR_STORE.put(doctor.getDoctorId(), doctor);

        String sql = "INSERT INTO dbo.Doctor (DoctorID, Name, Specialization, PhoneNo, EmailID, RoomNumber, Status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, doctor.getDoctorId());
            stmt.setString(2, doctor.getName());
            stmt.setString(3, doctor.getSpecialization());
            stmt.setString(4, doctor.getPhone());
            stmt.setString(5, doctor.getEmailId());
            stmt.setString(6, doctor.getRoomNumber());
            stmt.setString(7, doctor.getStatus());
            return stmt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            // Offline fallback confirmed
            return true;
        }
    }

    @Override
    public Doctor getById(String doctorId) throws SQLException
    {
        String sql = "SELECT DoctorID, Name, Specialization, PhoneNo, EmailID, RoomNumber, Status FROM dbo.Doctor WHERE DoctorID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, doctorId);
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
        return DOCTOR_STORE.get(doctorId);
    }

    @Override
    public List<Doctor> getAll() throws SQLException
    {
        String sql = "SELECT DoctorID, Name, Specialization, PhoneNo, EmailID, RoomNumber, Status FROM dbo.Doctor ORDER BY Name";
        List<Doctor> list = new ArrayList<>();
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
            return new ArrayList<>(DOCTOR_STORE.values());
        }
    }

    @Override
    public boolean update(Doctor doctor) throws SQLException
    {
        DOCTOR_STORE.put(doctor.getDoctorId(), doctor);

        String sql = "UPDATE dbo.Doctor SET Name = ?, Specialization = ?, PhoneNo = ?, EmailID = ?, RoomNumber = ?, Status = ? WHERE DoctorID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, doctor.getName());
            stmt.setString(2, doctor.getSpecialization());
            stmt.setString(3, doctor.getPhone());
            stmt.setString(4, doctor.getEmailId());
            stmt.setString(5, doctor.getRoomNumber());
            stmt.setString(6, doctor.getStatus());
            stmt.setString(7, doctor.getDoctorId());
            return stmt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            return true;
        }
    }

    @Override
    public boolean delete(String doctorId) throws SQLException
    {
        DOCTOR_STORE.remove(doctorId);

        String sql = "DELETE FROM dbo.Doctor WHERE DoctorID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, doctorId);
            return stmt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            return true;
        }
    }
}
