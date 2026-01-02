package lk.school.elite_driving.controller.instructor;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lk.school.elite_driving.bo.BOFactory;
import lk.school.elite_driving.bo.custom.InstructorBO;
import lk.school.elite_driving.dto.InstructorDTO;
import lk.school.elite_driving.util.InputValidator;

import java.net.URL;
import java.util.ResourceBundle;

public class AddInstructorFormController implements Initializable {
    private final InstructorBO instructorBO = (InstructorBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.INSTRUCTOR);

    public Label instructorId;
    public TextField txtName;
    public TextField txtEmail;
    public TextField txtAddress;
    public TextField txtContact;
    public CheckBox checkAvailability;
    public ComboBox<String> cmbSpecialization;

    public void addButtonOnAction(ActionEvent actionEvent) {
        if(validateFields()) {
            try {
                // Get form data
                String name = txtName.getText().trim();
                String email = txtEmail.getText().trim();
                String address = txtAddress.getText().trim();
                String contact = txtContact.getText().trim();
                String specialization = cmbSpecialization.getValue();
                boolean isActive = checkAvailability.isSelected();


                // Generate new instructor ID
                String newInstructorId = getNewUserId();

                // Create InstructorDTO
                InstructorDTO instructorDTO = new InstructorDTO(
                        newInstructorId,
                        name,
                        address,
                        contact,
                        email,
                        specialization,
                        isActive
                );

                // Save instructor using BO and TransactionalUtil
                boolean success = instructorBO.addInstructor(instructorDTO);

                if (success) {
                    // Update the instructorId label with the generated ID
                    instructorId.setText(newInstructorId);
                    clearForm();

                    System.out.println("Instructor added successfully with ID: " + newInstructorId);
                } else {
                    System.out.println("Failed to add instructor");
                }

            } catch (Exception e) {
                System.err.println("Error adding instructor: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void btnDiscardOnAction(ActionEvent actionEvent) {
        clearForm();
    }

    private void clearForm() {
        txtName.clear();
        txtEmail.clear();
        txtAddress.clear();
        txtContact.clear();
        cmbSpecialization.setValue(null);
        checkAvailability.setSelected(false);
        InputValidator.clearStyle(txtName,txtEmail,txtAddress,txtContact,cmbSpecialization);
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

        // Generate and display the initial instructor ID
        String initialId = getNewUserId();
        instructorId.setText(initialId);
    }

    String getNewUserId() {
        return instructorBO.getNewInstructorId();

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