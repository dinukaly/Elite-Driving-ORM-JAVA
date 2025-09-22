package lk.school.elite_driving.enitity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class Instructor {
    @Id
    private int instructorId;
    private String instructorName;
    private String instructorEmail;
    private String instructorPhone;
    private String instructorAddress;
    private boolean isActive;

    @ManyToOne
    private Course course;

    @OneToMany(mappedBy = "instructor")
    private List<Lesson> lessons;
}
