package lk.school.elite_driving.controller.lesson;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.school.elite_driving.bo.BOFactory;
import lk.school.elite_driving.bo.custom.LessonBO;
import lk.school.elite_driving.dto.LessonDTO;
import lk.school.elite_driving.util.AlertUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class LessonSchedulingController {
    public TableView<LessonDTO> tblLessons;
    public TableColumn<LessonDTO, String> colStudentId;
    public TableColumn<LessonDTO, String> colLesson;
    public TableColumn<LessonDTO, String> colTime;
    public TableColumn<LessonDTO, String> colCourseId;
    public TableColumn<LessonDTO, String> colInstructorId;
    public TableColumn<LessonDTO, String> colStatus;
    public TableColumn<LessonDTO, Void> colActions;
    public TextField txtSearch;
    public AnchorPane rootNode;

    private final LessonBO lessonBO = (LessonBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.LESSON);

    public void initialize() {
        setCellValueFactory();
        loadAllLessons();
    }

    private void setCellValueFactory() {
        colStudentId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colLesson.setCellValueFactory(new PropertyValueFactory<>("lessonName"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("lessonTime"));
        colCourseId.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        colInstructorId.setCellValueFactory(new PropertyValueFactory<>("instructorId"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        colActions.setCellFactory(param -> new TableCell<>() {
            private final JFXButton btnEdit = new JFXButton();
            private final JFXButton btnDelete = new JFXButton();

            {
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
                    LessonDTO lessonDTO = getTableView().getItems().get(getIndex());
                    updateLesson(lessonDTO, event);
                });

                btnDelete.setOnAction(event -> {
                    LessonDTO lessonDTO = getTableView().getItems().get(getIndex());
                    deleteLesson(lessonDTO.getLessonId());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(10, btnEdit, btnDelete);
                    buttons.setStyle("-fx-alignment: center;");
                    setGraphic(buttons);
                }
            }
        });
    }

    private void loadAllLessons() {
        try {
            ArrayList<LessonDTO> lessonList = lessonBO.getAllLessons();
            ObservableList<LessonDTO> lessonTMS = FXCollections.observableArrayList(lessonList);
            tblLessons.setItems(lessonTMS);
        } catch (Exception e) {
            AlertUtil.showError("Error loading lessons: " + e.getMessage());
        }
    }

    public void btnAddonAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk.school.elite_driving/view/lesson/addLessonShedules.fxml"));
            Parent parent = loader.load();

            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Schedule New Lesson");
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.showAndWait();
            
            loadAllLessons();
        } catch (IOException e) {
            AlertUtil.showError("Error opening add form: " + e.getMessage());
        }
    }

    public void txtSearch(ActionEvent actionEvent) {
        String searchText = txtSearch.getText().trim().toLowerCase();
        if (searchText.isEmpty()) {
            loadAllLessons();
            return;
        }

        try {
            ArrayList<LessonDTO> allLessons = lessonBO.getAllLessons();
            ObservableList<LessonDTO> filteredLessons = FXCollections.observableArrayList();

            for (LessonDTO lessonDTO : allLessons) {
                if (lessonDTO.getLessonName().toLowerCase().contains(searchText) ||
                    lessonDTO.getLessonId().toLowerCase().contains(searchText)) {
                    filteredLessons.add(lessonDTO);
                }
            }

            tblLessons.setItems(filteredLessons);
        } catch (Exception e) {
            AlertUtil.showError("Error searching lessons: " + e.getMessage());
        }
    }

    private void updateLesson(LessonDTO lessonDTO, ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk.school.elite_driving/view/lesson/updateLessonScheduleForm.fxml"));
            Parent parent = loader.load();

            LessonScheduleFormController controller = loader.getController();
            controller.initData(lessonDTO);

            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Update Lesson");
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.showAndWait();
            
            loadAllLessons();
        } catch (IOException e) {
            AlertUtil.showError("Error opening update form: " + e.getMessage());
        }
    }

    private void deleteLesson(String lessonId) {
        Optional<ButtonType> result = AlertUtil.showConfirmation("Delete Lesson", "Are you sure you want to delete this lesson?");
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                lessonBO.deleteLesson(lessonId);
                AlertUtil.showSuccess("Lesson deleted successfully");
                loadAllLessons();
            } catch (Exception e) {
                AlertUtil.showError("Failed to delete lesson: " + e.getMessage());
            }
        }
    }
}