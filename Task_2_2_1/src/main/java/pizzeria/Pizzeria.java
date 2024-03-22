package pizzeria;

import dto.OrderDTO;
import dto.PizzeriaDTO;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import utils.ExtendedBlockingQueue;

/**
 * Main pizzeria class.
 */
@Slf4j
@SuppressWarnings("AbbreviationAsWordInNameCheck")
public class Pizzeria {

    /**
     * Pizzeria constructor from pizzeriaDTO.
     *
     * @param pizzeriaDTO constructor data.
     */
    public Pizzeria(PizzeriaDTO pizzeriaDTO) {
        storage = new ExtendedBlockingQueue<>(pizzeriaDTO.getStorageSize());
        for (var baker : pizzeriaDTO.getBakers()) {
            bakersThreads.add(new Thread(new BakerThread(this, baker)));
        }
        for (var courier : pizzeriaDTO.getCouriers()) {
            couriersThreads.add(new Thread(new CourierThread(this, courier)));
        }
        this.workingTime = pizzeriaDTO.getWorkingTime();
    }

    /**
     * Start emulating working day: start bakers and couriers threads.
     */
    private void startWorkingDay() {
        isWorking.set(true);
        for (var thread : bakersThreads) {
            thread.start();
        }
        for (var thread : couriersThreads) {
            thread.start();
        }
    }

    /**
     * Finish all already accepted orders. Finish all bakers and couriers threads.
     *
     * @throws InterruptedException unpredictable situation.
     */
    private void finishWorkingDay() throws InterruptedException {
        isWorking.set(false);

        // Нужно закончить вначале потоки пекарей и только затем потоки курьеров.
        // Для того чтобы гарантированно не осталось пицц в листе заказов.
        // Затем гарантированно на складе не осталось пицц.

        ordersQueue.waitUntilEmpty();
        // После того как заказов в листе ожидания не осталось, можно заканчивать потоки пекарей.
        // Те котоые спят (делают заказы) должны закочить выпечку, остальных нужно закончить сразу.
        for (var thread : bakersThreads) {
            if (thread.getState() == Thread.State.WAITING) {
                thread.interrupt();
            } else {
                thread.join();
            }
        }

        storage.waitUntilEmpty();
        // Потоки которые спят должны завершиться нормально, т.к. пицца должна доехать.
        // Потоки которые находятся в poll'е могут быть прерваны, т.к. склад уже пуст.
        for (var thread : couriersThreads) {
            if (thread.getState() == Thread.State.WAITING) {
                thread.interrupt();
            } else {
                thread.join();
            }
        }
    }

    /**
     * Emulate pizzeria working day.
     */
    public void workDay() {
        try {
            startWorkingDay();
            log.info("Pizzeria is opened.");

            Thread.sleep(workingTime);
            log.info("Pizzeria stopped to accept new orders.");

            finishWorkingDay();
            log.info("Pizzeria is closed.");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Send order to pizzeria.
     *
     * @param orderDTO order parameters.
     * @return true if order added, false otherwise.
     */
    public boolean addOrder(OrderDTO orderDTO) {
        try {
            if (isWorking.get() == false) {
                return false;
            }

            Order order = new Order(orderDTO, idCounter.getAndAdd(1));
            ordersQueue.add(order);
            log.info("Order #{} was successfully added to queue.", order.id);
            return true;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    protected final ExtendedBlockingQueue<Order> ordersQueue = new ExtendedBlockingQueue<>(0);
    protected final ExtendedBlockingQueue<Order> storage;
    protected final List<Thread> bakersThreads = new ArrayList<>();
    protected final List<Thread> couriersThreads = new ArrayList<>();
    protected final AtomicBoolean isWorking = new AtomicBoolean(true);
    protected final AtomicInteger idCounter = new AtomicInteger(1);
    private final int workingTime;
}
