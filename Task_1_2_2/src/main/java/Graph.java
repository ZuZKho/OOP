
import java.util.ArrayList;
import java.util.HashMap;

public class Graph<V, E extends Number> {


    protected HashMap<Integer, Vertex<V>> vertices;
    protected HashMap<Integer, Edge<V, E>> edges;

    /**
     * Получить вершину по ее ID.
     * <p>
     * Время работы - O(1).
     *
     * @param vertexId айди вершины.
     * @return Объект вершины.
     */
    public Vertex<V> getVertexById(int vertexId) {
        return vertices.get(vertexId);
    }


    /**
     * Получить ребро по его IDю
     * <p>
     * Время работы - O(1).
     *
     * @param edgeId айди ребра.
     * @return объект ребра.
     */
    public Edge<V, E> getEdgeById(int edgeId) {
        return edges.get(edgeId);
    }

    /**
     * Получить список всех вершин.
     * Не гарантируется, что вершины будут отсортированны по ID.
     * <p>
     * Время работы - O(V). V - количество вершин.
     *
     * @return список объектов вершин.
     */
    public ArrayList<Vertex<V>> getVerticesList() {
        return new ArrayList<>(vertices.values());
    }

    /**
     * Получить список всех ребер.
     * Не гарантируется, что ребра будут отсортированны по ID.
     * <p>
     * Время работы - O(E). E - количество вершин.
     *
     * @return список объектов ребер.
     */
    public ArrayList<Edge<V, E>> getEdgesList() {
        return new ArrayList<>(edges.values());
    }

}
