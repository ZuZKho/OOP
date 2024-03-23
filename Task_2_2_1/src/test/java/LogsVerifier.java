import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Class for verifying logs sequence.
 */
public class LogsVerifier {

    private static HashMap<Integer, List<LocalDateTime>> ordersStatuses;
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");

    private static void checkStatuses(String str, int statusId) {
        int startIndex = str.indexOf("#");
        List<Integer> ordersList = Pattern.compile("\\d+")
                .matcher(str.substring(startIndex))
                .results()
                .map(MatchResult::group)
                .map(x -> Integer.parseInt(x))
                .collect(Collectors.toList());

        LocalDateTime logTime = LocalDateTime.parse(str.substring(0, 23), formatter);
        for (var order : ordersList) {
            if (ordersStatuses.get(order) == null) {
                ordersStatuses.put(order, Arrays.asList(new LocalDateTime[5]));
            }
            ordersStatuses.get(order).set(statusId, logTime);
        }
    }

    /**
     * Verify logs from Pizzeria.
     * Checking Sequence of actions of each order.
     *
     * @return true if logs correct, false otherwise.
     */
    public static boolean verify() {
        ordersStatuses = new HashMap<>();
        File logFile = new File("src/test/resources/log.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            while (reader.ready()) {
                String str = reader.readLine();
                if (!str.contains("Pizzeria")) {
                    if (str.contains("added to queue")) {
                        checkStatuses(str, 0);
                    } else if (str.contains("was taken")) {
                        checkStatuses(str, 1);
                    } else if (str.contains("to storage")) {
                        checkStatuses(str, 2);
                    } else if (str.contains("started delivery")) {
                        checkStatuses(str, 3);
                    } else if (str.contains("finished delivery")) {
                        checkStatuses(str, 4);
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }

        for (var status : ordersStatuses.entrySet()) {
            for (int i = 0; i < 4; i++) {
                var cur = status.getValue().get(i);
                var next = status.getValue().get(i + 1);
                if (cur.isAfter(next)) {
                    return false;
                }
            }
        }

        return true;
    }
}
