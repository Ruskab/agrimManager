package api.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name = "intervention")
public class Intervention implements Serializable {

    private static final long serialVersionUID = 1905122041950251207L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private State state;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @JoinColumn
    @ManyToOne
    private Vehicle vehicle;

    @JoinColumn
    @ManyToOne
    private RepairingPack repairingPack;

    public Intervention() {
        //JPA
    }

    public Intervention(String title, State state, LocalDateTime startTIme, LocalDateTime endTime) {
        this.title = title;
        this.state = state;
        this.startTime = startTIme;
        this.endTime = endTime;
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
