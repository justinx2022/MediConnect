/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.adse2509.mediconnect.controllers;

import com.adse2509.mediconnect.dao.*;
import com.adse2509.mediconnect.models.*;
import com.adse2509.mediconnect.services.*;
import com.adse2509.mediconnect.utils.AlertHelper;
import com.adse2509.mediconnect.utils.ValidationHelper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.nio.file.Path;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the main application window.
 * 
 * @author Group 3
 */
public class MainController implements Initializable
{
    private final DoctorDAO doctorDAO = new DoctorDAO();
    private final PatientDAO patientDAO = new PatientDAO();
    private final PatientDetailsDAO patientDetailsDAO = new PatientDetailsDAO();
    private final BillDAO billDAO = new BillDAO();
    private final BillingService billingService = new BillingService(billDAO, patientDAO);
    private final DirectoryService directoryService = new DirectoryService(doctorDAO, patientDAO, patientDetailsDAO, billDAO);
    private final InvoiceReportService reportService = new InvoiceReportService();

    private final ObservableList<DirectoryItem> masterDirectoryData = FXCollections.observableArrayList();
    private FilteredList<DirectoryItem> filteredDirectoryData;

    @FXML private TabPane mainTabPane;
    @FXML private Label lblStaffName, lblStaffRole;
    @FXML private Button btnNavAdd, btnNavUpdate, btnNavBilling, btnNavDirectory, btnExit;

    // Screen 1: Add
    @FXML private RadioButton rbAddPatient, rbAddDoctor;
    @FXML private VBox boxPatientForm, boxDoctorForm;
    @FXML private TextField txtPatId, txtPatName, txtPatAddress, txtPatPhone, txtPatAge, txtPatWeight, txtPatHeight, txtPatAilment, txtPatDesc;
    @FXML private ComboBox<String> cmbPatGender, cmbPatBlood;
    @FXML private ComboBox<Doctor> cmbPatDoctor;
    @FXML private TextField txtDocId, txtDocName, txtDocSpec, txtDocPhone, txtDocEmail, txtDocRoom;

    // Screen 2: Update & Delete
    @FXML private RadioButton rbUpdDoctorType, rbUpdPatientType;
    @FXML private VBox boxUpdateDoctor, boxUpdatePatient;
    @FXML private ComboBox<Doctor> cmbUpdateDoctorSelect;
    @FXML private TextField txtUpdDocId, txtUpdDocName, txtUpdDocSpec, txtUpdDocPhone, txtUpdDocEmail, txtUpdDocRoom;
    @FXML private RadioButton rbStatusAvailable, rbStatusSurgery, rbStatusOffDuty;
    @FXML private ComboBox<Patient> cmbUpdatePatientSelect;
    @FXML private TextField txtUpdPatId, txtUpdPatName, txtUpdPatAddress, txtUpdPatPhone, txtUpdPatAge, txtUpdPatWeight, txtUpdPatHeight;
    @FXML private ComboBox<String> cmbUpdPatGender, cmbUpdPatBlood;

    // Screen 3: Billing
    @FXML private ComboBox<Patient> cmbBillPatient;
    @FXML private ComboBox<Doctor> cmbBillDoctor;
    @FXML private TextField txtBillConsFee, txtBillOtherExpenses, txtBillDiscount;
    @FXML private ComboBox<String> cmbBillPaymentMode;
    @FXML private Label lblSubtotal, lblDiscountAmount, lblTotalPayable;
    @FXML private TextArea txtReceiptPreview;

    // Screen 4: Directory & Queries
    @FXML private Label lblStatDoctors, lblStatPatients, lblStatVisits, lblDateQueryResult;
    @FXML private DatePicker dpVisitDateQuery;
    @FXML private TextField txtSearch;
    @FXML private ComboBox<String> cmbCategoryFilter;
    @FXML private TableView<DirectoryItem> tblDirectory;
    @FXML private TableColumn<DirectoryItem, String> colCategory, colRecordId, colPrimaryName, colSecondaryInfo, colDetail, colStatus;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        cmbPatGender.setItems(FXCollections.observableArrayList("M", "F", "O"));
        cmbPatGender.getSelectionModel().selectFirst();
        cmbPatBlood.setItems(FXCollections.observableArrayList("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"));
        cmbPatBlood.getSelectionModel().selectFirst();

