package lk.school.elite_driving.dao.custom.impl;

import lk.school.elite_driving.dao.custom.CourseDAO;
import lk.school.elite_driving.enitity.Course;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class CourseDAOImpl implements CourseDAO {
    @Override
    public void save(Course entity, Session session) {
        session.persist(entity);
    }

    @Override
    public void update(Course entity, Session session) {
        session.merge(entity);
    }

    @Override
    public void delete(String pk, Session session) {
        Course course = session.find(Course.class, pk);
        if (course != null) {
            session.remove(course);
        }
    }

    @Override
    public List<Course> getAll(Session session) {
        return session.createQuery("FROM Course", Course.class).list();
    }

    @Override
    public Optional<Course> getById(String pk, Session session) {
        return Optional.ofNullable(session.find(Course.class, pk));
    }

    @Override
    public Optional<String> getLastPk(Session session) {
        List<String> list = session.createQuery("select courseId from Course ORDER BY courseId desc ").list();
        return list.size() > 0 ? Optional.of(list.get(0)) : Optional.empty();
    }
}
