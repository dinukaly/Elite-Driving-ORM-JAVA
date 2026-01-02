package lk.school.elite_driving.bo.util;

import lk.school.elite_driving.dto.*;
import lk.school.elite_driving.enitity.*;

import java.util.stream.Collectors;

public class DTOMapper {
    public static Course toEntity(CourseDTO courseDTO) {
        return new Course(courseDTO.getCourseId(), courseDTO.getCourseName(), courseDTO.getCourseDuration(), courseDTO.getCourseFee());
    }
    public static CourseDTO toDTO(Course course) {
        return new CourseDTO(course.getCourseId(), course.getCourseName(), course.getCourseDuration(), course.getCourseFee());
    }
    public static Student toEntity(StudentDTO studentDTO) {
        Student student = new Student();
        student.setStudentId(studentDTO.getStudentId());
        student.setStudentName(studentDTO.getStudentName());
        student.setStudentEmail(studentDTO.getStudentEmail());
        student.setStudentNic(studentDTO.getStudentNic());
        student.setStudentContact(studentDTO.getStudentContact());
        student.setStudentAddress(studentDTO.getStudentAddress());
        student.setTotalFee(studentDTO.getTotalFee());
        student.setRemainingFee(studentDTO.getRemainingFee());
        student.setDownPayment(studentDTO.getDownPayment());
        student.setRegistrationDate(studentDTO.getRegistrationDate());
        if (studentDTO.getCourses() != null) {
            student.setCourses(studentDTO.getCourses().stream().map(DTOMapper::toEntity).collect(Collectors.toList()));
        }
        // Avoid circular reference - don't map payments when converting to entity
        return student;
    }
    public static StudentDTO toDTO(Student student) {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setStudentId(student.getStudentId());
        studentDTO.setStudentName(student.getStudentName());
        studentDTO.setStudentEmail(student.getStudentEmail());
        studentDTO.setStudentNic(student.getStudentNic());
        studentDTO.setStudentContact(student.getStudentContact());
        studentDTO.setStudentAddress(student.getStudentAddress());
        studentDTO.setTotalFee(student.getTotalFee());
        studentDTO.setRemainingFee(student.getRemainingFee());
        studentDTO.setDownPayment(student.getDownPayment());
        studentDTO.setRegistrationDate(student.getRegistrationDate());
        if (student.getCourses() != null) {
            studentDTO.setCourses(student.getCourses().stream().map(DTOMapper::toDTO).collect(Collectors.toList()));
        }
        // Avoid circular reference - don't map payments for table display
        return studentDTO;
    }
    public static Instructor toEntity(InstructorDTO instructorDTO) {
        return new Instructor(instructorDTO.getInstructorId(), instructorDTO.getInstructorName(), instructorDTO.getInstructorAddress(), instructorDTO.getInstructorPhone(), instructorDTO.getInstructorEmail(), instructorDTO.getInstructorSpecialization(), instructorDTO.isActive());
    }
    public static InstructorDTO toDTO(Instructor instructor) {
        return new InstructorDTO(instructor.getInstructorId(), instructor.getInstructorName(), instructor.getInstructorAddress(), instructor.getInstructorPhone(), instructor.getInstructorEmail(), instructor.getInstructorSpecialization(), instructor.isActive());
    }
    public static Lesson toEntity(LessonDTO lessonDTO) {
        Lesson lesson = new Lesson();
        lesson.setLessonId(lessonDTO.getLessonId());
        lesson.setLessonName(lessonDTO.getLessonName());
        lesson.setLessonTime(lessonDTO.getLessonTime());
        lesson.setStatus(lessonDTO.getStatus());
        return lesson;
    }
    public static LessonDTO toDTO(Lesson lesson) {
        LessonDTO lessonDTO = new LessonDTO();
        lessonDTO.setLessonId(lesson.getLessonId());
        lessonDTO.setLessonName(lesson.getLessonName());
        lessonDTO.setLessonTime(lesson.getLessonTime());
        lessonDTO.setStatus(lesson.getStatus());
        
        if (lesson.getStudent() != null) {
            lessonDTO.setStudentId(lesson.getStudent().getStudentId());
            lessonDTO.setStudentName(lesson.getStudent().getStudentName());
        }
        
        if (lesson.getCourse() != null) {
            lessonDTO.setCourseId(lesson.getCourse().getCourseId());
            lessonDTO.setCourseName(lesson.getCourse().getCourseName());
        }
        
        if (lesson.getInstructor() != null) {
            lessonDTO.setInstructorId(lesson.getInstructor().getInstructorId());
            lessonDTO.setInstructorName(lesson.getInstructor().getInstructorName());
        }
        
        return lessonDTO;
    }
    public static Payment toEntity(PaymentDTO paymentDTO, org.hibernate.Session session) {
        if (paymentDTO == null) {
            return null;
        }
        Payment payment = new Payment();
        payment.setPaymentId(paymentDTO.getPaymentId());
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentDate(paymentDTO.getPaymentDate());
        payment.setStatus(paymentDTO.getStatus());

        if (paymentDTO.getStudent() != null && paymentDTO.getStudent().getStudentId() != null) {
            Student student = session.get(Student.class, paymentDTO.getStudent().getStudentId());
            payment.setStudent(student);
        }

        return payment;
    }
    public static PaymentDTO toDTO(Payment payment) {
        StudentDTO studentDTO = null;
        if (payment.getStudent() != null) {
            studentDTO = toDTO(payment.getStudent());
            // Avoid circular reference by not mapping payments back
            studentDTO.setPayments(null);
        }
        return new PaymentDTO(payment.getPaymentId(), payment.getAmount(), payment.getPaymentDate(), payment.getStatus(), studentDTO);
    }
    public static User toEntity(UserDTO userDTO) {
        return new User(userDTO.getUserId(), userDTO.getUserName(), userDTO.getPassword(), userDTO.getUserRole());
    }
    public static UserDTO toDTO(User user) {
        return new UserDTO(user.getUserId(), user.getUserName(), user.getPassword(), user.getUserRole());
    }


}