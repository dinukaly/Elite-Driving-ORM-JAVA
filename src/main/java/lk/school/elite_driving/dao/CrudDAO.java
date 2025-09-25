package lk.school.elite_driving.dao;

import lk.school.elite_driving.enitity.SuperEntity;
import org.hibernate.Session;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface CrudDAO<T extends SuperEntity,ID extends Serializable> extends SuperDAO{
    void save(T entity, Session session);
    void update(T entity, Session session);
    void delete(ID pk, Session session);
    List<T> getAll(Session session);
    Optional<T> getById(ID pk, Session session);
    Optional<String> getLastPk(Session session);
}
