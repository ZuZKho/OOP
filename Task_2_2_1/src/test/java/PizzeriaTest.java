import static org.junit.jupiter.api.Assertions.assertTrue;

import dto.OrderDTO;
import dto.PizzeriaDTO;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import pizzeria.Pizzeria;
import utils.JsonReader;


/**
 * Pizzeria work test by logs verifying.
 */
@SuppressWarnings("AbbreviationAsWordInName")
public class PizzeriaTest {

    @Test
    void testNormalOrders() throws IOException, InterruptedException {
        PizzeriaDTO pizzeriaDTO = JsonReader.readPizzeriaDTO("src/main/resources/Pizzeria2.json");
        List<OrderDTO> orders = JsonReader.readOrdersDTOList("src/main/resources/Orders1.json");

        Pizzeria pizzeria = new Pizzeria(pizzeriaDTO);
        Thread pizzeriaThread = new Thread(() -> {
            pizzeria.workDay();
        });
        Thread clientThread = new Thread(() -> {
            new Client(pizzeria, orders, 10);
        });

        pizzeriaThread.start();
        clientThread.start();
        pizzeriaThread.join();
        clientThread.interrupt();

        assertTrue(LogsVerifier.verify());
    }

}
