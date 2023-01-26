package il.cshaifasweng.MoneyRelatedServices;

import il.cshaifasweng.LogInEntities.Customers.RegisteredCustomer;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "Refund")
public class Refund extends Transactions {
    public Refund(String refundType, double value, RegisteredCustomer registeredCustomer) {
        this.refundType = refundType;
        this.value = value;
        this.registeredCustomer = registeredCustomer;
    }
    public static enum refundChart{
        LESS_THAN_ONE_HOUR,
        ONE_TO_THREE_HOURS,
        MORE_THAN_THREE_HOURS,
    }
    private String refundType;
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL,targetEntity = RegisteredCustomer.class)
    private RegisteredCustomer registeredCustomer;

    public Refund(){}


}
