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
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Main Controller for the MediConnect JavaFX front-end.
 * Bridges FXML views with DAOs and Service layers, adhering to MVC architecture.
 *
 * @author Group 3
 */
public class MainController implements Initializable
{
    // Services and DAOs
    private final DoctorDAO doctorDAO = new DoctorDAO();
    private final PatientDAO patientDAO = new PatientDAO();
    private final PatientDetailsDAO patientDetailsDAO = new PatientDetailsDAO();
    private final BillDAO billDAO = new BillDAO();
    private final BillingService billingService = new BillingService(billDAO, patientDAO);
    private final DirectoryService directoryService = new DirectoryService(doctorDAO, patientDAO, patientDetailsDAO, billDAO);
    private final InvoiceReportService reportService = new InvoiceReportService();

    // Directory observable list and filtered wrapper
    private final ObservableList<DirectoryItem> masterDirectoryData = FXCollections.observableArrayList();
    private FilteredList<DirectoryItem> filteredDirectoryData;

    /* ==================================================================================
     * FXML CONTROLS INJECTION
     * ================================================================================== */

    @FXML private TabPane mainTabPane;
    @FXML private Label lblStaffName;
    @FXML private Label lblStaffRole;
    @FXML private Label lblDbStatus;
    @FXML private Button btnNavAdd;
    @FXML private Button btnNavUpdate;
    @FXML private Button btnNavBilling;
    @FXML private Button btnNavDirectory;
    @FXML private Button btnExit;

    // Screen 1: Add New Records
    @FXML private RadioButton rbAddPatient;
    @FXML private RadioButton rbAddDoctor;
    @FXML private VBox boxPatientForm;
    @FXML private VBox boxDoctorForm;
    @FXML private TextField txtPatId;
    @FXML private TextField txtPatName;
    @FXML private TextField txtPatAddress;
    @FXML private TextField txtPatPhone;
    @FXML private TextField txtPatAge;
    @FXML private ComboBox<String> cmbPatGender;
    @FXML private ComboBox<String> cmbPatBlood;
    @FXML private TextField txtPatWeight;
    @FXML private TextField txtPatHeight;
    @FXML private ComboBox<Doctor> cmbPatDoctor;
    @FXML private TextField txtPatAilment;
    @FXML private TextField txtPatDesc;
    @FXML private Label toastLabel1;

    // Screen 1 Doctor fields
    @FXML private TextField txtDocId;
    @FXML private TextField txtDocName;
    @FXML private TextField txtDocSpec;
    @FXML private TextField txtDocPhone;
    @FXML private TextField txtDocEmail;
    @FXML private TextField txtDocRoom;

    // Screen 2: Update Details
    @FXML private ComboBox<Doctor> cmbUpdateDoctorSelect;
    @FXML private TextField txtUpdDocId;
    @FXML private TextField txtUpdDocName;
    @FXML private TextField txtUpdDocSpec;
    @FXML private TextField txtUpdDocPhone;
    @FXML private TextField txtUpdDocEmail;
    @FXML private TextField txtUpdDocRoom;
    @FXML private RadioButton rbStatusAvailable;
    @FXML private RadioButton rbStatusSurgery;
    @FXML private RadioButton rbStatusOffDuty;
    @FXML private Label toastLabel2;

    // Screen 3: Generate Bills
    @FXML private ComboBox<Patient> cmbBillPatient;
    @FXML private ComboBox<Doctor> cmbBillDoctor;
    @FXML private TextField txtBillConsFee;
    @FXML private TextField txtBillOtherExpenses;
    @FXML private ComboBox<String> cmbBillPaymentMode;
    @FXML private TextField txtBillDiscount;
    @FXML private Label lblSubtotal;
    @FXML private Label lblDiscountAmount;
    @FXML private Label lblTotalPayable;
    @FXML private TextArea txtReceiptPreview;
    @FXML private Label toastLabel3;

    // Screen 4: Display Information
    @FXML private TextField txtSearch;
    @FXML private ComboBox<String> cmbCategoryFilter;
    @FXML private TableView<DirectoryItem> tblDirectory;
    @FXML private TableColumn<DirectoryItem, String> colCategory;
    @FXML private TableColumn<DirectoryItem, String> colRecordId;
    @FXML private TableColumn<DirectoryItem, String> colPrimaryName;
    @FXML private TableColumn<DirectoryItem, String> colSecondaryInfo;
    @FXML private TableColumn<DirectoryItem, String> colDetail;
    @FXML private TableColumn<DirectoryItem, String> colStatus;

