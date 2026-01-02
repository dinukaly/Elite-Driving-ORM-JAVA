package lk.school.elite_driving.enitity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Student implements SuperEntity {
    @Id
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
    @ManyToMany
    private List<Course> courses;
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Payment> payments;
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List <Lesson> lessons;

    public Student(String studentId, String studentName, String studentEmail, String studentNic, String studentContact, String studentAddress, double totalFee, double remainingFee, double downPayment, Date registrationDate) {
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