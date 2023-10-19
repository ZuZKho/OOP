import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Класс реализующий загрузку графа Int Int из файла.
 */
public class GraphDownloader {

    /**
     * Функция загрузки графа из файла
     *
     * @param g граф
     * @param vertices список, куда будут сложены объекты вершин.
     * @param edges список, куда будут сложены объекты ребер.
     * @param fileName имя файла из которого нужно осуществить ввод.
     */
    static void downloadGraphFromFile(GraphInterface<Integer, Integer> g,
                                      ArrayList<Vertex<Integer>> vertices,
                                      ArrayList<Edge<Integer, Integer>> edges,
                                      String fileName) {

        Scanner scanner;
        try {
            scanner = new Scanner(new File(fileName));
        } catch (FileNotFoundException e) {
            return;
        }

        int verticesCount = scanner.nextInt();
        int edgesCount = scanner.nextInt();

        for (int i = 0; i < verticesCount; i++) {
            vertices.add(g.addVertex(i + 1));
        }

        for (int i = 0; i < edgesCount; i++) {
            int from;
            int to;
            int weight;
            from = scanner.nextInt();
            to = scanner.nextInt();
            weight = scanner.nextInt();
            edges.add(g.addEdge(weight, vertices.get(from - 1), vertices.get(to - 1)));
        }
    }
}
