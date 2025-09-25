package lk.school.elite_driving.tm;

import com.jfoenix.controls.JFXButton;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentTM {
    private String studentId;
    private String studentName;
    private String studentAddress;
    private String studentContact;
    private String studentEmail;
    private String courseName;
    private Date registrationDate;
}