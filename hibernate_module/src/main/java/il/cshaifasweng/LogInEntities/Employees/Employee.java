package il.cshaifasweng.LogInEntities.Employees;

import il.cshaifasweng.LogInEntities.User;

import javax.persistence.*;
import java.io.Serializable;
import java.net.PasswordAuthentication;

@Entity
@Inheritance(strategy =InheritanceType.JOINED)
public abstract class Employee extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    @Column(name="Title")
    private String title;
    @Column(name="Salary")
    private double salary;


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



    public Employee(String firstName,String lastName, String title,String email, double salary) {
        this.firstName = firstName;
        this.title = title;
        this.salary = salary;
        this.lastName=lastName;
        this.email=email;
        this.password=firstName;
    }
    public Employee(String firstName, String title ,double salary) {
        this.firstName = firstName;
        this.title = title;
        this.salary = salary;
        this.lastName=lastName;
        this.email=email;
        this.password=firstName;
    }

    public Employee() {

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
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
        return "{" +", id=" + id +
                "name='" + firstName +" "+lastName+ '\'' +
                ", title=" + title +
                ", Email="+email+
                '}';
    }

}

