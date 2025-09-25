package lk.school.elite_driving.dao.custom.impl;

import lk.school.elite_driving.dao.custom.PaymentDAO;
import lk.school.elite_driving.enitity.Payment;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class PaymentDAOImpl implements PaymentDAO {
    @Override
    public void save(Payment entity, Session session) {
        session.persist(entity);
    }

    @Override
    public void update(Payment entity, Session session) {
        session.merge(entity);
    }

    @Override
    public void delete(String pk, Session session) {
        Payment payment = session.get(Payment.class, pk);
        if (payment != null) {
            session.remove(payment);
        }
    }

    @Override
    public List<Payment> getAll(Session session) {
        return session.createQuery("FROM Payment", Payment.class).list();
    }

    @Override
    public Optional<Payment> getById(String pk, Session session) {
        Payment payment = session.get(Payment.class, pk);
        return Optional.ofNullable(payment);
    }

    @Override
    public Optional<String> getLastPk(Session session) {
        String sql = "SELECT p.paymentId FROM Payment p ORDER BY p.paymentId DESC";
        return session.createQuery(sql, String.class)
                .setMaxResults(1)
                .uniqueResultOptional();
    }
}