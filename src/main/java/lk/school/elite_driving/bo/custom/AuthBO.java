package lk.school.elite_driving.bo.custom;

import lk.school.elite_driving.bo.SuperBO;
import lk.school.elite_driving.dto.UserDTO;
import lk.school.elite_driving.exception.LoginException;
import lk.school.elite_driving.exception.RegistrationException;

public interface AuthBO extends SuperBO {
    UserDTO login(String username, String password) throws LoginException;
    boolean register(UserDTO userDTO) throws RegistrationException;
    String getNewUserId();
}