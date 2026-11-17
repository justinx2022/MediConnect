package com.adse2509.mediconnect.services;

import com.adse2509.mediconnect.models.Bill;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;

/**
 * Service class responsible for generating printable text receipts on disk.
 * Demonstrates the use of Java NIO.2 file handling from ADSE 2509 Session 03.
 *
 * @author Group 3
 */
public class InvoiceReportService
{
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /* ==================================================================================
     * TEXT FORMATTING & FILE WRITING
     * ================================================================================== */

    public String generateReceiptText(Bill bill)
    {
        double consFees = bill.getConsFees() > 0 ? bill.getConsFees() : 150.0;
        double otherExpenses = bill.getOtherExpenses();
        double subtotal = consFees + otherExpenses;
        double discount = bill.getDiscountPercent() > 0 ? (subtotal * (bill.getDiscountPercent() / 100.0)) : 0.0;
        double total = bill.getBillAmount() > 0 ? bill.getBillAmount() : (subtotal - discount);

        return """
            ====================================================================
                       PHILADELPHIA MEDICAL CENTER (PMC) - OFFICIAL RECEIPT
                                142 Medical Boulevard, Great Lakes Region
                                    Phone: +1 (555) 019-2834
            ====================================================================
            Invoice Number: INV-%d
            Patient ID    : %s
            Patient Name  : %s
            Attending Dr. : %s
            Billing Date  : %s
            Payment Mode  : %s
            Status        : %s
            --------------------------------------------------------------------
            DESCRIPTION                                                 AMOUNT
            --------------------------------------------------------------------
            Professional Consultation Fee ..................... $ %10.2f
            Laboratory & Other Expenses ....................... $ %10.2f
            --------------------------------------------------------------------
            Gross Subtotal                                     $ %10.2f
            Discretionary Discount (%.1f%%) .................... -$ %10.2f
            ====================================================================
            TOTAL AMOUNT DUE / SETTLED                         $ %10.2f
            ====================================================================
            Thank you for choosing Philadelphia Medical Center.
            Prompt payment ensures uninterrupted healthcare excellence.
            ====================================================================
            """.formatted(
                bill.getBillId(),
                bill.getPatientId(),
                bill.getPatientName(),
                bill.getDoctorName(),
                bill.getBillDate().format(FORMATTER),
                bill.getModeOfPayment(),
                bill.getPaymentStatus(),
                consFees,
                otherExpenses,
                subtotal,
                bill.getDiscountPercent(),
                discount,
                total
        );
    }

    public Path saveReceiptToFile(Bill bill) throws IOException
    {
        Path dir = Paths.get("receipts");
        if (Files.notExists(dir))
        {
            Files.createDirectories(dir);
        }

        String fileName = "INV-" + bill.getBillId() + "_Receipt.txt";
        Path targetPath = dir.resolve(fileName);
        String receipt = generateReceiptText(bill);

        Files.writeString(targetPath, receipt, StandardCharsets.UTF_8);
        System.out.println("[InvoiceReportService] Saved receipt file to: " + targetPath.toAbsolutePath());
        return targetPath;
    }
}
