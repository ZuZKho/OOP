import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GraphDownloader {

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
            int from, to, weight;
            from = scanner.nextInt();
            to = scanner.nextInt();
            weight = scanner.nextInt();
            edges.add(g.addEdge(weight, vertices.get(from - 1), vertices.get(to - 1)));
        }
    }
}
