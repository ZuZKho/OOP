import java.util.HashMap;

public interface Graph<V, E> {
    int addVertex(Vertex<V> vertex);
    default int addVertex(V value) {
        Vertex<V> vertex = new Vertex<V>(value);
        return addVertex(vertex);
    }


    default void deleteVertex(Vertex<V> vertex){
        deleteVertex(vertex.getId());
    }
    void deleteVertex(int vertexId);

    int addEdge(E value, int fromId, int toId);


    void deleteEdge(int edgeId);

    Vertex<V> getVertex(int id);

    Edge<V, E> getEdge(int edgeId);
}