        if (cmbUpdPatGender != null) cmbUpdPatGender.setItems(cmbPatGender.getItems());
        if (cmbUpdPatBlood != null) cmbUpdPatBlood.setItems(cmbPatBlood.getItems());

        cmbBillPaymentMode.setItems(FXCollections.observableArrayList("Cash", "Card", "Cheque", "Other"));
        cmbBillPaymentMode.getSelectionModel().selectFirst();

        cmbCategoryFilter.setItems(FXCollections.observableArrayList("All Categories", "Doctor", "Patient", "Checkup", "Invoice"));
        cmbCategoryFilter.getSelectionModel().selectFirst();

        loadDoctorsIntoControls();
        loadPatientsIntoControls();
        setupBillingListeners();
        setupDirectoryTable();
        refreshDirectory();
    }

    public void setStaffUser(Staff staff)
    {
        if (staff != null)
        {
            lblStaffName.setText(staff.getStaffName());
            lblStaffRole.setText("Staff ID: " + staff.getStaffId());
        }
    }

    // --------------------------------------------------
    // Navigation
    // --------------------------------------------------
    @FXML private void handleNavAdd() { selectTab(0, btnNavAdd); }
    @FXML private void handleNavUpdate() { selectTab(1, btnNavUpdate); }
    @FXML private void handleNavBilling() { selectTab(2, btnNavBilling); recalculateBill(); }
    @FXML private void handleNavDirectory() { selectTab(3, btnNavDirectory); refreshDirectory(); }

    private void selectTab(int index, Button btn)
    {
        mainTabPane.getSelectionModel().select(index);
        Button[] navs = {btnNavAdd, btnNavUpdate, btnNavBilling, btnNavDirectory};
        for (Button b : navs) b.getStyleClass().remove("active");
        if (!btn.getStyleClass().contains("active")) btn.getStyleClass().add("active");
    }

    @FXML
    private void handleExit()
    {
        if (AlertHelper.showConfirmation("Exit MediConnect", "Do you want to exit?"))
        {
            Platform.exit();
            System.exit(0);
        }
    }

    // --------------------------------------------------
    // Screen 1: Add Records
    // --------------------------------------------------
    @FXML
    private void handleRecordTypeToggle()
    {
        boolean isPatient = rbAddPatient.isSelected();
        boxPatientForm.setVisible(isPatient);
        boxPatientForm.setManaged(isPatient);
        boxDoctorForm.setVisible(!isPatient);
        boxDoctorForm.setManaged(!isPatient);
    }

    @FXML
    private void handleSavePatient()
    {
        String id = txtPatId.getText().trim();
        String name = txtPatName.getText().trim();
        if (!ValidationHelper.isNotEmpty(id) || !ValidationHelper.isNotEmpty(name))
        {
            AlertHelper.showWarning("Required Fields", "Patient ID and Full Name are required.");
            return;
        }

        int age = ValidationHelper.parseIntegerOrDefault(txtPatAge.getText(), 0);
        double weight = ValidationHelper.parseDoubleOrDefault(txtPatWeight.getText(), 0.0);
        double height = ValidationHelper.parseDoubleOrDefault(txtPatHeight.getText(), 0.0);
        Patient pat = new Patient(id, name, txtPatAddress.getText().trim(), txtPatPhone.getText().trim(), age, cmbPatGender.getValue(), cmbPatBlood.getValue(), weight, height, "Active");

        try
        {
            patientDAO.create(pat);
            String ailment = txtPatAilment.getText().trim();
            Doctor doc = cmbPatDoctor.getValue();
            if (!ailment.isEmpty() && doc != null)
            {
                PatientDetails pd = new PatientDetails(0, id, doc.getDoctorId(), LocalDateTime.now(), ailment, 1, txtPatDesc.getText().trim(), 150.0, 50.0);
                patientDetailsDAO.create(pd);
            }
            AlertHelper.showInfo("Saved", "Patient " + name + " (" + id + ") registered successfully.");
            handleClearPatientForm();
            loadPatientsIntoControls();
            refreshDirectory();
        }
        catch (SQLException ex)
        {
            AlertHelper.showError("Database Error", "Failed to save: " + ex.getMessage());
        }
    }

    @FXML
    private void handleClearPatientForm()
    {
        txtPatId.clear(); txtPatName.clear(); txtPatAddress.clear(); txtPatPhone.clear();
        txtPatAge.clear(); txtPatWeight.clear(); txtPatHeight.clear(); txtPatAilment.clear(); txtPatDesc.clear();
    }

    @FXML
    private void handleSaveDoctor()
    {
        String id = txtDocId.getText().trim();
        String name = txtDocName.getText().trim();
        String spec = txtDocSpec.getText().trim();
        if (!ValidationHelper.isNotEmpty(id) || !ValidationHelper.isNotEmpty(name) || !ValidationHelper.isNotEmpty(spec))
        {
            AlertHelper.showWarning("Required Fields", "Doctor ID, Name, and Specialization are required.");
            return;
        }

        String room = txtDocRoom.getText().trim();
        Doctor doc = new Doctor(id, name, spec, txtDocPhone.getText().trim(), txtDocEmail.getText().trim(), room.isEmpty() ? "Suite 101" : room, "Available");
        try
        {
            doctorDAO.create(doc);
            AlertHelper.showInfo("Saved", "Doctor " + name + " (" + id + ") registered successfully.");
            handleClearDoctorForm();
            loadDoctorsIntoControls();
            refreshDirectory();
        }
        catch (SQLException ex)
        {
            AlertHelper.showError("Database Error", "Failed to save: " + ex.getMessage());
        }
    }

    @FXML
    private void handleClearDoctorForm()
    {
        txtDocId.clear(); txtDocName.clear(); txtDocSpec.clear(); txtDocPhone.clear(); txtDocEmail.clear(); txtDocRoom.clear();
    }

    // --------------------------------------------------
    // Screen 2: Update & Delete Records
    // --------------------------------------------------
    @FXML
    private void handleUpdateRecordTypeToggle()
    {
        boolean isDoc = rbUpdDoctorType.isSelected();
        boxUpdateDoctor.setVisible(isDoc);
        boxUpdateDoctor.setManaged(isDoc);
        boxUpdatePatient.setVisible(!isDoc);
        boxUpdatePatient.setManaged(!isDoc);
    }

    @FXML
    public void loadDoctorsIntoControls()
    {
        try
        {
            List<Doctor> docs = doctorDAO.getAll();
            cmbPatDoctor.setItems(FXCollections.observableArrayList(docs));
            cmbUpdateDoctorSelect.setItems(FXCollections.observableArrayList(docs));
            cmbBillDoctor.setItems(FXCollections.observableArrayList(docs));
            if (!docs.isEmpty())
            {
                cmbPatDoctor.getSelectionModel().selectFirst();
                cmbBillDoctor.getSelectionModel().selectFirst();
            }
        }
        catch (SQLException ex) { ex.printStackTrace(); }
    }

    @FXML
    public void loadPatientsIntoControls()
    {
        try
        {
            List<Patient> pats = patientDAO.getAll();
            cmbBillPatient.setItems(FXCollections.observableArrayList(pats));
            if (cmbUpdatePatientSelect != null) cmbUpdatePatientSelect.setItems(FXCollections.observableArrayList(pats));
            if (!pats.isEmpty()) cmbBillPatient.getSelectionModel().selectFirst();
        }
        catch (SQLException ex) { ex.printStackTrace(); }
    }

    @FXML
    private void handleDoctorSelectedForUpdate()
    {
        Doctor doc = cmbUpdateDoctorSelect.getValue();
        if (doc == null) return;
        txtUpdDocId.setText(doc.getDoctorId());
        txtUpdDocName.setText(doc.getName());
        txtUpdDocSpec.setText(doc.getSpecialization());
        txtUpdDocPhone.setText(doc.getPhone());
        txtUpdDocEmail.setText(doc.getEmailId());
        txtUpdDocRoom.setText(doc.getRoomNumber());
        if ("In Surgery".equalsIgnoreCase(doc.getStatus())) rbStatusSurgery.setSelected(true);
        else if ("Off Duty".equalsIgnoreCase(doc.getStatus())) rbStatusOffDuty.setSelected(true);
        else rbStatusAvailable.setSelected(true);
    }

    @FXML
    private void handleUpdateDoctor()
    {
        String id = txtUpdDocId.getText().trim();
        if (id.isEmpty()) return;
        String status = rbStatusSurgery.isSelected() ? "In Surgery" : (rbStatusOffDuty.isSelected() ? "Off Duty" : "Available");
        Doctor doc = new Doctor(id, txtUpdDocName.getText().trim(), txtUpdDocSpec.getText().trim(), txtUpdDocPhone.getText().trim(), txtUpdDocEmail.getText().trim(), txtUpdDocRoom.getText().trim(), status);
        try
        {
            doctorDAO.update(doc);
            AlertHelper.showInfo("Updated", "Doctor details updated successfully.");
            loadDoctorsIntoControls();
            refreshDirectory();
        }
        catch (SQLException ex) { AlertHelper.showError("Error", ex.getMessage()); }
    }

    @FXML
    private void handleDeleteDoctor()
    {
        String id = txtUpdDocId.getText().trim();
        if (id.isEmpty()) return;
        if (AlertHelper.showConfirmation("Confirm Delete", "Delete doctor " + id + "?"))
        {
            try
            {
                doctorDAO.delete(id);
                AlertHelper.showInfo("Deleted", "Doctor removed successfully.");
                clearDoctorUpdateFields();
                loadDoctorsIntoControls();
                refreshDirectory();
            }
            catch (SQLException ex) { AlertHelper.showError("Error", ex.getMessage()); }
        }
    }

    private void clearDoctorUpdateFields()
    {
        txtUpdDocId.clear(); txtUpdDocName.clear(); txtUpdDocSpec.clear(); txtUpdDocPhone.clear(); txtUpdDocEmail.clear(); txtUpdDocRoom.clear();
        rbStatusAvailable.setSelected(true);
    }

    @FXML
    private void handlePatientSelectedForUpdate()
    {
        Patient pat = cmbUpdatePatientSelect.getValue();
        if (pat == null) return;
        txtUpdPatId.setText(pat.getPatientId());
        txtUpdPatName.setText(pat.getName());
        txtUpdPatAddress.setText(pat.getAddress());
        txtUpdPatPhone.setText(pat.getPhone());
        txtUpdPatAge.setText(String.valueOf(pat.getAge()));
        if (pat.getGender() != null) cmbUpdPatGender.getSelectionModel().select(pat.getGender());
        if (pat.getBloodGroup() != null) cmbUpdPatBlood.getSelectionModel().select(pat.getBloodGroup());
        txtUpdPatWeight.setText(String.valueOf(pat.getWeight()));
        txtUpdPatHeight.setText(String.valueOf(pat.getHeight()));
    }

    @FXML
    private void handleUpdatePatient()
    {
        String id = txtUpdPatId.getText().trim();
        if (id.isEmpty()) return;
        int age = ValidationHelper.parseIntegerOrDefault(txtUpdPatAge.getText(), 0);
        double weight = ValidationHelper.parseDoubleOrDefault(txtUpdPatWeight.getText(), 0.0);
        double height = ValidationHelper.parseDoubleOrDefault(txtUpdPatHeight.getText(), 0.0);
        Patient pat = new Patient(id, txtUpdPatName.getText().trim(), txtUpdPatAddress.getText().trim(), txtUpdPatPhone.getText().trim(), age, cmbUpdPatGender.getValue(), cmbUpdPatBlood.getValue(), weight, height, "Active");
        try
        {
            patientDAO.update(pat);
            AlertHelper.showInfo("Updated", "Patient details updated successfully.");
            loadPatientsIntoControls();
            refreshDirectory();
        }
        catch (SQLException ex) { AlertHelper.showError("Error", ex.getMessage()); }
    }

    @FXML
    private void handleDeletePatient()
    {
        String id = txtUpdPatId.getText().trim();
        if (id.isEmpty()) return;
        if (AlertHelper.showConfirmation("Confirm Delete", "Delete patient " + id + "?"))
        {
            try
            {
                patientDAO.delete(id);
                AlertHelper.showInfo("Deleted", "Patient removed successfully.");
                clearPatientUpdateFields();
                loadPatientsIntoControls();
                refreshDirectory();
            }
            catch (SQLException ex) { AlertHelper.showError("Error", ex.getMessage()); }
        }
    }

    private void clearPatientUpdateFields()
    {
        txtUpdPatId.clear(); txtUpdPatName.clear(); txtUpdPatAddress.clear(); txtUpdPatPhone.clear();
        txtUpdPatAge.clear(); txtUpdPatWeight.clear(); txtUpdPatHeight.clear();
    }

    // --------------------------------------------------
    // Screen 3: Billing
    // --------------------------------------------------
    private void setupBillingListeners()
    {
        txtBillConsFee.textProperty().addListener((o, ov, nv) -> recalculateBill());
        txtBillOtherExpenses.textProperty().addListener((o, ov, nv) -> recalculateBill());
        txtBillDiscount.textProperty().addListener((o, ov, nv) -> recalculateBill());
    }

    @FXML private void handleBillPatientSelected() { recalculateBill(); }
    @FXML private void handleDiscount0() { txtBillDiscount.setText("0.0"); }
    @FXML private void handleDiscount5() { txtBillDiscount.setText("5.0"); }
    @FXML private void handleDiscount10() { txtBillDiscount.setText("10.0"); }
    @FXML private void handleDiscount15() { txtBillDiscount.setText("15.0"); }
    @FXML private void handleDiscount20() { txtBillDiscount.setText("20.0"); }

    private void recalculateBill()
    {
        double cons = ValidationHelper.parseDoubleOrDefault(txtBillConsFee.getText(), 150.0);
        double other = ValidationHelper.parseDoubleOrDefault(txtBillOtherExpenses.getText(), 50.0);
        double disc = ValidationHelper.parseDoubleOrDefault(txtBillDiscount.getText(), 0.0);

        double subtotal = billingService.calculateSubtotal(cons, other);
        double discAmt = billingService.calculateDiscountAmount(subtotal, disc);
        double total = billingService.calculateTotalPayable(cons, other, disc);

        lblSubtotal.setText(String.format("%.2f", subtotal));
        lblDiscountAmount.setText(String.format("%.2f", discAmt));
        lblTotalPayable.setText(String.format("%.2f", total));

        Bill bill = buildCurrentBill(total, cons, other, disc);
        txtReceiptPreview.setText(reportService.generateReceiptText(bill));
    }

    private Bill buildCurrentBill(double net, double cons, double other, double disc)
    {
        Patient p = cmbBillPatient.getValue();
        Doctor d = cmbBillDoctor.getValue();
        Bill b = new Bill(0, p != null ? p.getPatientId() : "P001", LocalDateTime.now(), net, cmbBillPaymentMode.getValue());
        b.setPatientName(p != null ? p.getName() : "Patient");
        b.setDoctorName(d != null ? d.getName() : "Attending Doctor");
        b.setConsFees(cons);
        b.setOtherExpenses(other);
        b.setDiscountPercent(disc);
        return b;
    }

    @FXML
    private void handleProcessBill()
    {
        Patient p = cmbBillPatient.getValue();
        if (p == null)
        {
            AlertHelper.showWarning("Required", "Please select a patient.");
            return;
        }

        double cons = ValidationHelper.parseDoubleOrDefault(txtBillConsFee.getText(), 150.0);
        double other = ValidationHelper.parseDoubleOrDefault(txtBillOtherExpenses.getText(), 50.0);
        double disc = ValidationHelper.parseDoubleOrDefault(txtBillDiscount.getText(), 0.0);
        double total = billingService.calculateTotalPayable(cons, other, disc);

        Bill bill = buildCurrentBill(total, cons, other, disc);
        try
        {
            billingService.processBillTransaction(bill);
            Path receiptPath = reportService.saveReceiptToFile(bill);
            AlertHelper.showInfo("Processed", "Bill processed for " + bill.getPatientName() + " (Total: " + String.format("%.2f", total) + ")");
            if (AlertHelper.showConfirmation("Receipt", "Would you like to open the receipt?"))
            {
                try { java.awt.Desktop.getDesktop().open(receiptPath.toFile()); } catch (Exception ignored) {}
            }
            refreshDirectory();
        }
        catch (Exception ex) { AlertHelper.showError("Error", ex.getMessage()); }
    }

    // --------------------------------------------------
    // Screen 4: Directory & Queries
    // --------------------------------------------------
    private void setupDirectoryTable()
    {
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colRecordId.setCellValueFactory(new PropertyValueFactory<>("recordId"));
        colPrimaryName.setCellValueFactory(new PropertyValueFactory<>("primaryName"));
        colSecondaryInfo.setCellValueFactory(new PropertyValueFactory<>("secondaryInfo"));
        colDetail.setCellValueFactory(new PropertyValueFactory<>("detail"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        filteredDirectoryData = new FilteredList<>(masterDirectoryData, item -> true);
        tblDirectory.setItems(filteredDirectoryData);
        txtSearch.textProperty().addListener((o, ov, nv) -> applyFilter());
    }

    @FXML private void handleCategoryFilterChanged() { applyFilter(); }

    private void applyFilter()
    {
        String q = txtSearch.getText() != null ? txtSearch.getText().toLowerCase().trim() : "";
        String cat = cmbCategoryFilter.getValue();

        filteredDirectoryData.setPredicate(item -> {
            boolean matchCat = cat == null || "All Categories".equals(cat) || item.getCategory().equalsIgnoreCase(cat);
            if (!matchCat) return false;
            if (q.isEmpty()) return true;
            return item.getPrimaryName().toLowerCase().contains(q)
                    || item.getRecordId().toLowerCase().contains(q)
                    || item.getSecondaryInfo().toLowerCase().contains(q)
                    || item.getDetail().toLowerCase().contains(q);
        });
    }

    @FXML public void handleRefreshDirectory() { refreshDirectory(); }

    private void refreshDirectory()
    {
        try
        {
            masterDirectoryData.setAll(directoryService.getAllDirectoryRecords());
            applyFilter();
            if (lblStatDoctors != null) lblStatDoctors.setText("Doctors: " + directoryService.getDoctorCount());
            if (lblStatPatients != null) lblStatPatients.setText("Patients: " + directoryService.getPatientCount());
            if (lblStatVisits != null) lblStatVisits.setText("Visits: " + directoryService.getCheckupCount());
        }
        catch (SQLException ex) { ex.printStackTrace(); }
    }

    @FXML
    private void handleQueryPatientsByDate()
    {
        if (dpVisitDateQuery == null || dpVisitDateQuery.getValue() == null)
        {
            AlertHelper.showWarning("Date Required", "Please select a date to query visits.");
            return;
        }

        LocalDate d = dpVisitDateQuery.getValue();
        try
        {
            List<DirectoryItem> visits = directoryService.getVisitsByDate(d);
            if (lblDateQueryResult != null) lblDateQueryResult.setText("Found: " + visits.size() + " on " + d);
            masterDirectoryData.setAll(visits);
            applyFilter();
        }
        catch (SQLException ex) { AlertHelper.showError("Error", ex.getMessage()); }
    }

    @FXML
    private void handleClearDateQuery()
    {
        if (dpVisitDateQuery != null) dpVisitDateQuery.setValue(null);
        if (lblDateQueryResult != null) lblDateQueryResult.setText("");
        refreshDirectory();
    }
}
