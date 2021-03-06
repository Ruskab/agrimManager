package api.entity;

import api.entity.converters.MechanicRoleConverter;
import api.dtos.MechanicDto;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "mechanic")
@NamedQuery(name = "MechanicNames", query = "select m from Mechanic m where m.name=:name")
@NamedQuery(name = "MechanicByCredentials", query = "select m from Mechanic m where m.name=:name and m.password=:password")
public class Mechanic implements Serializable {

    private static final long serialVersionUID = 1905122041950251207L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String name;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "mechanic_roles", joinColumns = @JoinColumn(name = "ID"))
    @Enumerated(EnumType.STRING)
    @Convert(converter = MechanicRoleConverter.class)
    private Set<Role> roles = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Intervention> interventionList;

    public Mechanic() {
        //JPA
    }

    public Mechanic(String name, String password) {
        this.name = name;
        this.password = password;
        this.interventionList = new ArrayList<>();
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

}
