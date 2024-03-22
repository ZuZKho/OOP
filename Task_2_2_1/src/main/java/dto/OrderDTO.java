package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * OrderDTO class.
 */
@Getter
@NoArgsConstructor
@SuppressWarnings("AbbreviationAsWordInName")
public class OrderDTO {
    String address;
    @JsonProperty("delivery-time")
    int deliveryTime;

    public OrderDTO(OrderDTO dto) {
        this.address = dto.getAddress();
        this.deliveryTime = dto.getDeliveryTime();
    }
}
