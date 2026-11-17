package com.adse2509.mediconnect;

import com.adse2509.mediconnect.controllers.MainController;
import com.adse2509.mediconnect.dao.StaffDAO;
import com.adse2509.mediconnect.models.Staff;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Main Application Launcher for MediConnect Clinical Hospital System.
 * Developed in accordance with the ADSE 2509 Advanced Java and JavaFX syllabus
 * and the Aptech Project Guide specification for Philadelphia Medical Center (PMC).
 *
 * <p>
 * Follows the exact reference implementation from Page 45 of the Project Guide:
 * The primary stage directly hosts the Staff Login scene, and transitions
 * seamlessly to the Main Dashboard upon successful authentication.
 * </p>
 *
 * @author Group 3
 */
public class MediConnectApp extends Application
{
    private static final String APP_TITLE = "MediConnect - Philadelphia Medical Center (PMC)";
    private final StaffDAO staffDAO = new StaffDAO();

    @Override
    public void start(Stage primaryStage)
    {
        try
        {
            // Show Staff Login Form directly on primaryStage (Project Guide Page 45)
            showLoginScene(primaryStage);
        }
        catch (Exception ex)
        {
            System.err.println("[MediConnect CRITICAL] Failed to start application: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /* ==================================================================================
     * 1. STAFF LOGIN SCENE (Reference Code: Guide Page 45)
     * ================================================================================== */
    private void showLoginScene(Stage primaryStage)
    {
        primaryStage.setTitle("MediConnect - Staff Login (PMC)");
        primaryStage.setResizable(false);

        // Header Title
        Label iconLabel = new Label("🏥");
        iconLabel.setStyle("-fx-font-size: 32px;");

        Label headerTitle = new Label("Philadelphia Medical Center");
        headerTitle.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: #0F172A;");

        Label subTitle = new Label("MediConnect Staff Authentication");
        subTitle.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748B;");

        VBox headerBox = new VBox(4, iconLabel, headerTitle, subTitle);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(15, 0, 15, 0));

        // Form Grid (Matching Page 45)
        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(14);
        grid.setAlignment(Pos.CENTER);

        Label idLabel = new Label("Staff ID:");
        idLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #334155;");
        TextField idField = new TextField("ST01");
        idField.setPromptText("Enter Staff ID (e.g. ST01)");
        idField.setPrefWidth(220);

        Label passLabel = new Label("Password:");
        passLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #334155;");
        PasswordField passField = new PasswordField();
        passField.setText("admin123");
        passField.setPromptText("Enter password");
        passField.setPrefWidth(220);

        grid.add(idLabel, 0, 0);
        grid.add(idField, 1, 0);
        grid.add(passLabel, 0, 1);
        grid.add(passField, 1, 1);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #EF4444; -fx-font-size: 11px;");
        errorLabel.setVisible(false);

        // Buttons
        Button loginBtn = new Button("Login to MediConnect");
        loginBtn.setStyle("-fx-background-color: #0284C7; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 9 18; -fx-background-radius: 6; -fx-cursor: hand;");

        Button exitBtn = new Button("Exit");
        exitBtn.setStyle("-fx-background-color: #E2E8F0; -fx-text-fill: #334155; -fx-font-weight: bold; -fx-padding: 9 18; -fx-background-radius: 6; -fx-cursor: hand;");

        HBox btnBox = new HBox(12, exitBtn, loginBtn);
        btnBox.setAlignment(Pos.CENTER_RIGHT);

        // Login Action
        Runnable doLogin = () -> {
            String staffId = idField.getText().trim();
            String password = passField.getText().trim();

            Staff staff = staffDAO.authenticate(staffId, password);
            if (staff != null)
            {
                System.out.println("[MediConnect] Authentication successful for: " + staff.getStaffName());
                showMainScene(primaryStage, staff);
            }
            else
            {
                errorLabel.setText("Invalid credentials. Try ST01 / admin123");
                errorLabel.setVisible(true);
            }
        };

        loginBtn.setOnAction(e -> doLogin.run());
        passField.setOnAction(e -> doLogin.run());
        idField.setOnAction(e -> doLogin.run());

        exitBtn.setOnAction(e -> {
            Platform.exit();
            System.exit(0);
        });

        VBox root = new VBox(12, headerBox, grid, errorLabel, btnBox);
        root.setPadding(new Insets(20, 28, 24, 28));
        root.setStyle("-fx-background-color: #FFFFFF;");

        Scene loginScene = new Scene(root, 400, 330);
        primaryStage.setScene(loginScene);
        primaryStage.centerOnScreen();
        primaryStage.show();
        primaryStage.toFront();
        primaryStage.requestFocus();
    }

    /* ==================================================================================
     * 2. MAIN APPLICATION SCENE (Dashboard & Core 4 Screens)
     * ================================================================================== */
    private void showMainScene(Stage primaryStage, Staff staff)
    {
        try
        {
            URL fxmlUrl = getClass().getResource("views/MainView.fxml");
            if (fxmlUrl == null)
            {
                fxmlUrl = getClass().getResource("/com/adse2509/mediconnect/views/MainView.fxml");
            }
            if (fxmlUrl == null)
            {
                throw new IOException("Unable to locate MainView.fxml resource file.");
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            MainController controller = loader.getController();
            if (controller != null && staff != null)
            {
                controller.setStaffUser(staff);
            }

            Scene mainScene = new Scene(root, 1180, 780);
            primaryStage.setTitle(APP_TITLE);
            primaryStage.setScene(mainScene);
            primaryStage.setResizable(true);
            primaryStage.setMinWidth(980);
            primaryStage.setMinHeight(650);
            primaryStage.centerOnScreen();
            primaryStage.toFront();
            primaryStage.requestFocus();
        }
        catch (IOException e)
        {
            System.err.println("[MediConnect ERROR] Failed to load MainView.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
