package api.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "mechanic")
public class Mechanic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String password;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Intervention> interventionList;


    public Mechanic() {
        //JPA
    }

    public Mechanic(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public Mechanic(String name, String password, List<Intervention> interventions) {
        this.name = name;
        this.password = password;
        this.interventionList = interventions;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Intervention> getInterventionList() {
        return interventionList;
    }

    public void setInterventionList(List<Intervention> interventionList) {
        this.interventionList = interventionList;
    }

}
