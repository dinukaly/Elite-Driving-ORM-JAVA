package lk.school.elite_driving.controller.instructor;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.school.elite_driving.bo.BOFactory;
import lk.school.elite_driving.bo.custom.InstructorBO;
import lk.school.elite_driving.dto.InstructorDTO;
import lk.school.elite_driving.tm.InstructorTM;
import lk.school.elite_driving.util.AlertUtil;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class InstructorController {
    @FXML
    public TableView<InstructorTM> tblInstructor;
    @FXML
    public TableColumn<InstructorTM, String> colInstructorId;
    @FXML
    public TableColumn<InstructorTM, String> colName;
    @FXML
    public TableColumn<InstructorTM, String> colAddress;
    @FXML
    public TableColumn<InstructorTM, String> colContact;
    @FXML
    public TableColumn<InstructorTM, String> colEmail;
    @FXML
    public TableColumn<InstructorTM, String> colSpecialization;
    @FXML
    public TableColumn<InstructorTM, String> colAvailability;
    @FXML
    public TableColumn<InstructorTM, Void> colActions;
    public AnchorPane rootNode;

    InstructorBO instructorBO = (InstructorBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.INSTRUCTOR);

    public void initialize() {
        setCellValueFactory();
        loadAllInstructors();
    }

    private void setCellValueFactory() {
        colInstructorId.setCellValueFactory(new PropertyValueFactory<>("instructorId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("instructorName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("instructorAddress"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("instructorPhone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("instructorEmail"));
        colSpecialization.setCellValueFactory(new PropertyValueFactory<>("instructorSpecialization"));
        colAvailability.setCellValueFactory(new PropertyValueFactory<>("active"));
        colActions.setCellFactory(param -> new TableCell<>() {
            private final JFXButton btnEdit = new JFXButton();
            private final JFXButton btnDelete = new JFXButton();

            {
                // Set styles and icons for the buttons
                btnEdit.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
                btnDelete.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");

                ImageView editIcon = new ImageView(new Image(getClass().getResourceAsStream("/lk.school.elite_driving/icons/icons8-update-24.png")));
                editIcon.setFitWidth(16);
                editIcon.setFitHeight(16);
                btnEdit.setGraphic(editIcon);

                ImageView deleteIcon = new ImageView(new Image(getClass().getResourceAsStream("/lk.school.elite_driving/icons/icons8-delete-30.png")));
                deleteIcon.setFitWidth(16);
                deleteIcon.setFitHeight(16);
                btnDelete.setGraphic(deleteIcon);

                btnEdit.setCursor(Cursor.HAND);
                btnDelete.setCursor(Cursor.HAND);

                btnEdit.setOnAction(event -> {
                    InstructorTM instructorTM = getTableView().getItems().get(getIndex());
                    updateInstructor(instructorTM, event);
                });

                btnDelete.setOnAction(event -> {
                    InstructorTM instructorTM = getTableView().getItems().get(getIndex());
                    deleteInstructor(instructorTM.getInstructorId());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(10, btnEdit, btnDelete);
                    setGraphic(hbox);
                }
            }
        });
    }

    private void loadAllInstructors() {
        try {
            List<InstructorDTO> instructorList = instructorBO.getAllInstructors();
            ObservableList<InstructorTM> tmList = FXCollections.observableArrayList();
            for (InstructorDTO instructorDTO : instructorList) {
                tmList.add(new InstructorTM(
                        instructorDTO.getInstructorId(),
                        instructorDTO.getInstructorName(),
                        instructorDTO.getInstructorAddress(),
                        instructorDTO.getInstructorPhone(),
                        instructorDTO.getInstructorEmail(),
                        instructorDTO.getInstructorSpecialization(),
                        instructorDTO.isActive(),
                        new JFXButton("Edit"),
                        new JFXButton("Delete")
                ));
            }
            tblInstructor.setItems(tmList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteInstructor(String instructorId) {
        Optional<ButtonType> result = AlertUtil.showConfirmation("Delete Instructor", "Are you sure you want to delete this instructor?");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                instructorBO.deleteInstructor(instructorId);
                loadAllInstructors();
            } catch (Exception e) {
                AlertUtil.showError("Failed to delete instructor: " + e.getMessage());
            }
        }
    }

    private void updateInstructor(InstructorTM instructorTM, ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk.school.elite_driving/view/instructor/updateInstructorForm.fxml"));
            Parent root = loader.load();
            UpdateInstructorFormController controller = loader.getController();
            controller.initData(instructorTM);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            stage.showAndWait();
            loadAllInstructors();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void txtConntcatOnKeyReleased(KeyEvent keyEvent) {
    }

    public void btnAddonAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk.school.elite_driving/view/instructor/addInstructorForm.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            stage.showAndWait();
            loadAllInstructors();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void txtSearch(ActionEvent actionEvent) {
    }
}