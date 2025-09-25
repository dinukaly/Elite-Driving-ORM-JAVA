package lk.school.elite_driving.dao.custom.impl;

import lk.school.elite_driving.dao.custom.StudentDAO;
import lk.school.elite_driving.enitity.Student;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class StudentDAOImpl implements StudentDAO {
    @Override
    public void save(Student entity, Session session) {
        session.persist(entity);
    }

    @Override
    public void update(Student entity, Session session) {
        session.merge(entity);
    }

    @Override
    public void delete(String pk, Session session) {
        Student student = session.get(Student.class, pk);
        if (student != null) {
            session.remove(student);
        }
    }

    @Override
    public List<Student> getAll(Session session) {
        return session.createQuery("FROM Student", Student.class).list();
    }

    @Override
    public Optional<Student> getById(String pk, Session session) {
        Student student = session.get(Student.class, pk);
        return Optional.ofNullable(student);
    }

    @Override
    public Optional<String> getLastPk(Session session) {
        List<String> list = session.createQuery("select studentId from Student ORDER BY studentId desc ").list();
        return list.size() > 0 ? Optional.of(list.get(0)) : Optional.empty();
    }
}