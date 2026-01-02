package lk.school.elite_driving.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomePageController {
    public AnchorPane rootNode;

    public void btnReceptionistOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/lk.school.elite_driving/view/receptionist/receptionistLogin.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(anchorPane);
        stage.setTitle("Receptionist Login");
        stage.setScene(scene);
        stage.show();
        Stage prevStage = (Stage) rootNode.getScene().getWindow();
        prevStage.close();
    }
    public void btnAdminOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/lk.school.elite_driving/view/admin/adminLogin.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(anchorPane);
        stage.setTitle("Admin Login");
        stage.setScene(scene);
        stage.show();
        Stage prevStage = (Stage) rootNode.getScene().getWindow();
        prevStage.close();
    }
}
