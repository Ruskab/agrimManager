package api.entity;

import api.dtos.InterventionDto;

import javax.persistence.*;
import java.time.Duration;
import java.util.Optional;

@Entity
@Table(name = "intervention")
public class Intervention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private Enum state;

    private Duration period;

    @JoinColumn
    @ManyToOne
    private Vehicle vehicle;

    @JoinColumn
    @ManyToOne
    private RepairingPack repairingPack;

    public Intervention() {
        //JPA
    }

    public Intervention(String title, Enum state, Duration period, Vehicle vehicle) {
        this.title = title;
        this.state = state;
        this.period = period;
        this.vehicle = vehicle;
    }

    public Intervention(String title, Enum state, Duration period) {
        this.title = title;
        this.state = state;
        this.period = period;
    }

    public Intervention(InterventionDto interventionDto){
        //todo plantearme si Intervention deberia de conocer a su BO
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Enum getState() {
        return state;
    }

    public void setState(Enum state) {
        this.state = state;
    }

    public Duration getPeriod() {
        return period;
    }

    public void setPeriod(Duration period) {
        this.period = period;
    }

    public Optional<Vehicle> getVehicle() {
        return Optional.ofNullable(vehicle);
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Optional<RepairingPack> getRepairingPack() {
        return Optional.ofNullable(repairingPack);
    }

    public void setRepairingPack(RepairingPack repairingPack) {
        this.repairingPack = repairingPack;
    }
}
