package lk.school.elite_driving.enitity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Instructor implements SuperEntity {
    @Id
    private String instructorId;
    private String instructorName;
    private String instructorAddress;
    private String instructorPhone;
    private String instructorEmail;
    private String instructorSpecialization;

    private boolean isActive;

    @ManyToOne
    private Course course;

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL)
    private List<Lesson> lessons;

    public Instructor(String instructorId, String instructorName, String instructorAddress, String instructorPhone, String instructorEmail, String instructorSpecialization, boolean active) {
        this.instructorId = instructorId;
        this.instructorName = instructorName;
        this.instructorAddress = instructorAddress;
        this.instructorPhone = instructorPhone;
        this.instructorEmail = instructorEmail;
        this.instructorSpecialization = instructorSpecialization;
        this.isActive = active;
    }
}