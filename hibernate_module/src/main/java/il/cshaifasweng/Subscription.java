package il.cshaifasweng;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public abstract class Subscription implements Serializable {
    public final int NUMBER_OF_DAYS = 7;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "customer_id",nullable = false)
    private Customer customer;

    @Column(name = "hoursPerMonth")
    private int hoursPerMonth;

    @Convert(converter = LocalDateAttributeConverter.class)
    @Column(name = "start_date")
    private LocalDate startDate;

    @Convert(converter = LocalDateAttributeConverter.class)
    @Column(name = "experation_date")
    private LocalDate expirationDate;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "allowed_days")
    private String allowedDays;

    @OneToMany(fetch=FetchType.LAZY,mappedBy = "Car" ,cascade =CascadeType.ALL,orphanRemoval = true)
    private List<Car> carsList;
//    Should we get the cars by the customer? instead of redundantly retrieve the cars twice.

    public Subscription(Customer customer, int hoursPerMonth, LocalDate startDate, LocalDate expirationDate, boolean isActive, String allowedDays) {
        this.customer = customer;
        this.hoursPerMonth = hoursPerMonth;
        this.startDate = startDate;
        this.expirationDate = expirationDate;
        this.isActive = isActive;
        this.allowedDays = allowedDays;
    }

    public Subscription() {

    }


    public boolean[] getAllowedDays(){
        if(allowedDays.isBlank() || allowedDays.length() < 7 || allowedDays.length() > 7) {
            throw new IllegalArgumentException("Allowed days string is not valid!");
        }
        return toBooleanArray(allowedDays);
    }

    protected boolean[] toBooleanArray(String s){
        boolean[] array = new boolean[s.length()];
        for (int i = 0; i < s.length(); i++) {
            array[i] = s.charAt(i) == '1';
        }
        return array;
    }


}
