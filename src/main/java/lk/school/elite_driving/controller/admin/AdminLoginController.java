package lk.school.elite_driving.controller.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.school.elite_driving.bo.BOFactory;
import lk.school.elite_driving.bo.custom.AuthBO;
import lk.school.elite_driving.dto.UserDTO;
import lk.school.elite_driving.exception.InvalidCredentialsException;
import lk.school.elite_driving.exception.LoginException;
import lk.school.elite_driving.util.AlertUtil;


import java.io.IOException;

public class AdminLoginController {
    private final AuthBO authBO = (AuthBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.USER);
    public AnchorPane rootNode;
    public TextField txtUserId;
    public PasswordField txtPassword;

    public void txtUserNameOnKeyReleased(KeyEvent keyEvent) {
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
            if (user.getUserRole() == lk.school.elite_driving.enitity.UserRole.ADMIN) {
                navigateToAdminDashboard();
            } else {
                AlertUtil.showFailure("Login Failed! You are not authorized to access the admin panel.");
            }
        } catch (InvalidCredentialsException e) {
            AlertUtil.showFailure("Login Failed! Invalid username or password.");
        } catch (LoginException e) {
            AlertUtil.showFailure("Login Failed! " + e.getMessage());
        }
    }

    private void navigateToAdminDashboard() {
        try {
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/lk/school/elite_driving/view/admin/adminDashboard.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(anchorPane));
            stage.show();
            Stage prevStage = (Stage) rootNode.getScene().getWindow();
            prevStage.close();
        } catch (IOException e) {
            AlertUtil.showFailure("Navigation Error! Failed to load the admin dashboard.");
        }
    }

    public void btnRegesterOnAction(ActionEvent actionEvent) throws IOException {
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/lk/school/elite_driving/view/admin/registrationForm.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(anchorPane));
            stage.show();
            Stage prevStage = (Stage) rootNode.getScene().getWindow();
            prevStage.close();

    }

    public void txtPasswordOnKeyReleased(KeyEvent keyEvent) {

    }
}