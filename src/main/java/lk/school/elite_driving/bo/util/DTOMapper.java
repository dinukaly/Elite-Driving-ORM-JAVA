package lk.school.elite_driving.bo.util;

import lk.school.elite_driving.dto.*;
import lk.school.elite_driving.enitity.*;

public class DTOMapper {
    public static Course toEntity(CourseDTO courseDTO) {
        return new Course(courseDTO.getCourseId(), courseDTO.getCourseName(), courseDTO.getCourseDescription(), courseDTO.getCourseDuration(), courseDTO.getCourseFee());
    }
    public static CourseDTO toDTO(Course course) {
        return new CourseDTO(course.getCourseId(), course.getCourseName(), course.getCourseDescription(), course.getCourseDuration(), course.getCourseFee());
    }
    public static Student toEntity(StudentDTO studentDTO) {
        return new Student(studentDTO.getStudentId(), studentDTO.getStudentName(), studentDTO.getStudentEmail(), studentDTO.getStudentNic(), studentDTO.getStudentContact(),studentDTO.getStudentAddress(),studentDTO.getTotalFee(),studentDTO.getRemainingFee(),studentDTO.getRegistrationDate());
    }
    public static StudentDTO toDTO(Student student) {
        return new StudentDTO(student.getStudentId(), student.getStudentName(), student.getStudentEmail(), student.getStudentNic(), student.getStudentContact(),student.getStudentAddress(),student.getTotalFee(),student.getRemainingFee(),student.getRegistrationDate());
    }
    public static Instructor toEntity(InstructorDTO instructorDTO) {
        return new Instructor(instructorDTO.getInstructorId(), instructorDTO.getInstructorName(), instructorDTO.getInstructorEmail(), instructorDTO.getInstructorPhone(), instructorDTO.getInstructorAddress(), instructorDTO.isActive());
    }
    public static InstructorDTO toDTO(Instructor instructor) {
        return new InstructorDTO(instructor.getInstructorId(), instructor.getInstructorName(), instructor.getInstructorEmail(), instructor.getInstructorPhone(), instructor.getInstructorAddress(), instructor.isActive());
    }
    public static Lesson toEntity(LessonDTO lessonDTO) {
        return new Lesson(lessonDTO.getLessonId(), lessonDTO.getLessonName(), lessonDTO.getLessonDescription(), lessonDTO.getLessonTime(), lessonDTO.getStatus());
    }
    public static LessonDTO toDTO(Lesson lesson) {
        return new LessonDTO(lesson.getLessonId(), lesson.getLessonName(), lesson.getLessonDescription(), lesson.getLessonTime(), lesson.getStatus());
    }
    public static Payment toEntity(PaymentDTO paymentDTO) {
        return new Payment(paymentDTO.getPaymentId(), paymentDTO.getAmount(), paymentDTO.getPaymentDate(), paymentDTO.getStatus());
    }
    public static PaymentDTO toDTO(Payment payment) {
        return new PaymentDTO(payment.getPaymentId(), payment.getAmount(), payment.getPaymentDate(), payment.getStatus());
    }
    public static User toEntity(UserDTO userDTO) {
        return new User(userDTO.getUserId(), userDTO.getUserName(), userDTO.getPassword(), userDTO.getUserRole());
    }
    public static UserDTO toDTO(User user) {
        return new UserDTO(user.getUserId(), user.getUserName(), user.getPassword(), user.getUserRole());
    }


}
