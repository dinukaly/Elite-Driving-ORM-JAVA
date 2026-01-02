package lk.school.elite_driving.controller.lesson;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.school.elite_driving.bo.BOFactory;
import lk.school.elite_driving.bo.custom.CourseBO;
import lk.school.elite_driving.bo.custom.InstructorBO;
import lk.school.elite_driving.bo.custom.LessonBO;
import lk.school.elite_driving.bo.custom.StudentBO;
import lk.school.elite_driving.dto.CourseDTO;
import lk.school.elite_driving.dto.InstructorDTO;
import lk.school.elite_driving.dto.LessonDTO;
import lk.school.elite_driving.dto.StudentDTO;
import lk.school.elite_driving.exception.SchedulingException;
import lk.school.elite_driving.util.AlertUtil;

import java.util.List;
import java.util.UUID;

public class LessonScheduleFormController {
    @FXML
    public Label lblScheduleId;
    @FXML
    public ComboBox<String> cmbStudentId;
    @FXML
    public TextArea textAreaLesson;
    @FXML
    public TextField txtTime;
    @FXML
    public ComboBox<String> cmbInstructorId;
    @FXML
    public ComboBox<String> cmbCourseId;

    private final LessonBO lessonBO = (LessonBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.LESSON);
    private final StudentBO studentBO = (StudentBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.STUDENT);
    private final InstructorBO instructorBO = (InstructorBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.INSTRUCTOR);
    private final CourseBO courseBO = (CourseBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.COURSE);

    public void initialize() {
        loadComboBoxData();
        generateLessonId();
    }

    private void loadComboBoxData() {
        try {
            // Load Students
            List<StudentDTO> students = studentBO.getAllStudents();
            ObservableList<String> studentIds = FXCollections.observableArrayList();
            for (StudentDTO student : students) {
                studentIds.add(student.getStudentId() + " - " + student.getStudentName());
            }
            cmbStudentId.setItems(studentIds);

            // Load Instructors
            List<InstructorDTO> instructors = instructorBO.getAllInstructors();
            ObservableList<String> instructorIds = FXCollections.observableArrayList();
            for (InstructorDTO instructor : instructors) {
                if (instructor.isActive()) {
                    instructorIds.add(instructor.getInstructorId() + " - " + instructor.getInstructorName());
                }
            }
            cmbInstructorId.setItems(instructorIds);

            // Load Courses
            List<CourseDTO> courses = courseBO.getAllCourses();
            ObservableList<String> courseIds = FXCollections.observableArrayList();
            for (CourseDTO course : courses) {
                courseIds.add(course.getCourseId() + " - " + course.getCourseName());
            }
            cmbCourseId.setItems(courseIds);

        } catch (Exception e) {
            AlertUtil.showError("Error loading combo box data: " + e.getMessage());
        }
    }

    private void generateLessonId() {
        String lessonId = "LESSON-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        lblScheduleId.setText(lessonId);
    }

    public void addButtonOnAction(ActionEvent actionEvent) {
        if (validateFields()) {
            try {
                String lessonId = lblScheduleId.getText();
                String lessonName = textAreaLesson.getText().trim();
                String lessonTime = txtTime.getText().trim();
                String status = "Scheduled";

                // Extract IDs from combo box selections
                String studentId = cmbStudentId.getValue().split(" - ")[0];
                String instructorId = cmbInstructorId.getValue().split(" - ")[0];
                String courseId = cmbCourseId.getValue().split(" - ")[0];

                LessonDTO lessonDTO = new LessonDTO();
                lessonDTO.setLessonId(lessonId);
                lessonDTO.setLessonName(lessonName);
                lessonDTO.setLessonTime(lessonTime);
                lessonDTO.setStatus(status);
                lessonDTO.setStudentId(studentId);
                lessonDTO.setInstructorId(instructorId);
                lessonDTO.setCourseId(courseId);
                
                lessonBO.scheduleLesson(lessonDTO);
                
                AlertUtil.showSuccess("Lesson scheduled successfully!");
                clearFields();
                generateLessonId();
                
                // Close the window
                Stage stage = (Stage) lblScheduleId.getScene().getWindow();
                stage.close();
                
            } catch (SchedulingException e) {
                AlertUtil.showError("Scheduling Error: " + e.getMessage());
            } catch (Exception e) {
                AlertUtil.showError("Error scheduling lesson: " + e.getMessage());
            }
        }
    }

    public void btnDiscardOnAction(ActionEvent actionEvent) {
        clearFields();
        Stage stage = (Stage) lblScheduleId.getScene().getWindow();
        stage.close();
    }

    private boolean validateFields() {
        if (cmbStudentId.getValue() == null || cmbStudentId.getValue().isEmpty()) {
            AlertUtil.showError("Please select a student");
            return false;
        }
        if (cmbInstructorId.getValue() == null || cmbInstructorId.getValue().isEmpty()) {
            AlertUtil.showError("Please select an instructor");
            return false;
        }
        if (cmbCourseId.getValue() == null || cmbCourseId.getValue().isEmpty()) {
            AlertUtil.showError("Please select a course");
            return false;
        }
        if (textAreaLesson.getText().trim().isEmpty()) {
            AlertUtil.showError("Please enter lesson description");
            return false;
        }
        if (txtTime.getText().trim().isEmpty()) {
            AlertUtil.showError("Please enter lesson time");
            return false;
        }
        return true;
    }

    private void clearFields() {
        cmbStudentId.setValue(null);
        cmbInstructorId.setValue(null);
        cmbCourseId.setValue(null);
        textAreaLesson.clear();
        txtTime.clear();
    }

    // Method to initialize data for update operations
    public void initData(LessonDTO lessonDTO) {
        lblScheduleId.setText(lessonDTO.getLessonId());
        textAreaLesson.setText(lessonDTO.getLessonName());
        txtTime.setText(lessonDTO.getLessonTime());
        // todo:need to add more fields to LessonDTO to support full update functionality
    }
}