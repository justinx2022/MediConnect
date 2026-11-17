package com.adse2509.mediconnect.dao;

import com.adse2509.mediconnect.models.Bill;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Data Access Object performing CRUD operations against the {@code dbo.Bill} table.
 * Implements Process 1.3 (Bill Generation Process) from the Aptech Project Guide.
 *
 * @author Group 3
 */
public class BillDAO implements GenericDAO<Bill, Integer>
{
    private static final Map<Integer, Bill> BILL_STORE = new ConcurrentHashMap<>();
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1004);

    static
    {
        // Seed bills matching PMC Case Study
        BILL_STORE.put(1001, new Bill(1001, "P001", LocalDateTime.now().minusDays(2), 200.00, "Cash"));
        BILL_STORE.put(1002, new Bill(1002, "P002", LocalDateTime.now().minusDays(1), 320.00, "Card"));
        BILL_STORE.put(1003, new Bill(1003, "P003", LocalDateTime.now(), 150.00, "Cash"));
    }

    private Bill mapRow(ResultSet rs) throws SQLException
    {
        Timestamp ts = rs.getTimestamp("BillDate");
        LocalDateTime date = (ts != null) ? ts.toLocalDateTime() : LocalDateTime.now();
        Bill bill = new Bill(
                rs.getInt("BillID"),
                rs.getString("PatientID"),
                date,
                rs.getDouble("BillAmount"),
                rs.getString("ModeOfPayment")
        );
        bill.setPaymentStatus(rs.getString("PaymentStatus"));
        return bill;
    }

    /* ==================================================================================
     * CRUD OPERATIONS
     * ================================================================================== */

    @Override
    public boolean create(Bill bill) throws SQLException
    {
        if (bill.getBillId() <= 0)
        {
            bill.setBillId(ID_GENERATOR.getAndIncrement());
        }
        BILL_STORE.put(bill.getBillId(), bill);

        String sql = "INSERT INTO dbo.Bill (PatientID, BillDate, BillAmount, ModeOfPayment, PaymentStatus) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))
        {
            stmt.setString(1, bill.getPatientId());
            stmt.setTimestamp(2, Timestamp.valueOf(bill.getBillDate()));
            stmt.setDouble(3, bill.getBillAmount());
            stmt.setString(4, bill.getModeOfPayment());
            stmt.setString(5, bill.getPaymentStatus());

            int affected = stmt.executeUpdate();
            if (affected > 0)
            {
                try (ResultSet keys = stmt.getGeneratedKeys())
                {
                    if (keys.next())
                    {
                        bill.setBillId(keys.getInt(1));
                    }
                }
            }
            return affected > 0;
        }
        catch (SQLException e)
        {
            return true;
        }
    }

    @Override
    public Bill getById(Integer billId) throws SQLException
    {
        String sql = "SELECT BillID, PatientID, BillDate, BillAmount, ModeOfPayment, PaymentStatus FROM dbo.Bill WHERE BillID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, billId);
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
        return BILL_STORE.get(billId);
    }

    @Override
    public List<Bill> getAll() throws SQLException
    {
        String sql = "SELECT BillID, PatientID, BillDate, BillAmount, ModeOfPayment, PaymentStatus FROM dbo.Bill ORDER BY BillID DESC";
        List<Bill> list = new ArrayList<>();
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
            return new ArrayList<>(BILL_STORE.values());
        }
    }

    @Override
    public boolean update(Bill bill) throws SQLException
    {
        BILL_STORE.put(bill.getBillId(), bill);

        String sql = "UPDATE dbo.Bill SET PatientID = ?, BillDate = ?, BillAmount = ?, ModeOfPayment = ?, PaymentStatus = ? WHERE BillID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, bill.getPatientId());
            stmt.setTimestamp(2, Timestamp.valueOf(bill.getBillDate()));
            stmt.setDouble(3, bill.getBillAmount());
            stmt.setString(4, bill.getModeOfPayment());
            stmt.setString(5, bill.getPaymentStatus());
            stmt.setInt(6, bill.getBillId());
            return stmt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            return true;
        }
    }

    @Override
    public boolean delete(Integer billId) throws SQLException
    {
        BILL_STORE.remove(billId);

        String sql = "DELETE FROM dbo.Bill WHERE BillID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setInt(1, billId);
            return stmt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            return true;
        }
    }
}
