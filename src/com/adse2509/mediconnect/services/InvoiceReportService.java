/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.adse2509.mediconnect.services;

import com.adse2509.mediconnect.models.Bill;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;

/**
 * Service to format and save receipt files using NIO.2.
 * 
 * @author Group 3
 */
public class InvoiceReportService
{
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm");
    private static final String RECEIPT_DIR = "receipts";

    public String generateReceiptText(Bill bill)
    {
        if (bill == null) return "";

        String dateStr = (bill.getBillDate() != null) ? bill.getBillDate().format(DATE_FMT) : "N/A";
        String patId = (bill.getPatientId() != null) ? bill.getPatientId() : "P001";
        String patName = (bill.getPatientName() != null) ? bill.getPatientName() : "Patient";
        String docName = (bill.getDoctorName() != null) ? bill.getDoctorName() : "Attending Physician";

        double cons = bill.getConsFees();
        double other = bill.getOtherExpenses();
        double subtotal = cons + other;
        double discount = bill.getDiscountPercent();
        double discountAmount = subtotal * (discount / 100.0);
        double net = bill.getBillAmount();

        StringBuilder sb = new StringBuilder();
        sb.append("====================================================\n");
        sb.append("           PHILADELPHIA MEDICAL CENTER             \n");
        sb.append("              Official Billing Receipt             \n");
        sb.append("====================================================\n");
        sb.append(String.format(" Receipt No   : INV-%04d%n", bill.getBillId() > 0 ? bill.getBillId() : 1001));
        sb.append(String.format(" Date / Time  : %s%n", dateStr));
        sb.append(String.format(" Patient ID   : %s%n", patId));
        sb.append(String.format(" Patient Name : %s%n", patName));
        sb.append(String.format(" Consultant   : %s%n", docName));
        sb.append("----------------------------------------------------\n");
        sb.append(" Particulars                           Amount (USD) \n");
        sb.append("----------------------------------------------------\n");
        sb.append(String.format(" Consultation & Examination Fee       : %10.2f%n", cons));
        sb.append(String.format(" Laboratory & Ancillary Expenses       : %10.2f%n", other));
        sb.append("----------------------------------------------------\n");
        sb.append(String.format(" Gross Subtotal                       : %10.2f%n", subtotal));
        if (discount > 0.0)
        {
            sb.append(String.format(" Less Discount (%4.1f%%)                : %10.2f%n", discount, discountAmount));
        }
        sb.append(String.format(" Net Total Payable                    : %10.2f%n", net));
        sb.append("----------------------------------------------------\n");
        sb.append(String.format(" Mode of Settlement                   : %s%n", bill.getModeOfPayment()));
        sb.append(String.format(" Payment Status                       : %s%n", bill.getPaymentStatus() != null ? bill.getPaymentStatus() : "PAID"));
        sb.append("====================================================\n");
        sb.append("     Thank you for choosing Philadelphia Medical    \n");
        sb.append("====================================================\n");

        return sb.toString();
    }

    public Path saveReceiptToFile(Bill bill) throws IOException
    {
        Path dir = Paths.get(RECEIPT_DIR);
        if (!Files.exists(dir))
        {
            Files.createDirectories(dir);
        }

        int billNo = (bill.getBillId() > 0) ? bill.getBillId() : 1001;
        String fileName = String.format("Receipt_INV%04d_%s.txt", billNo, bill.getPatientId());
        Path filePath = dir.resolve(fileName);

        String content = generateReceiptText(bill);
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8))
        {
            writer.write(content);
        }

        return filePath;
    }
}
