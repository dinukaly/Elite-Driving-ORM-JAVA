package lk.school.elite_driving.controller.payment;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.school.elite_driving.bo.BOFactory;
import lk.school.elite_driving.bo.custom.PaymentBO;
import lk.school.elite_driving.bo.custom.StudentBO;
import lk.school.elite_driving.dto.PaymentDTO;
import lk.school.elite_driving.dto.StudentDTO;
import lk.school.elite_driving.exception.PaymentException;
import lk.school.elite_driving.util.AlertUtil;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class MakePaymentFormController {
    @FXML
    public Label lblPaymentId;
    @FXML
    public ComboBox<String> cmbStudentId;
    public Label lblDownPayment;
    @FXML
    public TextField txtFinalPayment;
    @FXML
    public DatePicker dateFinalDate;
    @FXML
    public Label lblRemainingAmount;
    @FXML
    public Label lblMinimumInstallment;

    private final PaymentBO paymentBO = (PaymentBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PAYMENT);
    private final StudentBO studentBO = (StudentBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.STUDENT);

    public void initialize() {
        loadStudentData();
        generatePaymentId();
        setCurrentDate();
        setupStudentSelectionListener();
    }

    private void loadStudentData() {
        try {
            List<StudentDTO> students = studentBO.getAllStudents();
            ObservableList<String> studentIds = FXCollections.observableArrayList();
            for (StudentDTO student : students) {
                studentIds.add(student.getStudentId() + " - " + student.getStudentName());
            }
            cmbStudentId.setItems(studentIds);
        } catch (Exception e) {
            AlertUtil.showError("Error loading student data: " + e.getMessage());
        }
    }

    private void generatePaymentId() {
        String paymentId = "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        lblPaymentId.setText(paymentId);
    }

    private void setCurrentDate() {
        dateFinalDate.setValue(LocalDate.now());
    }

    private void setupStudentSelectionListener() {
        cmbStudentId.setOnAction(event -> {
            if (cmbStudentId.getValue() != null && !cmbStudentId.getValue().isEmpty()) {
                updatePaymentInfo();
            }
        });
    }

    private void updatePaymentInfo() {
        try {
            String studentId = cmbStudentId.getValue().split(" - ")[0];
            StudentDTO student = studentBO.getStudent(studentId);
            if (student != null) {
                double remainingAmount = student.getRemainingFee();
                double minimumInstallment = remainingAmount * 0.05; // 5% of remaining value
                
                lblRemainingAmount.setText(String.format("Remaining Amount: LKR%.2f", remainingAmount));
                lblMinimumInstallment.setText(String.format("Minimum Installment (5%%): LKR%.2f", minimumInstallment));
            }
        } catch (Exception e) {
            AlertUtil.showError("Error loading student payment info: " + e.getMessage());
        }
    }

    public void addButtonOnAction(ActionEvent actionEvent) {
        if (validateFields()) {
            try {
                String paymentId = lblPaymentId.getText();
                String studentId = cmbStudentId.getValue().split(" - ")[0];
                double paymentAmount = Double.parseDouble(txtFinalPayment.getText().trim());
                LocalDate paymentDate = dateFinalDate.getValue();
                
                // Get student details
                StudentDTO student = studentBO.getStudent(studentId);
                if (student == null) {
                    AlertUtil.showError("Student not found");
                    return;
                }

                // Create payment DTO without status; BO will handle it
                PaymentDTO paymentDTO = new PaymentDTO(paymentId, paymentAmount, Date.valueOf(paymentDate), null, student);
                paymentBO.processPayment(paymentDTO);

                AlertUtil.showSuccess("Payment processed successfully!");
                clearFields();
                generatePaymentId();

                Stage stage = (Stage) lblPaymentId.getScene().getWindow();
                stage.close();

            } catch (NumberFormatException e) {
                AlertUtil.showError("Please enter a valid amount");
            } catch (PaymentException e) {
                AlertUtil.showError("Payment Error: " + e.getMessage());
            } catch (Exception e) {
                AlertUtil.showError("Error processing payment: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void btnDiscardOnAction(ActionEvent actionEvent) {
        clearFields();
        Stage stage = (Stage) lblPaymentId.getScene().getWindow();
        stage.close();
    }

    private boolean validateFields() {
        if (cmbStudentId.getValue() == null || cmbStudentId.getValue().isEmpty()) {
            AlertUtil.showError("Please select a student");
            return false;
        }
        if (txtFinalPayment.getText().trim().isEmpty()) {
            AlertUtil.showError("Please enter payment amount");
            return false;
        }
        try {
            double paymentAmount = Double.parseDouble(txtFinalPayment.getText().trim());
            if (paymentAmount <= 0) {
                AlertUtil.showError("Payment amount must be greater than 0");
                return false;
            }
            
            // Validate minimum installment (5% of remaining value)
            String studentId = cmbStudentId.getValue().split(" - ")[0];
            StudentDTO student = studentBO.getStudent(studentId);
            if (student != null) {
                double remainingAmount = student.getRemainingFee();
                double minimumInstallment = remainingAmount * 0.05; // 5% of remaining value
                
                if (paymentAmount < minimumInstallment) {
                    AlertUtil.showError(String.format("Payment amount must be at least LKR%.2f (5%% of remaining LKR%.2f)",
                        minimumInstallment, remainingAmount));
                    return false;
                }
                
                // Validate overpayment (payment cannot exceed remaining amount)
                if (paymentAmount > remainingAmount) {
                    AlertUtil.showError(String.format("Payment amount cannot exceed remaining amount of LKR%.2f",
                        remainingAmount));
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            AlertUtil.showError("Please enter a valid numeric amount");
            return false;
        } catch (Exception e) {
            AlertUtil.showError("Error validating payment: " + e.getMessage());
            return false;
        }
        return true;
    }

    private void clearFields() {
        cmbStudentId.setValue(null);
        txtFinalPayment.clear();
    }

    public void initData(PaymentDTO paymentDTO) {
        lblPaymentId.setText(paymentDTO.getPaymentId());
        txtFinalPayment.setText(String.valueOf(paymentDTO.getAmount()));
        dateFinalDate.setValue(paymentDTO.getPaymentDate().toLocalDate());
        
        // Set student selection if student is available
        if (paymentDTO.getStudent() != null) {
            StudentDTO student = paymentDTO.getStudent();
            cmbStudentId.setValue(student.getStudentId() + " - " + student.getStudentName());
        }
    }
}