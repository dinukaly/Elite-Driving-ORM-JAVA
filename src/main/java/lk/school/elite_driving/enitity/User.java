package lk.school.elite_driving.enitity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class User {
    @Id
    private int userId;
    private String userName;
    private String password;
    private enum UserRole{
        RECEPTIONIST,
        ADMIN
    }
}
