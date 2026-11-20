/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.adse2509.mediconnect;

import com.adse2509.mediconnect.controllers.MainController;
import com.adse2509.mediconnect.models.Staff;
import com.adse2509.mediconnect.views.LoginView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application launcher for MediConnect Hospital Management System.
 * 
 * @author Group 3
 */
public class MediConnectApp extends Application
{
    @Override
    public void start(Stage primaryStage)
    {
        try
        {
            LoginView loginView = new LoginView();
            if (loginView.showLoginDialog(primaryStage))
            {
                Staff staff = loginView.getLoggedInStaff();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("views/MainView.fxml"));
                Parent root = loader.load();

                MainController controller = loader.getController();
                if (controller != null && staff != null)
                {
                    controller.setStaffUser(staff);
                }

                primaryStage.setTitle("MediConnect - Philadelphia Medical Center");
                primaryStage.setScene(new Scene(root, 1180, 780));
                primaryStage.show();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
