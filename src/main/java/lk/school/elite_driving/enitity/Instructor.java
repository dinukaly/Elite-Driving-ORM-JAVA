package lk.school.elite_driving.enitity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
public class Instructor implements SuperEntity {
    @Id
    private String instructorId;
    private String instructorName;
    private String instructorEmail;
    private String instructorPhone;
    private String instructorAddress;
    private boolean isActive;

    @ManyToOne
    private Course course;

    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL)
    private List<Lesson> lessons;
}
