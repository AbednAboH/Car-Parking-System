package il.cshaifasweng.customerCatalogEntities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="registeredCustomer_id")
////    private RegisteredCustomer registeredCustomer;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="reports_id")
//    private Reports reports;
    public Order() {

    }
    // TODO: 1/10/2023 toString Function 
}
