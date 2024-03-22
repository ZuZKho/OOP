package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.OrderDTO;
import dto.PizzeriaDTO;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Utility class for reading configuration files.
 */

@SuppressWarnings("AbbreviationAsWordInNameCheck")
public class JsonReader {

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Read pizzeria configuration file.
     *
     * @param fileName path from project directory.
     * @return PizzeriaDTO obtained from configuration file.
     * @throws IOException wrong json file.
     */
    public static PizzeriaDTO readPizzeriaDTO(String fileName) throws IOException {
        return objectMapper.readValue(new File(fileName), PizzeriaDTO.class);
    }

    /**
     * Read orders configuration file.
     *
     * @param fileName path from project directory.
     * @return List of OrderDTO obtained from configuration file.
     * @throws IOException wrong json file.
     */
    public static List<OrderDTO> readOrdersDTOList(String fileName) throws IOException {
        return objectMapper.readValue(new File(fileName), new TypeReference<List<OrderDTO>>() {
        });
    }

}
