package lk.school.elite_driving.dao;

import org.hibernate.Session;

import java.util.List;

public interface QueryDAO extends SuperDAO {
    List<Object[]> getAllRegistrationDetails(Session session);
}
