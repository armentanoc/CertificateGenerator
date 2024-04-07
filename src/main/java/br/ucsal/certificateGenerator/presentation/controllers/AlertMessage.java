package br.ucsal.certificateGenerator.presentation.controllers;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class AlertMessage {
	private Alert alert;
	
    public void information(String message) {
        alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Information Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void errorm(String message) {
        alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public boolean confirm(String message) {

        alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> option = alert.showAndWait();

        return option.get().equals(ButtonType.OK);

    }
}
