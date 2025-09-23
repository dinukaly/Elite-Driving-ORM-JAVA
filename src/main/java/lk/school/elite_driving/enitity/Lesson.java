package lk.school.elite_driving.enitity;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.sql.Time;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
public class Lesson implements SuperEntity {
    @Id
    private String lessonId;
    private String lessonName;
    private String lessonDescription;
    private String lessonTime;
    private String status;
    @ManyToOne
    private Student student;
    @ManyToOne
    private Course course;
    @ManyToOne
    private Instructor instructor;
}
