package lk.school.elite_driving.enitity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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
public class Student {
    @Id
    private int studentId;
    private String studentName;
    private String studentEmail;
    private String studentNic;
    private String studentContact;
    private String studentAddress;
    private double totalFee; // sum of courses
    private double remainingFee;// auto updated
    private Date registrationDate;
    @ManyToMany
    private List<Course> courses;
    @OneToMany(mappedBy = "student")
    private List<Payment> payments;
    @OneToMany(mappedBy = "student")
    private List <Lesson> lessons;
}
