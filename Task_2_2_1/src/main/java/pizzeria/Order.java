package pizzeria;

import dto.OrderDTO;

/**
 * Extension of OrderDTO with id field.
 */
public class Order extends OrderDTO {
    int id;

    /**
     * Constructor from OrderDTO and id.
     *
     * @param dto OrderDTO.
     * @param id id of order.
     */
    public Order(OrderDTO dto, int id) {
        super(dto);
        this.id = id;
    }
}
