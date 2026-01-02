package lk.school.elite_driving.tm;

import com.jfoenix.controls.JFXButton;
import lk.school.elite_driving.enitity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserTM {
    private String userId;
    private String userName;
    private UserRole userRole;
    private JFXButton btnEdit;
    private JFXButton btnDelete;
}