package com.adse2509.mediconnect.utils;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.Optional;

/**
 * Utility helper for displaying JavaFX Alerts, Confirmation Dialogs, and Toast notifications.
 *
 * @author Group 3
 */
public class AlertHelper
{
    private AlertHelper()
    {
    }

    public static void showInfo(String title, String message)
    {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    public static void showError(String title, String message)
    {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    public static boolean showConfirmation(String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    public static void showToast(Label toastLabel, String message, boolean isSuccess)
    {
        if (toastLabel == null) return;

        Platform.runLater(() -> {
            toastLabel.setText(message);
            toastLabel.setStyle(isSuccess
                    ? "-fx-background-color: #10B981; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;"
                    : "-fx-background-color: #EF4444; -fx-text-fill: white; -fx-padding: 8 16; -fx-background-radius: 6;");
            toastLabel.setOpacity(1.0);
            toastLabel.setVisible(true);

            FadeTransition fade = new FadeTransition(Duration.seconds(2.5), toastLabel);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.setOnFinished(e -> toastLabel.setVisible(false));
            fade.play();
        });
    }
}
