package lk.school.elite_driving.controller.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.school.elite_driving.bo.BOFactory;
import lk.school.elite_driving.bo.custom.*;
import lk.school.elite_driving.dto.LessonDTO;
import lk.school.elite_driving.dto.PaymentDTO;
import lk.school.elite_driving.dto.StudentDTO;
import lk.school.elite_driving.tm.ScheduledLessonTM;
import lk.school.elite_driving.tm.StudentEnrollmentTM;
import lk.school.elite_driving.util.AlertUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {

    // BO instances
    private final StudentBO studentBO = (StudentBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.STUDENT);
    private final CourseBO courseBO = (CourseBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.COURSE);
    private final InstructorBO instructorBO = (InstructorBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.INSTRUCTOR);
    private final PaymentBO paymentBO = (PaymentBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PAYMENT);
    private final LessonBO lessonBO = (LessonBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.LESSON);

    // Table Views and Columns for Student Enrollment
    @FXML
    public TableView<StudentEnrollmentTM> tblStudentEnrollment;
    @FXML
    public TableColumn<StudentEnrollmentTM, String> colStudentId;
    @FXML
    public TableColumn<StudentEnrollmentTM, String> colStudentName;
    @FXML
    public TableColumn<StudentEnrollmentTM, String> colStudentEmail;
    @FXML
    public TableColumn<StudentEnrollmentTM, String> colCourseName;
    @FXML
    public TableColumn<StudentEnrollmentTM, String> colEnrollmentStatus;
    @FXML
    public TableColumn<StudentEnrollmentTM, Double> colTotalFee;
    @FXML
    public TableColumn<StudentEnrollmentTM, Double> colRemainingFee;

    // Table Views and Columns for Scheduled Lessons
    @FXML
    public TableView<ScheduledLessonTM> tblScheduledLessons;
    @FXML
    public TableColumn<ScheduledLessonTM, String> colLessonId;
    @FXML
    public TableColumn<ScheduledLessonTM, String> colLessonCourseName;
    @FXML
    public TableColumn<ScheduledLessonTM, String> colLessonName;
    @FXML
    public TableColumn<ScheduledLessonTM, String> colLessonTime;
    @FXML
    public TableColumn<ScheduledLessonTM, String> colInstructorName;
    @FXML
    public TableColumn<ScheduledLessonTM, String> colLessonStudentName;
    @FXML
    public TableColumn<ScheduledLessonTM, String> colLessonStatus;
    public AnchorPane rootNode;
    public Label lblTotStudent;
    public Label lblTotCourses;
    public Label lblTotEmployees;
    public Label lblTotRevenue;
    public AnchorPane childRootNode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDashboardData();
        setupTables();
    }

    public void btnUserSettingOnAction(MouseEvent mouseEvent) {
        // Refresh dashboard data
        loadDashboardData();
    }

    private void loadDashboardData() {
        loadStatsCards();
        loadStudentEnrollmentTable();
        loadScheduledLessonsTable();
    }

    private void loadStatsCards() {
        try {
            // Load total students
            List<StudentDTO> allStudents = studentBO.getAllStudents();
            lblTotStudent.setText(String.valueOf(allStudents.size()));

            // Load total courses
            lblTotCourses.setText(String.valueOf(courseBO.getAllCourses().size()));

            // Load total instructors (employees)
            lblTotEmployees.setText(String.valueOf(instructorBO.getAllInstructors().size()));

            // Load total revenue from payments
            double totalRevenue = 0.0;
            try {
                ArrayList<PaymentDTO> allPayments = paymentBO.getAllPayment();
                for (PaymentDTO payment : allPayments) {
                    totalRevenue += payment.getAmount();
                }
            } catch (Exception e) {
                System.err.println("Error loading payments: " + e.getMessage());
            }
            lblTotRevenue.setText(String.format("%.2f", totalRevenue));

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showError("Error loading dashboard statistics: " + e.getMessage());
        }
    }

    private void setupTables() {
        // Setup Student Enrollment Table
        colStudentId.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        colStudentName.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        colStudentEmail.setCellValueFactory(new PropertyValueFactory<>("studentEmail"));
        colCourseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        colEnrollmentStatus.setCellValueFactory(new PropertyValueFactory<>("enrollmentStatus"));
        colTotalFee.setCellValueFactory(new PropertyValueFactory<>("totalFee"));
        colRemainingFee.setCellValueFactory(new PropertyValueFactory<>("remainingFee"));

        // Setup Scheduled Lessons Table
        colLessonId.setCellValueFactory(new PropertyValueFactory<>("lessonId"));
        colLessonCourseName.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        colLessonName.setCellValueFactory(new PropertyValueFactory<>("lessonName"));
        colLessonTime.setCellValueFactory(new PropertyValueFactory<>("lessonTime"));
        colInstructorName.setCellValueFactory(new PropertyValueFactory<>("instructorName"));
        colLessonStudentName.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        colLessonStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadStudentEnrollmentTable() {
        try {
            ObservableList<StudentEnrollmentTM> enrollmentList = FXCollections.observableArrayList();
            List<StudentDTO> students = studentBO.getAllStudents();

            for (StudentDTO student : students) {
                String courseNames = "N/A";
                String enrollmentStatus = "Active";

                if (student.getCourses() != null && !student.getCourses().isEmpty()) {
                    courseNames = student.getCourses().stream()
                            .map(course -> course.getCourseName())
                            .reduce((a, b) -> a + ", " + b)
                            .orElse("N/A");
                }

                // Determine enrollment status based on payment completion
                // Only "Active" or "Completed" status
                if (student.getRemainingFee() <= 0) {
                    enrollmentStatus = "Completed";
                } else {
                    enrollmentStatus = "Active";
                }

                StudentEnrollmentTM enrollmentTM = new StudentEnrollmentTM(
                        student.getStudentId(),
                        student.getStudentName(),
                        student.getStudentEmail(),
                        courseNames,
                        enrollmentStatus,
                        student.getTotalFee(),
                        student.getRemainingFee()
                );
                enrollmentList.add(enrollmentTM);
            }

            tblStudentEnrollment.setItems(enrollmentList);
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showError("Error loading student enrollment data: " + e.getMessage());
        }
    }

    private void loadScheduledLessonsTable() {
        try {
            ObservableList<ScheduledLessonTM> lessonList = FXCollections.observableArrayList();
            ArrayList<LessonDTO> lessons = lessonBO.getAllLessons();

            for (LessonDTO lesson : lessons) {
                ScheduledLessonTM lessonTM = new ScheduledLessonTM(
                        lesson.getLessonId(),
                        lesson.getCourseName(),
                        lesson.getLessonName(),
                        lesson.getLessonTime(),
                        lesson.getInstructorName(),
                        lesson.getStudentName(),
                        lesson.getStatus()
                );
                lessonList.add(lessonTM);
            }

            tblScheduledLessons.setItems(lessonList);
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showError("Error loading scheduled lessons data: " + e.getMessage());
        }
    }

    public void btnDashboardOnAction(ActionEvent actionEvent) {
        //clear child root node
        rootNode.getChildren().clear();
        //load user setting view
        try {
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/lk.school.elite_driving/view/admin/adminDashboard.fxml"));
            rootNode.getChildren().add(anchorPane);
        } catch (IOException e) {
            AlertUtil.showError("Error loading user setting view: " + e.getMessage());
        }
    }

    public void btnInstructorManageOnAction(ActionEvent actionEvent) {
        //clear child root node
        childRootNode.getChildren().clear();
        //load user setting view
        try {
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/lk.school.elite_driving/view/instructor/instructorManage.fxml"));
            childRootNode.getChildren().add(anchorPane);
        } catch (IOException e) {
            AlertUtil.showError("Error loading user setting view: " + e.getMessage());
        }
    }

    public void btnStudentManageOnAction(ActionEvent actionEvent) {
        //clear child root node
        childRootNode.getChildren().clear();
        //load user setting view
        try {
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/lk.school.elite_driving/view/students/studentManage.fxml"));
            childRootNode.getChildren().add(anchorPane);
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showError("Error loading user setting view: " + e.getMessage());
        }
    }

    public void btnCourseManageOnAction(ActionEvent actionEvent) {
        //clear child root node
        childRootNode.getChildren().clear();
        //load user setting view
        try {
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/lk.school.elite_driving/view/course/coursesManage.fxml"));
            childRootNode.getChildren().add(anchorPane);
        } catch (IOException e) {
            AlertUtil.showError("Error loading user setting view: " + e.getMessage());
        }
    }

    public void btnpaymentManageOnAction(ActionEvent actionEvent) {
        //clear child root node
        childRootNode.getChildren().clear();
        //load user setting view
        try {
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/lk.school.elite_driving/view/payment/paymentsManage.fxml"));
            childRootNode.getChildren().add(anchorPane);
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.showError("Error loading payment view: " + e.getMessage());
        }
    }

    public void btnLogOutOnAction(ActionEvent actionEvent) throws IOException {

        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/lk.school.elite_driving/view/welcomePage.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(anchorPane);
        stage.setTitle("Receptionist Login");
        stage.setScene(scene);
        stage.show();
        Stage prevStage = (Stage) rootNode.getScene().getWindow();
        prevStage.close();
    }

    public void btnReceptionistManageOnAction(ActionEvent actionEvent) {
        //clear child root node
        childRootNode.getChildren().clear();
        //load receptionist management view
        try {
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/lk.school.elite_driving/view/receptionist/receptionistManage.fxml"));
            childRootNode.getChildren().add(anchorPane);
        } catch (IOException e) {
            AlertUtil.showError("Error loading receptionist management view: " + e.getMessage());
        }
    }

    public void btnLessonOnAction(ActionEvent actionEvent) {
        //clear child root node
        childRootNode.getChildren().clear();
        //load user setting view
        try {
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/lk.school.elite_driving/view/lesson/lessonScheduling.fxml"));
            childRootNode.getChildren().add(anchorPane);
        } catch (IOException e) {
            AlertUtil.showError("Error loading user setting view: " + e.getMessage());
        }
    }
}