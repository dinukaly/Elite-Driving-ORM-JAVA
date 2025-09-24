package lk.school.elite_driving.tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class InstructorTM {
    private String instructorId;
    private String instructorName;
    private String instructorAddress;
    private String instructorPhone;
    private String instructorEmail;
    private String instructorSpecialization;
    private boolean isActive;
    private Button btnEdit;
    private Button btnDelete;
}