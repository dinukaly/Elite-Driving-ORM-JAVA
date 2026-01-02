package lk.school.elite_driving.controller.receptionist;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.school.elite_driving.bo.BOFactory;
import lk.school.elite_driving.bo.custom.AuthBO;
import lk.school.elite_driving.dto.UserDTO;
import lk.school.elite_driving.enitity.UserRole;
import lk.school.elite_driving.exception.RegistrationException;
import lk.school.elite_driving.util.AlertUtil;
import lk.school.elite_driving.util.InputValidator;

import java.net.URL;
import java.util.ResourceBundle;

public class AddReceptionistFormController implements Initializable {
    @FXML
    public TextField txtUserId;
    @FXML
    public TextField txtUserName;
    @FXML
    public PasswordField txtPassword;
    @FXML
    public PasswordField txtConfirmPassword;
    @FXML
    public Label lblUserId;
    @FXML
    public Button btnSave;
    @FXML
    public Button btnCancel;

    private AuthBO authBO = (AuthBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.USER);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Generate and display the initial user ID
        String initialId = getNewUserId();
        txtUserId.setText(initialId);
        txtUserId.setEditable(false); // Make it read-only
    }

    String getNewUserId() {
        return authBO.getNewUserId();
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        if (validateFields()) {
            try {
                // Create UserDTO for receptionist
                UserDTO userDTO = new UserDTO(
                        txtUserId.getText(),
                        txtUserName.getText(),
                        txtPassword.getText(),
                        UserRole.RECEPTIONIST
                );

                // Register the receptionist
                boolean success = authBO.register(userDTO);

                if (success) {
                    AlertUtil.showSuccess("Receptionist created successfully!");
                    closeForm();
                }

            } catch (RegistrationException e) {
                AlertUtil.showError(e.getMessage());
            } catch (Exception e) {
                AlertUtil.showError("Error creating receptionist: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void btnCancelOnAction(ActionEvent actionEvent) {
        closeForm();
    }

    private void closeForm() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    private boolean validateFields(){
        InputValidator.clearStyle(txtUserName,txtPassword,txtConfirmPassword);
        return InputValidator.isNotEmpty(txtUserName, "User Name") &&
                InputValidator.isAlphabetic(txtUserName, "User Name") &&
                InputValidator.isNotEmpty(txtPassword, "Password") &&
                InputValidator.isNotEmpty(txtConfirmPassword, "Confirm Password") &&
                txtPassword.getText().equals(txtConfirmPassword.getText());
    }
}