package lk.school.elite_driving.bo.custom.impl;

import lk.school.elite_driving.bo.custom.AuthBO;
import lk.school.elite_driving.bo.util.DTOMapper;
import lk.school.elite_driving.bo.util.TransactionalUtil;
import lk.school.elite_driving.dao.DAOFactory;
import lk.school.elite_driving.dao.custom.UserDAO;
import lk.school.elite_driving.dto.UserDTO;
import lk.school.elite_driving.enitity.User;
import lk.school.elite_driving.exception.InvalidCredentialsException;
import lk.school.elite_driving.exception.LoginException;
import lk.school.elite_driving.exception.RegistrationException;
import lk.school.elite_driving.util.PasswordUtil;
import org.hibernate.Session;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AuthBOImpl implements AuthBO {
    private final UserDAO userDAO = (UserDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.USER);

    @Override
    public UserDTO login(String username, String password) {
        try {
            System.out.println("Attempting to find user: " + username);
            User user = TransactionalUtil.doInTransaction((Function<Session, User>) session -> {
                try {
                    return userDAO.findByUsername(username, session);
                } catch (Exception e) {
                    System.err.println("Error in findByUsername: " + e.getMessage());
                    e.printStackTrace();
                    throw e;
                }
            });
            
            if (user == null) {
                System.out.println("User not found: " + username);
                throw new InvalidCredentialsException("Invalid username or password");
            }
            
            System.out.println("User found, checking password...");
            if (!PasswordUtil.checkPassword(password, user.getPassword())) {
                System.out.println("Invalid password for user: " + username);
                throw new InvalidCredentialsException("Invalid username or password");
            }
            
            System.out.println("Login successful for user: " + username);
            return DTOMapper.toDTO(user);
            
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
            if (e instanceof LoginException) {
                throw e;
            }
            if (e.getCause() != null) {
                throw new LoginException("Error during login: " + e.getCause().getMessage(), e);
            }
            throw new LoginException("Error during login: " + e.getMessage(), e);
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

    @Override
    public List<UserDTO> getAllUsers() {
        try {
            List<User> users = TransactionalUtil.doInTransaction((Function<Session, List<User>>) session -> userDAO.getAll(session));
            return users.stream()
                    .map(DTOMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw new RuntimeException("Error while getting all users", e);
        }
    }

    @Override
    public boolean updateUser(UserDTO userDTO) {
        if (userDTO.getUserId() == null || userDTO.getUserId().trim().isEmpty()) {
            throw new RegistrationException("User ID cannot be empty");
        }
        if (userDTO.getUserName() == null || userDTO.getUserName().trim().isEmpty()) {
            throw new RegistrationException("Username cannot be empty");
        }
        try {
            TransactionalUtil.doInTransaction(session -> {
                User existingUser = userDAO.findByUsername(userDTO.getUserName(), session);
                if (existingUser != null && !existingUser.getUserId().equals(userDTO.getUserId())) {
                    throw new RegistrationException("Username already exists");
                }

                if (userDTO.getPassword() != null && !userDTO.getPassword().trim().isEmpty()) {
                    userDTO.setPassword(PasswordUtil.hashPassword(userDTO.getPassword()));
                } else {
                    // Keep existing password if not provided
                    User currentUser = userDAO.getById(userDTO.getUserId(), session)
                            .orElseThrow(() -> new RegistrationException("User not found"));
                    userDTO.setPassword(currentUser.getPassword());
                }
                
                userDAO.update(DTOMapper.toEntity(userDTO), session);
                return null;
            });
            return true;
        } catch (RuntimeException e) {
            if (e instanceof RegistrationException) {
                throw e;
            }
            throw new RegistrationException("Error during user update", e);
        }
    }

    @Override
    public boolean deleteUser(String userId) {
        try {
            TransactionalUtil.doInTransaction(session -> {
                userDAO.delete(userId, session);
                return null;
            });
            return true;
        } catch (RuntimeException e) {
            throw new RuntimeException("Error while deleting user", e);
        }
    }
}