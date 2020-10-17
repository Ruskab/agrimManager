package front.dtos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Intervention implements Serializable {
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

    public static boolean isActiveIntervention(Intervention intervention) {
        return intervention.getEndTime() == null;
    }

    public long timeSpentMinutes() {
        return timeSpent().toMinutes();
    }

    public long timeSpentHours() {
        return timeSpent().toHours();
    }

    private Duration timeSpent() {
        if (isActive()) {
            return Duration.between(getStartTime(), LocalDateTime.now());
        }
        return Duration.between(getStartTime(), getEndTime());
    }

    public boolean isActive() {
        return endTime == null;
    }
}
