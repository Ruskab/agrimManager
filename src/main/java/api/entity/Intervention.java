package api.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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

    private InterventionType interventionType;

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

    public Intervention(String title, InterventionType interventionType, LocalDateTime startTIme, LocalDateTime endTime) {
        this.title = title;
        this.interventionType = interventionType;
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

    public InterventionType getInterventionType() {
        return interventionType;
    }

    public void setInterventionType(InterventionType interventionType) {
        this.interventionType = interventionType;
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

    public boolean isActive() {
        return endTime == null;
    }
}
