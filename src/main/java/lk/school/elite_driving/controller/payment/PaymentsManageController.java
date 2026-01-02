package lk.school.elite_driving.controller.payment;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.school.elite_driving.bo.BOFactory;
import lk.school.elite_driving.bo.custom.PaymentBO;
import lk.school.elite_driving.dto.PaymentDTO;
import lk.school.elite_driving.dto.StudentDTO;
import lk.school.elite_driving.util.AlertUtil;

import java.io.IOException;
import java.util.ArrayList;

public class PaymentsManageController {
    @FXML
    public AnchorPane rootNode;
    @FXML
    public TableView<PaymentDTO> tblPayment;
    @FXML
    public TableColumn<PaymentDTO, String> colPaymentId;
    @FXML
    public TableColumn<PaymentDTO, String> colStudentId;
    @FXML
    public TableColumn<PaymentDTO, Double> colDownPayment;
    @FXML
    public TableColumn<PaymentDTO, Double> colBalance;
    @FXML
    public TableColumn<PaymentDTO, String> colPaidDate;
    @FXML
    public TableColumn<PaymentDTO, String> colStatus;
    @FXML
    public Label lblBalance;
    @FXML
    public TextField txtSearch;

    private final PaymentBO paymentBO = (PaymentBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PAYMENT);

    public void initialize() {
        setCellValueFactory();
        loadAllPayments();
    }

    private void setCellValueFactory() {
        colPaymentId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colStudentId.setCellValueFactory(cellData -> {
            StudentDTO student = cellData.getValue().getStudent();
            return new SimpleStringProperty(student != null ? student.getStudentId() : "N/A");
        });
        colDownPayment.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colBalance.setCellValueFactory(cellData -> {
            StudentDTO student = cellData.getValue().getStudent();
            double remainingFee = student != null ? student.getRemainingFee() : 0.0;
            return new javafx.beans.property.SimpleDoubleProperty(remainingFee).asObject();
        });
        colPaidDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        // Add color coding for status
        colStatus.setCellFactory(column -> new TableCell<PaymentDTO, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    if ("Completed".equals(status)) {
                        setStyle("-fx-background-color: #d4edda; -fx-text-fill: #155724; -fx-font-weight: bold;");
                    } else if ("Pending".equals(status)) {
                        setStyle("-fx-background-color: #fff3cd; -fx-text-fill: #856404; -fx-font-weight: bold;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });
    }

    private void loadAllPayments() {
        try {
            ArrayList<PaymentDTO> paymentList = paymentBO.getAllPayment();
            ObservableList<PaymentDTO> paymentTMS = FXCollections.observableArrayList(paymentList);
            tblPayment.setItems(paymentTMS);
        } catch (Exception e) {
            AlertUtil.showError("Error loading payments: " + e.getMessage());
        }
    }

    public void btnAddonAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk.school.elite_driving/view/payment/makePaymentForm.fxml"));
            Parent parent = loader.load();

            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Make New Payment");
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.showAndWait();
            
            loadAllPayments();
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtil.showError("Error opening add form: " + e.getMessage());
        }
    }

    public void txtSearch(ActionEvent actionEvent) {
        String searchText = txtSearch.getText().trim().toLowerCase();
        if (searchText.isEmpty()) {
            loadAllPayments();
            return;
        }

        try {
            ArrayList<PaymentDTO> allPayments = paymentBO.getAllPayment();
            ObservableList<PaymentDTO> filteredPayments = FXCollections.observableArrayList();

            for (PaymentDTO paymentDTO : allPayments) {
                if (paymentDTO.getPaymentId().toLowerCase().contains(searchText) ||
                    String.valueOf(paymentDTO.getAmount()).contains(searchText) ||
                    (paymentDTO.getStudent() != null && (
                        paymentDTO.getStudent().getStudentId().toLowerCase().contains(searchText) ||
                        paymentDTO.getStudent().getStudentName().toLowerCase().contains(searchText) ||
                        String.valueOf(paymentDTO.getStudent().getRemainingFee()).contains(searchText)
                    )
                    )) {
                    filteredPayments.add(paymentDTO);
                }
            }

            tblPayment.setItems(filteredPayments);
        } catch (Exception e) {
            AlertUtil.showError("Error searching payments: " + e.getMessage());
        }
    }

}