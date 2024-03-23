package pizzeria;

import dto.CourierDTO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * Runnable class emulating courier work.
 */
@Slf4j
@SuppressWarnings("AbbreviationAsWordInName")
public class CourierThread implements Runnable {

    /**
     * Constructor.
     *
     * @param pizzeria   courier's pizzeria.
     * @param courierDTO courier's dto.
     */
    CourierThread(Pizzeria pizzeria, CourierDTO courierDTO) {
        this.name = courierDTO.getName();
        this.volume = courierDTO.getVolume();
        this.pizzeria = pizzeria;
    }

    /**
     * run method for Runnable.
     */
    @Override
    public void run() {
        while ((pizzeria.isWorking.get() || !pizzeria.storage.isEmpty())
                && !Thread.interrupted()) {
            try {
                List<Order> orders = pizzeria.storage.multipoll(volume);

                StringBuilder stringBuilder = new StringBuilder();
                int sleepingTime = 0;
                for (int i = 0; i < orders.size(); i++) {
                    sleepingTime += orders.get(i).getDeliveryTime();
                    stringBuilder.append("#");
                    stringBuilder.append(orders.get(i).id);
                    if (i != orders.size() - 1) {
                        stringBuilder.append(", ");
                    }
                }

                log.info("Courier {} started delivery with orders: {}.", name, stringBuilder);

                Thread.sleep(sleepingTime);
                log.info("Courier {} finished delivery of orders: {}.", name, stringBuilder);
            } catch (InterruptedException e) {
                return;
            }
        }
    }


    private final String name;
    private final int volume;
    private final Pizzeria pizzeria;
}

