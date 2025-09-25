package lk.school.elite_driving.controller.receptionist;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.school.elite_driving.bo.BOFactory;
import lk.school.elite_driving.bo.custom.AuthBO;
import lk.school.elite_driving.dto.UserDTO;
import lk.school.elite_driving.enitity.UserRole;
import lk.school.elite_driving.exception.RegistrationException;
import lk.school.elite_driving.util.AlertUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ReceptionistRegisterFormController implements Initializable {

    @FXML
    public AnchorPane rootNode;
    @FXML
    public TextField txtReceptionistId;
    @FXML
    public TextField txtUserName;
    @FXML
    public TextField txtPassword;

    private final AuthBO authBO = (AuthBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.USER);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadNextReceptionistId();
    }

    private void loadNextReceptionistId() {
        try {
            String newUserId = authBO.getNewUserId();
            txtReceptionistId.setText(newUserId);
        } catch (Exception e) {
            AlertUtil.showError("Error generating receptionist ID: " + e.getMessage());
        }
    }

    public void btnReceptionistRegisterOnAction(ActionEvent actionEvent) {
        String userName = txtUserName.getText().trim();
        String password = txtPassword.getText().trim();
        String receptionistId = txtReceptionistId.getText().trim();

        if (userName.isEmpty() || password.isEmpty()) {
            AlertUtil.showFailure("Registration Failed! Username and password cannot be empty.");
            return;
        }

        try {
            boolean isRegistered = authBO.register(
                    new UserDTO(receptionistId, userName, password, UserRole.RECEPTIONIST)
            );
            
            if (isRegistered) {
                AlertUtil.showSuccess("Receptionist registered successfully!");
                navigateToLoginPage();
            } else {
                AlertUtil.showFailure("Receptionist registration failed!");
            }
        } catch (RegistrationException e) {
            AlertUtil.showFailure("Registration Failed! " + e.getMessage());
        } catch (Exception e) {
            AlertUtil.showError("An unexpected error occurred: " + e.getMessage());
        }
    }

    private void navigateToLoginPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk/school/elite_driving/view/receptionist/receptionistLogin.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Receptionist Login");
            stage.setScene(new Scene(root));
            stage.show();
            
            // Close the registration window
            Stage registrationStage = (Stage) rootNode.getScene().getWindow();
            registrationStage.close();
            
        } catch (IOException e) {
            AlertUtil.showError("Error loading login page: " + e.getMessage());
        }
    }
}