import static server.DelayBytesReader.readInt;
import static server.DelayBytesReader.readLong;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Random;
import server.Executor;

/**
 * Copy of server class with randomly happening errors.
 */
public class RandomlyWorkingServer {

    private static final int DELAY = 50;

    private long delay;
    private int connectionChance;
    private int answerChance;
    private Random rnd = new Random(System.currentTimeMillis());

    /**
     * Constructor for setting error coefficients.
     *
     * @param delay delay for emulating slow servers.
     * @param connectionChance chance in percent that server will close socket during reading.
     * @param answerChance chance in percent that server will close socket without answering.
     */
    public RandomlyWorkingServer(long delay, int connectionChance, int answerChance) {
        this.delay = delay;
        this.connectionChance = connectionChance;
        this.answerChance = answerChance;
    }

    /**
     * Start server.
     *
     * @param port server port.
     */
    public void run(int port) {
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
                System.out.println("SERVER " + port
                        + ": Connection successfully established with "
                        + clientSocket.getLocalAddress() + ":" + clientSocket.getLocalPort());

                var in = clientSocket.getInputStream();
                var out = clientSocket.getOutputStream();
                Executor executor = new Executor(4);

                // Если клиент долго не отвечает или возникли ошибки, отключаемся от него.
                try {
                    int count = readInt(in, DELAY);

                    // Random error check
                    if (rnd.nextInt(101) > connectionChance) {
                        clientSocket.close();
                        System.out.println(
                                String.format("SERVER %s: Connection randomly dropped %s:%s",
                                port, clientSocket.getLocalAddress(), clientSocket.getLocalPort()));
                        continue;
                    }

                    for (; count > 0; count--) {
                        executor.send(readLong(in, DELAY));
                    }
                } catch (IOException e) {
                    clientSocket.close();
                    continue;
                }

                // Waiting emulating slow servers
                Thread.sleep(delay);

                // Random error check
                if (rnd.nextInt(101) > answerChance) {
                    clientSocket.close();
                    System.out.println(String.format("SERVER %s: Connection randomly dropped %s:%s",
                            port, clientSocket.getLocalAddress(), clientSocket.getLocalPort()));
                    continue;
                }

                out.write(executor.get());
                out.flush();
                System.out.println("SERVER " + port + ": Data from "
                        + clientSocket.getLocalAddress()
                        + clientSocket.getLocalPort()
                        + " was handled, socket closed.");
                clientSocket.close();
            }
        } catch (InterruptedException e) {
            System.out.println("SERVER " + port + " was turned off.");
        } catch (Exception e) {
            System.err.println("SERVER " + port + ": ERROR: " + e);
        }
    }
}
