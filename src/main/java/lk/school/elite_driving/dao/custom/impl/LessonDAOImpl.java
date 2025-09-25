package lk.school.elite_driving.dao.custom.impl;

import lk.school.elite_driving.dao.custom.LessonDAO;
import lk.school.elite_driving.enitity.Lesson;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class LessonDAOImpl implements LessonDAO {
    
    @Override
    public void save(Lesson entity, Session session) {
        session.save(entity);
    }

    @Override
    public void update(Lesson entity, Session session) {
        session.update(entity);
    }

    @Override
    public void delete(String pk, Session session) {
        Lesson lesson = session.get(Lesson.class, pk);
        if (lesson != null) {
            session.delete(lesson);
        }
    }

    @Override
    public List<Lesson> getAll(Session session) {
        String hql = "FROM Lesson l LEFT JOIN FETCH l.student LEFT JOIN FETCH l.course LEFT JOIN FETCH l.instructor";
        Query<Lesson> query = session.createQuery(hql, Lesson.class);
        return query.list();
    }

    @Override
    public Optional<Lesson> getById(String pk, Session session) {
        String hql = "FROM Lesson l LEFT JOIN FETCH l.student LEFT JOIN FETCH l.course LEFT JOIN FETCH l.instructor WHERE l.lessonId = :lessonId";
        Query<Lesson> query = session.createQuery(hql, Lesson.class);
        query.setParameter("lessonId", pk);
        return query.uniqueResultOptional();
    }

    @Override
    public Optional<String> getLastPk(Session session) {
        String hql = "SELECT l.lessonId FROM Lesson l ORDER BY l.lessonId DESC";
        Query<String> query = session.createQuery(hql, String.class);
        query.setMaxResults(1);
        return query.uniqueResultOptional();
    }
}