package lk.school.elite_driving.enitity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Payment implements SuperEntity {
    @Id
    private String paymentId;
    private double amount;
    private Date paymentDate;
    private String status;

    @ManyToOne
    private Student student;

    public Payment(String paymentId, double amount, Date paymentDate, String status) {
        this.paymentId = paymentId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.status = status;
    }
}