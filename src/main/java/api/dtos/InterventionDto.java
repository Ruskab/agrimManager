package api.dtos;

import api.entity.Intervention;
import api.entity.State;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.io.Serializable;
import java.time.LocalDate;

public class InterventionDto implements Serializable {
    private int id;

    private String title;

    private State state;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate startTime;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate endTime;

    private String vehicleId;

    private String repairingPackId;

    public InterventionDto(String title, State state, String vehicleId, String repairingPackId, LocalDate startTime, LocalDate endTime) {
        this.title = title;
        this.state = state;
        //todo si es de caffe no deberia de tener vehicle
        this.vehicleId = vehicleId;
        this.repairingPackId = repairingPackId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public InterventionDto(Intervention intervention) {
        this.id = intervention.getId();
        this.title = intervention.getTitle();
        this.state = intervention.getState();
        intervention.getVehicle().ifPresent(vehicle -> this.vehicleId = Integer.toString(vehicle.getId()));
        intervention.getRepairingPack().ifPresent(repairingPack -> this.repairingPackId = Integer.toString(repairingPack.getId()));
        this.startTime = intervention.getStartTime();
        this.endTime = intervention.getEndTime();
    }

    public InterventionDto(){

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

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getRepairingPackId() {
        return repairingPackId;
    }

    public void setRepairingPackId(String repairingPackId) {
        this.repairingPackId = repairingPackId;
    }


    @Override
    public String toString() {
        return "InterventionDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", state=" + state +
                ", vehicleId=" + vehicleId +
                ", repairingPackId=" + repairingPackId +
                '}';
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
