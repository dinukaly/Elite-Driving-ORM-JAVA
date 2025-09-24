package lk.school.elite_driving.controller.payment;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class PaymentsManageController {
    public AnchorPane rootNode;
    public TableView tblPayment;
    public TableColumn colStudentId;
    public TableColumn colDownPayment;
    public TableColumn colBalance;
    public TableColumn colInstallment;
    public TableColumn colPaidDate;
    public TableColumn colStatus;
    public TableColumn colActions;
    public Label lblBalance;

    public void btnAddonAction(ActionEvent actionEvent) {
    }

    public void txtSearch(ActionEvent actionEvent) {

    }
}
