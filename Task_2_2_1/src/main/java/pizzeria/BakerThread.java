package pizzeria;

import dto.BakerDTO;
import lombok.extern.slf4j.Slf4j;

/**
 * Runnable class emulating Baker work.
 */
@Slf4j
@SuppressWarnings("AbbreviationAsWordInName")
public class BakerThread implements Runnable {

    /**
     * Initialize baker.
     *
     * @param pizzeria baker's pizzeria.
     * @param bakerDTO baker's dto.
     */
    BakerThread(Pizzeria pizzeria, BakerDTO bakerDTO) {
        this.name = bakerDTO.getName();
        this.bakingTime = bakerDTO.getBakingTime();
        this.pizzeria = pizzeria;
    }

    /**
     * Run baker.
     */
    @Override
    public void run() {
        while ((pizzeria.isWorking.get() || !pizzeria.ordersQueue.isEmpty())
                && !Thread.interrupted()) {
            try {
                Order order = pizzeria.ordersQueue.poll();

                log.info("Order #{} was taken by baker {}.", order.id, name);
                Thread.sleep(bakingTime);

                log.info("Order #{} was moved to storage.", order.id);
                pizzeria.storage.add(order);
            } catch (InterruptedException e) {
                return;
            }
        }
    }


    private final String name;
    private final int bakingTime;
    private final Pizzeria pizzeria;

}
