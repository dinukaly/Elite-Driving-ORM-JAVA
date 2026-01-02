package lk.school.elite_driving.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LessonDTO {
    private String lessonId;
    private String lessonName;
    private String lessonTime;
    private String status;
    private String studentId;
    private String studentName;
    private String courseId;
    private String courseName;
    private String instructorId;
    private String instructorName;
}