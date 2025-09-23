package lk.school.elite_driving.dto;

import lk.school.elite_driving.enitity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private String userId;
    private String userName;
    private String password;
    private UserRole userRole;
}
