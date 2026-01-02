package lk.school.elite_driving.bo.custom.impl;

import lk.school.elite_driving.bo.custom.PaymentBO;
import lk.school.elite_driving.bo.util.DTOMapper;
import lk.school.elite_driving.bo.util.TransactionalUtil;
import lk.school.elite_driving.dao.DAOFactory;
import lk.school.elite_driving.dao.custom.PaymentDAO;
import lk.school.elite_driving.dto.PaymentDTO;
import lk.school.elite_driving.enitity.Payment;
import lk.school.elite_driving.enitity.Student;
import lk.school.elite_driving.exception.PaymentException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PaymentBOImpl implements PaymentBO {
    private final PaymentDAO paymentDAO = (PaymentDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.PAYMENT);

    @Override
    public void processPayment(PaymentDTO paymentDTO) throws PaymentException {
        if (paymentDTO.getStudent() == null || paymentDTO.getStudent().getStudentId() == null) {
            throw new PaymentException("Student information is missing.");
        }

        try {
            TransactionalUtil.doInTransaction(session -> {
                try {
                    Student student = session.get(Student.class, paymentDTO.getStudent().getStudentId());
                    if (student == null) {
                        throw new RuntimeException("Student not found");
                    }

                    // Validate payment amount to prevent overpayment
                    if (paymentDTO.getAmount() <= 0) {
                        throw new RuntimeException("Payment amount must be greater than 0");
                    }

                    // Check if payment amount exceeds remaining fee
                    if (paymentDTO.getAmount() > student.getRemainingFee()) {
                        throw new RuntimeException("Payment amount LKR" + paymentDTO.getAmount() + 
                            " exceeds remaining fee LKR" + student.getRemainingFee() + 
                            ". Please enter a valid amount.");
                    }

                    double remainingFee = student.getRemainingFee() - paymentDTO.getAmount();
                    student.setRemainingFee(remainingFee);

                    String status = (remainingFee <= 0) ? "Completed" : "Pending";
                    paymentDTO.setStatus(status);

                    Payment payment = DTOMapper.toEntity(paymentDTO, session);
                    session.save(payment);
                    session.update(student);
                } catch (Exception e) {
                    throw new RuntimeException("Error processing payment: " + e.getMessage(), e);
                }
            });
        } catch (Exception e) {
            throw new PaymentException("Failed to process payment: " + e.getMessage(), e);
        }
    }

    @Override
    public void updatePaymentStatus(String paymentId, String status) throws PaymentException {
        // Input validation
        if (paymentId == null || paymentId.trim().isEmpty()) {
            throw new PaymentException("Payment ID cannot be null or empty");
        }
        if (status == null || status.trim().isEmpty()) {
            throw new PaymentException("Payment status cannot be null or empty");
        }
        
        try {
            TransactionalUtil.doInTransaction(session -> {
                Optional<Payment> paymentOpt = paymentDAO.getById(paymentId, session);
                if (paymentOpt.isPresent()) {
                    Payment payment = paymentOpt.get();
                    payment.setStatus(status);
                    paymentDAO.update(payment, session);
                } else {
                    throw new RuntimeException("Payment not found with ID: " + paymentId);
                }
            });
        } catch (RuntimeException e) {
            if (e.getMessage() != null && e.getMessage().startsWith("Payment not found")) {
                throw new PaymentException(e.getMessage());
            }
            throw new PaymentException("Failed to update payment status: " + e.getMessage(), e);
        } catch (Exception e) {
            // Include the cause for better debugging
            throw new PaymentException("Failed to update payment status: " + e.getMessage(), e);
        }
    }

    @Override
    public ArrayList<PaymentDTO> getAllPayment() throws PaymentException {
        try {
            return TransactionalUtil.doInTransaction(session -> {
                List<Payment> payments = paymentDAO.getAll(session);
                return payments.stream()
                        .map(DTOMapper::toDTO)
                        .collect(Collectors.toCollection(ArrayList::new));
            });
        } catch (Exception e) {
            // Include the cause for better debugging
            throw new PaymentException("Failed to retrieve all payments: " + e.getMessage(), e);
        }
    }
}