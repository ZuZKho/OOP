import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GraphTest {

    private void test1Helper(Supplier<GraphInterface<Integer, Integer>> constructor) {

        GraphInterface<Integer, Integer> g = constructor.get();
        ArrayList<Vertex<Integer>> vertices = g.getVerticesList(); // will be empty initially
        ArrayList<Edge<Integer, Integer>> edges = g.getEdgesList(); // will be empty initially

        GraphDownloader.downloadGraphFromFile(g, vertices, edges, "src/main/java/input1.txt");

        assertEquals(vertices.size(), 7);

        ArrayList<PairComparable<Double, Vertex<Integer>>> ans = g.sortByDistance(vertices.get(2));
        assertEquals(Arrays.asList(0, 2, 4, 5, 9, 10, 14), ans.stream().map(item -> item.first.intValue()).toList());
        assertEquals(Arrays.asList(3, 4, 5, 6, 7, 2, 1), ans.stream().map(item -> item.second.getValue()).toList());

        g.deleteEdge(edges.get(5));
        ans = g.sortByDistance(vertices.get(2));
        assertEquals(Arrays.asList(0, 4, 5, 9, 34, 39, 46), ans.stream().map(item -> item.first.intValue()).toList());
        assertEquals(Arrays.asList(3, 5, 6, 7, 1, 2, 4), ans.stream().map(item -> item.second.getValue()).toList());

        edges.add(g.addEdge(3, vertices.get(2), vertices.get(0)));
        ans = g.sortByDistance(vertices.get(2));
        assertEquals(Arrays.asList(0, 3, 4, 5, 8, 9, 15), ans.stream().map(item -> item.first.intValue()).toList());
        assertEquals(Arrays.asList(3, 1, 5, 6, 2, 7, 4), ans.stream().map(item -> item.second.getValue()).toList());
    }

    @Nested
    class test1Group {
        @Test
        void test1AdjacencyMatrix() {
            test1Helper(AdjacencyMatrixGraph::new);
        }

        @Test
        void test1AdjacencyList() {
            test1Helper(AdjacencyListGraph::new);
        }

        @Test
        void test1IncidenceMatrix() {
            test1Helper(IncidenceMatrixGraph::new);
        }
    }


    private void test2Helper(Supplier<GraphInterface<Integer, Integer>> constructor) {
        GraphInterface<Integer, Integer> g = constructor.get();
        var vertices = g.getVerticesList();

        for (int i = 0; i < 2; i++) {
            vertices.add(g.addVertex(i + 1));
        }

        assertThrows(IllegalArgumentException.class, () -> {
            g.addEdge(13, new Vertex<Integer>(2), new Vertex<Integer>(3));
        });

        assertThrows(IllegalArgumentException.class, () -> {
            g.addEdge(13, vertices.get(0), new Vertex<Integer>(4));
        });
    }


    @Nested
    @DisplayName("Adding edge between non existing vertices")
    class test2Group {
        @Test
        void testAdjacencyMatrix() {
            test2Helper(AdjacencyMatrixGraph::new);
        }

        @Test
        void testAdjacencyList() {
            test2Helper(AdjacencyListGraph::new);
        }

        @Test
        void testIncidenceMatrix() {
            test2Helper(IncidenceMatrixGraph::new);
        }
    }

    private void test3Helper(Supplier<GraphInterface<Integer, Integer>> constructor) {
        GraphInterface<Integer, Integer> g = constructor.get();

        ArrayList<Vertex<Integer>> vertices = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            vertices.add(g.addVertex(i + 1));
        }
        g.addEdge(5, vertices.get(0), vertices.get(2));
        g.addEdge(5, vertices.get(2), vertices.get(0));
        g.addEdge(5, vertices.get(2), vertices.get(3));
        g.addEdge(5, vertices.get(2), vertices.get(3));
        for (int i = 0; i < 2; i++) {
            g.deleteVertex(vertices.get(i));
        }

        assertEquals(2, g.getVerticesList().size());
        assertEquals(2, g.getEdgesList().size());
    }

    @Nested
    @DisplayName("Deleting vertices")
    class test3Group {
        @Test
        void testAdjacencyMatrix() {
            test3Helper(AdjacencyMatrixGraph::new);
        }

        @Test
        void testAdjacencyList() {
            test3Helper(AdjacencyListGraph::new);
        }

        @Test
        void testIncidenceMatrix() {
            test3Helper(IncidenceMatrixGraph::new);
        }
    }

    private void test4Helper(Supplier<GraphInterface<Integer, Integer>> constructor) {
        GraphInterface<Integer, Integer> g = constructor.get();
        ArrayList<Vertex<Integer>> vertices = g.getVerticesList(); // will be empty initially
        ArrayList<Edge<Integer, Integer>> edges = g.getEdgesList(); // will be empty initially

        GraphDownloader.downloadGraphFromFile(g, vertices, edges, "src/main/java/input1.txt");

        Set<Vertex<Integer>> expectedVertices = new HashSet<Vertex<Integer>>(vertices);
        Set<Edge<Integer, Integer>> expectedEdges = new HashSet<Edge<Integer, Integer>>(edges);
        Set<Vertex<Integer>> realVertices = new HashSet<Vertex<Integer>>(g.getVerticesList());
        Set<Edge<Integer, Integer>> realEdges = new HashSet<Edge<Integer, Integer>>(g.getEdgesList());
        assertEquals(expectedVertices, realVertices);
        assertEquals(expectedEdges, realEdges);
    }

    @Nested
    @DisplayName("Common Graph class test: getLists")
    class test4Group {
        @Test
        void testAdjacencyMatrix() {
            test4Helper(AdjacencyMatrixGraph::new);
        }

        @Test
        void testAdjacencyList() {
            test4Helper(AdjacencyListGraph::new);
        }

        @Test
        void testIncidenceMatrix() {
            test4Helper(IncidenceMatrixGraph::new);
        }
    }
    private void test5Helper(Supplier<GraphInterface<Integer, Integer>> constructor) {
        GraphInterface<Integer, Integer> g = constructor.get();
        ArrayList<Vertex<Integer>> vertices = g.getVerticesList(); // will be empty initially
        ArrayList<Edge<Integer, Integer>> edges = g.getEdgesList(); // will be empty initially

        GraphDownloader.downloadGraphFromFile(g, vertices, edges, "src/main/java/input1.txt");

        for (int i = 0; i < vertices.size(); i++) {
            assertEquals(vertices.get(i), g.getVertexById(vertices.get(i).getId()));
        }
        for (int i = 0; i < edges.size(); i++) {
            assertEquals(edges.get(i), g.getEdgeById(edges.get(i).getId()));
        }
    }

    @Nested
    @DisplayName("Common Graph class test: getById")
    class test5Group {
        @Test
        void testAdjacencyMatrix() {
            test5Helper(AdjacencyMatrixGraph::new);
        }

        @Test
        void testAdjacencyList() {
            test5Helper(AdjacencyListGraph::new);
        }

        @Test
        void testIncidenceMatrix() {
            test5Helper(IncidenceMatrixGraph::new);
        }
    }

}