    /* ==================================================================================
     * INITIALIZATION
     * ================================================================================== */

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        // 1. Check database connectivity
        boolean online = DatabaseConnection.isServerAvailable();
        lblDbStatus.setText(online ? "● SQL Server: Online" : "● Offline Demo Mode (Memory Store)");
        lblDbStatus.setStyle(online ? "-fx-text-fill: #10B981;" : "-fx-text-fill: #F59E0B;");

        // 2. Initialize dropdown values
        cmbPatGender.setItems(FXCollections.observableArrayList("M", "F", "O"));
        cmbPatGender.getSelectionModel().select("M");

        cmbPatBlood.setItems(FXCollections.observableArrayList("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"));
        cmbPatBlood.getSelectionModel().select("O+");

        cmbBillPaymentMode.setItems(FXCollections.observableArrayList("Cash", "Card", "Cheque", "Other"));
        cmbBillPaymentMode.getSelectionModel().select("Cash");

        cmbCategoryFilter.setItems(FXCollections.observableArrayList("All Categories", "Doctor", "Patient", "Checkup", "Invoice"));
        cmbCategoryFilter.getSelectionModel().selectFirst();

        // 3. Load dynamic entities into combo boxes
        loadDoctorsIntoControls();
        loadPatientsIntoControls();

        // 4. Attach real-time calculation listeners to billing fields
        setupBillingCalculationListeners();

        // 5. Setup Directory TableView
        setupDirectoryTable();

