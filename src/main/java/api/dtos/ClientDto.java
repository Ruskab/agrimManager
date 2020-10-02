package api.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto implements Serializable {

    private static final long serialVersionUID = 6285845014865471015L;

    private int id;

    @NotBlank
    private String fullName;

    @Positive
    private int hours;

}
