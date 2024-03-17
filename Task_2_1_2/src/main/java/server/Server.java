package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static server.BytesConverter.readInt;
import static server.BytesConverter.readLong;

public class Server {

    public static void Run(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port, 10)) {
            while (true) {
                if (Thread.interrupted()) {
                    serverSocket.close();
                    return;
                }

                System.out.println("SERVER " + port + ": Waiting for client...");

                Socket clientSocket;
                serverSocket.setSoTimeout(50);
                try {
                    clientSocket = serverSocket.accept();
                } catch (SocketTimeoutException e) {
                    continue;
                }

                System.out.println(String.format("SERVER %s: Connection successfully established with %s:%s", port, clientSocket.getLocalAddress(), clientSocket.getLocalPort()));

                var in = clientSocket.getInputStream();
                var out = clientSocket.getOutputStream();

                int count = readInt(in);

                var executorService = Executors.newFixedThreadPool(4);
                List<Future<?>> futures = new ArrayList<>();

                while (count > 0) {
                    long number = readLong(in);

                    Callable<Boolean> cur = new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return PrimeNumberDetector.isPrime(number);
                        }
                    };
                    futures.add(executorService.submit(cur));

                    count--;
                }

                boolean finished = false;
                for (var future : futures) {
                    if (((Boolean) future.get()).equals(false)) {
                        executorService.shutdown();
                        out.write(0);
                        out.flush();

                        System.out.println("SERVER " + port + ": Data from " + clientSocket.getLocalAddress() + clientSocket.getLocalPort() + " was handled, socket closed.");
                        clientSocket.close();
                        finished = true;
                        break;
                    }
                }

                if (!finished) {
                    executorService.shutdown();
                    out.write(1);
                    out.flush();
                    System.out.println("SERVER " + port + ": Data from " + clientSocket.getLocalAddress() + clientSocket.getLocalPort() + " was handled, socket closed.");
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            Run(port);
        } catch (Exception e) {
            System.err.println("SERVER " + port + ": ERROR: " + e);
        }
    }
}
