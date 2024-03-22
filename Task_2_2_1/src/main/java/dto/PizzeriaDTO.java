package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

/**
 * PizzeriaDTO class.
 */
@Getter
@SuppressWarnings("AbbreviationAsWordInNameCheck")
public class PizzeriaDTO {
    List<BakerDTO> bakers;
    List<CourierDTO> couriers;
    @JsonProperty("storage-size")
    int storageSize;
    @JsonProperty("working-time")
    int workingTime;
}
