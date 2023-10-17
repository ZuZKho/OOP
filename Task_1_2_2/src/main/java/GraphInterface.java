import java.io.File;
import java.util.*;

public interface GraphInterface<V, E extends Number> {

    Vertex<V> addVertex(V value);

    Vertex<V> getVertexById(int vertexId);

    void deleteVertex(Vertex<V> vertex);

    ArrayList<Vertex<V>> getVerticesList();

    ArrayList<Edge<V, E>> getEdgesList();


    Edge<V, E> addEdge(E value, Vertex<V> from, Vertex<V> to) throws IllegalArgumentException;

    Edge<V, E> getEdgeById(int edgeId);

    void deleteEdge(Edge<V, E> edge);


    HashMap<Integer, Double> dijkstra(int startVertexId);

    default ArrayList<PairComparable<Double, Vertex<V>>> sortByDistance(Vertex<V> startVertex) {
        HashMap<Integer, Double> distances = dijkstra(startVertex.getId());
        ArrayList<PairComparable<Double, Vertex<V>>> answer = new ArrayList<>();

        for (Map.Entry<Integer, Double> entry : distances.entrySet()) {
            Integer vertexId = entry.getKey();
            Double distance = entry.getValue();

            answer.add(new PairComparable<>(distance, getVertexById(vertexId)));
        }

        Collections.sort(answer);
        return answer;
    }

}
