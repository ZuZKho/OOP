import dto.OrderDTO;
import pizzeria.Pizzeria;

import java.util.List;
import java.util.Random;

/**
 * Class for sending orders to pizzeria.
 */
public class Client {

    private final Random random = new Random();

    /**
     * Start sending orders to pizzeria with some time interval.
     *
     * @param pizzeria        pizzeria to send orders.
     * @param orders          list of possible orderDTOs
     * @param averageInterval averageInterval between orders.
     */
    public Client(Pizzeria pizzeria, List<OrderDTO> orders, int averageInterval) {
        while (!Thread.interrupted()) {
            try {
                Thread.sleep(random.nextInt(averageInterval * 2));
                boolean res = pizzeria.addOrder(orders.get(random.nextInt(orders.size())));
                if (!res) {
                    return;
                }
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
