import dto.OrderDTO;
import dto.PizzeriaDTO;
import org.junit.jupiter.api.Test;
import pizzeria.Pizzeria;
import utils.JsonReader;

import java.io.IOException;
import java.util.List;

public class PizzeriaTest {

    @Test
    void test1() throws IOException, InterruptedException {
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
    }
}
