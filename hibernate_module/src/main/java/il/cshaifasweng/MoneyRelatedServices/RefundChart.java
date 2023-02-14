package il.cshaifasweng.MoneyRelatedServices;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "Refund_Chart")
public class RefundChart implements Serializable {
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    private int id;
    @Column(name = "fromTime", nullable = false,unique = true)
    private int fromTime;
    @Column(name = "toTime", nullable = false,unique = true)
    private int toTime;
    @Column(name = "refund_value",nullable = false)
    private double value;

    public RefundChart(){}

    public RefundChart(int fromTime, int toTime, double value) {
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.value = value;
    }

}
