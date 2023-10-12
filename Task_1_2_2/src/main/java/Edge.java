public class Edge<V, E> {
    private static int freeId = 0;
    E value;
    private int id;
    Vertex<V> from, to;

    public Edge(E value, Vertex<V> from, Vertex<V> to) {
        this.value = value;
        this.from = from;
        this.to = to;
        this.id = freeId;
        freeId++;
    }

    public int getId() {
        return this.id;
    }
}
