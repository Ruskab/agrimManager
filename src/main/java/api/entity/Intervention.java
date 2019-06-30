package api.entity;

import api.dtos.InterventionDto;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name = "intervention")
public class Intervention implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private State state;

    private LocalDate startTime;

    private LocalDate endTime;

    @JoinColumn
    @ManyToOne
    private Vehicle vehicle;

    @JoinColumn
    @ManyToOne
    private RepairingPack repairingPack;

    public Intervention() {
        //JPA
    }

    public Intervention(String title, State state) {
        this.title = title;
        this.state = state;
        this.vehicle = vehicle;
    }

    public Intervention(String title, State state, LocalDate startTIme, LocalDate endTime) {
        this.title = title;
        this.state = state;
        this.startTime = startTIme;
        this.endTime = endTime;
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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
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

    public LocalDate getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDate endTime) {
        this.endTime = endTime;
    }
}
