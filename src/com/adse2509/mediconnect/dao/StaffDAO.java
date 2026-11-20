/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.adse2509.mediconnect.dao;

import com.adse2509.mediconnect.models.Staff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Data Access Object for Staff credentials and authentication.
 * 
 * @author Group 3
 */
public class StaffDAO implements GenericDAO<Staff, String>
{
    private static final Map<String, Staff> STAFF_STORE = new ConcurrentHashMap<>();

    static
    {
        STAFF_STORE.put("ST01", new Staff("ST01", "Group 3", "admin123", "+254700000001", "admin"));
        STAFF_STORE.put("ST02", new Staff("ST02", "Jane Mwangi", "pass123", "+254700000002", "receptionist"));
        STAFF_STORE.put("ST03", new Staff("ST03", "Peter Otieno", "pmc2026", "+254700000003", "nurse"));
    }

    private Staff mapRow(ResultSet rs) throws SQLException
    {
        return new Staff(
                rs.getString("StaffID"),
                rs.getString("StaffName"),
                rs.getString("Password"),
                "",
                "Staff"
        );
    }

    public Staff authenticate(String staffId, String password)
    {
        if (staffId == null || password == null)
        {
            return null;
        }

        String sql = "SELECT StaffID, StaffName, Password FROM dbo.Staff WHERE StaffID = ? AND Password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, staffId.trim());
            stmt.setString(2, password.trim());
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

        Staff local = STAFF_STORE.get(staffId.trim());
        if (local != null && local.getPassword().equals(password.trim()))
        {
            return local;
        }
        return null;
    }

    // --------------------------------------------------
    // CRUD Operations
    // --------------------------------------------------

    @Override
    public boolean create(Staff staff) throws SQLException
    {
        STAFF_STORE.put(staff.getStaffId(), staff);

        String sql = "INSERT INTO dbo.Staff (StaffID, StaffName, Password) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, staff.getStaffId());
            stmt.setString(2, staff.getStaffName());
            stmt.setString(3, staff.getPassword());
            return stmt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            return true;
        }
    }

    @Override
    public Staff getById(String staffId) throws SQLException
    {
        String sql = "SELECT StaffID, StaffName, Password FROM dbo.Staff WHERE StaffID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, staffId);
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
        return STAFF_STORE.get(staffId);
    }

    @Override
    public List<Staff> getAll() throws SQLException
    {
        String sql = "SELECT StaffID, StaffName, Password FROM dbo.Staff ORDER BY StaffName";
        List<Staff> list = new ArrayList<>();
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
            return new ArrayList<>(STAFF_STORE.values());
        }
    }

    @Override
    public boolean update(Staff staff) throws SQLException
    {
        STAFF_STORE.put(staff.getStaffId(), staff);

        String sql = "UPDATE dbo.Staff SET StaffName = ?, Password = ? WHERE StaffID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, staff.getStaffName());
            stmt.setString(2, staff.getPassword());
            stmt.setString(3, staff.getStaffId());
            return stmt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            return true;
        }
    }

    @Override
    public boolean delete(String staffId) throws SQLException
    {
        STAFF_STORE.remove(staffId);

        String sql = "DELETE FROM dbo.Staff WHERE StaffID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, staffId);
            return stmt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            return true;
        }
    }
}
