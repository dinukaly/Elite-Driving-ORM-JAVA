package lk.school.elite_driving.controller.course;

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
import lk.school.elite_driving.bo.custom.CourseBO;
import lk.school.elite_driving.dto.CourseDTO;
import lk.school.elite_driving.tm.CourseTM;
import lk.school.elite_driving.util.AlertUtil;

import java.util.List;

public class CourseManageController {
    public TableView<CourseTM> tblProgram;
    public TableColumn<CourseTM, String> colProgramId;
    public TableColumn<CourseTM, String> colProgramName;
    public TableColumn<CourseTM, String> colDuration;
    public TableColumn<CourseTM, Double> colProgramFee;
    public TableColumn<CourseTM, Void> colActions;
    public TextField txtSearch;
    public AnchorPane rootNode;

    CourseBO courseBO = (CourseBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.COURSE);

public void initialize() {
        setCellValueFactory();
        loadAllCourses();
}

    private void setCellValueFactory() {
        colProgramId.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        colProgramName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("courseDuration"));
        colProgramFee.setCellValueFactory(new PropertyValueFactory<>("courseFee"));
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
                    CourseTM courseTM = getTableView().getItems().get(getIndex());
                    updateCourse(courseTM, event);
                });

                btnDelete.setOnAction(event -> {
                    CourseTM courseTM = getTableView().getItems().get(getIndex());
                    deleteCourse(courseTM.getCourseId());
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

    private void loadAllCourses() {
        try {
            if (courseBO == null) {
                System.err.println("CourseBO is null - cannot load courses");
                return;
            }
            List<CourseDTO> courseDTOList = courseBO.getAllCourses();
            ObservableList<CourseTM> courseTMS = FXCollections.observableArrayList();

            for (CourseDTO courseDTO : courseDTOList) {
                CourseTM courseTM = new CourseTM(
                        courseDTO.getCourseId(),
                        courseDTO.getCourseName(),
                        courseDTO.getCourseDuration(),
                        courseDTO.getCourseFee(),
                        new JFXButton(),
                        new JFXButton()
                );
                courseTMS.add(courseTM);
            }

            tblProgram.setItems(courseTMS);
        } catch (Exception e) {
            System.err.println("Error in loadAllCourses: " + e.getMessage());
            e.printStackTrace();
            AlertUtil.showError("Error loading courses: " + e.getMessage());
        }
    }

    public void addButtonOnAction(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk.school.elite_driving/view/course/addCourse.fxml"));
            Parent parent = loader.load();

            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add New Course");
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.showAndWait();
            // Refresh after closing
            //loadAllCourses();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void txtSearch(ActionEvent actionEvent) {
        String searchText = txtSearch.getText().trim().toLowerCase();
        if (searchText.isEmpty()) {
            //loadAllCourses();
            return;
        }

        try {
            List<CourseDTO> allCourses = courseBO.getAllCourses();
            ObservableList<CourseTM> filteredCourses = FXCollections.observableArrayList();

            for (CourseDTO courseDTO : allCourses) {
                if (courseDTO.getCourseName().toLowerCase().contains(searchText) ||
                    courseDTO.getCourseId().toLowerCase().contains(searchText)) {
                    CourseTM courseTM = new CourseTM(
                            courseDTO.getCourseId(),
                            courseDTO.getCourseName(),
                            courseDTO.getCourseDuration(),
                            courseDTO.getCourseFee(),
                            new JFXButton(),
                            new JFXButton()
                    );
                    filteredCourses.add(courseTM);
                }
            }

            tblProgram.setItems(filteredCourses);
        } catch (Exception e) {
            AlertUtil.showError("Error searching courses: " + e.getMessage());
        }
    }

    private void updateCourse(CourseTM courseTM, ActionEvent triggerEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/lk.school.elite_driving/view/course/updateCourse.fxml"));
            Parent parent = loader.load();

            UpdateCourseFormController controller = loader.getController();
            controller.initData(courseTM);

            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Update Course");
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.showAndWait();

            // After closing, reload table
           // loadAllCourses();
        } catch (Exception e) {
            AlertUtil.showError("Error opening update form: " + e.getMessage());
        }
    }

    private void deleteCourse(String courseId) {
        try {
            if (courseBO == null) {
                System.err.println("CourseBO is null - cannot delete course");
                AlertUtil.showError("CourseBO is not initialized");
                return;
            }
            var result = AlertUtil.showConfirmation("Confirm Delete", "Are you sure you want to delete course " + courseId + "?");
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean isDeleted = courseBO.deleteCourse(courseId);
                if (isDeleted) {
                    AlertUtil.showSuccess("Course deleted successfully");
                    loadAllCourses();
                } else {
                    AlertUtil.showFailure("Failed to delete course");
                }
            }
        } catch (Exception e) {
            System.err.println("Error in deleteCourse: " + e.getMessage());
            e.printStackTrace();
            AlertUtil.showError("Error deleting course: " + e.getMessage());
        }
    }
}