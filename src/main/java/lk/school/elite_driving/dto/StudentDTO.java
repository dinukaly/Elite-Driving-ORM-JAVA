package lk.school.elite_driving.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentDTO {
    private String studentId;
    private String studentName;
    private String studentEmail;
    private String studentNic;
    private String studentContact;
    private String studentAddress;
    private double totalFee; // sum of courses
    private double remainingFee;// auto updated
    private double downPayment; // initial payment
    private Date registrationDate;
    private List<CourseDTO> courses;
    private List<PaymentDTO> payments;

    public StudentDTO(String studentId, String studentName, String studentEmail, String studentNic, String studentContact, String studentAddress, double totalFee, double remainingFee, double downPayment, Date registrationDate) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.studentNic = studentNic;
        this.studentContact = studentContact;
        this.studentAddress = studentAddress;
        this.totalFee = totalFee;
        this.remainingFee = remainingFee;
        this.downPayment = downPayment;
        this.registrationDate = registrationDate;
    }
}