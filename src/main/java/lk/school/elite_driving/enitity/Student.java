package lk.school.elite_driving.enitity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.sql.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
    private Date registrationDate;
    @ManyToMany
    private List<Course> courses;
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Payment> payments;
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List <Lesson> lessons;
}
