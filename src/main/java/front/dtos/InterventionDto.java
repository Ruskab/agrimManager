package front.dtos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.io.Serializable;
import java.time.LocalDateTime;

public class InterventionDto implements Serializable {
    private int id;

    private String title;

    private String interventionType;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime startTime;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime endTime;

    private String vehicleId;

    private String repairingPackId;

    public InterventionDto(String title, String interventionType, String vehicleId, String repairingPackId, LocalDateTime startTime, LocalDateTime endTime) {
        this.title = title;
        this.interventionType = interventionType;
        //todo si es de caffe no deberia de tener vehicle
        this.vehicleId = vehicleId;
        this.repairingPackId = repairingPackId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public InterventionDto() {

    }

    public static boolean isActiveIntervention(InterventionDto interventionDto) {
        return interventionDto.getEndTime() == null;
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

    public String getInterventionType() {
        return interventionType;
    }

    public void setInterventionType(String interventionType) {
        this.interventionType = interventionType;
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
                ", state=" + interventionType +
                ", vehicleId=" + vehicleId +
                ", repairingPackId=" + repairingPackId +
                '}';
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
