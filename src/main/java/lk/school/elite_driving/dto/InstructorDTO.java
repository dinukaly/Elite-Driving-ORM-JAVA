package lk.school.elite_driving.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InstructorDTO {
    private String instructorId;
    private String instructorName;
    private String instructorAddress;
    private String instructorPhone;
    private String instructorEmail;
    private String instructorSpecialization;

    private boolean isActive;
}
