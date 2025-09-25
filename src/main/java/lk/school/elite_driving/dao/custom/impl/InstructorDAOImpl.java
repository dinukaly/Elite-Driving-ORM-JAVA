package lk.school.elite_driving.dao.custom.impl;

import lk.school.elite_driving.dao.custom.InstructorDAO;
import lk.school.elite_driving.enitity.Instructor;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class InstructorDAOImpl implements InstructorDAO {
    @Override
    public void save(Instructor entity, Session session) {
        session.persist(entity);
    }

    @Override
    public void update(Instructor entity, Session session) {
        session.merge(entity);
    }

    @Override
    public void delete(String id, Session session) {
        session.remove(session.get(Instructor.class, id));
    }

    @Override
    public List<Instructor> getAll(Session session) {
        return session.createQuery("FROM Instructor", Instructor.class).list();
    }

    @Override
    public Optional<Instructor> getById(String id, Session session) {
        return Optional.ofNullable(session.get(Instructor.class, id));
    }

    @Override
    public Optional<String> getLastPk(Session session) {
        List<String> list = session.createQuery("select instructorId from Instructor ORDER BY instructorId desc ").list();
        return list.size() > 0 ? Optional.of(list.get(0)) : Optional.empty();
    }
}
