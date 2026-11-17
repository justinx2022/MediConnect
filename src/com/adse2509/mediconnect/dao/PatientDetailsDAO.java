package com.adse2509.mediconnect.dao;

import com.adse2509.mediconnect.models.PatientDetails;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Data Access Object performing CRUD operations against the {@code dbo.PatientDetails} table.
 * Records clinical checkup visits, ailments, consultation fees, and other expenses.
 *
 * @author Group 3
 */
public class PatientDetailsDAO implements GenericDAO<PatientDetails, String>
{
    private static final Map<String, PatientDetails> DETAILS_STORE = new ConcurrentHashMap<>();

    static
    {
        // Seed patient details matching PMC Case Study
        DETAILS_STORE.put("PD01", new PatientDetails("PD01", "D001", "P001", "Hypertension", "Regular cardiovascular checkup and ECG", 1, 150, 50));
        DETAILS_STORE.put("PD02", new PatientDetails("PD02", "D002", "P002", "Chronic Migraine", "Neurological assessment and brain scan review", 2, 200, 120));
        DETAILS_STORE.put("PD03", new PatientDetails("PD03", "D003", "P003", "Pediatric Asthma", "Pulmonary function consultation and inhaler", 1, 120, 30));
    }

    private PatientDetails mapRow(ResultSet rs) throws SQLException
    {
        return new PatientDetails(
                rs.getString("PatientDetailsID"),
                rs.getString("DoctorID"),
                rs.getString("PatientID"),
                rs.getString("Ailment"),
                rs.getString("Description"),
                rs.getInt("CheckupVisitNumber"),
                rs.getInt("ConsFees"),
                rs.getInt("OtherExpenses")
        );
    }

    /* ==================================================================================
     * CRUD OPERATIONS
     * ================================================================================== */

    @Override
    public boolean create(PatientDetails details) throws SQLException
    {
        DETAILS_STORE.put(details.getPatientDetailsId(), details);

        String sql = "INSERT INTO dbo.PatientDetails (PatientDetailsID, DoctorID, PatientID, Ailment, Description, CheckupVisitNumber, ConsFees, OtherExpenses) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, details.getPatientDetailsId());
            stmt.setString(2, details.getDoctorId());
            stmt.setString(3, details.getPatientId());
            stmt.setString(4, details.getAilment());
            stmt.setString(5, details.getDescription());
            stmt.setInt(6, details.getCheckupVisitNumber());
            stmt.setInt(7, details.getConsFees());
            stmt.setInt(8, details.getOtherExpenses());
            return stmt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            return true;
        }
    }

    @Override
    public PatientDetails getById(String id) throws SQLException
    {
        String sql = "SELECT PatientDetailsID, DoctorID, PatientID, Ailment, Description, CheckupVisitNumber, ConsFees, OtherExpenses FROM dbo.PatientDetails WHERE PatientDetailsID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, id);
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
            // Memory fallback
        }
        return DETAILS_STORE.get(id);
    }

    @Override
    public List<PatientDetails> getAll() throws SQLException
    {
        String sql = "SELECT PatientDetailsID, DoctorID, PatientID, Ailment, Description, CheckupVisitNumber, ConsFees, OtherExpenses FROM dbo.PatientDetails ORDER BY PatientDetailsID";
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

        String sql = "UPDATE dbo.PatientDetails SET DoctorID = ?, PatientID = ?, Ailment = ?, Description = ?, CheckupVisitNumber = ?, ConsFees = ?, OtherExpenses = ? WHERE PatientDetailsID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, details.getDoctorId());
            stmt.setString(2, details.getPatientId());
            stmt.setString(3, details.getAilment());
            stmt.setString(4, details.getDescription());
            stmt.setInt(5, details.getCheckupVisitNumber());
            stmt.setInt(6, details.getConsFees());
            stmt.setInt(7, details.getOtherExpenses());
            stmt.setString(8, details.getPatientDetailsId());
            return stmt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            return true;
        }
    }

    @Override
    public boolean delete(String id) throws SQLException
    {
        DETAILS_STORE.remove(id);

        String sql = "DELETE FROM dbo.PatientDetails WHERE PatientDetailsID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            return true;
        }
    }
}
