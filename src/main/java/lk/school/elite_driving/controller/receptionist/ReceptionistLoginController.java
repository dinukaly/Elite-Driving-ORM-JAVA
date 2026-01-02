package lk.school.elite_driving.controller.receptionist;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.school.elite_driving.bo.BOFactory;
import lk.school.elite_driving.bo.custom.AuthBO;
import lk.school.elite_driving.dto.UserDTO;
import lk.school.elite_driving.exception.InvalidCredentialsException;
import lk.school.elite_driving.exception.LoginException;
import lk.school.elite_driving.util.AlertUtil;

import java.io.IOException;

public class ReceptionistLoginController {
    @FXML
    public AnchorPane rootNode;
    @FXML
    public TextField txtUserId;
    @FXML
    public PasswordField txtPassword;

    private final AuthBO authBO = (AuthBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.USER);

    public void txtUserNameOnKeyReleased(KeyEvent keyEvent) {
        // Handle Enter key press for login
        if (keyEvent.getCode().toString().equals("ENTER")) {
            btnLoginOnAction(new ActionEvent());
        }
    }

    public void btnLoginOnAction(ActionEvent actionEvent) {
        String userName = txtUserId.getText();
        String password = txtPassword.getText();

        if (userName.isEmpty() || password.isEmpty()) {
            AlertUtil.showFailure("Login Failed! Username and password cannot be empty.");
            return;
        }

        try {
            UserDTO user = authBO.login(userName, password);
            if (user.getUserRole() == lk.school.elite_driving.enitity.UserRole.RECEPTIONIST) {
                navigateToReceptionistDashboard();
            } else {
                AlertUtil.showFailure("Login Failed! You are not authorized to access the receptionist panel.");
            }
        } catch (InvalidCredentialsException e) {
            AlertUtil.showFailure("Login Failed! Invalid username or password.");
        } catch (LoginException e) {
            AlertUtil.showFailure("Login Failed! " + e.getMessage());
        } catch (Exception e) {
            AlertUtil.showError("An unexpected error occurred: " + e.getMessage());
        }
    }

    public void btnRegesterOnAction(ActionEvent actionEvent) {
        try {
            // Load the registration form
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk.school.elite_driving/view/receptionist/ReceptionistRegistration.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Receptionist Registration");
            stage.setScene(new Scene(root));
            stage.show();
            
            // Close the login window
            Stage loginStage = (Stage) rootNode.getScene().getWindow();
            loginStage.close();
            
        } catch (IOException e) {
            AlertUtil.showError("Error loading registration form: " + e.getMessage());
        }
    }

    public void txtPasswordOnKeyReleased(KeyEvent keyEvent) {
        // Handle Enter key press for login
        if (keyEvent.getCode().toString().equals("ENTER")) {
            btnLoginOnAction(new ActionEvent());
        }
    }

    private void navigateToReceptionistDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk.school.elite_driving/view/receptionist/ReceptionistDashboard.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Receptionist Dashboard");
            stage.setScene(new Scene(root));
            stage.show();
            
            // Close the login window
            Stage loginStage = (Stage) rootNode.getScene().getWindow();
            loginStage.close();
            
        } catch (IOException e) {
            AlertUtil.showError("Error loading dashboard: " + e.getMessage());
        }
    }
}