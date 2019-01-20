package api.entity;

import javax.persistence.*;

@Entity
@Table(name = "vehicle")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String registrationPlate;

    private  String brand;

    @JoinColumn
    @ManyToOne
    private Client client;
}
