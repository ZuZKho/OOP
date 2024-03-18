package server;

import static server.DelayBytesReader.readInt;
import static server.DelayBytesReader.readLong;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Server class for detecting prime numbers.
 */
public class Server {

    private static final int DELAY = 50;

    /**
     * Start server.
     *
     * @param port server port.
     */
    public static void run(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port, 10)) {
            serverSocket.setSoTimeout(DELAY);

            while (true) {
                // Нужно ли закончить работу сервера
                if (Thread.interrupted()) {
                    serverSocket.close();
                    return;
                }

                // Подключение клиента
                System.out.println("SERVER " + port + ": Waiting for client...");
                Socket clientSocket;
                try {
                    clientSocket = serverSocket.accept();
                } catch (SocketTimeoutException e) {
                    continue;
                }
                System.out.println(String.format("SERVER %s: Connection successfully established with %s:%s",
                        port, clientSocket.getLocalAddress(), clientSocket.getLocalPort()));

                var in = clientSocket.getInputStream();
                var out = clientSocket.getOutputStream();
                Executor executor = new Executor(4);

                // Если клиент долго не отвечает или возникли ошибки, отключаемся от него.
                try {
                    int count = readInt(in, DELAY);
                    for (; count > 0; count--) {
                        executor.send(readLong(in, DELAY));
                    }
                } catch (IOException e) {
                    clientSocket.close();
                    continue;
                }

                out.write(executor.get());
                out.flush();
                System.out.println("SERVER " + port + ": Data from " + clientSocket.getLocalAddress() +
                        clientSocket.getLocalPort() + " was handled, socket closed.");
                clientSocket.close();
            }
        } catch (Exception e) {
            System.err.println("SERVER " + port + ": ERROR: " + e);
        }
    }
}

