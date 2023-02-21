package il.cshaifasweng.MoneyRelatedServices;

import il.cshaifasweng.LocalDateAttributeConverter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
@Entity
@Getter
@Setter
@Inheritance(strategy =InheritanceType.JOINED)
public class Transactions implements Serializable {
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    protected int id;

    @Column(name = "value", nullable = false)
    protected double value;
    @Column(name = "date", nullable = false)
    protected LocalDate date;
    @Column(name = "payment_method", nullable = false)
    protected String transaction_method;
    @Column(name = "payment_status", nullable = false)
    protected boolean transactionStatus;
    // TODO: 11/02/2023 maybe add a field for the vihecl , not necessary as refund might be used here , so here the transaction is not related to a vehicle
    public String getGUI() {
        return "";
    }

}
