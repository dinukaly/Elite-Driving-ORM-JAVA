package lk.school.elite_driving.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourseDTO {
    private String courseId;
    private String courseName;
    private String courseDuration;
    private double courseFee;
}
