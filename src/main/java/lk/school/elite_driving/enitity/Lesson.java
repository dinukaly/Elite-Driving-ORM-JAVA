package lk.school.elite_driving.enitity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Lesson implements SuperEntity {
    @Id
    private String lessonId;
    private String lessonName;
    private String lessonTime;
    private String status;
    @ManyToOne
    private Student student;
    @ManyToOne
    private Course course;
    @ManyToOne
    private Instructor instructor;

    public Lesson(String lessonId, String lessonName, String lessonTime, String status) {
        this.lessonId = lessonId;
        this.lessonName = lessonName;
        this.lessonTime = lessonTime;
        this.status = status;
    }
}
