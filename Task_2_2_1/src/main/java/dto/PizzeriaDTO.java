package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * PizzeriaDTO class.
 */
@Getter
@ToString
public class PizzeriaDTO {
    List<BakerDTO> bakers;
    List<CourierDTO> couriers;
    @JsonProperty("storage-size")
    int storageSize;
    @JsonProperty("working-time")
    int workingTime;
}
