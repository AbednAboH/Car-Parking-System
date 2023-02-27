package il.cshaifasweng.LogInEntities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
//@Entity
@Getter
@Setter
@MappedSuperclass
public abstract class User implements Serializable {

    @Column(name="email")
    protected String email;
    @Column(name="firstName")
    protected String firstName;
    @Column(name="lastName")
    protected String lastName;
    @Column(name="password")
    protected String  password;

    // TODO: 15/02/2023 gui names can be changed in the future using an enum class !
    public  String getGUI() {
        return "ExecutiveManager";
    }

}
