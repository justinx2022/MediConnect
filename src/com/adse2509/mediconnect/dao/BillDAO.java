/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.adse2509.mediconnect.dao;

import com.adse2509.mediconnect.models.Bill;

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
 * Data Access Object for Bill transactions and invoicing.
 * 
 * @author Group 3
 */
public class BillDAO implements GenericDAO<Bill, Integer>
{
    private static final Map<Integer, Bill> BILL_STORE = new ConcurrentHashMap<>();

    static
    {
        BILL_STORE.put(1001, new Bill(1001, "P001", LocalDateTime.now().minusDays(3), 180.00, "Cash"));
        BILL_STORE.put(1002, new Bill(1002, "P002", LocalDateTime.now().minusDays(2), 320.00, "Card"));
        BILL_STORE.put(1003, new Bill(1003, "P003", LocalDateTime.now().minusDays(1), 135.00, "Cash"));
        BILL_STORE.put(1004, new Bill(1004, "P004", LocalDateTime.now(), 235.00, "Cheque"));
    }

    private Bill mapRow(ResultSet rs) throws SQLException
    {
        Timestamp ts = rs.getTimestamp("Date");
        LocalDateTime ldt = (ts != null) ? ts.toLocalDateTime() : LocalDateTime.now();
        return new Bill(
                rs.getInt("BillNo"),
                rs.getString("PatientID"),
                ldt,
                rs.getDouble("TotalAmount"),
                rs.getString("ModeOfPayment")
        );
    }

    // --------------------------------------------------
    // CRUD Operations
    // --------------------------------------------------

    @Override
    public boolean create(Bill bill) throws SQLException
    {
        int key = (bill.getBillId() <= 0) ? (1000 + BILL_STORE.size() + 1) : bill.getBillId();
        bill.setBillId(key);
        BILL_STORE.put(key, bill);

        String sql = "INSERT INTO dbo.Bill (PatientID, Date, TotalAmount, ModeOfPayment) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, bill.getPatientId());
            stmt.setTimestamp(2, Timestamp.valueOf(bill.getBillDate()));
            stmt.setDouble(3, bill.getBillAmount());
            stmt.setString(4, bill.getModeOfPayment());
            return stmt.executeUpdate() > 0;
        }
        catch (SQLException e)
        {
            return true;
        }
    }

    @Override
    public Bill getById(Integer id) throws SQLException
    {
        String sql = "SELECT BillNo, PatientID, Date, TotalAmount, ModeOfPayment FROM dbo.Bill WHERE BillNo = ?";
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
        return BILL_STORE.get(id);
    }

    @Override
    public List<Bill> getAll() throws SQLException
    {
        String sql = "SELECT BillNo, PatientID, Date, TotalAmount, ModeOfPayment FROM dbo.Bill ORDER BY Date DESC";
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

        String sql = "UPDATE dbo.Bill SET PatientID = ?, Date = ?, TotalAmount = ?, ModeOfPayment = ? WHERE BillNo = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, bill.getPatientId());
            stmt.setTimestamp(2, Timestamp.valueOf(bill.getBillDate()));
            stmt.setDouble(3, bill.getBillAmount());
            stmt.setString(4, bill.getModeOfPayment());
            stmt.setInt(5, bill.getBillId());
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
        BILL_STORE.remove(id);

        String sql = "DELETE FROM dbo.Bill WHERE BillNo = ?";
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
