package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * BakerDTO class.
 */
@Getter
public class BakerDTO {
    String name;
    @JsonProperty("baking-time")
    int bakingTime;
}
