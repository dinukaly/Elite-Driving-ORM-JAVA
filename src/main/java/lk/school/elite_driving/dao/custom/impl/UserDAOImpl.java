package lk.school.elite_driving.dao.custom.impl;

import lk.school.elite_driving.dao.custom.UserDAO;
import lk.school.elite_driving.enitity.User;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {
    @Override
    public void save(User entity, Session session) {
        session.persist(entity);
    }

    @Override
    public void update(User entity, Session session) {

    }

    @Override
    public void delete(String pk, Session session) {

    }

    @Override
    public List<User> getAll(Session session) {
        return List.of();
    }

    @Override
    public Optional<User> getById(String pk, Session session) {
        return Optional.empty();
    }

    @Override
    public Optional<String> getLastPk(Session session) {
        List<String> list = session.createQuery("select userId from User ORDER BY userId desc ").list();
        return list.size() > 0 ? Optional.of(list.get(0)) : Optional.empty();
    }

    @Override
    public User findByUsername(String username, Session session) {
        return session.createQuery("FROM User WHERE userName = :username", User.class)
                .setParameter("username", username)
                .uniqueResult();
    }

}
