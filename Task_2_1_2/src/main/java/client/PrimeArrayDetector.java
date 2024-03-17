package client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;


import static server.BytesConverter.*;
/**
 * Остановка по плохим соединениям или нерабочим серверам не предусмотрена,
 * т.е. если все сервера "плохие" проверка будет происходить бесконечно.
 */
public class PrimeArrayDetector {

    private static final long MAX_DELAY = 400;

    public static void main(String[] args) {
        long[] arr = new long[]{17, 17, 17, 19, 7};
        System.out.println(isArrayPrime(arr, new InetSocketAddress[]{new InetSocketAddress("0.0.0.0", 18080), new InetSocketAddress("0.0.0.0", 18081)}));
    }

    private static ArrayDivider arrayDivider;
    private static Selector selector;
    private static HashMap<InetSocketAddress, Integer> statuses = new HashMap<>();
    private static HashMap<SocketChannel, Integer> ids = new HashMap<>();
    private static HashMap<Integer, InetSocketAddress> idToAddress = new HashMap<>();
    private static HashMap<InetSocketAddress, Long> activityTime = new HashMap<>();
    private static HashMap<InetSocketAddress, Integer> priority = new HashMap<>();
    
    private static void decreaseServerPriority(InetSocketAddress address) {
        Integer old = priority.get(address);
        if (old == null) {
            priority.put(address, -5);
        } else {
            priority.put(address, old - 5);
        }
    }
    
    private static void increaseServerPriority(InetSocketAddress address) {
        Integer old = priority.get(address);
        if (old == null) {
            priority.put(address, 5);
        } else {
            priority.put(address, old + 5);
        }    
    }
    
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


    private static void connectServer(InetSocketAddress address) {
        SocketChannel serverChannel;
        try {
            serverChannel = SocketChannel.open();
            serverChannel.configureBlocking(true);
            serverChannel.connect(address);
            serverChannel.configureBlocking(false);
            
            System.out.println("CLIENT: Connected to " + address);
        } catch (Exception e) {
            decreaseServerPriority(address);
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
    // Нужно добавить обработку приоритетов.
    private static boolean UpdateConnections() {
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
            if (priority.get(server) < 0) continue;
            connectServer(server);
        }

        return true;
    }

    private static void reject(SelectionKey key, InetSocketAddress address, int taskId) {
        key.cancel();
        decreaseServerPriority(address);
        arrayDivider.rejectTask(taskId);
        statuses.put(address, 0);
        System.out.println("CLIENT: " + address + "'s task rejected.");
    }

    private static void approve(SelectionKey key, InetSocketAddress address, int taskId) {
        key.cancel();
        statuses.put(address, 0);
        arrayDivider.approveTask(taskId);
        increaseServerPriority(address);
        System.out.println("CLIENT: " + address + "'s task approved.");
    }

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
        UpdateConnections();

        while(true) {
            try {
                int count = selector.select(MAX_DELAY);
                if (count == 0) {
                    for(var key : selector.keys()) {
                        SocketChannel in = (SocketChannel) key.channel();
                        int id = ids.get(in);
                        InetSocketAddress address = idToAddress.get(id);
                        reject(key, address, id);
                    }
                    UpdateConnections();
                    continue;
                }
            } catch (Exception e){
                System.out.println("CLIENT: ERROR: Selector error.");
                return null;
            }

            for(var key : selector.keys()) {
                SocketChannel in = (SocketChannel) key.channel();
                int id = ids.get(in);
                InetSocketAddress address = idToAddress.get(id);

                if (key.isReadable()) {
                    try {
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1);

                        int readed = in.read(byteBuffer);

                        if (readed == 1) {
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
            if (!UpdateConnections()) {
                return true;
            }
        }
    }
}
