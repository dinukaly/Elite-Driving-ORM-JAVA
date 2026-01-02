package lk.school.elite_driving.controller.course;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lk.school.elite_driving.bo.BOFactory;
import lk.school.elite_driving.bo.custom.CourseBO;
import lk.school.elite_driving.dto.CourseDTO;
import lk.school.elite_driving.util.AlertUtil;
import lk.school.elite_driving.util.InputValidator;

import java.net.URL;
import java.util.ResourceBundle;

public class AddCourseFormController implements Initializable {
    private final CourseBO courseBO = (CourseBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.COURSE);

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
        generateNewCourseId();
    }

    private void generateNewCourseId() {
        String newCourseId = courseBO.getNewCourseId();
        lblProgramId.setText(newCourseId);
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

                boolean isAdded = courseBO.addCourse(courseDTO);
                if (isAdded) {
                    AlertUtil.showConfirmation("Success", "Course added successfully!");
                    clearFields();
                    generateNewCourseId();
                    closeModal(actionEvent);
                } else {
                    AlertUtil.showError("Failed to add course");
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
        javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    private void clearFields() {
        txtProgramName.clear();
        txtDuration.clear();
        txtProgramFee.clear();
        InputValidator.clearStyle(txtProgramName,txtDuration,txtProgramFee);
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