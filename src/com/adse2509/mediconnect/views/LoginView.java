/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
 * Staff authentication dialog.
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
        dialog.setTitle("MediConnect - Staff Login");
        dialog.setResizable(false);

        Label headerTitle = new Label("Philadelphia Medical Center");
        headerTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #0F172A;");

        Label subTitle = new Label("Staff Login Authentication");
        subTitle.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748B;");

        VBox headerBox = new VBox(4, headerTitle, subTitle);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(10, 0, 15, 0));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(12);
        grid.setAlignment(Pos.CENTER);

        TextField idField = new TextField("ST01");
        idField.setPromptText("Staff ID");
        PasswordField passField = new PasswordField();
        passField.setText("admin123");
        passField.setPromptText("Password");

        grid.add(new Label("Staff ID:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passField, 1, 1);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #EF4444; -fx-font-size: 11px;");
        errorLabel.setVisible(false);

        Button loginBtn = new Button("Login");
        loginBtn.setStyle("-fx-background-color: #0284C7; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 7 16;");

        Button exitBtn = new Button("Exit");
        exitBtn.setStyle("-fx-padding: 7 16;");

        HBox btnBox = new HBox(10, exitBtn, loginBtn);
        btnBox.setAlignment(Pos.CENTER_RIGHT);

        Runnable doLogin = () -> {
            String sid = idField.getText().trim();
            String pwd = passField.getText().trim();
            Staff staff = staffDAO.authenticate(sid, pwd);
            if (staff != null)
            {
                authenticated = true;
                loggedInStaff = staff;
                dialog.close();
            }
            else
            {
                errorLabel.setText("Invalid credentials. Use ST01 / admin123");
                errorLabel.setVisible(true);
            }
        };

        loginBtn.setOnAction(e -> doLogin.run());
        passField.setOnAction(e -> doLogin.run());
        idField.setOnAction(e -> doLogin.run());
        exitBtn.setOnAction(e -> {
            authenticated = false;
            dialog.close();
        });

        VBox root = new VBox(12, headerBox, grid, errorLabel, btnBox);
        root.setPadding(new Insets(20));
        dialog.setScene(new Scene(root, 360, 240));
        dialog.showAndWait();

        return authenticated;
    }

    public Staff getLoggedInStaff()
    {
        return loggedInStaff;
    }
}
