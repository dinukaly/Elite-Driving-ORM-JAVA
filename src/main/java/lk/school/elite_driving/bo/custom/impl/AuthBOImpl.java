package lk.school.elite_driving.bo.custom.impl;

import lk.school.elite_driving.bo.custom.AuthBO;
import lk.school.elite_driving.dao.DAOFactory;
import lk.school.elite_driving.dao.custom.UserDAO;
import lk.school.elite_driving.dto.UserDTO;
import lk.school.elite_driving.enitity.User;
import lk.school.elite_driving.exception.InvalidCredentialsException;
import lk.school.elite_driving.exception.LoginException;
import lk.school.elite_driving.exception.RegistrationException;
import lk.school.elite_driving.util.PasswordUtil;

import java.util.function.Function;

import lk.school.elite_driving.bo.util.DTOMapper;
import lk.school.elite_driving.bo.util.TransactionalUtil;
import org.hibernate.Session;

public class AuthBOImpl implements AuthBO {
    private final UserDAO userDAO = (UserDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.USER);

    @Override
    public UserDTO login(String username, String password) {
        try {
            User user = TransactionalUtil.doInTransaction((Function<Session, User>) session -> userDAO.findByUsername(username, session));
            if (user == null) {
                throw new InvalidCredentialsException("Invalid username or password");
            }
            if (!PasswordUtil.checkPassword(password, user.getPassword())) {
                throw new InvalidCredentialsException("Invalid username or password");
            }
            return DTOMapper.toDTO(user);
        } catch (RuntimeException e) {
            if (e instanceof LoginException) {
                throw e;
            }
            throw new LoginException("Error during login", e);
        }
    }

    @Override
    public boolean register(UserDTO userDTO) {
        if (userDTO.getUserName() == null || userDTO.getUserName().trim().isEmpty() ||
                userDTO.getPassword() == null || userDTO.getPassword().trim().isEmpty()) {
            throw new RegistrationException("Username and password cannot be empty");
        }
        try {
            TransactionalUtil.doInTransaction(session -> {
                if (userDAO.findByUsername(userDTO.getUserName(), session) != null) {
                    throw new RegistrationException("Username already exists");
                }
                userDTO.setPassword(PasswordUtil.hashPassword(userDTO.getPassword()));
                userDAO.save(DTOMapper.toEntity(userDTO), session);
                return null;
            });
            return true;
        } catch (RuntimeException e) {
            if (e instanceof RegistrationException) {
                throw e;
            }
            throw new RegistrationException("Error during registration", e);
        }
    }

    @Override
    public String getNewUserId() {
        try {
            String lastPk = TransactionalUtil.doInTransaction((Function<Session, String>) session -> userDAO.getLastPk(session).orElse("U000"));
            int nextId = Integer.parseInt(lastPk.substring(1)) + 1;
            return String.format("U%03d", nextId);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error while getting new user ID", e);
        }
    }
}