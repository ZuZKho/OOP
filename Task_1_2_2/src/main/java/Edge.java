/**
 * Класс ребра.
 *
 * @param <V> тип значения вершины.
 * @param <E> тип значения ребра.
 */
public class Edge<V, E> {
    private static int freeId = 0;
    E value;
    private int id;
    Vertex<V> from;
    Vertex<V> to;

    public Edge(E value, Vertex<V> from, Vertex<V> to) {
        this.value = value;
        this.from = from;
        this.to = to;
        this.id = freeId;
        freeId++;
    }

    /**
     * Получить id ребра.
     *
     * @return id ребра.
     */
    public int getId() {
        return this.id;
    }
}
