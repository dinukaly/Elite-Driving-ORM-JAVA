package lk.school.elite_driving.dao.custom;

import lk.school.elite_driving.dao.CrudDAO;
import lk.school.elite_driving.enitity.User;
import org.hibernate.Session;

public interface UserDAO extends CrudDAO<User, String> {
    User findByUsername(String username, Session session);
}
