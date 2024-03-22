package pizzeria;

import dto.CourierDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class CourierThread implements Runnable {

    private final String name;
    private final int volume;
    private final Pizzeria pizzeria;

    CourierThread(Pizzeria pizzeria, CourierDTO courierDTO) {
        this.name = courierDTO.getName();
        this.volume = courierDTO.getVolume();
        this.pizzeria = pizzeria;
    }

    @Override
    public void run() {
        while((pizzeria.isWorking.get() || !pizzeria.storage.isEmpty()) && !Thread.interrupted()) {
            try {
                List<Order> orders = pizzeria.storage.multipoll(volume);

                StringBuilder stringBuilder = new StringBuilder();
                int sleepingTime = 0;
                for(int i = 0; i < orders.size(); i++) {
                    sleepingTime += orders.get(i).getDeliveryTime();
                    stringBuilder.append("#");
                    stringBuilder.append(orders.get(i).id);
                    if (i != orders.size() - 1) {
                        stringBuilder.append(", ");
                    }
                }

                log.info("Courier {} started delivery with orders: {}.", name, stringBuilder);

                Thread.sleep(sleepingTime);
                log.info("Courier {} finished delivery of orders: {}", name, stringBuilder);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
