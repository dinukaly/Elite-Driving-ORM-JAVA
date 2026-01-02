package lk.school.elite_driving.controller.student;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lk.school.elite_driving.bo.BOFactory;
import lk.school.elite_driving.bo.custom.StudentBO;
import lk.school.elite_driving.dto.StudentDTO;
import lk.school.elite_driving.util.AlertUtil;
import lk.school.elite_driving.util.InputValidator;

import java.util.stream.Collectors;

public class UpdateStudentFormController {
    public Label lblStudentId;
    public TextField txtStudentName;
    public TextField txtEmail;
    public TextField txtNic;
    public TextField txtContact;
    public TextField txtAddress;
    public TextField txtCourseName;

    private final StudentBO studentBO = (StudentBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.STUDENT);
    private StudentDTO studentDTO;

    public void initData(StudentDTO studentDTO) {
        this.studentDTO = studentDTO;
        lblStudentId.setText(studentDTO.getStudentId());
        txtStudentName.setText(studentDTO.getStudentName());
        txtEmail.setText(studentDTO.getStudentEmail());
        txtNic.setText(studentDTO.getStudentNic());
        txtContact.setText(studentDTO.getStudentContact());
        txtAddress.setText(studentDTO.getStudentAddress());
        
        // Set course name (non-editable)
        if (studentDTO.getCourses() != null && !studentDTO.getCourses().isEmpty()) {
            String courseNames = studentDTO.getCourses().stream()
                .map(course -> course.getCourseName())
                .collect(Collectors.joining(", "));
            txtCourseName.setText(courseNames);
        } else {
            txtCourseName.setText("No courses assigned");
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (validateFields()) {
            studentDTO.setStudentName(txtStudentName.getText());
            studentDTO.setStudentEmail(txtEmail.getText());
            studentDTO.setStudentNic(txtNic.getText());
            studentDTO.setStudentContact(txtContact.getText());
            studentDTO.setStudentAddress(txtAddress.getText());

            if (studentBO.updateStudent(studentDTO)) {
                AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Success", "Student updated successfully!");
            } else {
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Failed to update student.");
            }
        }
    }

    private boolean validateFields() {
        InputValidator.clearStyle(txtStudentName, txtEmail, txtNic, txtContact, txtAddress);
        return InputValidator.isNotEmpty(txtStudentName, "Student Name") &&
                InputValidator.isAlphabetic(txtStudentName, "Student Name") &&
                InputValidator.isNotEmpty(txtEmail, "Email") &&
                InputValidator.isValidEmail(txtEmail, "Email") &&
                InputValidator.isNotEmpty(txtNic, "NIC") &&
                InputValidator.isNotEmpty(txtContact, "Contact") &&
                InputValidator.isValidPhone(txtContact, "Contact") &&
                InputValidator.isNotEmpty(txtAddress, "Address");
    }
}