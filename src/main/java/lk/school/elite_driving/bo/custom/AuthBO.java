package lk.school.elite_driving.bo.custom;

import lk.school.elite_driving.bo.SuperBO;
import lk.school.elite_driving.dto.UserDTO;
import lk.school.elite_driving.exception.LoginException;
import lk.school.elite_driving.exception.RegistrationException;

import java.util.List;

public interface AuthBO extends SuperBO {
    UserDTO login(String username, String password) throws LoginException;
    boolean register(UserDTO userDTO) throws RegistrationException;
    String getNewUserId();
    List<UserDTO> getAllUsers();
    boolean updateUser(UserDTO userDTO) throws RegistrationException;
    boolean deleteUser(String userId) throws RegistrationException;
}