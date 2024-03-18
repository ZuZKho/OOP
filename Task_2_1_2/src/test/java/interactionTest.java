import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import server.Server;

import java.net.InetSocketAddress;
import java.util.ArrayList;

import static client.PrimeArrayDetector.isArrayPrime;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class interactionTest {

    private static String host = "0.0.0.0";

    private long[] primeArray = new long[]{
            20319251, 6997901, 6997927, 6997937, 17858849, 6997967,
            6998009, 6998029, 6998039, 20165149, 6998051, 6998053,
            1000000007, 1000000007, 1000000009, 1000000009, 10000000469L,
            999999999847L, 10000000469L, 999999999937L, 999999999959L, 999999999989L,
            999999999767L, 999999999767L, 100000009069L, 100000008937L, 100000008947L};

    private long[] complexArray = new long[]{
            20319251, 6997901, 6997927, 6997937, 17858849, 6997967,
            6998009, 6998029, 6998039, 20165149, 6998051, 6998053,
            1000000007, 1000000007, 1000000009, 1000000009, 10000000469L,
            999999999847L, 10000000469L, 999999999937L, 999999999959L, 999999999989L,
            999999999767L, 999999999767L, 100000009069L, 100000008937L, 12, 100000008947L};

    @Test
    void workingServersPrimeTest() throws InterruptedException {
        InetSocketAddress[] servers = new InetSocketAddress[]{
                new InetSocketAddress(host, 18080),
                new InetSocketAddress(host, 18081)
        };
        Thread[] threads = new Thread[]{
                new Thread(() -> {
                    Server.Run(18080);
                }),
                new Thread(() -> {
                    Server.Run(18081);
                })
        };
        for (var thread : threads) {
            thread.start();
        }
        Thread.sleep(100);

        Boolean res = isArrayPrime(primeArray, servers);
        assertTrue(res);

        for (var thread : threads) {
            thread.interrupt();
        }
        Thread.sleep(50);
    }

    @Test
    void workingServersComplexTest() throws InterruptedException {
        InetSocketAddress[] servers = new InetSocketAddress[]{new InetSocketAddress(host, 18080), new InetSocketAddress(18081)};
        ArrayList<Thread> threads = new ArrayList<>();
        threads.add(new Thread(() -> {
            Server.Run(18080);
        }));
        threads.add(new Thread(() -> {
            Server.Run(18081);
        }));

        for (var thread : threads) {
            thread.start();
        }
        Thread.sleep(100);

        Boolean res = isArrayPrime(complexArray, servers);
        assertFalse(res);

        for (var thread : threads) {
            thread.interrupt();
        }


        Thread.sleep(50);
    }

    @RepeatedTest(5)
    void badServersComplexTest() throws InterruptedException {
        InetSocketAddress[] servers = new InetSocketAddress[]{
                new InetSocketAddress(host, 18080),
                new InetSocketAddress(host, 18081),
                new InetSocketAddress(host, 18082)};

        ArrayList<Thread> threads = new ArrayList<>();
        threads.add(new Thread(() -> {
            RandomlyWorkingServer rwServer = new RandomlyWorkingServer(200, 20, 40);
            rwServer.Run(18081);
        }));
        threads.add(new Thread(() -> {
            RandomlyWorkingServer rwServer = new RandomlyWorkingServer(100, 60, 60);
            rwServer.Run(18080);
        }));
        threads.add(new Thread(() -> {
            RandomlyWorkingServer rwServer = new RandomlyWorkingServer(200, 90, 90);
            rwServer.Run(18082);
        }));


        for (var thread : threads) {
            thread.start();
        }
        Thread.sleep(50);

        Boolean res = isArrayPrime(complexArray, servers);
        assertFalse(res);

        for (var thread : threads) {
            thread.interrupt();
        }

        Thread.sleep(50);
    }


    @RepeatedTest(5)
    void badServersPrimePriorityTest() throws InterruptedException {
        InetSocketAddress[] servers = new InetSocketAddress[]{
                new InetSocketAddress(host, 18080),
                new InetSocketAddress(host, 18081),
                new InetSocketAddress(host, 18082),
                new InetSocketAddress(host, 18084)};

        ArrayList<Thread> threads = new ArrayList<>();
        threads.add(new Thread(() -> {
            RandomlyWorkingServer rwServer = new RandomlyWorkingServer(200, 80, 70);
            rwServer.Run(18081);
        }));
        threads.add(new Thread(() -> {
            RandomlyWorkingServer rwServer = new RandomlyWorkingServer(100, 90, 60);
            rwServer.Run(18080);
        }));
        threads.add(new Thread(() -> {
            RandomlyWorkingServer rwServer = new RandomlyWorkingServer(200, 80, 80);
            rwServer.Run(18082);
        }));


        for (var thread : threads) {
            thread.start();
        }
        Thread.sleep(50);

        Boolean res = isArrayPrime(complexArray, servers);
        assertFalse(res);

        for (var thread : threads) {
            thread.interrupt();
        }

        Thread.sleep(50);
    }

}
