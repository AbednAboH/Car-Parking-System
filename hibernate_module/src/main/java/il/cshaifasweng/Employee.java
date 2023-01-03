package il.cshaifasweng;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public abstract class Employee implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="firstName")
    private String firstName;
    @Column(name="LastName")
    private String lastName;
    @Column(name="Title")
    private String title;
    @Column(name="Salary")
    private double salary;
    @Column(name="Email")
    private String email;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public Employee(String firstName, String title, double salary) {
        this.firstName = firstName;
        this.title = title;
        this.salary = salary;
    }

    public Employee() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }



    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public String getAge() {
        return title;
    }

    public void setAge(String title) {
        this.title = title;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }



    @Override
    public String toString() {
        return "ParkingLotEmployee{" +
                "name='" + firstName + '\'' +
                ", age=" + title +
                ", id=" + id +
                '}';
    }

}
