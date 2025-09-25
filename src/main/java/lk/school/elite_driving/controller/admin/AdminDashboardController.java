package lk.school.elite_driving.controller.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.school.elite_driving.util.AlertUtil;

import java.io.IOException;

public class AdminDashboardController {
    public AnchorPane rootNode;
    public Label lblTotStudent;
    public Label lblTotCourses;
    public Label lblTotEmployees;
    public Label lblTotRevenue;
    public AnchorPane childRootNode;

    public void btnUserSettingOnAction(MouseEvent mouseEvent) {

    }

    public void btnDashboardOnAction(ActionEvent actionEvent) {
    }

    public void btnInstructorManageOnAction(ActionEvent actionEvent) {
        //clear child root node
        childRootNode.getChildren().clear();
        //load user setting view
        try {
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/lk/school/elite_driving/view/instructor/instructorManage.fxml"));
            childRootNode.getChildren().add(anchorPane);
        } catch (IOException e) {
            AlertUtil.showError("Error loading user setting view: " + e.getMessage());
        }
    }

    public void btnStudentManageOnAction(ActionEvent actionEvent) {
        //clear child root node
        childRootNode.getChildren().clear();
        //load user setting view
        try {
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/lk/school/elite_driving/view/students/studentManage.fxml"));
            childRootNode.getChildren().add(anchorPane);
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showError("Error loading user setting view: " + e.getMessage());
        }
    }

    public void btnCourseManageOnAction(ActionEvent actionEvent) {
        //clear child root node
        childRootNode.getChildren().clear();
        //load user setting view
        try {
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/lk/school/elite_driving/view/course/coursesManage.fxml"));
            childRootNode.getChildren().add(anchorPane);
        } catch (IOException e) {
            AlertUtil.showError("Error loading user setting view: " + e.getMessage());
        }
    }

    public void btnpaymentManageOnAction(ActionEvent actionEvent) {
        //clear child root node
        childRootNode.getChildren().clear();
        //load user setting view
        try {
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/lk/school/elite_driving/view/payment/paymentsManage.fxml"));
            childRootNode.getChildren().add(anchorPane);
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showError("Error loading payment view: " + e.getMessage());
        }
    }

    public void btnLogOutOnAction(ActionEvent actionEvent) {
        //clear child root node
        childRootNode.getChildren().clear();
        //load login view
        try {
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/lk/school/elite_driving/view/login/login.fxml"));
            childRootNode.getChildren().add(anchorPane);
        } catch (IOException e) {
            AlertUtil.showError("Error loading login view: " + e.getMessage());
        }
    }

    public void btnReceptionistManageOnAction(ActionEvent actionEvent) {
        //clear child root node
        childRootNode.getChildren().clear();
        //load user setting view
        try {
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/lk/school/elite_driving/view/receptionist/receptionistLogin.fxml"));
            childRootNode.getChildren().add(anchorPane);
        } catch (IOException e) {
            AlertUtil.showError("Error loading user setting view: " + e.getMessage());
        }
    }

    public void btnLessonOnAction(ActionEvent actionEvent) {
        //clear child root node
        childRootNode.getChildren().clear();
        //load user setting view
        try {
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/lk/school/elite_driving/view/lesson/lessonScheduling.fxml"));
            childRootNode.getChildren().add(anchorPane);
        } catch (IOException e) {
            AlertUtil.showError("Error loading user setting view: " + e.getMessage());
        }
    }
}