package com.adse2509.mediconnect.test;

import com.adse2509.mediconnect.dao.*;
import com.adse2509.mediconnect.models.*;
import com.adse2509.mediconnect.services.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Headless Automated Test Harness for MediConnect.
 * Verifies core functionality without requiring a graphical display environment.
 *
 * @author Group 3
 */
public class MediConnectTest
{
    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args)
    {
        System.out.println("=================================================");
        System.out.println("    RUNNING MEDICONNECT AUTOMATED TEST SUITE     ");
        System.out.println("   Philadelphia Medical Center (PMC Case Study)  ");
        System.out.println("=================================================");

        testStaffAuthentication();
        testDoctorDAO();
        testPatientDAO();
        testPatientDetailsDAO();
        testBillingCalculationsAndTransaction();
        testDirectoryServiceSearch();
        testInvoiceReportServiceNio2();

        System.out.println("=================================================");
        System.out.printf("TEST RESULTS: %d PASSED, %d FAILED%n", testsPassed, testsFailed);
        System.out.println("=================================================");

        if (testsFailed > 0)
        {
            System.exit(1);
        }
    }

    private static void testStaffAuthentication()
    {
        System.out.print("[TEST 1] Testing Staff Authentication (dbo.Staff)... ");
        try
        {
            StaffDAO dao = new StaffDAO();
            Staff valid = dao.authenticate("ST01", "admin123");
            Staff invalid = dao.authenticate("ST01", "wrongpass");

            if (valid != null && "ST01".equals(valid.getStaffId()) && invalid == null)
            {
                System.out.println("PASSED (Staff ST01 verified)");
                testsPassed++;
            }
            else
            {
                System.out.println("FAILED (Authentication mismatch)");
                testsFailed++;
            }
        }
        catch (Exception ex)
        {
            System.out.println("FAILED with exception: " + ex.getMessage());
            testsFailed++;
        }
    }

    private static void testDoctorDAO()
    {
        System.out.print("[TEST 2] Testing DoctorDAO CRUD Operations... ");
        try
        {
            DoctorDAO dao = new DoctorDAO();
            Doctor doc = dao.getById("D001");
            if (doc != null && doc.getName().contains("Sarah"))
            {
                System.out.println("PASSED (Found Dr. Sarah Jenkins)");
                testsPassed++;
            }
            else
            {
                System.out.println("FAILED (Could not find D001)");
                testsFailed++;
            }
        }
        catch (Exception ex)
        {
            System.out.println("FAILED with exception: " + ex.getMessage());
            testsFailed++;
        }
    }

    private static void testPatientDAO()
    {
        System.out.print("[TEST 3] Testing PatientDAO CRUD Operations... ");
        try
        {
            PatientDAO dao = new PatientDAO();
            List<Patient> patients = dao.getAll();
            if (!patients.isEmpty())
            {
                System.out.println("PASSED (Found " + patients.size() + " patients)");
                testsPassed++;
            }
            else
            {
                System.out.println("FAILED (Empty patient list)");
                testsFailed++;
            }
        }
        catch (Exception ex)
        {
            System.out.println("FAILED with exception: " + ex.getMessage());
            testsFailed++;
        }
    }

    private static void testPatientDetailsDAO()
    {
        System.out.print("[TEST 4] Testing PatientDetailsDAO (Checkup Visits)... ");
        try
        {
            PatientDetailsDAO dao = new PatientDetailsDAO();
            List<PatientDetails> list = dao.getAll();
            if (!list.isEmpty())
            {
                System.out.println("PASSED (Found " + list.size() + " checkup visit records)");
                testsPassed++;
            }
            else
            {
                System.out.println("FAILED (Empty details list)");
                testsFailed++;
            }
        }
        catch (Exception ex)
        {
            System.out.println("FAILED with exception: " + ex.getMessage());
            testsFailed++;
        }
    }

    private static void testBillingCalculationsAndTransaction()
    {
        System.out.print("[TEST 5] Testing Billing Service & ACID Transaction... ");
        try
        {
            BillingService service = new BillingService();
            double subtotal = service.calculateSubtotal(150.0, 50.0);
            double net = service.calculateTotalPayable(150.0, 50.0, 10.0);

            if (subtotal != 200.0 || net != 180.0)
            {
                System.out.println("FAILED (Calculation error: subtotal=" + subtotal + ", net=" + net + ")");
                testsFailed++;
                return;
            }

            Bill bill = new Bill(1099, "P001", LocalDateTime.now(), net, "Cash");
            bill.setPatientName("Robert Chen");
            bill.setDoctorName("Dr. Sarah Jenkins");

            boolean success = service.processBillTransaction(bill);
            if (success)
            {
                System.out.println("PASSED (Bill #1099 processed: $" + net + ")");
                testsPassed++;
            }
            else
            {
                System.out.println("FAILED (Transaction returned false)");
                testsFailed++;
            }
        }
        catch (Exception ex)
        {
            System.out.println("FAILED with exception: " + ex.getMessage());
            testsFailed++;
        }
    }

    private static void testDirectoryServiceSearch()
    {
        System.out.print("[TEST 6] Testing DirectoryService Aggregation & Search... ");
        try
        {
            DirectoryService service = new DirectoryService();
            List<DirectoryItem> all = service.getAllDirectoryRecords();
            List<DirectoryItem> matches = service.search("Sarah");

            if (!all.isEmpty() && !matches.isEmpty())
            {
                System.out.println("PASSED (Aggregated: " + all.size() + " items, Search matches: " + matches.size() + ")");
                testsPassed++;
            }
            else
            {
                System.out.println("FAILED (Search returned no results)");
                testsFailed++;
            }
        }
        catch (Exception ex)
        {
            System.out.println("FAILED with exception: " + ex.getMessage());
            testsFailed++;
        }
    }

    private static void testInvoiceReportServiceNio2()
    {
        System.out.print("[TEST 7] Testing Java NIO.2 Printable Receipt Output... ");
        try
        {
            InvoiceReportService service = new InvoiceReportService();
            Bill bill = new Bill(9999, "P001", LocalDateTime.now(), 180.0, "Cash");
            bill.setPatientName("Robert Chen");
            bill.setDoctorName("Dr. Sarah Jenkins");
            bill.setConsFees(150.0);
            bill.setOtherExpenses(50.0);
            bill.setDiscountPercent(10.0);

            Path saved = service.saveReceiptToFile(bill);
            if (Files.exists(saved) && Files.size(saved) > 100)
            {
                System.out.println("PASSED (Saved to " + saved.getFileName() + ")");
                testsPassed++;
            }
            else
            {
                System.out.println("FAILED (File missing or empty)");
                testsFailed++;
            }
        }
        catch (Exception ex)
        {
            System.out.println("FAILED with exception: " + ex.getMessage());
            testsFailed++;
        }
    }
}
