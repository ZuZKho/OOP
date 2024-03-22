package client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Main client class.
 * There is one client and several servers. All computing tasks processing by servers.
 * Client stop because of "bad servers" not supported.
 * Each server has priority, it works in next way:
 *      Every connections request all priorities increasing by 1.
 *      Every successful data handle server's priority increasing by 5.
 *      Every non-successful data handle server's priority decreasing by 5.
 *      If server's priority less than 0, client will not connect to this server.
 */
public class PrimeArrayDetector {

    private static final long MAX_DELAY = 400;
    private static ArrayDivider arrayDivider;
    private static Selector selector;
    private static HashMap<InetSocketAddress, Integer> statuses = new HashMap<>();
    private static HashMap<SocketChannel, Integer> ids = new HashMap<>();
    private static HashMap<Integer, InetSocketAddress> idToAddress = new HashMap<>();
    private static HashMap<InetSocketAddress, Long> activityTime = new HashMap<>();
    private static HashMap<InetSocketAddress, Integer> priority = new HashMap<>();

    /**
     * Decrease server priority by value.
     *
     * @param address address of server.
     * @param value value by which to decrease.
     */
    private static void decreaseServerPriority(InetSocketAddress address, int value) {
        Integer old = priority.get(address);
        priority.put(address, old - value);
    }

    /**
     * Increase server priority by value.
     *
     * @param address address of server.
     * @param value value by which to increase.
     */
    private static void increaseServerPriority(InetSocketAddress address, int value) {
        Integer old = priority.get(address);
        priority.put(address, old + value);
    }

    /**
     * Assign task to a server ans send it.
     *
     * @param server serverSocket.
     * @param address serverAddress.
     * @return success of operation.
     */
    private static boolean sendTaskToServer(SocketChannel server, InetSocketAddress address) {
        ArrayDivider.Task task = arrayDivider.getTask();
        if (task == null) {
            System.out.println("CLIENT: no available tasks");
            return false;
        }

        long[] array = task.arr;
        int length = array.length;
        try {
            ByteBuffer bytesLength = ByteBuffer.allocate(4).putInt(length).flip();
            server.write(bytesLength);
            for (long a : array) {
                ByteBuffer bytes = ByteBuffer.allocate(8).putLong(a).flip();
                server.write(bytes);
            }
        } catch (Exception e) {
            System.out.println(e);
            arrayDivider.rejectTask(task.id);
            return false;
        }

        ids.put(server, task.id);
        idToAddress.put(task.id, address);
        activityTime.put(address, System.currentTimeMillis());
        return true;
    }


    /**
     * Try to create connection with server.
     *
     * @param address address of server.
     */
    private static void connectServer(InetSocketAddress address) {
        SocketChannel serverChannel;
        try {
            serverChannel = SocketChannel.open();
            serverChannel.configureBlocking(true);
            serverChannel.connect(address);
            serverChannel.configureBlocking(false);

            System.out.println("CLIENT: Connected to " + address);
        } catch (Exception e) {
            decreaseServerPriority(address, 5);
            System.out.println("CLIENT: ERROR while connecting to server " + address);
            return;
        }

        if (sendTaskToServer(serverChannel, address)) {
            System.out.println("CLIENT: Data successfully sent to " + address);
            try {
                serverChannel.register(selector, SelectionKey.OP_READ);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            statuses.put(address, 1);
        } else {
            System.out.println("CLIENT: ERROR: Can't send data.");
            try {
                serverChannel.finishConnect();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // true если нельзя завершать, false если можно завершать.

    /**
     * Go through all servers and try to make connections and send tasks.
     *
     * @return true if all tasks finished, false otherwise.
     */
    private static boolean updateConnections() {
        if (arrayDivider.isFinished()) {
            return false;
        }

        ArrayList<InetSocketAddress> servers = new ArrayList<>();
        for (var status : statuses.entrySet()) {
            int oldPriority = priority.get(status.getKey());
            priority.put(status.getKey(), oldPriority + 1);
            if (status.getValue() == 0) {
                servers.add(status.getKey());
            }
        }

        servers.sort(Comparator.comparing((InetSocketAddress addr) -> priority.get(addr)));

        for (var server : servers) {
            if (priority.get(server) < 0) {
                continue;
            }
            connectServer(server);
        }

        return true;
    }

    /**
     * Reject previously assigned task.
     *
     * @param key selector selectionKey.
     * @param address address of server.
     * @param taskId id of assigned task.
     */
    private static void reject(SelectionKey key, InetSocketAddress address, int taskId) {
        key.cancel();
        decreaseServerPriority(address, 5);
        arrayDivider.rejectTask(taskId);
        statuses.put(address, 0);
        System.out.println("CLIENT: " + address + "'s task rejected.");
    }

    /**
     * Approve previously assigned task.
     *
     * @param key selector selectionKey.
     * @param address address of server.
     * @param taskId id of assigned task.
     */
    private static void approve(SelectionKey key, InetSocketAddress address, int taskId) {
        key.cancel();
        statuses.put(address, 0);
        arrayDivider.approveTask(taskId);
        increaseServerPriority(address, 5);
        System.out.println("CLIENT: " + address + "'s task approved.");
    }

    /**
     * Main client function.
     *
     * @param arr array to check.
     * @param addresses available servers list.
     * @return is array prime.
     */
    public static Boolean isArrayPrime(long[] arr, InetSocketAddress[] addresses) {

        statuses.clear();
        ids.clear();
        for (var adr : addresses) {
            statuses.put(adr, 0);
            priority.put(adr, 0);
        }

        try {
            selector = Selector.open();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        arrayDivider = new ArrayDivider(arr, Math.max(arr.length / addresses.length / 3, 2));
        updateConnections();

        while (true) {
            try {
                selector.select(MAX_DELAY);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            for (var key : selector.keys()) {
                SocketChannel in = (SocketChannel) key.channel();
                int id = ids.get(in);
                InetSocketAddress address = idToAddress.get(id);

                if (key.isReadable()) {
                    try {
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1);
                        if (in.read(byteBuffer) == 1) {
                            approve(key, address, id);
                            if (byteBuffer.flip().get() == 0) {
                                return false;
                            }
                        } else {
                            reject(key, address, id);
                        }
                    } catch (IOException e) {
                        reject(key, address, id);
                    }
                } else {
                    long lastActivity = activityTime.get(address);
                    if (System.currentTimeMillis() - lastActivity > MAX_DELAY) {
                        reject(key, address, id);
                    }
                }
            }
            if (!updateConnections()) {
                return true;
            }
        }
    }
}
