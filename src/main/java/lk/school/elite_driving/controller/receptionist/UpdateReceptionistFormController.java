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
import lk.school.elite_driving.tm.UserTM;
import lk.school.elite_driving.util.AlertUtil;
import lk.school.elite_driving.util.InputValidator;

import java.net.URL;
import java.util.ResourceBundle;

public class UpdateReceptionistFormController implements Initializable {
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
    public Button btnUpdate;
    @FXML
    public Button btnCancel;

    private AuthBO authBO = (AuthBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.USER);
    private UserTM currentUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtUserId.setEditable(false); // Make it read-only
    }

    public void initData(UserTM userTM) {
        this.currentUser = userTM;
        txtUserId.setText(userTM.getUserId());
        txtUserName.setText(userTM.getUserName());
        // Password field is left empty intentionally - user can update it if they want
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        if(validateFields()) {
            try {
                // Create UserDTO for receptionist
                UserDTO userDTO = new UserDTO(
                        txtUserId.getText(),
                        txtUserName.getText(),
                        txtPassword.getText(), // Empty password means don't change it
                        UserRole.RECEPTIONIST
                );

                // Update the receptionist
                boolean success = authBO.updateUser(userDTO);

                if (success) {
                    AlertUtil.showSuccess("Receptionist updated successfully!");
                    closeForm();
                }

            } catch (RegistrationException e) {
                AlertUtil.showError(e.getMessage());
            } catch (Exception e) {
                AlertUtil.showError("Error updating receptionist: " + e.getMessage());
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

    private boolean validateFields() {
        InputValidator.clearStyle(txtUserName, txtPassword, txtConfirmPassword);
        if (!InputValidator.isNotEmpty(txtUserName, "User Name") || !InputValidator.isAlphabetic(txtUserName, "User Name")) {
            return false;
        }

        if (!txtPassword.getText().isEmpty()) {
            if (!InputValidator.isNotEmpty(txtConfirmPassword, "Confirm Password")) {
                return false;
            }
            if (!txtPassword.getText().equals(txtConfirmPassword.getText())) {
                AlertUtil.showError("Passwords do not match");
                InputValidator.markInvalid(txtPassword);
                InputValidator.markInvalid(txtConfirmPassword);
                return false;
            }
        }
        return true;
    }
}