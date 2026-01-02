package lk.school.elite_driving.controller.receptionist;

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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.school.elite_driving.bo.BOFactory;
import lk.school.elite_driving.bo.custom.AuthBO;
import lk.school.elite_driving.dto.UserDTO;
import lk.school.elite_driving.enitity.UserRole;
import lk.school.elite_driving.tm.UserTM;
import lk.school.elite_driving.util.AlertUtil;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ReceptionistManageController {
    @FXML
    public TableView<UserTM> tblReceptionist;
    @FXML
    public TableColumn<UserTM, String> colReceptionistId;
    @FXML
    public TableColumn<UserTM, String> colReceptionistName;
    @FXML
    public TableColumn<UserTM, String> colUserRole;
    @FXML
    public TableColumn<UserTM, Void> colActions;
    public AnchorPane rootNode;
    public TextField txtSearch;

    private AuthBO authBO = (AuthBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.USER);

    public void initialize() {
        setCellValueFactory();
        loadAllReceptionists();
    }

    private void setCellValueFactory() {
        colReceptionistId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colReceptionistName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colUserRole.setCellValueFactory(new PropertyValueFactory<>("userRole"));
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
                    UserTM userTM = getTableView().getItems().get(getIndex());
                    updateReceptionist(userTM, event);
                });

                btnDelete.setOnAction(event -> {
                    UserTM userTM = getTableView().getItems().get(getIndex());
                    deleteReceptionist(userTM.getUserId());
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

    private void loadAllReceptionists() {
        try {
            List<UserDTO> userList = authBO.getAllUsers();
            ObservableList<UserTM> tmList = FXCollections.observableArrayList();
            for (UserDTO userDTO : userList) {
                if (userDTO.getUserRole() == UserRole.RECEPTIONIST) {
                    tmList.add(new UserTM(
                            userDTO.getUserId(),
                            userDTO.getUserName(),
                            userDTO.getUserRole(),
                            new JFXButton("Edit"),
                            new JFXButton("Delete")
                    ));
                }
            }
            tblReceptionist.setItems(tmList);
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showError("Error loading receptionists: " + e.getMessage());
        }
    }

    private void deleteReceptionist(String userId) {
        Optional<ButtonType> result = AlertUtil.showConfirmation("Delete Receptionist", "Are you sure you want to delete this receptionist?");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                authBO.deleteUser(userId); // We need to add this method to AuthBO
                loadAllReceptionists();
                AlertUtil.showSuccess("Receptionist deleted successfully!");
            } catch (Exception e) {
                AlertUtil.showError("Failed to delete receptionist: " + e.getMessage());
            }
        }
    }

    private void updateReceptionist(UserTM userTM, ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk.school.elite_driving/view/receptionist/updateReceptionistForm.fxml"));
            Parent root = loader.load();
            UpdateReceptionistFormController controller = loader.getController();
            controller.initData(userTM);
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            stage.showAndWait();
            loadAllReceptionists();
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtil.showError("Error loading update form: " + e.getMessage());
        }
    }

    public void txtSearch(ActionEvent actionEvent) {
        // Implement search functionality if needed
    }

    public void txtSearchKeyReleased(KeyEvent keyEvent) {
        // Implement search functionality if needed
    }

    public void btnAddonAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk.school.elite_driving/view/receptionist/addReceptionistForm.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
            stage.showAndWait();
            loadAllReceptionists();
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtil.showError("Error loading add form: " + e.getMessage());
        }
    }
}