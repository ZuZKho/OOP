package utils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Utility class based on BlockingQueue mechanic extended for simpler pizzeria work.
 *
 * @param <T> BlockingQueue element type.
 */
public class ExtendedBlockingQueue<T> {

    private final int queueSize;
    private final Queue<T> queue = new ArrayDeque<>();

    /**
     * Create blocking queue of some size.
     *
     * @param queueSize if 0 queue won't be restricted by size, otherwise it's maximal number of elements in queue.
     */
    public ExtendedBlockingQueue(int queueSize) {
        if (queueSize == 0) {
            this.queueSize = -1;
        } else {
            this.queueSize = queueSize;
        }
    }

    /**
     * Add element to queue. Will wait() if queue is full.
     *
     * @param item element.
     * @throws InterruptedException
     */
    public synchronized void add(T item) throws InterruptedException {
        while (queue.size() == queueSize) {
            wait();
        }
        queue.add(item);
        notifyAll();
    }

    /**
     * Remove element from queue. Will wait() if queue is empty.
     *
     * @return removed element.
     * @throws InterruptedException if was interrupted during wait();
     */
    public synchronized T poll() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        var res = queue.poll();
        notifyAll();
        return res;
    }

    /**
     * Remove multiple elements from queue which already in queue.
     * Will wait() only if queue is fully empty.
     *
     * @param limit maximum number of removed elements.
     * @return List of removed elements.
     * @throws InterruptedException if was interrupted during wait(0
     */
    public synchronized List<T> multipoll(int limit) throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }

        List<T> res = new ArrayList<>();
        do {
            res.add(queue.poll());
            limit--;
        } while (!queue.isEmpty() && limit > 0);

        notifyAll();
        return res;
    }

    /**
     * Check if queue is empty.
     *
     * @return empty or not.
     */
    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    /**
     * Returns only when queue becomes empty.
     *
     * @throws InterruptedException
     */
    public synchronized void waitUntilEmpty() throws InterruptedException {
        while (!queue.isEmpty()) {
            wait();
        }
    }

}
