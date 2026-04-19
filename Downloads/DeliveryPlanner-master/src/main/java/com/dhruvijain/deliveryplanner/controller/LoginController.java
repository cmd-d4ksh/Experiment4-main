package com.dhruvijain.deliveryplanner.controller;

import com.dhruvijain.deliveryplanner.model.User;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;

    // Demo credentials — to be replaced with proper auth service
    private static final String DEMO_USERNAME = "admin";
    private static final String DEMO_PASSWORD = "admin123";

    @FXML
    public void initialize() {
        errorLabel.setVisible(false);
        loginButton.setDefaultButton(true);
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password.");
            return;
        }

        com.dhruvijain.deliveryplanner.dao.UserDAO userDAO = new com.dhruvijain.deliveryplanner.dao.UserDAO();
        User user = userDAO.authenticate(username, password);

        if (user != null) {
            com.dhruvijain.deliveryplanner.model.UserSession.createSession(user);
            System.out.println("Login successful: " + user);
            try {
                java.net.URL fxmlUrl = getClass().getResource("/com/dhruvijain/deliveryplanner/dashboard.fxml");
                javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(fxmlUrl);
                javafx.scene.Scene scene = new javafx.scene.Scene(loader.load(), 1000, 700);
                javafx.stage.Stage stage = (javafx.stage.Stage) loginButton.getScene().getWindow();
                stage.setScene(scene);
                stage.centerOnScreen();
            } catch (java.io.IOException e) {
                e.printStackTrace();
                showError("Failed to load dashboard.");
            }
        } else {
            showError("Invalid username or password.");
            passwordField.clear();
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);

        FadeTransition fade = new FadeTransition(Duration.millis(300), errorLabel);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();
    }
}
