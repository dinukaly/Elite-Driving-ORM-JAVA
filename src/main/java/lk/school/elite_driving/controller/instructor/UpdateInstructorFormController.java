package lk.school.elite_driving.controller.instructor;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.school.elite_driving.bo.BOFactory;
import lk.school.elite_driving.bo.custom.InstructorBO;
import lk.school.elite_driving.dto.InstructorDTO;
import lk.school.elite_driving.tm.InstructorTM;
import lk.school.elite_driving.util.AlertUtil;
import lk.school.elite_driving.util.InputValidator;

import java.net.URL;
import java.util.ResourceBundle;

public class UpdateInstructorFormController implements Initializable {

    private final InstructorBO instructorBO = (InstructorBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.INSTRUCTOR);
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtContact;
    public Label instructorId;
    public TextField txtEmail;
    public CheckBox checkAvailability;
    public ComboBox<String> cmbSpecialization;
    public AnchorPane updateForm;

    public void initData(InstructorTM instructorTM) {
        instructorId.setText(instructorTM.getInstructorId());
        String[] nameParts = instructorTM.getInstructorName().split(" ");
        txtName.setText(nameParts[0]);
        txtAddress.setText(instructorTM.getInstructorAddress());
        txtContact.setText(instructorTM.getInstructorPhone());
        txtEmail.setText(instructorTM.getInstructorEmail());
        checkAvailability.setSelected(instructorTM.isActive());
        cmbSpecialization.setValue(instructorTM.getInstructorSpecialization());
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        if(validateFields()) {
            try {
                InstructorDTO instructorDTO = new InstructorDTO(
                        instructorId.getText(),
                        txtName.getText(),
                        txtAddress.getText(),
                        txtContact.getText(),
                        txtEmail.getText(),
                        (String) cmbSpecialization.getValue(),
                        checkAvailability.isSelected()
                );
                instructorBO.updateInstructor(instructorDTO);
                AlertUtil.showConfirmation("Update Success", "Instructor updated successfully.");
            } catch (Exception e) {
                AlertUtil.showError("Failed to update instructor: " + e.getMessage());
            }
        }
    }

    public void btnDiscardOnAction(ActionEvent actionEvent) {
        // Close the form
        Stage stage = (Stage) updateForm.getScene().getWindow();
        stage.close();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize specialization combobox with vehicle training examples
        cmbSpecialization.getItems().addAll(
                "Car Training",
                "Motorcycle Training", 
                "Heavy Vehicle Training",
                "Bus/Passenger Vehicle Training",
                "Truck Training",
                "Three-Wheeler Training",
                "Manual Transmission Training",
                "Automatic Transmission Training",
                "Defensive Driving Training",
                "Advanced Driving Training"
        );
    }

    private boolean validateFields() {
        InputValidator.clearStyle(txtName,txtEmail,txtAddress,txtContact,cmbSpecialization);
        return InputValidator.isNotEmpty(txtName, "Instructor Name") &&
                InputValidator.isAlphabetic(txtName, "Instructor Name") &&
                InputValidator.isNotEmpty(txtEmail, "Email") &&
                InputValidator.isValidEmail(txtEmail, "Email") &&
                InputValidator.isNotEmpty(txtAddress, "Address") &&
                InputValidator.isNotEmpty(txtContact, "Contact") &&
                InputValidator.isValidPhone(txtContact, "Contact") &&
                InputValidator.isSelected(cmbSpecialization, "Specialization");
    }
}