        // 6. Refresh directory records
        refreshDirectory();
    }

    public void setStaffUser(Staff staff)
    {
        if (staff != null)
        {
            lblStaffName.setText(staff.getStaffName());
            lblStaffRole.setText("Staff ID: " + staff.getStaffId() + " (PMC)");
        }
    }

    /* ==================================================================================
     * SIDEBAR NAVIGATION HANDLERS (Matching Flowchart Figure 1.2)
     * ================================================================================== */

    @FXML
    private void handleNavAdd()
    {
        mainTabPane.getSelectionModel().select(0);
        updateNavButtonActive(btnNavAdd);
    }

    @FXML
    private void handleNavUpdate()
    {
        mainTabPane.getSelectionModel().select(1);
        updateNavButtonActive(btnNavUpdate);
    }

    @FXML
    private void handleNavBilling()
    {
        mainTabPane.getSelectionModel().select(2);
        updateNavButtonActive(btnNavBilling);
        recalculateBill();
    }

    @FXML
    private void handleNavDirectory()
    {
        mainTabPane.getSelectionModel().select(3);
        updateNavButtonActive(btnNavDirectory);
        refreshDirectory();
    }

    @FXML
    private void handleExit()
    {
        boolean confirm = AlertHelper.showConfirmation("Exit MediConnect", "Are you sure you want to exit the application?");
        if (confirm)
        {
            Platform.exit();
            System.exit(0);
        }
    }

    private void updateNavButtonActive(Button activeBtn)
    {
        Button[] buttons = {btnNavAdd, btnNavUpdate, btnNavBilling, btnNavDirectory};
        for (Button btn : buttons)
        {
            btn.getStyleClass().remove("active");
        }
        if (!activeBtn.getStyleClass().contains("active"))
        {
            activeBtn.getStyleClass().add("active");
        }
    }

    /* ==================================================================================
     * SCREEN 1: ADD NEW RECORDS
     * ================================================================================== */

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
        String address = txtPatAddress.getText().trim();
        String phone = txtPatPhone.getText().trim();
        String ageStr = txtPatAge.getText().trim();
        String gender = cmbPatGender.getValue();
        String blood = cmbPatBlood.getValue();
        String weightStr = txtPatWeight.getText().trim();
        String heightStr = txtPatHeight.getText().trim();

        if (!ValidationHelper.isNotEmpty(id) || !ValidationHelper.isNotEmpty(name) || !ValidationHelper.isNotEmpty(address))
        {
            AlertHelper.showToast(toastLabel1, "Please fill in all mandatory patient fields.", false);
            return;
        }

        int age;
        double weight, height;
        try
        {
            age = Integer.parseInt(ageStr);
            weight = Double.parseDouble(weightStr.isEmpty() ? "70.0" : weightStr);
            height = Double.parseDouble(heightStr.isEmpty() ? "170.0" : heightStr);
        }
        catch (NumberFormatException nfe)
        {
            AlertHelper.showToast(toastLabel1, "Age, weight, and height must be valid numbers.", false);
            return;
        }

        Patient patient = new Patient(id, name, phone, address, gender, age, blood, weight, height, "Active Record");

        try
        {
            patientDAO.create(patient);

            // If checkup visit details provided, save to PatientDetails table
            Doctor doc = cmbPatDoctor.getValue();
            String ailment = txtPatAilment.getText().trim();
            if (doc != null && !ailment.isEmpty())
            {
                String pdId = "PD" + (int)(Math.random() * 900 + 100);
                PatientDetails details = new PatientDetails(pdId, doc.getDoctorId(), id, ailment, txtPatDesc.getText().trim(), 1, 150, 0);
                patientDetailsDAO.create(details);
            }

            AlertHelper.showToast(toastLabel1, "Patient " + name + " (" + id + ") registered successfully!", true);
            handleClearPatientForm();
            loadPatientsIntoControls();
            refreshDirectory();
        }
        catch (SQLException ex)
        {
            AlertHelper.showError("Database Error", "Failed to save patient: " + ex.getMessage());
        }
    }

    @FXML
    private void handleClearPatientForm()
    {
        txtPatId.clear();
        txtPatName.clear();
        txtPatAddress.clear();
        txtPatPhone.clear();
        txtPatAge.clear();
        txtPatWeight.clear();
        txtPatHeight.clear();
        txtPatAilment.clear();
        txtPatDesc.clear();
    }

    @FXML
    private void handleSaveDoctor()
    {
        String id = txtDocId.getText().trim();
        String name = txtDocName.getText().trim();
        String spec = txtDocSpec.getText().trim();
        String phone = txtDocPhone.getText().trim();
        String email = txtDocEmail.getText().trim();
        String room = txtDocRoom.getText().trim();

        if (!ValidationHelper.isNotEmpty(id) || !ValidationHelper.isNotEmpty(name) || !ValidationHelper.isNotEmpty(spec))
        {
            AlertHelper.showToast(toastLabel1, "Doctor ID, Name, and Specialization are required.", false);
            return;
        }

        Doctor doc = new Doctor(id, name, spec, phone, email, room.isEmpty() ? "Suite 101" : room, "Available");
        try
        {
            doctorDAO.create(doc);
            AlertHelper.showToast(toastLabel1, "Doctor " + name + " (" + id + ") registered successfully!", true);
            handleClearDoctorForm();
            loadDoctorsIntoControls();
            refreshDirectory();
        }
        catch (SQLException ex)
        {
            AlertHelper.showError("Database Error", "Failed to save doctor: " + ex.getMessage());
        }
    }

    @FXML
    private void handleClearDoctorForm()
    {
        txtDocId.clear();
        txtDocName.clear();
        txtDocSpec.clear();
        txtDocPhone.clear();
        txtDocEmail.clear();
        txtDocRoom.clear();
    }

    /* ==================================================================================
     * SCREEN 2: UPDATE DETAILS
     * ================================================================================== */

    @FXML
    public void loadDoctorsIntoControls()
    {
        try
        {
            List<Doctor> doctors = doctorDAO.getAll();
            cmbPatDoctor.setItems(FXCollections.observableArrayList(doctors));
            cmbUpdateDoctorSelect.setItems(FXCollections.observableArrayList(doctors));
            cmbBillDoctor.setItems(FXCollections.observableArrayList(doctors));
            if (!doctors.isEmpty())
            {
                cmbPatDoctor.getSelectionModel().selectFirst();
                cmbBillDoctor.getSelectionModel().selectFirst();
            }
        }
        catch (SQLException ex)
        {
            System.err.println("Error loading doctors: " + ex.getMessage());
        }
    }

    private void loadPatientsIntoControls()
    {
        try
        {
            List<Patient> patients = patientDAO.getAll();
            cmbBillPatient.setItems(FXCollections.observableArrayList(patients));
            if (!patients.isEmpty())
            {
                cmbBillPatient.getSelectionModel().selectFirst();
            }
        }
        catch (SQLException ex)
        {
            System.err.println("Error loading patients: " + ex.getMessage());
        }
    }

    @FXML
    private void handleDoctorSelectedForUpdate()
    {
        Doctor doc = cmbUpdateDoctorSelect.getValue();
        if (doc != null)
        {
            txtUpdDocId.setText(doc.getDoctorId());
            txtUpdDocName.setText(doc.getName());
            txtUpdDocSpec.setText(doc.getSpecialization());
            txtUpdDocPhone.setText(doc.getPhone());
            txtUpdDocEmail.setText(doc.getEmailId());
            txtUpdDocRoom.setText(doc.getRoomNumber());

            String status = doc.getStatus();
            if ("In Surgery".equalsIgnoreCase(status))
            {
                rbStatusSurgery.setSelected(true);
            }
            else if ("Off Duty".equalsIgnoreCase(status))
            {
                rbStatusOffDuty.setSelected(true);
            }
            else
            {
                rbStatusAvailable.setSelected(true);
            }
        }
    }

    @FXML
    private void handleUpdateDoctor()
    {
        String id = txtUpdDocId.getText().trim();
        if (id.isEmpty())
        {
            AlertHelper.showToast(toastLabel2, "Please select a doctor to update.", false);
            return;
        }

        String name = txtUpdDocName.getText().trim();
        String spec = txtUpdDocSpec.getText().trim();
        String phone = txtUpdDocPhone.getText().trim();
        String email = txtUpdDocEmail.getText().trim();
        String room = txtUpdDocRoom.getText().trim();

        String status = "Available";
        if (rbStatusSurgery.isSelected()) status = "In Surgery";
        if (rbStatusOffDuty.isSelected()) status = "Off Duty";

        Doctor doc = new Doctor(id, name, spec, phone, email, room, status);
        try
        {
            doctorDAO.update(doc);
            AlertHelper.showToast(toastLabel2, "Doctor " + name + " details updated successfully!", true);
            loadDoctorsIntoControls();
            refreshDirectory();
        }
        catch (SQLException ex)
        {
            AlertHelper.showError("Database Error", "Failed to update doctor: " + ex.getMessage());
        }
    }

    /* ==================================================================================
     * SCREEN 3: GENERATE BILLS
     * ================================================================================== */

    private void setupBillingCalculationListeners()
    {
        txtBillConsFee.textProperty().addListener((obs, oldV, newV) -> recalculateBill());
        txtBillOtherExpenses.textProperty().addListener((obs, oldV, newV) -> recalculateBill());
        txtBillDiscount.textProperty().addListener((obs, oldV, newV) -> recalculateBill());
    }

    @FXML
    private void handleBillPatientSelected()
    {
        recalculateBill();
    }

    @FXML private void handleDiscount0() { txtBillDiscount.setText("0.0"); }
    @FXML private void handleDiscount5() { txtBillDiscount.setText("5.0"); }
    @FXML private void handleDiscount10() { txtBillDiscount.setText("10.0"); }
    @FXML private void handleDiscount15() { txtBillDiscount.setText("15.0"); }
    @FXML private void handleDiscount20() { txtBillDiscount.setText("20.0"); }

    private void recalculateBill()
    {
        double cons = parseDoubleSafe(txtBillConsFee.getText(), 150.0);
        double other = parseDoubleSafe(txtBillOtherExpenses.getText(), 50.0);
        double discPercent = parseDoubleSafe(txtBillDiscount.getText(), 0.0);

        double subtotal = billingService.calculateSubtotal(cons, other);
        double discAmount = billingService.calculateDiscountAmount(subtotal, discPercent);
        double netPayable = billingService.calculateTotalPayable(cons, other, discPercent);

        lblSubtotal.setText(String.format("$%.2f", subtotal));
        lblDiscountAmount.setText(String.format("-$%.2f", discAmount));
        lblTotalPayable.setText(String.format("$%.2f", netPayable));

        // Generate live text preview
        Bill preview = buildCurrentBill(netPayable, cons, other, discPercent);
        txtReceiptPreview.setText(reportService.generateReceiptText(preview));
    }

    private double parseDoubleSafe(String val, double defaultVal)
    {
        try
        {
            return Double.parseDouble(val.trim());
        }
        catch (Exception e)
        {
            return defaultVal;
        }
    }

    private Bill buildCurrentBill(double netAmount, double cons, double other, double discount)
    {
        Patient p = cmbBillPatient.getValue();
        Doctor d = cmbBillDoctor.getValue();
        String patId = (p != null) ? p.getPatientId() : "P001";
        String patName = (p != null) ? p.getName() : "Sample Patient";
        String docName = (d != null) ? d.getName() : "Attending Physician";

        Bill bill = new Bill(0, patId, LocalDateTime.now(), netAmount, cmbBillPaymentMode.getValue());
        bill.setPatientName(patName);
        bill.setDoctorName(docName);
        bill.setConsFees(cons);
        bill.setOtherExpenses(other);
        bill.setDiscountPercent(discount);
        return bill;
    }

    @FXML
    private void handleProcessBill()
    {
        Patient patient = cmbBillPatient.getValue();
        if (patient == null)
        {
            AlertHelper.showToast(toastLabel3, "Please select a patient for billing.", false);
            return;
        }

        double cons = parseDoubleSafe(txtBillConsFee.getText(), 150.0);
        double other = parseDoubleSafe(txtBillOtherExpenses.getText(), 50.0);
        double discPercent = parseDoubleSafe(txtBillDiscount.getText(), 0.0);
        double netPayable = billingService.calculateTotalPayable(cons, other, discPercent);

        Bill bill = buildCurrentBill(netPayable, cons, other, discPercent);

        try
        {
            billingService.processBillTransaction(bill);
            Path receiptPath = reportService.saveReceiptToFile(bill);

            AlertHelper.showToast(toastLabel3, "Bill processed and receipt generated for " + bill.getPatientName() + "!", true);

            boolean printConfirm = AlertHelper.showConfirmation(
                    "Print Receipt Confirmation",
                    "Bill successfully settled for $" + String.format("%.2f", netPayable) + ".\n\n"
                            + "Receipt saved to:\n" + receiptPath.toAbsolutePath() + "\n\n"
                            + "Would you like to open the receipt document now?"
            );

            if (printConfirm)
            {
                try
                {
                    java.awt.Desktop.getDesktop().open(receiptPath.toFile());
                }
                catch (Exception ignored)
                {
                }
            }

            refreshDirectory();
        }
        catch (Exception ex)
        {
            AlertHelper.showError("Billing Transaction Error", "Failed to process bill: " + ex.getMessage());
        }
    }

    /* ==================================================================================
     * SCREEN 4: DISPLAY INFORMATION (Directory & Real-time Search)
     * ================================================================================== */

    private void setupDirectoryTable()
    {
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colRecordId.setCellValueFactory(new PropertyValueFactory<>("recordId"));
        colPrimaryName.setCellValueFactory(new PropertyValueFactory<>("primaryName"));
        colSecondaryInfo.setCellValueFactory(new PropertyValueFactory<>("secondaryInfo"));
        colDetail.setCellValueFactory(new PropertyValueFactory<>("detail"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        filteredDirectoryData = new FilteredList<>(masterDirectoryData, p -> true);
        tblDirectory.setItems(filteredDirectoryData);

        // Real-time search filter listener
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> applyDirectoryFilter());
    }

    @FXML
    private void handleCategoryFilterChanged()
    {
        applyDirectoryFilter();
    }

    private void applyDirectoryFilter()
    {
        String query = txtSearch.getText();
        String selectedCategory = cmbCategoryFilter.getValue();

        filteredDirectoryData.setPredicate(item -> {
            boolean matchesCategory = (selectedCategory == null
                    || "All Categories".equals(selectedCategory)
                    || item.getCategory().equalsIgnoreCase(selectedCategory));

            if (!matchesCategory) return false;

            if (query == null || query.trim().isEmpty()) return true;

            String lower = query.toLowerCase().trim();
            return item.getPrimaryName().toLowerCase().contains(lower)
                    || item.getRecordId().toLowerCase().contains(lower)
                    || item.getSecondaryInfo().toLowerCase().contains(lower)
                    || item.getDetail().toLowerCase().contains(lower);
        });
    }

    @FXML
    public void handleRefreshDirectory()
    {
        refreshDirectory();
    }

    private void refreshDirectory()
    {
        try
        {
            List<DirectoryItem> records = directoryService.getAllDirectoryRecords();
            masterDirectoryData.setAll(records);
            applyDirectoryFilter();
        }
        catch (SQLException ex)
        {
            System.err.println("Error fetching directory records: " + ex.getMessage());
        }
    }
}
