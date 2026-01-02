package lk.school.elite_driving.controller.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

public class AdminRegistrationController implements Initializable {
    private final AuthBO authBO = (AuthBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.USER);
    public TextField txtPassword;
    public TextField txtUserName;
    public AnchorPane rootNode;

    public void btnRegesterOnAction(ActionEvent actionEvent) {
        String userName = txtUserName.getText();
        String password = txtPassword.getText();
        String newUserId = authBO.getNewUserId();
        try {
            boolean isRegistered = authBO.register(
                    new UserDTO(newUserId, userName, password, UserRole.ADMIN)
            );
            if (isRegistered) {
                AlertUtil.showSuccess("Admin registered successfully");
                loadLoginPage();

            } else {
                AlertUtil.showFailure("Admin registration failed");
            }
        } catch (RegistrationException e) {
            AlertUtil.showError(e.getMessage());
        } catch (Exception e) {
            AlertUtil.showError(e.getMessage());
        }
    }

    public void loadLoginPage() throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/lk.school.elite_driving/view/admin/adminLogin.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(anchorPane));
        stage.show();
        Stage prevStage = (Stage) rootNode.getScene().getWindow();
        prevStage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}