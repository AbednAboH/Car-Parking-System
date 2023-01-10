package il.cshaifasweng.MoneyRelatedServices;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "Refund")
public class Refund implements Serializable {

    public static enum refundChart{
        LESS_THAN_ONE_HOUR,
        ONE_TO_THREE_HOURS,
        MORE_THAN_THREE_HOURS,
    }


    @Id
    @Column(name = "id", nullable = false,unique = true)
    private String refundType;
    @Column(name = "refund_value",nullable = false)
    private double value;

}
