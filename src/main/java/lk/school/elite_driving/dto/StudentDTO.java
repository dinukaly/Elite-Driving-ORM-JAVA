package lk.school.elite_driving.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentDTO {
    private String studentId;
    private String studentName;
    private String studentEmail;
    private String studentNic;
    private String studentContact;
    private String studentAddress;
    private double totalFee; // sum of courses
    private double remainingFee;// auto updated
    private Date registrationDate;
}
