package lk.school.elite_driving.controller.student;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import lk.school.elite_driving.bo.BOFactory;
import lk.school.elite_driving.bo.custom.CourseBO;
import lk.school.elite_driving.bo.custom.StudentBO;
import lk.school.elite_driving.dto.CourseDTO;
import lk.school.elite_driving.dto.PaymentDTO;
import lk.school.elite_driving.dto.StudentDTO;
import lk.school.elite_driving.util.AlertUtil;
import lk.school.elite_driving.util.InputValidator;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;

public class StudentRegistrationFormController implements Initializable {
    public TextField txtFirstName;
    public TextField txtAddress;
    public Label lblStudentId;
    public TextField txtContact;
    public TextField txtEmail;
    public DatePicker dtRegisterDate;
    public ComboBox<String> cmbCourseName;
    public Label lblCourseFee;
    public TextField txtUpfrontPayment;
    public TextField txtNIC;
    @FXML
    public ListView<String> listSelectedCourses;

    private final StudentBO studentBO = (StudentBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.STUDENT);
    private final CourseBO courseBO = (CourseBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.COURSE);
    private final ObservableList<String> selectedCourses = FXCollections.observableArrayList();
    private final List<CourseDTO> allCourses = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadNextStudentId();
        loadCourses();
        dtRegisterDate.setValue(LocalDate.now());
        listSelectedCourses.setItems(selectedCourses);
    }

    private void loadNextStudentId() {
        lblStudentId.setText(studentBO.getNextStudentId());
    }

    private void loadCourses() {
        allCourses.clear();
        allCourses.addAll(courseBO.getAllCourses());
        List<String> courseNames = allCourses.stream().map(CourseDTO::getCourseName).collect(Collectors.toList());
        cmbCourseName.setItems(FXCollections.observableArrayList(courseNames));
    }

    public void registerbtnOnAction(ActionEvent actionEvent) {
        if (validateFields()) {
            try {
                double totalFee = Double.parseDouble(lblCourseFee.getText());
                double downPayment = Double.parseDouble(txtUpfrontPayment.getText());

                if (downPayment < totalFee * 0.1) {
                    AlertUtil.showAlert(Alert.AlertType.ERROR, "Payment Error", "Down payment must be at least 10% of the total fee.");
                    return;
                }

                List<CourseDTO> selectedCourseDTOs = allCourses.stream()
                        .filter(courseDTO -> selectedCourses.contains(courseDTO.getCourseName()))
                        .collect(Collectors.toList());

                // Create a temporary student DTO for the payment (since student is being registered)
                StudentDTO tempStudentDTO = new StudentDTO();
                tempStudentDTO.setStudentId(lblStudentId.getText());
                tempStudentDTO.setStudentName(txtFirstName.getText());

                PaymentDTO paymentDTO = new PaymentDTO(
                        "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(),
                        downPayment,
                        Date.valueOf(LocalDate.now()),
                        "Paid",
                        tempStudentDTO
                );

                List<PaymentDTO> payments = new ArrayList<>();
                payments.add(paymentDTO);

                StudentDTO studentDTO = new StudentDTO(
                        lblStudentId.getText(),
                        txtFirstName.getText(),
                        txtEmail.getText(),
                        txtNIC.getText(),
                        txtContact.getText(),
                        txtAddress.getText(),
                        totalFee,
                        totalFee - downPayment,
                        downPayment,
                        Date.valueOf(dtRegisterDate.getValue()),
                        selectedCourseDTOs,
                        payments
                );

                studentBO.registerStudent(studentDTO);
                AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Success", "Student registered successfully!");
                clearFields();
            } catch (NumberFormatException e) {
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter valid numeric values for fees.");
            } catch (Exception e) {
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Registration Failed", "An error occurred during registration: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void btnDiscardOnAction(ActionEvent actionEvent) {
        clearFields();
        //close the modal
    }

    public void cmbCourseNameOnAction(ActionEvent actionEvent) {
        String selectedCourse = cmbCourseName.getSelectionModel().getSelectedItem();
        if (selectedCourse != null && !selectedCourses.contains(selectedCourse)) {
            selectedCourses.add(selectedCourse);
            updateTotalFee();
        }
    }

    public void removeCourseOnAction(ActionEvent actionEvent) {
        String selectedCourse = listSelectedCourses.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            selectedCourses.remove(selectedCourse);
            updateTotalFee();
        }
    }

    private void updateTotalFee() {
        double totalFee = allCourses.stream()
                .filter(courseDTO -> selectedCourses.contains(courseDTO.getCourseName()))
                .mapToDouble(CourseDTO::getCourseFee)
                .sum();
        lblCourseFee.setText(String.format("%.2f", totalFee));
    }

    private void clearFields() {
        InputValidator.clearStyle(txtFirstName, txtAddress, txtContact, txtEmail, txtNIC, txtUpfrontPayment, cmbCourseName, dtRegisterDate);
        txtFirstName.clear();
        txtAddress.clear();
        txtContact.clear();
        txtEmail.clear();
        txtNIC.clear();
        txtUpfrontPayment.clear();
        selectedCourses.clear();
        lblCourseFee.setText("0.00");
        loadNextStudentId();
        dtRegisterDate.setValue(LocalDate.now());
    }

    private boolean validateFields() {
        return InputValidator.isNotEmpty(txtFirstName, "First Name") &&
                InputValidator.isAlphabetic(txtFirstName, "First Name") &&
                InputValidator.isNotEmpty(txtAddress, "Address") &&
                InputValidator.isNotEmpty(txtContact, "Contact") &&
                InputValidator.isValidPhone(txtContact, "Contact") &&
                InputValidator.isNotEmpty(txtEmail, "Email") &&
                InputValidator.isValidEmail(txtEmail, "Email") &&
                InputValidator.isNotEmpty(txtNIC, "NIC") &&
                InputValidator.isNotEmpty(txtUpfrontPayment, "Upfront Payment") &&
                InputValidator.isNumeric(txtUpfrontPayment, "Upfront Payment") &&
                InputValidator.isDateSelected(dtRegisterDate, "Register Date") &&
                InputValidator.isComboBoxSelected(cmbCourseName, "Course");
    }
}