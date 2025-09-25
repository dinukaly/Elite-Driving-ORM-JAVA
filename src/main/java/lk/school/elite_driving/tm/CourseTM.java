package lk.school.elite_driving.tm;

import com.jfoenix.controls.JFXButton;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourseTM {
    private String courseId;
    private String courseName;
    private String courseDuration;
    private double courseFee;
    private JFXButton btnEdit;
    private JFXButton btnDelete;
}