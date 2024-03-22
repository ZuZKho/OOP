package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * BakerDTO class.
 */
@Getter
@SuppressWarnings("AbbreviationAsWordInNameCheck")
public class BakerDTO {
    String name;
    @JsonProperty("baking-time")
    int bakingTime;
}
