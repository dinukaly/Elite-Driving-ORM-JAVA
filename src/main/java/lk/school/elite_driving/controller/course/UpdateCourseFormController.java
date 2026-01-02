package lk.school.elite_driving.controller.course;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import lk.school.elite_driving.bo.BOFactory;
import lk.school.elite_driving.bo.custom.CourseBO;
import lk.school.elite_driving.dto.CourseDTO;
import lk.school.elite_driving.tm.CourseTM;
import lk.school.elite_driving.util.AlertUtil;
import lk.school.elite_driving.util.InputValidator;

import java.net.URL;
import java.util.ResourceBundle;

public class UpdateCourseFormController implements Initializable {
    private final CourseBO courseBO = (CourseBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.COURSE);
    private CourseTM courseTM;

    @FXML
    public Label lblProgramId;
    @FXML
    public TextField txtProgramName;
    @FXML
    public TextField txtDuration;
    @FXML
    public TextField txtProgramFee;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void initData(CourseTM courseTM) {
        this.courseTM = courseTM;
        lblProgramId.setText(courseTM.getCourseId());
        txtProgramName.setText(courseTM.getCourseName());
        txtDuration.setText(courseTM.getCourseDuration());
        txtProgramFee.setText(String.valueOf(courseTM.getCourseFee()));
    }

    @FXML
    public void addButtonOnAction(ActionEvent actionEvent) {
        if(validateFields()) {
            try {
                String courseId = lblProgramId.getText();
                String courseName = txtProgramName.getText().trim();
                String courseDuration = txtDuration.getText().trim();
                double courseFee = Double.parseDouble(txtProgramFee.getText().trim());

                CourseDTO courseDTO = new CourseDTO(
                        courseId,
                        courseName,
                        courseDuration,
                        courseFee
                );

                boolean isUpdated = courseBO.updateCourse(courseDTO);
                if (isUpdated) {
                    AlertUtil.showConfirmation("Success", "Course updated successfully!");
                    closeModal(actionEvent);
                } else {
                    AlertUtil.showError("Failed to update course");
                }
            } catch (Exception e) {
                AlertUtil.showError("Error: " + e.getMessage());
            }
        }
    }

    @FXML
    public void btnDiscardOnAction(ActionEvent actionEvent) {
        closeModal(actionEvent);
    }

    private void closeModal(ActionEvent actionEvent) {
        Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void txtNameKeyAction(KeyEvent keyEvent) {
    }

    @FXML
    public void txtDurationOnKeyReleased(KeyEvent keyEvent) {
    }

    @FXML
    public void txtFeeKeyAction(KeyEvent keyEvent) {
    }

    private boolean validateFields(){
        InputValidator.clearStyle(txtProgramName,txtDuration,txtProgramFee);
        return InputValidator.isNotEmpty(txtProgramName, "Program Name") &&
                InputValidator.isAlphabetic(txtProgramName, "Program Name") &&
                InputValidator.isNotEmpty(txtDuration, "Duration") &&
                InputValidator.isNotEmpty(txtProgramFee, "Program Fee") &&
                InputValidator.isNumeric(txtProgramFee, "Program Fee");
    }
}