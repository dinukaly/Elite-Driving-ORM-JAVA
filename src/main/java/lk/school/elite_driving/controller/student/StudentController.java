package lk.school.elite_driving.controller.student;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.school.elite_driving.bo.BOFactory;
import lk.school.elite_driving.bo.custom.StudentBO;
import lk.school.elite_driving.dto.StudentDTO;
import lk.school.elite_driving.tm.StudentTM;
import lk.school.elite_driving.util.AlertUtil;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class StudentController implements Initializable {
    public AnchorPane rootNode;
    public TextField txtSearch;
    public TableView<StudentTM> tblRegistration;
    public TableColumn<StudentTM, String> colStudentId;
    public TableColumn<StudentTM, String> colName;
    public TableColumn<StudentTM, String> colAddress;
    public TableColumn<StudentTM, String> colContact;
    public TableColumn<StudentTM, String> colEmail;
    public TableColumn<StudentTM, String> colCourseName;
    public TableColumn<StudentTM, String> colRegisterDate;
    public TableColumn<StudentTM, String> colActions;

    private final StudentBO studentBO = (StudentBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.STUDENT);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        loadAllStudents();
    }

    private void setCellValueFactory() {
        colStudentId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("studentAddress"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("studentContact"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("studentEmail"));

        colCourseName.setCellFactory(param -> new TableCell<>() {
            private final JFXButton btnView = new JFXButton("View");

            {
                btnView.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
                btnView.setCursor(Cursor.HAND);
                btnView.setOnAction(event -> {
                    StudentTM studentTM = getTableView().getItems().get(getIndex());
                    StudentDTO studentDTO = studentBO.getStudent(studentTM.getStudentId());
                    if (studentDTO != null && studentDTO.getCourses() != null && !studentDTO.getCourses().isEmpty()) {
                        List<String> courseNames = studentDTO.getCourses().stream()
                                .map(course -> course.getCourseName())
                                .collect(Collectors.toList());

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Courses");
                        alert.setHeaderText("Courses for " + studentTM.getStudentName());

                        ListView<String> listView = new ListView<>();
                        listView.setItems(FXCollections.observableArrayList(courseNames));
                        alert.getDialogPane().setContent(listView);
                        alert.showAndWait();
                    } else {
                        AlertUtil.showAlert(Alert.AlertType.INFORMATION, "No Courses", "This student is not enrolled in any courses.");
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnView);
                }
            }
        });

        colRegisterDate.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
        
        colActions.setCellFactory(param -> new TableCell<StudentTM, String>() {
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
                    StudentTM studentTM = getTableView().getItems().get(getIndex());
                    StudentDTO studentDTO = studentBO.getStudent(studentTM.getStudentId());
                    openUpdateForm(studentDTO);
                });

                btnDelete.setOnAction(event -> {
                    StudentTM studentTM = getTableView().getItems().get(getIndex());
                    deleteStudent(studentTM.getStudentId());
                });
            }

            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    javafx.scene.layout.HBox hbox = new javafx.scene.layout.HBox(btnEdit, btnDelete);
                    hbox.setSpacing(5);
                    setGraphic(hbox);
                }
            }
        });
    }

    private void loadAllStudents() {
        ObservableList<StudentTM> studentTMS = FXCollections.observableArrayList();
        List<StudentDTO> allStudents = studentBO.getAllStudents();

        for (StudentDTO studentDTO : allStudents) {
            studentTMS.add(new StudentTM(
                    studentDTO.getStudentId(),
                    studentDTO.getStudentName(),
                    studentDTO.getStudentAddress(),
                    studentDTO.getStudentContact(),
                    studentDTO.getStudentEmail(),
                    studentDTO.getCourses().stream().map(c -> c.getCourseName()).collect(Collectors.joining(", ")),
                    studentDTO.getRegistrationDate()
            ));
        }
        tblRegistration.setItems(studentTMS);
    }

    private void openUpdateForm(StudentDTO studentDTO) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk.school.elite_driving/view/students/updateStudentDetails.fxml"));
            Parent root = loader.load();
            UpdateStudentFormController controller = loader.getController();
            controller.initData(studentDTO);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteStudent(String studentId) {
        Optional<ButtonType> result = AlertUtil.showConfirmation("Delete Student", "Are you sure you want to delete this student?");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (studentBO.deleteStudent(studentId)) {
                AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Success", "Student deleted successfully!");
                loadAllStudents();
            } else {
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete student.");
            }
        }
    }

    @FXML
    void btnAddonAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk.school.elite_driving/view/students/studentEnrollmentForm.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    void txtSearch(ActionEvent event) {
        searchStudents();
    }

    @FXML
    void txtConntcatOnKeyReleased(KeyEvent event) {
        searchStudents();
    }

    private void searchStudents() {
        String searchText = txtSearch.getText().toLowerCase();
        ObservableList<StudentTM> filteredList = FXCollections.observableArrayList();
        List<StudentDTO> studentList = studentBO.search(searchText);

        for (StudentDTO studentDTO : studentList) {
            filteredList.add(new StudentTM(
                    studentDTO.getStudentId(),
                    studentDTO.getStudentName(),
                    studentDTO.getStudentAddress(),
                    studentDTO.getStudentContact(),
                    studentDTO.getStudentEmail(),
                    studentDTO.getCourses().stream().map(c -> c.getCourseName()).collect(Collectors.joining(", ")),
                    studentDTO.getRegistrationDate()
            ));
        }
        tblRegistration.setItems(filteredList);
    }
}