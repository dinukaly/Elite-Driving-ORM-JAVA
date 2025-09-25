package lk.school.elite_driving.dao.custom.impl;

import lk.school.elite_driving.dao.QueryDAO;
import org.hibernate.Session;

import java.util.List;

public class QueryDAOImpl implements QueryDAO {
    @Override
    public List<Object[]> getAllRegistrationDetails(Session session) {
        return List.of();
    }
}
