package com.adse2509.mediconnect.dao;

import com.adse2509.mediconnect.models.Staff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Data Access Object for validating staff login credentials.
 * Implements Process 1.1 (Authentication Process) from the Aptech Project Guide.
 *
 * @author Group 3
 */
public class StaffDAO
{
    // In-memory fallback map for offline demonstrations
    private static final Map<String, Staff> STAFF_STORE = new ConcurrentHashMap<>();

    static
    {
        // Seed default staff members matching schema.sql
        STAFF_STORE.put("ST01", new Staff("ST01", "Group 3", "admin123"));
        STAFF_STORE.put("ST02", new Staff("ST02", "Alice Kimani", "pmc2026"));
        STAFF_STORE.put("ST03", new Staff("ST03", "Front Desk", "desk123"));
    }

    /* ==================================================================================
     * AUTHENTICATION METHOD
     * ================================================================================== */
    public Staff authenticate(String staffId, String password)
    {
        String sql = "SELECT StaffID, StaffName, Password FROM dbo.Staff WHERE StaffID = ? AND Password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, staffId);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery())
            {
                if (rs.next())
                {
                    return new Staff(
                            rs.getString("StaffID"),
                            rs.getString("StaffName"),
                            rs.getString("Password")
                    );
                }
            }
        }
        catch (SQLException e)
        {
            // Offline fallback: check against seeded memory store
            Staff memoryStaff = STAFF_STORE.get(staffId);
            if (memoryStaff != null && memoryStaff.getPassword().equals(password))
            {
                return memoryStaff;
            }
        }
        return null;
    }
}
