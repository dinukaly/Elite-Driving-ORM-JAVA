package lk.school.elite_driving.bo.custom;

import lk.school.elite_driving.bo.SuperBO;
import lk.school.elite_driving.dto.PaymentDTO;
import lk.school.elite_driving.exception.PaymentException;

import java.util.ArrayList;

public interface PaymentBO extends SuperBO {
    void processPayment(PaymentDTO paymentDTO) throws PaymentException;
    void updatePaymentStatus(String paymentId, String status) throws PaymentException;
    ArrayList<PaymentDTO> getAllPayment() throws PaymentException;
}