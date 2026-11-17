package com.adse2509.mediconnect.views;

import com.adse2509.mediconnect.dao.StaffDAO;
import com.adse2509.mediconnect.models.Staff;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Staff Authentication Dialog.
 * Direct implementation of the Aptech Reference Code (Project Guide Pages 45-46).
 * Authenticates staff credentials against {@code dbo.Staff}.
 *
 * @author Group 3
 */
public class LoginView
{
    private final StaffDAO staffDAO = new StaffDAO();
    private boolean authenticated = false;
    private Staff loggedInStaff = null;

    public boolean showLoginDialog(Stage parentStage)
    {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(parentStage);
        dialog.setTitle("MediConnect - Staff Login (PMC)");
        dialog.setResizable(false);

        // Header Title
        Label headerTitle = new Label("Philadelphia Medical Center");
        headerTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #0F172A;");

        Label subTitle = new Label("MediConnect Staff Authentication");
        subTitle.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748B;");

        VBox headerBox = new VBox(4, headerTitle, subTitle);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(10, 0, 15, 0));

        // Form Grid
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(12);
        grid.setAlignment(Pos.CENTER);

        Label idLabel = new Label("Staff ID:");
        idLabel.setStyle("-fx-font-weight: bold;");
        TextField idField = new TextField("ST01");
        idField.setPromptText("e.g. ST01");

        Label passLabel = new Label("Password:");
        passLabel.setStyle("-fx-font-weight: bold;");
        PasswordField passField = new PasswordField();
        passField.setText("admin123");
        passField.setPromptText("Enter password");

        grid.add(idLabel, 0, 0);
        grid.add(idField, 1, 0);
        grid.add(passLabel, 0, 1);
        grid.add(passField, 1, 1);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #EF4444; -fx-font-size: 11px;");
        errorLabel.setVisible(false);

        // Action Buttons
        Button loginBtn = new Button("Login to MediConnect");
        loginBtn.setStyle("-fx-background-color: #0284C7; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 16; -fx-background-radius: 6;");

        Button exitBtn = new Button("Exit");
        exitBtn.setStyle("-fx-background-color: #E2E8F0; -fx-text-fill: #334155; -fx-padding: 8 16; -fx-background-radius: 6;");

        HBox btnBox = new HBox(10, exitBtn, loginBtn);
        btnBox.setAlignment(Pos.CENTER_RIGHT);

        loginBtn.setOnAction(e -> {
            String staffId = idField.getText().trim();
            String password = passField.getText().trim();

            Staff staff = staffDAO.authenticate(staffId, password);
            if (staff != null)
            {
                authenticated = true;
                loggedInStaff = staff;
                dialog.close();
            }
            else
            {
                errorLabel.setText("Invalid Staff ID or Password. Try ST01 / admin123");
                errorLabel.setVisible(true);
            }
        });

        exitBtn.setOnAction(e -> {
            authenticated = false;
            dialog.close();
        });

        VBox root = new VBox(12, headerBox, grid, errorLabel, btnBox);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #FFFFFF;");

        Scene scene = new Scene(root, 360, 260);
        dialog.setScene(scene);
        dialog.showAndWait();

        return authenticated;
    }

    public Staff getLoggedInStaff()
    {
        return loggedInStaff;
    }
}
