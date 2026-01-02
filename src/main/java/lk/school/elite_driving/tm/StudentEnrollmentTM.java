package lk.school.elite_driving.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentEnrollmentTM {
    private String studentId;
    private String studentName;
    private String studentEmail;
    private String courseName;
    private String enrollmentStatus;
    private double totalFee;
    private double remainingFee;
}