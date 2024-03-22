package server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Class for handling prime numbers.
 * Can be used only once after creation.
 * Use multiple send commands to send number.
 * After data sent you can once call get to compute result.
 */
public class Executor {

    private ExecutorService executorService;
    private List<Future<?>> futures = new ArrayList<>();

    /**
     * Create executor.
     *
     * @param nthreads amount of threads of fixedThreadPoolExecutorService.
     */
    public Executor(int nthreads) {
        executorService = Executors.newFixedThreadPool(nthreads);
    }

    /**
     * Send number which have to be processed.
     *
     * @param number long number.
     */
    public void send(long number) {
        Callable<Boolean> cur = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return PrimeNumberDetector.isPrime(number);
            }
        };
        futures.add(executorService.submit(cur));
    }

    /**
     * Is all already sent numbers is prime.
     *
     * @return Is all already sent numbers is prime.
     * @throws ExecutionException problem with executorService.
     * @throws InterruptedException can be thrown when server stops by interruption.
     */
    public int get() throws ExecutionException, InterruptedException {
        for (var future : futures) {
            if (((Boolean) future.get()).equals(false)) {
                executorService.shutdown();
                return 0;
            }
        }
        executorService.shutdown();
        return 1;
    }
}