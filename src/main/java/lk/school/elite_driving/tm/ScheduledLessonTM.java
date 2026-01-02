package lk.school.elite_driving.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ScheduledLessonTM {
    private String lessonId;
    private String courseName;
    private String lessonName;
    private String lessonTime;
    private String instructorName;
    private String studentName;
    private String status;
}