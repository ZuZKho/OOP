package pizzeria;

import dto.OrderDTO;

public class Order extends OrderDTO {
    int id;

    public Order(OrderDTO dto, int id) {
        super(dto);
        this.id = id;
    }
}
